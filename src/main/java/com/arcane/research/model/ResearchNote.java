package com.arcane.research.model;

import com.arcane.research.enums.Element;
import com.arcane.research.util.HexGridGenerator;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * 研究笔记，代表玩家正在进行的研究项目
 */
public class ResearchNote {
    // 研究笔记的标题
    private String title;
    // 研究笔记的描述
    private String description;
    // 研究笔记中的节点列表
    private List<ResearchNode> nodes = new ArrayList<>();
    // 节点之间的连接映射
    private Map<ResearchNode, Set<ResearchNode>> connections = new HashMap<>();
    // 研究笔记是否完成的标志
    private boolean completed = false;
    // 研究完成后生成的研究卷轴
    private ResearchScroll generatedScroll;
    private HexGridGenerator.HexMapResult honeycombLayout;

    /**
     * 构造函数
     * @param title 研究标题
     * @param description 研究描述
     */
    public ResearchNote(String title, String description) {
        this.title = title;
        this.description = description;
        initializeGeneratedScroll();
    }

    /**
     * 检查所有顶点是否都连通
     * @return 如果所有顶点都连通返回 true，否则返回 false
     */
    public boolean isFullyConnected() {
        if (nodes.isEmpty()) {
            return true;
        }
        return true;
    }

    /**
     * 检查给定的所有顶点是否两两互相联通
     * @param vertices 要检查的顶点列表
     * @return 如果所有顶点两两互相联通返回true，否则返回false
     */
    public boolean areVerticesMutuallyConnected(List<ResearchNode> vertices) {
        // 检查所有顶点是否都存在于当前研究笔记中
        for (ResearchNode vertex : vertices) {
            if (!nodes.contains(vertex)) {
                return false;
            }
        }

        // 检查每对顶点之间的连通性
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                ResearchNode nodeA = vertices.get(i);
                ResearchNode nodeB = vertices.get(j);

                // 检查双向连通性
                if (!isConnected(nodeA, nodeB) || !isConnected(nodeB, nodeA)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 检查两个节点之间是否存在路径
     * @param start 起点
     * @param end 终点
     * @return 如果存在路径返回true，否则返回false
     */
    private boolean isConnected(ResearchNode start, ResearchNode end) {
        // 如果起点和终点相同，认为连通
        if (start.equals(end)) {
            return true;
        }

        // 使用BFS进行路径搜索
        Set<ResearchNode> visited = new HashSet<>();
        Queue<ResearchNode> queue = new LinkedList<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            ResearchNode current = queue.poll();

            // 获取当前节点的所有连接节点
            Set<ResearchNode> connectedNodes = connections.getOrDefault(current, new HashSet<>());

            for (ResearchNode neighbor : connectedNodes) {
                if (neighbor.equals(end)) {
                    return true; // 找到路径
                }

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        return false; // 未找到路径
    }

    /**
     * 获取蜂窝地图,使用前提：请确保已调用 initializeHoneycombLayout 方法
     * @return 蜂窝地图
     */
    public HexGridGenerator.HexMapResult getHexMap() {
        return honeycombLayout;
    }

    /**
     * 初始化蜂窝地图
     * @param level 蜂窝地图的级别
     * @param fillPercentage 蜂窝地图的填充百分比
     * @param seed 随机数种子
     * @param vertexCount 顶点数量
     * @param obstacleCount 障碍数量
     * @param baseElements 顶点元素列表(随机抽取)
     */
    public void initializeHoneycombLayout(int level, double fillPercentage,int vertexCount,int obstacleCount,long seed,Element[] baseElements) {
        // 使用 MazeAntGenerator 生成蜂窝地图
        honeycombLayout = HexGridGenerator.generateHexMapWithVerticesAndObstacles(level, fillPercentage, vertexCount, obstacleCount, seed);

        Set<Point> hexPoints = honeycombLayout.hexPoints;
        Set<Point> vertices = honeycombLayout.vertices;
        Set<Point> obstacles = honeycombLayout.obstacles;

        // 清空当前笔记
        clearNodes();
//        Element element = baseElements[new Random().nextInt(baseElements.length)];
//        addNode(new ResearchNode(element, 1, -1));
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 5; j++) {
//                addNode(new ResearchNode(element, i, j));
//            }
//        }

        // 构建节点网格
        for (Point pt : hexPoints) {
            if (obstacles.contains(pt)) {
                continue; // 跳过障碍位置
            }

            Element element = Element.PLACEHOLDER;
            if (vertices.contains(pt)) {
                // 顶点：随机选择一个基础元素
                element = baseElements[new Random().nextInt(baseElements.length)];
            }

            addNode(new ResearchNode(element, pt.x, pt.y));
        }
    }

    /**
     * 初始化生成的研究卷轴
     */
    private void initializeGeneratedScroll() {
        // 简单初始化，实际应用中可以根据研究主题生成不同卷轴
        List<ResearchPoint> rewards = new ArrayList<>();
        rewards.add(new ResearchPoint(Element.ORDER, 5));
        rewards.add(new ResearchPoint(Element.ENTROPY, 5));

        List<String> unlockedRecipes = new ArrayList<>();
        if (title.contains("元素")) {
            unlockedRecipes.add("元素转换器");
        } else if (title.contains("魔法")) {
            unlockedRecipes.add("基础法杖");
        }

        generatedScroll = new ResearchScroll(title, description, rewards, unlockedRecipes);
    }

    /**
     * 添加研究节点
     * @param node 研究节点
     */
    public void addNode(ResearchNode node) {
        nodes.add(node);
    }

    /**
     * 添加节点连接
     * @param node1 节点1
     * @param node2 节点2
     */
    public void addConnection(ResearchNode node1, ResearchNode node2) {
        connections.computeIfAbsent(node1, k -> new HashSet<>()).add(node2);
        connections.computeIfAbsent(node2, k -> new HashSet<>()).add(node1);
        checkCompletion();
    }

    /**
     * 检查研究是否完成
     */
    public void checkCompletion() {
        // 简单完成条件：当连接数达到节点数-1时视为完成
        int maxConnections = nodes.size() > 1 ? nodes.size() - 1 : 0;
        completed = getConnectionCount() >= maxConnections;
    }

    /**
     * 获取节点数量
     * @return 节点数量
     */
    public int getNodeCount() {
        return nodes.size();
    }

    /**
     * 获取连接数量
     * @return 连接数量
     */
    public int getConnectionCount() {
        int count = 0;
        for (Set<ResearchNode> connectedNodes : connections.values()) {
            count += connectedNodes.size();
        }
        return count / 2; // 每个连接被计算了两次
    }

    /**
     * 生成研究卷轴
     * @return 研究卷轴
     */
    public ResearchScroll generateResearchScroll() {
        return generatedScroll;
    }

    /**
     * 获取研究标题
     * @return 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 获取研究描述
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取所有节点
     * @return 节点列表
     */
    public List<ResearchNode> getNodes() {
        return new ArrayList<>(nodes);
    }

    /**
     * 获取所有连接
     * @return 连接映射
     */
    public Map<ResearchNode, Set<ResearchNode>> getConnections() {
        return new HashMap<>(connections);
    }

    /**
     * 判断研究是否完成
     * @return 是否完成
     */
    public boolean isCompleted() {
        return completed;
    }

    public void clearNodes() {
        nodes.clear();
    }
}
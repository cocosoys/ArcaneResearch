package com.arcane.research.util;

import java.awt.Point;
import java.util.*;

public class HexGridGenerator {
    private final int level;          // 蜂窝层数
    private final double fillPercentage;
    private final long seed;
    private final Random random;

    public HexGridGenerator(int level, double fillPercentage, long seed) {
        this.level = level;
        this.fillPercentage = fillPercentage;
        this.seed = seed;
        this.random = new Random(seed);
    }

    // 六边形六个方向的坐标偏移
    private static final int[][] HEX_DIRS = {
            {1, 0},   // 右
            {-1, 0},  // 左
            {1, -1},  // 右上
            {-1, -1}, // 左上
            {1, 1},   // 右下
            {-1, 1}   // 左下
    };

    // 获取中心点 (0, 0)
    public Point getCenter() {
        return new Point(0, 0);
    }

    // 判断坐标是否在层数范围内
    private boolean isValidPosition(Point pt, int level) {
        // 计算曼哈顿距离（六边形坐标系中的距离）
        int distance = (Math.abs(pt.x) + Math.abs(pt.y) + Math.abs(pt.x + pt.y)) / 2;
        return distance <= level - 1;
    }

    // 获取指定点的六边形邻居
    private List<Point> getHexNeighbors(Point p, int level) {
        List<Point> neighbors = new ArrayList<>();
        for (int[] dir : HEX_DIRS) {
            Point neighbor = new Point(p.x + dir[0], p.y + dir[1]);
            if (isValidPosition(neighbor, level)) {
                neighbors.add(neighbor);
            }
        }
        Collections.shuffle(neighbors, random); // 随机化邻居顺序
        return neighbors;
    }

    // 选择边界点作为顶点
    private Set<Point> selectBoundaryVertices(Set<Point> hexPoints, int n, int level) {
        Set<Point> vertices = new HashSet<>();
        List<Point> boundaryPoints = new ArrayList<>();

        for (Point pt : hexPoints) {
            // 计算曼哈顿距离，找到最外层节点作为边界
            int distance = (Math.abs(pt.x) + Math.abs(pt.y) + Math.abs(pt.x + pt.y)) / 2;
            if (distance == level - 1) {
                boundaryPoints.add(pt);
            }
        }

        Collections.shuffle(boundaryPoints, random);
        for (int i = 0; i < Math.min(n, boundaryPoints.size()); i++) {
            vertices.add(boundaryPoints.get(i));
        }
        return vertices;
    }

    // 生成随机障碍物
    private Set<Point> generateObstacles(Set<Point> hexPoints, Set<Point> vertices, int obstacleCount) {
        Set<Point> obstacles = new HashSet<>();
        List<Point> availablePoints = new ArrayList<>(hexPoints);
        availablePoints.removeAll(vertices);
        Collections.shuffle(availablePoints, random);

        for (int i = 0; i < Math.min(obstacleCount, availablePoints.size()); i++) {
            obstacles.add(availablePoints.get(i));
        }
        return obstacles;
    }

    // 计算指定层数的最大节点数
    private int getMaxNodeCount(int level) {
        return 1 + 3 * level * (level - 1); // 公式: 1 + 6*(1+2+...+(level-1))
    }

    // 生成蜂窝地图（基于层数）
    public HexMapResult generateHexMap(int vertexCount, int obstacleCount) {
        Point center = getCenter();
        Set<Point> hexPoints = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();

        // 从中心原点(0,0)开始生成
        hexPoints.add(center);
        queue.add(center);

        // 计算目标节点数量（考虑填充百分比）
        int maxNodes = getMaxNodeCount(level);
        int targetCount = (int) Math.ceil(maxNodes * fillPercentage / 100.0);
        targetCount = Math.min(targetCount, maxNodes); // 确保不超过最大节点数

        // 广度优先搜索生成蜂窝结构
        while (!queue.isEmpty() && hexPoints.size() < targetCount) {
            Point current = queue.poll();
            List<Point> neighbors = getHexNeighbors(current, level);

            for (Point neighbor : neighbors) {
                if (!hexPoints.contains(neighbor)) {
                    hexPoints.add(neighbor);
                    queue.add(neighbor);
                }
                if (hexPoints.size() >= targetCount) break;
            }
        }

        // 生成顶点和障碍物
        Set<Point> vertices = selectBoundaryVertices(hexPoints, vertexCount, level);
        Set<Point> obstacles = generateObstacles(hexPoints, vertices, obstacleCount);

        return new HexMapResult(hexPoints, vertices, obstacles);
    }

    // 结果封装类
    public static class HexMapResult {
        public final Set<Point> hexPoints;  // 所有蜂窝节点
        public final Set<Point> vertices;   // 顶点节点
        public final Set<Point> obstacles;  // 障碍节点

        public HexMapResult(Set<Point> hexPoints, Set<Point> vertices, Set<Point> obstacles) {
            this.hexPoints = hexPoints;
            this.vertices = vertices;
            this.obstacles = obstacles;
        }
    }

    // 打印蜂窝地图（可视化）
    public void printHexMap(Set<Point> hexPoints, Set<Point> vertices, Set<Point> obstacles) {
        // 计算地图边界
        int minX = 0, maxX = 0, minY = 0, maxY = 0;
        for (Point pt : hexPoints) {
            minX = Math.min(minX, pt.x);
            maxX = Math.max(maxX, pt.x);
            minY = Math.min(minY, pt.y);
            maxY = Math.max(maxY, pt.y);
        }

        int mapWidth = maxX - minX + 1;
        int mapHeight = maxY - minY + 1;
        char[][] map = new char[mapHeight][mapWidth];
        for (char[] row : map) Arrays.fill(row, ' ');

        // 标记蜂窝节点
        for (Point pt : hexPoints) {
            int x = pt.x - minX;
            int y = pt.y - minY;
            map[y][x] = '.';
        }

        // 标记顶点
        for (Point pt : vertices) {
            int x = pt.x - minX;
            int y = pt.y - minY;
            map[y][x] = 'V';
        }

        // 标记障碍物
        for (Point pt : obstacles) {
            int x = pt.x - minX;
            int y = pt.y - minY;
            map[y][x] = '#';
        }

        // 打印地图（添加行偏移实现蜂窝效果）
        System.out.println("蜂窝地图可视化 (层数: " + level + "):");
        for (int y = 0; y < mapHeight; y++) {
            StringBuilder sb = new StringBuilder();
            // 偶数行左偏移，形成蜂窝排列
            if (y % 2 == 0) {
                sb.append(" ");
            }
            for (char c : map[y]) {
                sb.append(c).append(" ");
            }
            System.out.println(sb.toString().trim());
        }
    }

    // 静态生成方法
    public static HexMapResult generateHexMapWithVerticesAndObstacles(
            int level, double fillPercentage, int vertexCount, int obstacleCount, long seed) {
        HexGridGenerator generator = new HexGridGenerator(level, fillPercentage, seed);
        return generator.generateHexMap(vertexCount, obstacleCount);
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试生成不同层数的蜂窝
        testLevel(1);  // 仅中心节点
        testLevel(2);  // 中心+第一圈
        testLevel(3);  // 中心+第一圈+第二圈
    }

    private static void testLevel(int level) {
        System.out.println("\n=== 测试层数: " + level + " ===");
        double fillPercentage = 100;  // 100%填充
        long seed = 12345;             // 固定种子便于复现
        int vertexCount = 6;
        int obstacleCount = 0;

        HexMapResult result = generateHexMapWithVerticesAndObstacles(
                level, fillPercentage, vertexCount, obstacleCount, seed
        );

        // 打印生成的节点数和坐标
        System.out.println("生成节点数: " + result.hexPoints.size());
        System.out.println("节点坐标:");
        for (Point pt : result.hexPoints) {
            System.out.print("(" + pt.x + "," + pt.y + ") ");
        }
        System.out.println();

        // 打印地图
        HexGridGenerator generator = new HexGridGenerator(level, fillPercentage, seed);
        generator.printHexMap(result.hexPoints, result.vertices, result.obstacles);
    }
}
package com.arcane.research.ui.component.element;

import com.arcane.research.enums.Element;
import com.arcane.research.model.ResearchNote;
import com.arcane.research.model.ResearchNode;
import com.arcane.research.service.ElementConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElementResearchPanel extends JPanel {
    // 当前的研究笔记
    public ResearchNote currentResearchNote;
    // 选中的研究节点列表
    public List<ResearchNode> selectedNodes = new ArrayList<>();
    // 目标研究节点
    public ResearchNode targetNode = null;

    // 节点大小和间距配置
    public int NODE_SIZE = 40;
    public double HEX_SCALE = 1.0;
    public double HEX_WIDTH;
    public double HEX_HEIGHT;

    /**
     * 构造函数
     * @param currentResearchNote 当前的研究笔记
     */
    public ElementResearchPanel(ResearchNote currentResearchNote) {
        this.currentResearchNote = currentResearchNote;
        updateHexagonDimensions();
        setBackground(new Color(20, 20, 40));
    }

    /**
     * 重写绘制组件的方法，用于绘制网格、连接、节点和选择指示器
     * @param g 图形上下文对象
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(20, 20, 40));
        g2d.fillRect(0, 0, getWidth(), getHeight());
//        drawGrid(g2d);
        drawConnections(g2d);
        drawResearchNodes(g2d);
//        drawSelectionIndicators(g2d);

        // 绘制目标节点高亮
        if (targetNode != null) {
            drawTargetNodeHighlight(g2d, targetNode);
        }
    }

    /**
     * 绘制目标节点的高亮效果
     * @param g2d 二维图形上下文对象
     * @param node 目标节点
     */
    public void drawTargetNodeHighlight(Graphics2D g2d, ResearchNode node) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        Point pixel = hexToPixel(node.getX(), node.getY(), centerX, centerY);

        // 可放置节点高亮为绿色，不可放置为红色
        if (node.getElement() == null || node.getElement() == Element.PLACEHOLDER) {
            g2d.setColor(new Color(0, 255, 0, 100)); // 绿色高亮
        } else {
            g2d.setColor(new Color(255, 0, 0, 100)); // 红色高亮
        }
        g2d.setStroke(new BasicStroke(4));
        int size = NODE_SIZE + 10;
        g2d.drawOval(pixel.x - size/2, pixel.y - size/2, size, size);
    }

    /**
     * 绘制六边形网格
     * @param g2d 二维图形上下文对象
     */
    public void drawGrid(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // 绘制六边形网格
//        for (int q = -5; q <= 5; q++) {
//            for (int r = -5; r <= 5; r++) {
//                if (Math.abs(q + r) <= 5) {
//                    drawHexagon(g2d, q, r, centerX, centerY, true);
//                }
//            }
//        }
//        for (ResearchNode node : currentResearchNote.getNodes()) {
//
//        }
        currentResearchNote.getConnections().forEach((node, connections) -> {
            int q = node.getX();
            int r = node.getY();
//            currentResearchNote.addNode(new ResearchNode(Element.PLACEHOLDER, q, r));
//            drawHexagon(g2d, q, r, centerX, centerY, true);
        });

    }

    /**
     * 绘制节点之间的连接
     * @param g2d 二维图形上下文对象
     */
    public void drawConnections(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        for (Map.Entry<ResearchNode, Set<ResearchNode>> entry : currentResearchNote.getConnections().entrySet()) {
            ResearchNode fromNode = entry.getKey();
            Point fromPixel = hexToPixel(fromNode.getX(), fromNode.getY(), centerX, centerY);

            for (ResearchNode toNode : entry.getValue()) {
                Point toPixel = hexToPixel(toNode.getX(), toNode.getY(), centerX, centerY);

                if (fromNode.getElement() == Element.PLACEHOLDER || toNode.getElement() == Element.PLACEHOLDER) {
                    continue;
                }
                if (!ElementConnectionManager.canConnect(fromNode.getElement(), toNode.getElement())) {
                    continue;
                }
                // 绘制连接线
                g2d.setColor(new Color(100, 100, 200, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(fromPixel.x, fromPixel.y, toPixel.x, toPixel.y);
            }
        }
    }

    /**
     * 绘制研究节点
     * @param g2d 二维图形上下文对象
     */
    public void drawResearchNodes(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        for (ResearchNode node : currentResearchNote.getNodes()) {
            Element element = node.getElement();

//            if(currentResearchNote.getConnections().containsKey(node)){
//                Color color = new Color(element.getColor());
//                drawHexagon(g2d, node.getX(), node.getY(), centerX, centerY, true);
//                g2d.setColor(color);
//                g2d.setStroke(new BasicStroke(2));
//                drawHexagonBorder(g2d, node.getX(), node.getY(), centerX, centerY);
//
//                // 绘制元素名称
//                g2d.setColor(Color.WHITE);
//                g2d.setFont(new Font("微软雅黑", Font.BOLD, 12));
//                String name = element.getName();
//                Point pixel = hexToPixel(node.getX(), node.getY(), centerX, centerY);
//                FontMetrics metrics = g2d.getFontMetrics();
//                int nameX = pixel.x - metrics.stringWidth(name) / 2;
//                int nameY = pixel.y + metrics.getHeight() / 4;
//                g2d.drawString(name, nameX, nameY);
//                continue;
//            }

            // 绘制PLACEHOLDER节点（空心六边形）
            if (element == Element.PLACEHOLDER) {
                // 渲染多了卡顿
//                drawHexagonBorder(g2d, node.getX(), node.getY(), centerX, centerY);
                drawHexagon(g2d, node.getX(), node.getY(), centerX, centerY, true);

                continue;
            }

            // 绘制普通元素节点
            if (element != null) {
                Color color = new Color(element.getColor());
                drawHexagon(g2d, node.getX(), node.getY(), centerX, centerY, true);
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2));
                drawHexagonBorder(g2d, node.getX(), node.getY(), centerX, centerY);

                // 绘制元素名称
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 12));
                String name = element.getName();
                Point pixel = hexToPixel(node.getX(), node.getY(), centerX, centerY);
                FontMetrics metrics = g2d.getFontMetrics();
                int nameX = pixel.x - metrics.stringWidth(name) / 2;
                int nameY = pixel.y + metrics.getHeight() / 4;
                g2d.drawString(name, nameX, nameY);
            }
        }
    }

    /**
     * 绘制选中节点的选择指示器
     * @param g2d 二维图形上下文对象
     */
    public void drawSelectionIndicators(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        for (ResearchNode node : selectedNodes) {
            // 绘制选择指示器
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(3));
            drawHexagonBorder(g2d, node.getX(), node.getY(), centerX, centerY);
        }
    }

    /**
     * 绘制六边形
     * @param g2d 二维图形上下文对象
     * @param q 六边形的q坐标
     * @param r 六边形的r坐标
     * @param centerX 中心的x坐标
     * @param centerY 中心的y坐标
     * @param filled 是否填充
     */
    public void drawHexagon(Graphics2D g2d, int q, int r, int centerX, int centerY, boolean filled) {
        Point pixel = hexToPixel(q, r, centerX, centerY);
        Polygon hexagon = createHexagon(pixel.x, pixel.y);

        if (filled) {
            g2d.setColor(new Color(60, 60, 100, 100));
            g2d.fill(hexagon);
        } else {
            g2d.setColor(new Color(40, 40, 80, 50));
            g2d.draw(hexagon);
        }
    }

    /**
     * 绘制六边形的边框
     * @param g2d 二维图形上下文对象
     * @param q 六边形的q坐标
     * @param r 六边形的r坐标
     * @param centerX 中心的x坐标
     * @param centerY 中心的y坐标
     */
    public void drawHexagonBorder(Graphics2D g2d, int q, int r, int centerX, int centerY) {
        Point pixel = hexToPixel(q, r, centerX, centerY);
        Polygon hexagon = createHexagon(pixel.x, pixel.y);
        g2d.draw(hexagon);
    }

    /**
     * 将六边形坐标转换为像素坐标
     * @param q 六边形的q坐标
     * @param r 六边形的r坐标
     * @param centerX 中心的x坐标
     * @param centerY 中心的y坐标
     * @return 像素坐标点
     */
    public Point hexToPixel(int q, int r, int centerX, int centerY) {
        double x = HEX_WIDTH * (3.0 / 2 * q);
        double y = HEX_WIDTH * (Math.sqrt(3) / 2 * q + Math.sqrt(3) * r);
        return new Point(centerX + (int) x, centerY + (int) y);
    }

    /**
     * 创建六边形多边形对象
     * @param x 六边形中心的x坐标
     * @param y 六边形中心的y坐标
     * @return 六边形多边形对象
     */
    public Polygon createHexagon(int x, int y) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];

        for (int i = 0; i < 6; i++) {
            double angle = 2.0 * Math.PI * i / 6;
            xPoints[i] = (int) (x + NODE_SIZE * Math.cos(angle));
            yPoints[i] = (int) (y + NODE_SIZE * Math.sin(angle));
        }

        return new Polygon(xPoints, yPoints, 6);
    }

    /**
     * 更新六边形的尺寸
     */
    public void updateHexagonDimensions() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenSize.width < 1024) {
            NODE_SIZE = 30;
        } else if (screenSize.width < 1600) {
            NODE_SIZE = 40;
        } else {
            NODE_SIZE = 50;
        }
        HEX_SCALE = 1.0;
        HEX_WIDTH = NODE_SIZE * HEX_SCALE;
        HEX_HEIGHT = HEX_WIDTH * Math.sqrt(3) / 2;
    }

    /**
     * 获取指定点所在的研究节点
     * @param point 点对象
     * @return 研究节点，如果不存在则返回null
     */
    public ResearchNode getNodeAtPoint(Point point) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        for (ResearchNode node : currentResearchNote.getNodes()) {
            Point pixel = hexToPixel(node.getX(), node.getY(), centerX, centerY);
            int distance = (int) Math.sqrt(
                    Math.pow(point.x - pixel.x, 2) + Math.pow(point.y - pixel.y, 2));
            if (distance <= NODE_SIZE) {
                return node;
            }
        }

        return null;
    }

    /**
     * 处理元素拖放事件
     * @param element 拖放的元素
     * @param targetNode 目标节点
     */
    public void handleElementDrop(Element element, ResearchNode targetNode) {
        if (targetNode.getElement() == null || targetNode.getElement() == Element.PLACEHOLDER) {
            targetNode.setElement(element);
            checkAndConnectNeighbors(targetNode);

            // 强制触发完整重绘（元素 + 连接）
            repaint();
            // 新增：放置元素后清除目标节点
            this.targetNode = null;
        }
    }

    // ElementResearchPanel.java
    public void logPlaceableNodes() {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        System.out.println("可放置元素的槽位信息：");
        for (ResearchNode node : currentResearchNote.getNodes()) {
            Point pixel = hexToPixel(node.getX(), node.getY(), centerX, centerY);
            System.out.println("坐标（六边形坐标系）：(" + node.getX() + ", " + node.getY() +
                    ")，像素坐标：(" + pixel.x + ", " + pixel.y +
                    ")，当前元素状态：" + (node.getElement() == null ? "null" : node.getElement().getName()));
        }
    }

    /**
     * 检查相邻节点并连接可以连接的节点
     * @param node 要检查的节点
     */
    private void checkAndConnectNeighbors(ResearchNode node) {
        int q = node.getX();
        int r = node.getY();
        // 六边形六个相邻节点的偏移量
        int[][] neighbors = {
                {1, 0}, {1, -1}, {0, -1},
                {-1, 0}, {-1, 1}, {0, 1}
        };

        for (int[] offset : neighbors) {
            int neighborQ = q + offset[0];
            int neighborR = r + offset[1];
            ResearchNode neighbor = findNode(neighborQ, neighborR);
            if (neighbor != null && neighbor.getElement() != null && neighbor.getElement() != Element.PLACEHOLDER) {
                if (ElementConnectionManager.canConnect(node.getElement(), neighbor.getElement())) {
                    currentResearchNote.addConnection(node, neighbor);
                }
            }
        }
    }

    /**
     * 根据坐标查找研究节点
     * @param q 节点的q坐标
     * @param r 节点的r坐标
     * @return 研究节点，如果不存在则返回null
     */
    private ResearchNode findNode(int q, int r) {
        for (ResearchNode node : currentResearchNote.getNodes()) {
            if (node.getX() == q && node.getY() == r) {
                return node;
            }
        }
        return null;
    }
}
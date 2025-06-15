package com.arcane.research.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

// 【新增】自定义绘制组件，专门负责标题、游戏名称绘制
public class TitleDrawer extends JComponent {
    /**
     * 重写绘制组件的方法，用于绘制游戏名称和主标题
     * @param g 图形上下文对象
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
//            g2d.setRenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2d.setRenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 绘制游戏名称（上方英文）
        drawGameName(g2d);
        // 绘制主标题（神秘研究）
        drawMainTitle(g2d);
    }

    /**
     * 绘制游戏名称，包括渐变、发光效果和装饰元素
     * @param g2d 二维图形上下文对象
     */
    public void drawGameName(Graphics2D g2d) {
        String gameName = "ARCANE RESEARCH";
        Font nameFont = new Font("Arial", Font.BOLD, 24);
        g2d.setFont(nameFont);

        FontMetrics metrics = g2d.getFontMetrics(nameFont);
        Rectangle2D bounds = metrics.getStringBounds(gameName, g2d);
        int x = (getWidth() - (int) bounds.getWidth()) / 2;
        int y = (getHeight() / 2) - 100; // 调整垂直位置，确保在上方

        // 渐变、发光等效果（保持原有逻辑，按需微调）
        GradientPaint gradient = new GradientPaint(
                x, y - metrics.getAscent(),
                new Color(255, 215, 0),
                x, y + metrics.getDescent(),
                new Color(184, 134, 11));

        for (int i = 0; i < 5; i++) {
            float alpha = 0.1f - (i * 0.02f);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.WHITE);
            g2d.setFont(nameFont.deriveFont(Font.BOLD, 24 + (i * 0.5f)));
            g2d.drawString(gameName, x, y);
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setPaint(gradient);
        g2d.drawString(gameName, x, y);

        // 装饰线条、星星（保持原有逻辑）
        int lineLength = (int) bounds.getWidth() + 40;
        g2d.setColor(new Color(255, 215, 0, 150));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(x - 20, y + 15, x - 20 + lineLength, y + 15);

//            drawStar(g2d, x - 30, y, 8, 5, new Color(255, 215, 0, 200));
        drawStar(g2d, x + (int) bounds.getWidth() + 25, y - 17, 8, 5, new Color(255, 215, 0, 200));
    }

    /**
     * 绘制主标题，包括渐变、发光效果和金色边框
     * @param g2d 二维图形上下文对象
     */
    public void drawMainTitle(Graphics2D g2d) {
        String text = "神秘研究";
        Font font = new Font("华文琥珀", Font.BOLD, 64);
        g2d.setFont(font);

        FontMetrics metrics = g2d.getFontMetrics(font);
        Rectangle2D bounds = metrics.getStringBounds(text, g2d);
        int x = (getWidth() - (int) bounds.getWidth()) / 2;
        int y = (getHeight() / 2) + metrics.getAscent(); // 主标题垂直居中

        GradientPaint gradient = new GradientPaint(
                x, y - metrics.getAscent(),
                new Color(255, 0, 200),
                x, y + metrics.getDescent(),
                new Color(120, 0, 200));

        for (int i = 0; i < 10; i++) {
            float alpha = 0.1f - (i * 0.01f);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.WHITE);
            g2d.setFont(font.deriveFont(Font.BOLD, 64 + (i * 0.5f)));
            g2d.drawString(text, x, y);
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setPaint(gradient);
        g2d.drawString(text, x, y);

        g2d.setColor(new Color(255, 215, 0, 180));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawString(text, x, y);

//            drawDecorations(g2d, x, y, metrics);
    }

    /**
     * 绘制装饰元素，如星星和圆形
     * @param g2d 二维图形上下文对象
     * @param x 文本的x坐标
     * @param y 文本的y坐标
     * @param metrics 字体度量对象
     */
    public void drawDecorations(Graphics2D g2d, int x, int y, FontMetrics metrics) {
        int textWidth = (int) metrics.getStringBounds("神秘研究", g2d).getWidth();
        drawStar(g2d, x - 40, y - metrics.getAscent() / 2, 15, 5, new Color(255, 215, 0, 180));
        drawStar(g2d, x + textWidth + 25, y - metrics.getAscent() / 2, 15, 5, new Color(255, 215, 0, 180));
        drawCircle(g2d, x + textWidth / 2, y - metrics.getAscent() - 30, 10, new Color(255, 255, 255, 150));
        drawCircle(g2d, x + textWidth / 2, y + metrics.getDescent() + 20, 8, new Color(255, 255, 255, 150));
    }

    /**
     * 绘制星星
     * @param g2d 二维图形上下文对象
     * @param x 星星的x坐标
     * @param y 星星的y坐标
     * @param size 星星的大小
     * @param points 星星的角数
     * @param color 星星的颜色
     */
    public void drawStar(Graphics2D g2d, int x, int y, int size, int points, Color color) {
        int[] xPoints = new int[points * 2];
        int[] yPoints = new int[points * 2];
        double angle = Math.PI / 2;
        double step = Math.PI / points;
        for (int i = 0; i < points * 2; i++) {
            double radius = (i % 2 == 0) ? size : size / 2;
            xPoints[i] = (int) (x + radius * Math.cos(angle));
            yPoints[i] = (int) (y - radius * Math.sin(angle));
            angle += step;
        }
        g2d.setColor(color);
        g2d.fillPolygon(xPoints, yPoints, points * 2);
    }

    /**
     * 绘制圆形
     * @param g2d 二维图形上下文对象
     * @param x 圆形的x坐标
     * @param y 圆形的y坐标
     * @param radius 圆形的半径
     * @param color 圆形的颜色
     */
    public void drawCircle(Graphics2D g2d, int x, int y, int radius, Color color) {
        g2d.setColor(color);
        g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}
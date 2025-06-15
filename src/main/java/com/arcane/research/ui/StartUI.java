package com.arcane.research.ui;

import com.arcane.research.enums.Element;
import com.arcane.research.enums.Elements;
import com.arcane.research.ui.component.TitleDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * 开始界面
 */
public class StartUI extends BaseUI {
    private JLabel titleLabel;
    private JButton startButton;
    private JButton settingsButton;
    private JButton exitButton;

    public StartUI(String title) {
        super(title);
        initComponents();
        layoutComponents();
        addEventListeners();

        // 设置窗口大小
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        new Elements();

        System.out.println(Arrays.toString(Element.getAllElements().toArray()));
        // 创建自定义标题标签
        titleLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // 绘制游戏名称（在标题上方）
                drawGameName(g2d);

                // 绘制原有标题
                drawMainTitle(g2d);
            }

            private void drawGameName(Graphics2D g2d) {
                String gameName = "ARCANE RESEARCH";
                Font nameFont = new Font("Arial", Font.BOLD, 24);
                g2d.setFont(nameFont);

                // 计算文本尺寸
                FontMetrics metrics = g2d.getFontMetrics(nameFont);
                Rectangle2D bounds = metrics.getStringBounds(gameName, g2d);
                int x = (getWidth() - (int) bounds.getWidth()) / 2;
                int y = (getHeight() - (int) bounds.getHeight()) / 2 - 100; // 标题上方100像素

                // 创建金色渐变
                GradientPaint gradient = new GradientPaint(
                        x, y - metrics.getAscent(),
                        new Color(255, 215, 0),
                        x, y + metrics.getDescent(),
                        new Color(184, 134, 11));

                // 绘制发光效果
                for (int i = 0; i < 5; i++) {
                    float alpha = 0.1f - (i * 0.02f);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.setFont(nameFont.deriveFont(Font.BOLD, 24 + (i * 0.5f)));
                    g2d.drawString(gameName, x, y);
                }

                // 重置合成模式
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                // 绘制渐变文本
                g2d.setPaint(gradient);
                g2d.setFont(nameFont);
                g2d.drawString(gameName, x, y);

                // 绘制装饰线条
                int lineLength = (int) bounds.getWidth() + 40;
                g2d.setColor(new Color(255, 215, 0, 150));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawLine(x - 20, y + 15, x - 20 + lineLength, y + 15);

                // 绘制装饰星星
                drawStar(g2d, x - 30, y, 8, 5, new Color(255, 215, 0, 200));
                drawStar(g2d, x + (int) bounds.getWidth() + 10, y, 8, 5, new Color(255, 215, 0, 200));
            }

            private void drawMainTitle(Graphics2D g2d) {
                String text = "神秘研究";
                Font font = new Font("华文琥珀", Font.BOLD, 64);
                g2d.setFont(font);

                // 计算文本尺寸
                FontMetrics metrics = g2d.getFontMetrics(font);
                Rectangle2D bounds = metrics.getStringBounds(text, g2d);
                int x = (getWidth() - (int) bounds.getWidth()) / 2;
                int y = (getHeight() - (int) bounds.getHeight()) / 2 + metrics.getAscent();

                // 创建渐变
                GradientPaint gradient = new GradientPaint(
                        x, y - metrics.getAscent(),
                        new Color(255, 0, 200),
                        x, y + metrics.getDescent(),
                        new Color(120, 0, 200));

                // 绘制发光效果
                for (int i = 0; i < 10; i++) {
                    float alpha = 0.1f - (i * 0.01f);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.setFont(font.deriveFont(Font.BOLD, 64 + (i * 0.5f)));
                    g2d.drawString(text, x, y);
                }

                // 重置合成模式
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                // 绘制渐变文本
                g2d.setPaint(gradient);
                g2d.setFont(font);
                g2d.drawString(text, x, y);

                // 绘制金色边框
                g2d.setColor(new Color(255, 215, 0, 180));
                g2d.setStroke(new BasicStroke(2.0f));
                g2d.drawString(text, x, y);

                // 绘制装饰元素
                drawDecorations(g2d, x, y, metrics);
            }

            private void drawDecorations(Graphics2D g2d, int x, int y, FontMetrics metrics) {
                int textWidth = (int) metrics.getStringBounds("神秘研究", g2d).getWidth();

                // 左侧装饰
                drawStar(g2d, x - 40, y - metrics.getAscent() / 2, 15, 5, new Color(255, 215, 0, 180));

                // 右侧装饰
                drawStar(g2d, x + textWidth + 25, y - metrics.getAscent() / 2, 15, 5, new Color(255, 215, 0, 180));

                // 上方装饰
                drawCircle(g2d, x + textWidth / 2, y - metrics.getAscent() - 30, 10, new Color(255, 255, 255, 150));

                // 下方装饰
                drawCircle(g2d, x + textWidth / 2, y + metrics.getDescent() + 20, 8, new Color(255, 255, 255, 150));
            }

            private void drawStar(Graphics2D g2d, int x, int y, int size, int points, Color color) {
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

            private void drawCircle(Graphics2D g2d, int x, int y, int radius, Color color) {
                g2d.setColor(color);
                g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
            }
        };

        // 按钮
        startButton = createButton("开始游戏", 24);
        settingsButton = createButton("游戏设置", 24);
        exitButton = createButton("退出游戏", 24);
    }

    private JButton createButton(String text, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 100));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 150), 2));
        button.setPreferredSize(new Dimension(200, 50));

        // 按钮悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 70, 130));
                button.setForeground(Color.LIGHT_GRAY);
                // 添加悬停动画
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(50, 50, 100));
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 150), 2));
            }
        });

        return button;
    }

    private void layoutComponents() {
        // 创建主面板
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // 绘制背景渐变
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(20, 10, 30),
                        0, getHeight(), new Color(5, 5, 20));

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // 绘制星星背景
                drawStars(g2d);
            }

            private void drawStars(Graphics2D g2d) {
                Random random = new Random(42); // 固定种子以获得一致的星星图案
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                for (int i = 0; i < 100; i++) {
                    int x = random.nextInt(getWidth());
                    int y = random.nextInt(getHeight());
                    int size = random.nextInt(2) + 1;
                    int alpha = random.nextInt(150) + 100;

                    g2d.setColor(new Color(255, 255, 255, alpha));
                    g2d.fillOval(x, y, size, size);
                }
            }
        };

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 添加【自定义绘制组件】，占满垂直空间
        TitleDrawer titleDrawer = new TitleDrawer();
        titleDrawer.setPreferredSize(new Dimension(0, 300)); // 预留标题绘制高度
        titleDrawer.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleDrawer);

        // 添加按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(exitButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalGlue());

        // 添加版本信息
        JLabel versionLabel = new JLabel("v0.0.1 beta");
        versionLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(150, 150, 150));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        versionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(versionLabel);

        // 添加主面板到窗口
        add(mainPanel);
    }

    private void addEventListeners() {
        // 开始按钮事件
        startButton.addActionListener(e -> {
            // 切换到游戏界面
            switchToUI(GameUI.class);
        });

        // 设置按钮事件
        settingsButton.addActionListener(e -> {
            // 切换到设置界面
            switchToUI(SettingsUI.class);
        });

        // 退出按钮事件
        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "确定要退出游戏吗？",
                    "退出确认",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }
}
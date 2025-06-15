package com.arcane.research.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CharSquareComponent extends JComponent {
    private char displayChar; // 要显示的字符
    private int squareSize;   // 正方形边长
    private Color backgroundColor = new Color(40, 40, 80); // 默认背景色
    private Color hoverColor = new Color(70, 70, 130);     // 悬停颜色
    public boolean isHovered = false;

    public CharSquareComponent(char ch, int size) {
        this.displayChar = ch;
        this.squareSize = size;
        setPreferredSize(new Dimension(squareSize, squareSize));
        setMinimumSize(new Dimension(squareSize, squareSize));
        setMaximumSize(new Dimension(squareSize, squareSize));
        setOpaque(false);
        // 添加鼠标悬停事件
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    // 添加 getSquareSize 方法
    public int getSquareSize() {
        return squareSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 绘制背景
        g2d.setColor(isHovered ? hoverColor : backgroundColor);
        g2d.fillRoundRect(0, 0, squareSize, squareSize, 8, 8); // 圆角矩形
        // 绘制边框
        g2d.setColor(new Color(80, 80, 150));
        g2d.drawRoundRect(0, 0, squareSize - 1, squareSize - 1, 8, 8);
        // 绘制字符
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("微软雅黑", Font.BOLD, squareSize / 2));
        FontMetrics fm = g2d.getFontMetrics();
        int charWidth = fm.charWidth(displayChar);
        int charHeight = fm.getAscent();
        int x = (squareSize - charWidth) / 2;
        int y = (squareSize + charHeight) / 2 - fm.getDescent();
        g2d.drawString(String.valueOf(displayChar), x, y);
    }

    // 可选：设置字符
    public void setDisplayChar(char ch) {
        this.displayChar = ch;
        repaint();
    }

    // 可选：获取当前字符
    public char getDisplayChar() {
        return displayChar;
    }
}
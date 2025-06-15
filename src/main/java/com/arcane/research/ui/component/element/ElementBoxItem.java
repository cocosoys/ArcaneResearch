package com.arcane.research.ui.component.element;

import com.arcane.research.enums.Element;
import com.arcane.research.model.ResearchPool;
import com.arcane.research.ui.component.CharSquareComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

public class ElementBoxItem extends CharSquareComponent implements DragGestureListener, MouseMotionListener {
    private final Element element;
    private ResearchPool researchPool;
    private ElementTooltip tooltip; // 悬浮提示框
    private ElementBoxPanel elementBoxPanel; // 要素箱面板引用

    public ElementBoxItem(Element element, int size, ResearchPool researchPool, ElementBoxPanel elementBoxPanel) {
        super(!element.getName().isEmpty() ? element.getName().charAt(0) : ' ', size);
        this.element = element;
        this.researchPool = researchPool;
        this.elementBoxPanel = elementBoxPanel;
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size));

        // 添加鼠标监听器
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                showTooltip(e);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                hideTooltip();
                repaint();
                clearHighlights();
            }
        });

        addMouseMotionListener(this);

        DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);
    }

    private Color highlightColor = null;

    // 高亮显示指定元素
    public void highlightElement(Element elementToHighlight, Color color) {
        if (element == elementToHighlight) {
            highlightColor = color;
            repaint();
        }
    }

    // 清除高亮
    public void clearHighlight() {
        highlightColor = null;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制要素名称首字母和研究点数
        super.paintComponent(g);
        int pointCount = researchPool.getResearchPointCount(element);
        if (pointCount > 0) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("微软雅黑", Font.BOLD, getSquareSize() / 4));
            String pointText = "x" + pointCount;
            int textWidth = g2d.getFontMetrics().stringWidth(pointText);
            g2d.drawString(pointText, getSquareSize() - textWidth - 5, getSquareSize() - 5);
        }

        // 绘制高亮边框
        if (isHovered || highlightColor != null) {
            Color borderColor = highlightColor != null ? highlightColor : new Color(0, 255, 0, 150);
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(0, 0, getSquareSize() - 1, getSquareSize() - 1, 8, 8);
        }
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        try {
            if (researchPool.getResearchPointCount(element) >= 1) {
                ElementTransferable transferable = new ElementTransferable(element);
                dge.startDrag(DragSource.DefaultCopyNoDrop, transferable, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // 鼠标移动事件已在mouseEntered中处理
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // 鼠标拖动事件由DragGestureListener处理
    }

    // 显示悬浮提示框
    private void showTooltip(MouseEvent e) {
        if (tooltip == null) {
            tooltip = new ElementTooltip(this, elementBoxPanel);
        }
        tooltip.showElementInfo(element);

        // 获取按钮在屏幕上的位置
        Point buttonPos = getLocationOnScreen();
        int buttonWidth = getWidth();
        int buttonHeight = getHeight();

        // 获取悬浮窗的尺寸
        Dimension tooltipSize = tooltip.getSize();

        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 计算悬浮窗的理想位置（默认显示在按钮右侧）
        int tooltipX = buttonPos.x + buttonWidth + 10;
        int tooltipY = buttonPos.y;

        // 检查右侧是否超出屏幕
        if (tooltipX + tooltipSize.width > screenSize.width) {
            // 右侧超出，显示在按钮左侧
            tooltipX = buttonPos.x - tooltipSize.width - 10;
            // 确保左侧有足够空间
            if (tooltipX < 0) {
                tooltipX = 10; // 至少距离左侧10像素
            }
        }

        // 检查底部是否超出屏幕
        if (tooltipY + tooltipSize.height > screenSize.height) {
            // 底部超出，显示在按钮上方
            tooltipY = buttonPos.y - tooltipSize.height - 10;
            // 确保上方有足够空间
            if (tooltipY < 0) {
                tooltipY = screenSize.height - tooltipSize.height - 10;
            }
        }

        // 设置悬浮窗位置
        tooltip.setLocation(tooltipX, tooltipY);
        tooltip.setVisible(true);
    }

    // 隐藏悬浮提示框
    private void hideTooltip() {
        if (tooltip != null) {
            tooltip.setVisible(false);
        }
    }

    // 清除所有高亮
    public void clearHighlights() {
        for (ElementBoxItem item : elementBoxPanel.elementButtons.values()) {
            item.repaint();
        }
    }

    // 高亮显示指定元素
    public void highlightElement(Element elementToHighlight) {
        ElementBoxItem item = elementBoxPanel.elementButtons.get(elementToHighlight);
        if (item != null) {
            // 重绘以显示高亮边框
            item.repaint();
        }
    }

    public Element getElement() {
        return element;
    }
}
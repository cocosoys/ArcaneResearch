package com.arcane.research.ui.component;

import java.awt.*;
import java.util.Map;
import java.util.WeakHashMap;

public class CustomButtonLayout implements LayoutManager2 {
    private int rows = 5;         // 每列显示的元素数量
    private int hGap = 10;           // 水平间距
    private int vGap = 10;           // 垂直间距
    private Map<Container, Insets> insetsMap = new WeakHashMap<>();

    @Override
    public void addLayoutComponent(String name, Component comp) {}

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof Integer) {
            rows = (Integer) constraints;
        }
    }

    @Override
    public void removeLayoutComponent(Component comp) {}

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = getInsets(parent);
            int n = parent.getComponentCount();
            if (n == 0) return new Dimension(insets.left + insets.right, insets.top + insets.bottom);

            Component comp = parent.getComponent(0);
            Dimension d = comp.getPreferredSize();
            int cellWidth = d.width + hGap;
            int cellHeight = d.height + vGap;

            int columns = (int) Math.ceil((double) n / rows);
            int totalWidth = columns * cellWidth - hGap + insets.left + insets.right;
            int totalHeight = rows * cellHeight - vGap + insets.top + insets.bottom;

            return new Dimension(totalWidth, totalHeight);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = getInsets(parent);
            int n = parent.getComponentCount();
            if (n == 0) return;

            Component comp = parent.getComponent(0);
            Dimension d = comp.getPreferredSize();
            int cellWidth = d.width;
            int cellHeight = d.height;

            int x = insets.left;
            int y = insets.top;
            int currentRow = 0; // 纵向行数
            int currentCol = 0; // 横向列数

            for (int i = 0; i < n; i++) {
                comp = parent.getComponent(i);
                comp.setBounds(
                        x + currentCol * (cellWidth + hGap),
                        y + currentRow * (cellHeight + vGap),
                        cellWidth, cellHeight
                );

                currentRow++;
                if (currentRow >= 5) { // 每列最多5个
                    currentRow = 0;
                    currentCol++;
                }
            }
        }
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {}

    private Insets getInsets(Container parent) {
        Insets insets = insetsMap.get(parent);
        if (insets == null) {
            insets = parent.getInsets();
            insetsMap.put(parent, insets);
        }
        return insets;
    }
}
package com.arcane.research.ui.component.element;

import com.arcane.research.enums.Element;
import com.arcane.research.model.ResearchPool;
import com.arcane.research.ui.component.CustomButtonLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ElementBoxPanel extends JPanel {
    public Map<Element, ElementBoxItem> elementButtons = new HashMap<>();
    private int itemSize = 40;
    private ResearchPool researchPool;

    public ElementBoxPanel(ResearchPool researchPool) {
        this.researchPool = researchPool;
        setLayout(new CustomButtonLayout());
        setBackground(new Color(30, 30, 60));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 150)),
                "要素箱",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("微软雅黑", Font.BOLD, 16),
                Color.WHITE
        ));
        initializeElementBox();
    }

    public void updatePointCounts() {
        for (ElementBoxItem item : elementButtons.values()) {
            item.repaint();
        }
    }

    private void initializeElementBox() {
        removeAll();
        Element[] elements = Element.values();
        for (Element element : elements) {
            ElementBoxItem button = new ElementBoxItem(element, itemSize, researchPool, this);
            add(button);
            elementButtons.put(element, button);
        }
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        synchronized (getTreeLock()) {
            int n = getComponentCount();
            if (n == 0) return new Dimension(0, 0);
            int columns = (int) Math.ceil((double) n / 5);
            int totalWidth = columns * (itemSize + 10) - 10;
            int totalHeight = 5 * (itemSize + 10) - 10;
            return new Dimension(totalWidth, totalHeight);
        }
    }
}
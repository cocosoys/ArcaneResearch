package com.arcane.research.ui.component.element;

import com.arcane.research.enums.Element;
import com.arcane.research.model.ResearchPool;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ElementPoolPanel extends JPanel {
    public ResearchPool researchPool;

    public ElementPoolPanel(ResearchPool researchPool) {
        this.researchPool = researchPool;
        setLayout(new GridLayout(0, 3, 10, 10));
        setBackground(new Color(15, 15, 35));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 100)),
                "研究点数池",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("微软雅黑", Font.BOLD, 16),
                Color.WHITE
        ));
        updatePoolPanel();
    }

    public void updatePoolPanel() {
        removeAll();
        for (Map.Entry<Element, Integer> entry : researchPool.getResearchPoints().entrySet()) {
            JPanel pointPanel = new JPanel(new BorderLayout());
            pointPanel.setBackground(new Color(30, 30, 60));
            pointPanel.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 100)));

            JLabel nameLabel = new JLabel(entry.getKey().getName());
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));

            JLabel countLabel = new JLabel("x" + entry.getValue());
            countLabel.setForeground(Color.WHITE);
            countLabel.setHorizontalAlignment(JLabel.CENTER);
            countLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));

            pointPanel.add(nameLabel, BorderLayout.NORTH);
            pointPanel.add(countLabel, BorderLayout.CENTER);
            add(pointPanel);
        }

        revalidate();
        repaint();
    }
}
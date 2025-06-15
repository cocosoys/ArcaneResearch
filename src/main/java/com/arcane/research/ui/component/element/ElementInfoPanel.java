package com.arcane.research.ui.component.element;

import com.arcane.research.model.ResearchNote;

import javax.swing.*;
import java.awt.*;

public class ElementInfoPanel extends JPanel {
    // 当前的研究笔记
    public ResearchNote currentResearchNote;

    /**
     * 构造函数
     * @param currentResearchNote 当前的研究笔记
     */
    public ElementInfoPanel(ResearchNote currentResearchNote) {
        this.currentResearchNote = currentResearchNote;
        setLayout(new BorderLayout());
        setBackground(new Color(15, 15, 35));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 100)),
                "研究信息",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("微软雅黑", Font.BOLD, 16),
                Color.WHITE
        ));
        updateInfoPanel();
    }

    /**
     * 更新信息面板的内容
     */
    public void updateInfoPanel() {
        removeAll();
        JPanel infoContentPanel = new JPanel();
        infoContentPanel.setLayout(new BoxLayout(infoContentPanel, BoxLayout.Y_AXIS));
        infoContentPanel.setBackground(new Color(30, 30, 60));

        JLabel titleLabel = new JLabel(currentResearchNote.getTitle());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel progressLabel = new JLabel("研究进度: " +
                currentResearchNote.getConnectionCount() + "/" +
                (currentResearchNote.getNodeCount() - 1));
        progressLabel.setForeground(Color.WHITE);
        progressLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        progressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statusLabel = new JLabel("研究状态: " +
                (currentResearchNote.isCompleted() ? "已完成" : "进行中"));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoContentPanel.add(Box.createVerticalStrut(10));
        infoContentPanel.add(titleLabel);
        infoContentPanel.add(Box.createVerticalStrut(10));
        infoContentPanel.add(progressLabel);
        infoContentPanel.add(Box.createVerticalStrut(5));
        infoContentPanel.add(statusLabel);
        infoContentPanel.add(Box.createVerticalGlue());

        add(infoContentPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }
}
package com.arcane.research;

import com.arcane.research.ui.StartUI;

import javax.swing.*;

/**
 * 游戏主类 - 程序入口
 */
public class ArcaneResearchGame {
    public static void main(String[] args) {
        // 设置Swing外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 创建并显示开始界面
        SwingUtilities.invokeLater(() -> new StartUI("神秘研究 - 开始界面"));
    }
}
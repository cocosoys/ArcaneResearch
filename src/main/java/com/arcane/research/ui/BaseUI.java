package com.arcane.research.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 基础界面类 - 所有界面的基类
 */
public abstract class BaseUI extends JFrame {
    // 界面尺寸
    protected static final int WIDTH = 800;
    protected static final int HEIGHT = 600;

    // 鼠标拖动相关变量
    private int x, y;

    public BaseUI(String title) {
        super(title);
        initUI();
    }

    private void initUI() {
        // 去除窗口边框
        setUndecorated(true);
        // 设置背景为透明
        setBackground(new Color(0, 0, 0, 0));

        // 设置窗口大小和位置
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        // 添加鼠标拖动事件，使窗口可以移动
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - x, p.y + e.getY() - y);
            }
        });

        // 添加关闭按钮事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitGame();
            }
        });

        // 设置为可见
        setVisible(true);
    }

    // 退出游戏方法
    protected void exitGame() {
        int choice = JOptionPane.showConfirmDialog(this,
                "确定要退出游戏吗？", "退出确认",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // 界面切换方法
    protected void switchToUI(Class<? extends BaseUI> uiClass) {
        try {
            // 隐藏当前界面
            setVisible(false);
            // 创建并显示新界面
            uiClass.getConstructor(String.class).newInstance(uiClass.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "界面切换失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }


}

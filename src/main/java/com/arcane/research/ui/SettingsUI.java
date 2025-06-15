package com.arcane.research.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

/**
 * 设置界面
 */
public class SettingsUI extends BaseUI {
    private JSlider volumeSlider;
    private JButton applyButton;
    private JButton backButton;

    private Preferences prefs;

    public SettingsUI(String title) {
        super(title);
        prefs = Preferences.userNodeForPackage(SettingsUI.class);
        initComponents();
        layoutComponents();
        addEventListeners();

        // 设置窗口大小为屏幕的80%
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(screenSize.width * 0.8), (int)(screenSize.height * 0.8));
        setLocationRelativeTo(null);
    }

    private void loadSettings() {
        // 从首选项加载设置
        int volume = prefs.getInt("volume", 80);
        volumeSlider.setValue(volume);
    }

    private void saveSettings() {
        // 保存设置到首选项
        prefs.putInt("volume", volumeSlider.getValue());
    }

    private void initComponents() {
        // 音量滑块
        volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 80);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        volumeSlider.setBackground(new Color(10, 10, 30));
        volumeSlider.setForeground(Color.WHITE);

        // 应用按钮
        applyButton = new JButton("应用设置");
        applyButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        applyButton.setForeground(Color.WHITE);
        applyButton.setBackground(new Color(50, 50, 100));
        applyButton.setBorderPainted(false);
        applyButton.setFocusPainted(false);
        applyButton.setOpaque(true);

        // 返回按钮
        backButton = new JButton("返回");
        backButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(50, 50, 100));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);

        // 按钮悬停效果
        setupButtonHoverEffect(applyButton);
        setupButtonHoverEffect(backButton);

        // 加载设置
        loadSettings();
    }

    private void setupButtonHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 70, 130));
                button.setForeground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(50, 50, 100));
                button.setForeground(Color.WHITE);
            }
        });
    }

    private void layoutComponents() {
        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(10, 10, 30));

        // 创建标题标签
        JLabel titleLabel = new JLabel("游戏设置");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        // 创建设置面板
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBackground(new Color(10, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        // 添加音量设置
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel volumeLabel = new JLabel("音量设置:");
        volumeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        volumeLabel.setForeground(Color.WHITE);
        settingsPanel.add(volumeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        settingsPanel.add(volumeSlider, gbc);

        // 添加按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        buttonPanel.setBackground(new Color(10, 10, 30));
        buttonPanel.add(applyButton);
        buttonPanel.add(backButton);

        // 添加组件到主面板
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(settingsPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(buttonPanel);

        // 添加主面板到窗口
        add(mainPanel);
    }

    private void addEventListeners() {
        // 应用按钮事件
        applyButton.addActionListener(e -> {
            applySettings();
        });

        // 返回按钮事件
        backButton.addActionListener(e -> {
            // 切换回开始界面
            switchToUI(StartUI.class);
        });
    }

    private void applySettings() {
        // 保存设置
        saveSettings();

        // 显示应用成功消息
        JOptionPane.showMessageDialog(this,
                "设置已应用！",
                "应用成功",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
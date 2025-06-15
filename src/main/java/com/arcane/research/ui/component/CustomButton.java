package com.arcane.research.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton {
    public CustomButton(String text, int fontSize) {
        super(text);
        setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        setForeground(Color.WHITE);
        setBackground(new Color(50, 50, 100));
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(new Color(80, 80, 150), 2));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(70, 70, 130));
                setForeground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(50, 50, 100));
                setForeground(Color.WHITE);
            }
        });
    }
}
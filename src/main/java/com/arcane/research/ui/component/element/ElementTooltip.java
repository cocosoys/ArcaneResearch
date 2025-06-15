package com.arcane.research.ui.component.element;

import com.arcane.research.enums.Element;
import com.arcane.research.enums.Elements;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ElementTooltip extends JDialog {
    private ElementBoxItem sourceItem;
    private JPanel contentPanel;
    private JLabel titleLabel;
    private JTextArea infoArea;
    private Element currentElement;
    private ElementBoxPanel elementBoxPanel;
    private float originalOpacity = 0.9f; // 原始透明度
    private float hoverOpacity = 0.5f;   // 鼠标悬停时的透明度

    public ElementTooltip(ElementBoxItem sourceItem, ElementBoxPanel elementBoxPanel) {
        this.sourceItem = sourceItem;
        this.elementBoxPanel = elementBoxPanel;
        initComponents();
        setupEventListeners();
    }

    private void initComponents() {
        setUndecorated(true);
        setOpacity(originalOpacity);
        setAlwaysOnTop(true); // 确保悬浮窗始终在顶部

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(30, 30, 60));
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 150)));

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setBackground(new Color(30, 30, 60));
        infoArea.setForeground(Color.WHITE);
        infoArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        contentPanel.add(infoArea, BorderLayout.CENTER);

        add(contentPanel);
        pack();
    }

    private void setupEventListeners() {
        // 鼠标进入信息区域时的处理
        infoArea.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                try {
                    processMouseMove(e);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // 鼠标在悬浮窗上移动时的处理
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                try {
                    processMouseMove(e);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // 鼠标离开悬浮窗时的处理
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                clearHighlights();
                setOpacity(originalOpacity);
            }
        });

        contentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                clearHighlights();
                setOpacity(originalOpacity);
            }
        });
    }

    public void showElementInfo(Element element) {
        currentElement = element;
        titleLabel.setText(element.getName());

        StringBuilder info = new StringBuilder();
        info.append("元素信息:\n\n");

        // 添加元素类型信息
        if (Element.isBaseElement(element)) {
            info.append("类型: 基础元素\n\n");
            info.append("可参与合成的组合元素:\n");
            List<Element> composedElements = findComposedElements(element);
            for (Element composed : composedElements) {
                Element otherElement = findOtherBaseElement(element, composed);
                info.append("- ").append(element.getName()).append(" + ")
                        .append(otherElement.getName()).append(" = ")
                        .append(composed.getName()).append("\n");
            }
        } else {
            info.append("类型: 组合元素\n");
            info.append("合成公式: ").append(element.getBase1().getName())
                    .append(" + ").append(element.getBase2().getName())
                    .append(" = ").append(element.getName()).append("\n\n");

            // 显示由当前组合元素参与合成的更高级元素
            info.append("可参与合成的更高级元素:\n");
            List<Element> composedElements = findComposedElements(element);
            for (Element composed : composedElements) {
                Element otherElement = findOtherBaseElement(element, composed);
                info.append("- ").append(element.getName()).append(" + ")
                        .append(otherElement.getName()).append(" = ")
                        .append(composed.getName()).append("\n");
            }
        }

        infoArea.setText(info.toString());
        pack();
    }

    // 查找由指定元素参与合成的所有元素
    private List<Element> findComposedElements(Element element) {
        List<Element> result = new ArrayList<>();
        Class<?> elementClass = Elements.class;

        try {
            Field[] fields = elementClass.getFields();
            for (Field field : fields) {
                Object value = field.get(null);
                if (value instanceof Element) {
                    Element e = (Element) value;
                    if (!Element.isBaseElement(e) &&
                            (e.getBase1() == element || e.getBase2() == element)) {
                        result.add(e);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // 查找合成时的另一个基础元素
    private Element findOtherBaseElement(Element knownElement, Element composedElement) {
        if (composedElement.getBase1() == knownElement) {
            return composedElement.getBase2();
        } else {
            return composedElement.getBase1();
        }
    }

    // 处理鼠标移动事件
    private void processMouseMove(MouseEvent e) throws BadLocationException {
        // 确保悬浮窗可见
        if (!isVisible()) return;

        JComponent component = (e.getSource() instanceof JComponent) ?
                (JComponent) e.getSource() : contentPanel;
        String text = infoArea.getText();
        int pos = infoArea.viewToModel(e.getPoint());
        int line = infoArea.getLineOfOffset(pos);
        int start = infoArea.getLineStartOffset(line);
        int end = infoArea.getLineEndOffset(line);
        String lineText = text.substring(start, end);

        // 清除之前的高亮
        clearHighlights();

        // 查找元素名称
        for (Element element : Element.getAllElements()) {
            String elementName = element.getName();
            int index = lineText.indexOf(elementName);
            if (index != -1) {
                // 计算鼠标是否在元素名称上
                int nameStart = start + index;
                int nameEnd = nameStart + elementName.length();
                if (pos >= nameStart && pos <= nameEnd) {
                    // 高亮显示该元素
                    highlightElement(element, Color.GREEN);

                    // 检查是否是合成公式中的结果元素
                    if (isResultElementInFormula(lineText, elementName)) {
                        highlightElement(element, Color.RED);
                    }

                    // 降低提示框透明度
                    setOpacity(hoverOpacity);
                    return;
                }
            }
        }

        // 清除高亮并恢复透明度
        setOpacity(originalOpacity);
    }

    // 判断元素是否是合成公式中的结果
    private boolean isResultElementInFormula(String lineText, String elementName) {
        return lineText.contains("= " + elementName);
    }

    // 高亮显示元素
    private void highlightElement(Element element, Color color) {
        ElementBoxItem item = elementBoxPanel.elementButtons.get(element);
        if (item != null) {
            item.highlightElement(element, color);
        }
    }

    // 清除所有高亮
    private void clearHighlights() {
        for (ElementBoxItem item : elementBoxPanel.elementButtons.values()) {
            item.clearHighlight();
        }
    }
}
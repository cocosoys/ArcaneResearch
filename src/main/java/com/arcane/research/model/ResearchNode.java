package com.arcane.research.model;

import com.arcane.research.enums.Element;

import java.awt.*;
import java.util.Objects;

/**
 * 研究节点，代表研究笔记中的六边形格子
 */
public class ResearchNode {
    public Element element;
    public int x;
    public int y;
    private Image elementIcon; // 要素图标

    /**
     * 构造函数
     * @param element 元素类型
     * @param x x坐标
     * @param y y坐标
     */
    public ResearchNode(Element element, int x, int y) {
        this.element = element;
        this.x = x;
        this.y = y;
    }

    /**
     * 获取元素类型
     * @return 元素类型
     */
    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Image getElementIcon() {
        return elementIcon;
    }

    public void setElementIcon(Image elementIcon) {
        this.elementIcon = elementIcon;
    }

    /**
     * 获取x坐标
     * @return x坐标
     */
    public int getX() {
        return x;
    }

    /**
     * 获取y坐标
     * @return y坐标
     */
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchNode that = (ResearchNode) o;
        return x == that.x && y == that.y && element == that.element;
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, x, y);
    }
}
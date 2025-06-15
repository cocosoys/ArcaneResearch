package com.arcane.research.model;

import com.arcane.research.enums.Element;

/**
 * 研究点，研究过程中的资源单位
 */
public class ResearchPoint {
    private Element element;
    private int count;

    /**
     * 构造函数
     * @param element 元素类型
     * @param count 数量
     */
    public ResearchPoint(Element element, int count) {
        this.element = element;
        this.count = count;
    }

    /**
     * 获取元素类型
     * @return 元素类型
     */
    public Element getElement() {
        return element;
    }

    /**
     * 获取数量
     * @return 数量
     */
    public int getCount() {
        return count;
    }

    /**
     * 增加数量
     * @param count 增加的数量
     */
    public void increaseCount(int count) {
        this.count += count;
    }

    /**
     * 减少数量
     * @param count 减少的数量
     * @return 是否减少成功
     */
    public boolean decreaseCount(int count) {
        if (this.count >= count) {
            this.count -= count;
            return true;
        }
        return false;
    }
}
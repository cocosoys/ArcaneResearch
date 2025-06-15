package com.arcane.research.model;

import com.arcane.research.enums.Element;
import java.util.HashMap;
import java.util.Map;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ResearchPool {
    private Map<Element, Integer> researchPoints = new HashMap<>();
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    /**
     * 添加研究点
     * @param element 元素类型
     * @param count 数量
     */
    public void addResearchPoint(Element element, int count) {
        int oldCount = researchPoints.getOrDefault(element, 0);
        researchPoints.put(element, oldCount + count);
        changeSupport.firePropertyChange("researchPoints", oldCount, oldCount + count);
    }

    /**
     * 消耗研究点（两个元素版本）
     * @param element1 第一种元素
     * @param count1 第一种元素数量
     * @param element2 第二种元素
     * @param count2 第二种元素数量
     * @return 是否消耗成功
     */
    public boolean consumeResearchPoint(Element element1, int count1, Element element2, int count2) {
        if (researchPoints.getOrDefault(element1, 0) >= count1 &&
                researchPoints.getOrDefault(element2, 0) >= count2) {
            int oldCount1 = researchPoints.get(element1);
            int oldCount2 = researchPoints.get(element2);
            researchPoints.put(element1, oldCount1 - count1);
            researchPoints.put(element2, oldCount2 - count2);
            changeSupport.firePropertyChange("researchPoints",
                    oldCount1 + oldCount2,
                    (oldCount1 - count1) + (oldCount2 - count2));
            return true;
        }
        return false;
    }

    /**
     * 消耗研究点（单个元素版本）
     * @param element 元素类型
     * @param count 数量
     * @return 是否消耗成功
     */
    public boolean consumeResearchPoint(Element element, int count) {
        return consumeResearchPoint(element, count, element, count);
    }

    /**
     * 获取所有研究点
     * @return 元素到数量的映射
     */
    public Map<Element, Integer> getResearchPoints() {
        return new HashMap<>(researchPoints);
    }

    /**
     * 获取指定元素的研究点数量
     * @param element 元素类型
     * @return 研究点数量
     */
    public int getResearchPointCount(Element element) {
        return researchPoints.getOrDefault(element, 0);
    }

    // 添加属性变化监听方法
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }
}
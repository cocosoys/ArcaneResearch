package com.arcane.research.service;

import com.arcane.research.enums.Element;

/**
 * 元素连接管理服务，处理元素之间的连接逻辑
 */
public class ElementConnectionManager {
    /**
     * 检查两个元素是否可以连接
     * @param element1 第一个元素
     * @param element2 第二个元素
     * @return 是否可以连接
     */
    public static boolean canConnect(Element element1, Element element2) {
        return Element.canConnect(element1, element2);
    }

    /**
     * 获取连接两个元素的研究点消耗
     * @param element1 第一个元素
     * @param element2 第二个元素
     * @return 研究点消耗
     */
    public static int getConnectionCost(Element element1, Element element2) {
        // 基本元素之间的连接消耗为1
        if (isBasicElement(element1) && isBasicElement(element2)) {
            return 1;
        }

        // 基本元素与其他元素的连接消耗为2
        if (isBasicElement(element1) || isBasicElement(element2)) {
            return 2;
        }

        // 其他元素之间的连接消耗为3
        return 3;
    }

    private static boolean isBasicElement(Element element) {
        return element == Element.AIR || element == Element.EARTH ||
                element == Element.FIRE || element == Element.WATER;
    }
}
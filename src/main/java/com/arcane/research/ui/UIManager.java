package com.arcane.research.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * 界面管理器 - 管理界面的创建和切换
 */
public class UIManager {
    private static Map<Class<? extends BaseUI>, BaseUI> uiMap = new HashMap<>();

    public static <T extends BaseUI> T getUI(Class<T> uiClass, String title) {
        if (uiMap.containsKey(uiClass)) {
            return (T) uiMap.get(uiClass);
        }

        try {
            T ui = uiClass.getConstructor(String.class).newInstance(title);
            uiMap.put(uiClass, ui);
            return ui;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建界面失败: " + e.getMessage(), e);
        }
    }

    public static void switchUI(Class<? extends BaseUI> fromClass, Class<? extends BaseUI> toClass) {
        if (uiMap.containsKey(fromClass)) {
            uiMap.get(fromClass).setVisible(false);
        }

        getUI(toClass, toClass.getSimpleName()).setVisible(true);
    }
}
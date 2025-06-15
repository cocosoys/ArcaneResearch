package com.arcane.research.model;

import com.arcane.research.enums.Element;
import java.util.HashSet;
import java.util.Set;

/**
 * 玩家实体，管理玩家的研究进度、资源和已解锁的知识
 */
public class Player {
    private String name;
    private ResearchPool researchPool;
    private Set<String> unlockedRecipes;

    /**
     * 构造函数
     * @param name 玩家名称
     */
    public Player(String name) {
        this.name = name;
        initializePlayer();
    }

    /**
     * 初始化玩家信息
     */
    private void initializePlayer() {
        this.researchPool = new ResearchPool();
        this.unlockedRecipes = new HashSet<>();

        // 初始化默认研究点
        for (Element allElement : Element.getAllElements()) {
            researchPool.addResearchPoint(allElement, 9999);
        }
    }

    /**
     * 解锁配方
     * @param recipeName 配方名称
     */
    public void unlockRecipe(String recipeName) {
        unlockedRecipes.add(recipeName);
    }

    /**
     * 判断是否解锁了配方
     * @param recipeName 配方名称
     * @return 是否已解锁
     */
    public boolean hasUnlockedRecipe(String recipeName) {
        return unlockedRecipes.contains(recipeName);
    }

    /**
     * 获取研究池
     * @return 研究池
     */
    public ResearchPool getResearchPool() {
        return researchPool;
    }

    /**
     * 获取玩家名称
     * @return 玩家名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取已解锁的配方
     * @return 已解锁配方集合
     */
    public Set<String> getUnlockedRecipes() {
        return new HashSet<>(unlockedRecipes);
    }
}
package com.arcane.research.model;

import com.arcane.research.enums.Element;

import java.util.List;

/**
 * 研究卷轴，代表研究成功后的成果
 */
public class ResearchScroll {
    private String title;
    private String description;
    private List<ResearchPoint> rewards;
    private List<String> unlockedRecipes;

    /**
     * 构造函数
     * @param title 卷轴标题
     * @param description 卷轴描述
     * @param rewards 奖励研究点
     * @param unlockedRecipes 解锁的配方
     */
    public ResearchScroll(String title, String description, List<ResearchPoint> rewards, List<String> unlockedRecipes) {
        this.title = title;
        this.description = description;
        this.rewards = rewards;
        this.unlockedRecipes = unlockedRecipes;
    }

    /**
     * 应用奖励到玩家
     * @param player 玩家
     */
    public void applyReward(Player player) {
        // 应用研究点奖励
        for (ResearchPoint reward : rewards) {
            player.getResearchPool().addResearchPoint(reward.getElement(), reward.getCount());
        }

        // 解锁配方
        for (String recipeName : unlockedRecipes) {
            player.unlockRecipe(recipeName);
        }
    }

    /**
     * 获取奖励描述
     * @return 奖励描述
     */
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("【").append(title).append("】\n");
        sb.append(description).append("\n\n");

        if (!rewards.isEmpty()) {
            sb.append("■ 研究点奖励:\n");
            for (ResearchPoint reward : rewards) {
                sb.append("  - ").append(reward.getElement().getName())
                        .append(": x").append(reward.getCount()).append("\n");
            }
        }

        if (!unlockedRecipes.isEmpty()) {
            sb.append("\n■ 解锁配方:\n");
            for (String recipe : unlockedRecipes) {
                sb.append("  - ").append(recipe).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * 获取标题
     * @return 标题
     */
    public String getTitle() {
        return title;
    }
}
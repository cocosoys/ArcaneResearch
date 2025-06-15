package com.arcane.research.enums;

public class Elements {

    // 基础元素 (0层)
    public static final Element AIR = Element.AIR;
    public static final Element EARTH = Element.EARTH;
    public static final Element FIRE = Element.FIRE;
    public static final Element WATER = Element.WATER;
    public static final Element ORDER = Element.ORDER;
    public static final Element ENTROPY = Element.ENTROPY;
    // 第一层组合元素 (1层)
    public static final Element LIGHT = Element.createCompositeElement("光", 0xFFFFE0, AIR, FIRE);
    public static final Element MUD = Element.createCompositeElement("泥", 0x8B4513, EARTH, WATER);
    public static final Element STEAM = Element.createCompositeElement("蒸汽", 0xE0FFFF, WATER, FIRE);
    public static final Element DUST = Element.createCompositeElement("尘土", 0xD2B48C, AIR, EARTH);
    public static final Element LAVA = Element.createCompositeElement("熔岩", 0xFF4500, EARTH, FIRE);
    public static final Element FOG = Element.createCompositeElement("雾气", 0xE6E6FA, AIR, WATER);
    public static final Element ASH = Element.createCompositeElement("灰烬", 0xA9A9A9, FIRE, EARTH);
    public static final Element SALT = Element.createCompositeElement("盐", 0xF0E68C, WATER, EARTH);
    public static final Element WIND = Element.createCompositeElement("气流", 0xADD8E6, AIR, ORDER);
    public static final Element CHAOS = Element.createCompositeElement("混乱", 0x800080, ENTROPY, FIRE);
    public static final Element LIFE = Element.createCompositeElement("生命", 0x00FF00, WATER, ORDER);
    public static final Element ENERGY = Element.createCompositeElement("能量", 0xFFD700, FIRE, ORDER);
    public static final Element VOID = Element.createCompositeElement("虚空", 0x000000, CHAOS, WIND);

    // 第二层组合元素 (2层)
    public static final Element RAIN = Element.createCompositeElement("雨", 0x6495ED, WATER, FOG);
    public static final Element MIST = Element.createCompositeElement("薄雾", 0xFAFAD2, WATER, FOG);
    public static final Element FIRE_STORM = Element.createCompositeElement("烈火", 0xFF6347, FIRE, WIND);
    public static final Element SAND = Element.createCompositeElement("沙", 0xF5DEB3, EARTH, DUST);
    public static final Element ACID = Element.createCompositeElement("酸", 0xBDFCC9, WATER, CHAOS);
    public static final Element PLANT = Element.createCompositeElement("植物", 0x228B22, WATER, LIGHT);
    public static final Element CRYSTAL = Element.createCompositeElement("晶体", 0x00BFFF, EARTH, LIGHT);
    public static final Element ICE = Element.createCompositeElement("冰", 0x00FFFF, WATER, EARTH);
    public static final Element STONE = Element.createCompositeElement("石头", 0x00FFFF, MUD, EARTH);
    public static final Element MAGIC = Element.createCompositeElement("魔法", 0x8A2BE2, ENERGY, VOID);

    // 第三层组合元素 (3层)
    public static final Element METAL = Element.createCompositeElement("金属", 0xC0C0C0, EARTH, ORDER);
    public static final Element GLASS = Element.createCompositeElement("玻璃", 0x87CEEB, SAND, FIRE);
    public static final Element SNOW = Element.createCompositeElement("雪", 0xFFFFFF, ICE, AIR);
    public static final Element STORM = Element.createCompositeElement("风暴", 0x4169E1, WIND, RAIN);
    public static final Element ACID_RAIN = Element.createCompositeElement("酸雨", 0x9ACD32, ACID, RAIN);
    public static final Element FOREST = Element.createCompositeElement("森林", 0x228B22, PLANT, PLANT);
    public static final Element ROCK = Element.createCompositeElement("岩石", 0x808080, EARTH, STONE);
    public static final Element MAGMA = Element.createCompositeElement("岩浆", 0xFF8C00, LAVA, FIRE);
    public static final Element SALT_WATER = Element.createCompositeElement("盐水", 0xF0FFFF, SALT, WATER);
    public static final Element STEAM_CLOUD = Element.createCompositeElement("蒸汽云", 0xE0FFFF, STEAM, AIR);
    public static final Element LIGHTNING = Element.createCompositeElement("闪电", 0x8A2BE2, FIRE_STORM, AIR);
    public static final Element DUST_STORM = Element.createCompositeElement("沙尘暴", 0xD2B48C, DUST, WIND);
    public static final Element ACID_LAKE = Element.createCompositeElement("酸湖", 0x3CB371, ACID, WATER);
    public static final Element ICE_CRYSTAL = Element.createCompositeElement("冰晶", 0xB0E0E6, ICE, CRYSTAL);
    public static final Element MUD_ROCK = Element.createCompositeElement("泥岩", 0x8B4513, MUD, ROCK);
    public static final Element LIGHTNING_STAFF = Element.createCompositeElement("闪电法杖", 0x8A2BE2, LIGHTNING, MAGIC);
    public static final Element GROWTH_POTION = Element.createCompositeElement("生长药剂", 0x00FF00, PLANT, MAGIC);
    public static final Element FLOATING_ROCK = Element.createCompositeElement("浮空石", 0x808080, ROCK, ENERGY);


    // 第四层组合元素 (4层)
    public static final Element IRON = Element.createCompositeElement("铁", 0xC0C0C0, METAL, ROCK);
    public static final Element STEEL = Element.createCompositeElement("钢", 0xBEBEBE, IRON, FIRE);
    public static final Element SNOWSTORM = Element.createCompositeElement("暴风雪", 0xADD8E6, SNOW, STORM);
    public static final Element VOLCANO = Element.createCompositeElement("火山", 0xFF4500, MAGMA, ROCK);
    public static final Element DESERT = Element.createCompositeElement("沙漠", 0xF5DEB3, SAND, DUST_STORM);
    public static final Element OCEAN = Element.createCompositeElement("海洋", 0x1E90FF, WATER, WATER);
    public static final Element MOUNTAIN = Element.createCompositeElement("山脉", 0x808080, ROCK, ROCK);
    public static final Element ICEBERG = Element.createCompositeElement("冰山", 0xB0E0E6, ICE, MOUNTAIN);
    public static final Element SWAMP = Element.createCompositeElement("沼泽", 0x556B2F, MUD, FOREST);
    public static final Element GLACIER = Element.createCompositeElement("冰川", 0xE0FFFF, ICE, SNOW);
    public static final Element MINE = Element.createCompositeElement("矿", 0x808080, METAL, ROCK);
    public static final Element FROZEN_LAKE = Element.createCompositeElement("冰冻湖", 0xB0E0E6, ICE, WATER);
    public static final Element FOREST_FIRE = Element.createCompositeElement("森林火", 0xFF4500, FOREST, FIRE);
    public static final Element STORM_CLOUD = Element.createCompositeElement("雷云", 0x4169E1, STORM, AIR);
    public static final Element FOGGY_MOUNTAIN = Element.createCompositeElement("雾山", 0x808080, FOG, MOUNTAIN);
    public static final Element CAVE = Element.createCompositeElement("洞穴", 0x808080, WATER, MOUNTAIN);
    public static final Element ANIMAL = Element.createCompositeElement("动物", 0xFFA500, LIFE, PLANT);
    public static final Element MAGIC_CRYSTAL = Element.createCompositeElement("魔法晶体", 0x00BFFF, CRYSTAL, MAGIC);
    public static final Element ENERGY_FIELD = Element.createCompositeElement("能量场", 0xFFD700, ENERGY, ORDER);
    public static final Element HUMAN = Element.createCompositeElement("人类", 0xFFFF00, ANIMAL, LIFE);
    public static final Element MAGIC_WEAPON = Element.createCompositeElement("魔法武器", 0xFF4500, METAL, MAGIC);
    public static final Element ENERGY_CLOUD = Element.createCompositeElement("能量云", 0xFFD700, ENERGY_FIELD, AIR);


    // 第五层组合元素 (5层)
    public static final Element IRON_ORE = Element.createCompositeElement("铁矿石", 0xA0522D, IRON, ROCK);
    public static final Element ICE_CAVE = Element.createCompositeElement("冰洞", 0xB0E0E6, ICE, CAVE);
    public static final Element DESERT_OASIS = Element.createCompositeElement("沙漠绿洲", 0x228B22, DESERT, WATER);
    public static final Element OCEAN_CURRENT = Element.createCompositeElement("洋流", 0x1E90FF, OCEAN, WIND);
    public static final Element GLACIER_LAKE = Element.createCompositeElement("冰川湖", 0xB0E0E6, GLACIER, WATER);
    public static final Element MINERAL = Element.createCompositeElement("矿物", 0x808080, METAL, CRYSTAL);
    public static final Element SALT_MINE = Element.createCompositeElement("盐矿", 0xF0E68C, SALT, MINE);
    public static final Element STORM_SURGE = Element.createCompositeElement("风暴潮", 0x4169E1, STORM, OCEAN);
    public static final Element VOLCANIC_ASH = Element.createCompositeElement("火山灰", 0xA9A9A9, VOLCANO, ASH);
    public static final Element FOGGY_FOREST = Element.createCompositeElement("雾林", 0x808080, FOG, FOREST);
    public static final Element CIVILIZATION = Element.createCompositeElement("文明", 0xFFFF00, HUMAN, ORDER);
    public static final Element ANCIENT_MAGIC = Element.createCompositeElement("古代魔法", 0x8A2BE2, MAGIC, ENTROPY);
    public static final Element ENERGY_CORE = Element.createCompositeElement("能量核心", 0xFFD700, ENERGY_CLOUD, CRYSTAL);

    // 第六层组合元素 (6层)
    public static final Element JOY = Element.createCompositeElement("喜悦", 0xFFFF00, HUMAN, LIGHT);
    public static final Element SADNESS = Element.createCompositeElement("悲伤", 0x00008B, HUMAN, FOG);
    public static final Element CURIOSITY = Element.createCompositeElement("好奇", 0xADD8E6, HUMAN, ENERGY);

    public static final Element TOOL = Element.createCompositeElement("工具", 0xC0C0C0, HUMAN, ORDER);

    public static final Element CULTURE = Element.createCompositeElement("文化", 0x8B4513, HUMAN, CIVILIZATION);
    public static final Element HOPE = Element.createCompositeElement("希望", 0x00FF7F, HUMAN, LIGHT);
    public static final Element LOVE = Element.createCompositeElement("爱", 0xFF1493, HUMAN, LIFE);
    public static final Element ANGER = Element.createCompositeElement("愤怒", 0xFF0000, HUMAN, FIRE);
    public static final Element WISDOM = Element.createCompositeElement("智慧", 0x00BFFF, HUMAN, ENERGY_FIELD);
    public static final Element AMBITION = Element.createCompositeElement("野心", 0xFF8C00, HUMAN, ORDER);
    public static final Element JEALOUSY = Element.createCompositeElement("嫉妒", 0x7CFC00, HUMAN, ENTROPY);
    public static final Element HONOR = Element.createCompositeElement("荣誉", 0xFFD700, HUMAN, METAL);

    public static final Element CITY = Element.createCompositeElement("城市", 0x808080, CIVILIZATION, METAL);
    public static final Element SHIP = Element.createCompositeElement("船", 0x708090, HUMAN, OCEAN);
    public static final Element ROAD = Element.createCompositeElement("道路", 0xA9A9A9, HUMAN, ORDER);
    public static final Element MACHINE = Element.createCompositeElement("机器", 0xC0C0C0, HUMAN, METAL);
    public static final Element LIBRARY = Element.createCompositeElement("图书馆", 0xD2B48C, HUMAN, CIVILIZATION);
    public static final Element ALCHEMY = Element.createCompositeElement("炼金术", 0x8A2BE2, HUMAN, MAGIC);
    public static final Element LAW = Element.createCompositeElement("法律", 0x00BFFF, HUMAN, ORDER);
    public static final Element ART = Element.createCompositeElement("艺术", 0xFF69B4, HUMAN, ENERGY);
    public static final Element SCIENCE = Element.createCompositeElement("科学", 0x00FFFF, HUMAN, ENERGY_FIELD);

}
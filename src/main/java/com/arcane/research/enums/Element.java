package com.arcane.research.enums;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 支持动态创建组合元素的类
 */
public final class Element {
    private static final AtomicInteger ID_GEN = new AtomicInteger(0);
    private static final Map<String, Element> ELEMENT_REGISTRY = new HashMap<>();

    // 预定义的基础元素
    public static final Element PLACEHOLDER = register(new Element("PLACEHOLDER", 0x800080));
    public static final Element AIR = register(new Element("风", 0xADD8E6));
    public static final Element EARTH = register(new Element("土", 0x8B4513));
    public static final Element FIRE = register(new Element("火", 0xFF4500));
    public static final Element WATER = register(new Element("水", 0x1E90FF));
    public static final Element ORDER = register(new Element("秩序", 0xFFFF00));
    public static final Element ENTROPY = register(new Element("混沌", 0x800080));

    private final String name;
    private final int color;
    private final int id;
    private final Element base1;
    private final Element base2;

    // 基础构造函数（基础元素）
    private Element(String name, int color) {
        this.name = name;
        this.color = color;
        this.id = ID_GEN.incrementAndGet();
        this.base1 = null;
        this.base2 = null;
    }

    // 组合构造函数（组合元素）
    public Element(String name, int color, Element base1, Element base2) {
        this.name = name;
        this.color = color;
        this.id = ID_GEN.incrementAndGet();
        this.base1 = base1;
        this.base2 = base2;
    }

    public static Element[] values() {
        return ELEMENT_REGISTRY.values().toArray(new Element[0]);
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public boolean isComposite() {
        return base1 != null && base2 != null;
    }

    public Element getBase1() {
        return base1;
    }

    public Element getBase2() {
        return base2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element)) return false;
        Element element = (Element) o;
        return id == element.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // 注册元素到全局注册表
    public static Element register(Element element) {
        if(element==null || element.getName()==null || element.getName().isEmpty()){
            throw new RuntimeException("元素名称不能为空");
        }
        // 若存在相同名称的元素，则返回其对象
        if (ELEMENT_REGISTRY.containsKey(element.getName()) || element.getName().equals("PLACEHOLDER")) {
            return element;
        }
        ELEMENT_REGISTRY.put(element.getName(), element);
        return element;
    }

    // 获取所有已注册元素
    public static Collection<Element> getAllElements() {
        return Collections.unmodifiableCollection(ELEMENT_REGISTRY.values());
    }

    // 根据名称获取元素
    public static Element getElementByName(String name) {
        return ELEMENT_REGISTRY.get(name);
    }

    // 动态创建组合元素
    public static Element createCompositeElement(String name, int color, Element base1, Element base2) {
        if (ELEMENT_REGISTRY.containsKey(name)) {
            throw new IllegalArgumentException("元素 " + name + " 已存在");
        }
        Element composite = new Element(name, color, base1, base2);
        register(composite);
        return composite;
    }

    /**
     * 判断两个元素是否可以连接
     * 规则：只有当其中一个元素是另一个元素的组成部分时才允许连接
     */
    public static boolean canConnect(Element e1, Element e2) {
        if (e1 == null || e2 == null) return false;
        if (e1.equals(e2)) return false; // 自己不能连接自己

        // 如果其中一个是占位符，则允许连接
        if (e1 == PLACEHOLDER || e2 == PLACEHOLDER) return true;

        // 如果有一个是基础元素，另一个是复合元素且包含该基础元素，则允许连接
        if (!e1.isComposite() && e2.isComposite()) {
            return e2.getBase1() == e1 || e2.getBase2() == e1;
        } else if (e1.isComposite() && !e2.isComposite()) {
            return e1.getBase1() == e2 || e1.getBase2() == e2;
        }

        // 复合元素之间的连接：如果彼此互为组成部分
        if (e1.isComposite() && e2.isComposite()) {
            return e1.getBase1() == e2 || e1.getBase2() == e2 ||
                    e2.getBase1() == e1 || e2.getBase2() == e1;
        }

        return false;
    }

    // 获取随机元素（用于测试或初始化）
    public static Element getRandomElement() {
        List<Element> elements = new ArrayList<>(getAllElements());
        Collections.shuffle(elements);
        return elements.get(0);
    }

    // 元素是否为基础元素
    public static boolean isBaseElement(Element element) {
        // AIR，EARTH，FIRE，WATER，ORDER，ENTROPY都为基础元素
        return element == AIR || element == EARTH || element == FIRE || element == WATER || element == ORDER || element == ENTROPY;
    }

    @Override
    public String toString() {
        return name + (isComposite() ? " (" + base1.getName() + "+" + base2.getName() + ")" : "");
    }
}

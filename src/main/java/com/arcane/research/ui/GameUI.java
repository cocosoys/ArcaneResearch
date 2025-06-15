package com.arcane.research.ui;

import com.arcane.research.enums.Element;
import com.arcane.research.model.*;
import com.arcane.research.ui.component.CustomButton;
import com.arcane.research.ui.component.element.*;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * 游戏主界面
 */
public class GameUI extends BaseUI {
    private Player player;
    private ResearchPool researchPool;
    private ResearchNote currentResearchNote;
    private JPanel mainPanel;
    private ElementResearchPanel elementResearchPanel;
    private ElementPoolPanel elementPoolPanel;
    private ElementInfoPanel elementInfoPanel;
    private ElementBoxPanel elementBoxPanel; // 要素箱面板
    private CustomButton backButton;
    private CustomButton newResearchButton;
    private CustomButton submitButton;

    // 新增：存储已完成的研究笔记
    private List<ResearchNote> completedResearchNotes = new ArrayList<>();
    // 记录当前显示的研究笔记（当前进行中 + 已完成可切换）
    private ResearchNote displayedResearchNote;

    public GameUI(String title) {
        super(title);
        initGame();
        initComponents();
        layoutComponents();
        addEventListeners();

        // 调试：强制触发全局布局计算
        SwingUtilities.invokeLater(() -> {
            pack();
            setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.95),
                    (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.95));
            setLocationRelativeTo(null);
        });

        elementResearchPanel.logPlaceableNodes();
    }

    private void initGame() {
        player = new Player("玩家");
        researchPool = player.getResearchPool();
        createNewResearchNote();
    }

    private void createNewResearchNote() {
        String[] researchTopics = {"元素本质", "魔法基础", "神秘学研究", "元素融合"};
        String topic = researchTopics[new Random().nextInt(researchTopics.length)];
        currentResearchNote = new ResearchNote(topic, "");
        long seed = new Random().nextLong();
        Element[] baseElements = {Element.AIR, Element.EARTH, Element.FIRE, Element.WATER};
        currentResearchNote.initializeHoneycombLayout(4, 100 ,3, 0, seed, baseElements);
    }

    // 新增：重置研究笔记的核心函数
    private void resetResearchNote() {
        // 1. 创建新的研究笔记
        createNewResearchNote();
        // 2. 将新笔记关联到所有面板
        updatePanelsWithNewResearchNote(currentResearchNote);
        // 3. 刷新界面组件
        elementResearchPanel.repaint();
        elementInfoPanel.updateInfoPanel();
        elementBoxPanel.updatePointCounts();
        elementPoolPanel.updatePoolPanel();
        // 4. 显示提示信息
        JOptionPane.showMessageDialog(this,
                "已开始新的研究：" + currentResearchNote.getTitle(),
                "新研究启动",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // 新增：将研究笔记关联到所有依赖它的面板
    private void updatePanelsWithNewResearchNote(ResearchNote newNote) {
        elementResearchPanel.currentResearchNote = newNote;
        elementInfoPanel.currentResearchNote = newNote;
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(10, 10, 30));

        elementResearchPanel = new ElementResearchPanel(currentResearchNote);
        elementPoolPanel = new ElementPoolPanel(researchPool);
        elementInfoPanel = new ElementInfoPanel(currentResearchNote);
        elementBoxPanel = new ElementBoxPanel(researchPool); // 现在是新的实现

        backButton = new CustomButton("返回", 18);
        newResearchButton = new CustomButton("新研究", 18);
        submitButton = new CustomButton("提交研究", 18);
    }

    private void layoutComponents() {
        elementResearchPanel.setPreferredSize(new Dimension(800, 600));
        elementInfoPanel.updateInfoPanel();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(15, 15, 35));
        buttonPanel.add(backButton);
        buttonPanel.add(newResearchButton);
        buttonPanel.add(submitButton);

        // 右侧面板：研究点数池 + 研究信息 + 按钮
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(15, 15, 35));
        rightPanel.add(elementInfoPanel, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.setPreferredSize(new Dimension(280, 0));

        // 中间面板：研究面板 + 要素箱（滚动面板）
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setPreferredSize(new Dimension(800, 600)); // 或动态计算
        centerPanel.add(elementResearchPanel, BorderLayout.CENTER);
        // 优化滚动条配置
        JScrollPane scrollPane = new JScrollPane(elementBoxPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

// 关键：设置滚动面板的首选大小，确保内容超出时触发滚动
        scrollPane.setPreferredSize(new Dimension(
                800, // 与右侧面板宽度适配
                275 // 固定高度，或动态计算
        ));

        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel);

        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
            // 手动触发滚动面板的布局计算
            scrollPane.revalidate();
            scrollPane.repaint();
        });
    }

    private void addEventListeners() {
        backButton.addActionListener(e -> switchToUI(StartUI.class));

        newResearchButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "确定要开始新的研究吗？当前研究将被放弃。",
                    "新研究确认",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                // 调用新的重置函数
                resetResearchNote();
            }
        });

        // 研究点数变化时刷新要素箱
        researchPool.addPropertyChangeListener("researchPoints", e -> {
            elementBoxPanel.updatePointCounts();
        });

        submitButton.addActionListener(e -> {
            ResearchNote newNote = createNewResearchNoteInstance();
            submitResearchAndGenerateNewNote(newNote);

//            if (currentResearchNote.isFullyConnected()) {
//                ResearchScroll scroll = currentResearchNote.generateResearchScroll();
//                scroll.applyReward(player);
//                JOptionPane.showMessageDialog(this,
//                        "恭喜！研究完成！\n获得奖励：" + scroll.getDescription(),
//                        "研究成功",
//                        JOptionPane.INFORMATION_MESSAGE);
//                createNewResearchNote();
//                elementResearchPanel.repaint();
//                elementInfoPanel.updateInfoPanel();
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "研究尚未完成！请确保所有顶点之间都已联通。",
//                        "研究未完成",
//                        JOptionPane.WARNING_MESSAGE);
//            }
        });

        elementResearchPanel.setDropTarget(new DropTarget() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    Transferable transferable = dtde.getTransferable();
                    if (transferable.isDataFlavorSupported(ElementFlavor.ELEMENT_FLAVOR)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        Element draggedElement = (Element) transferable.getTransferData(ElementFlavor.ELEMENT_FLAVOR);

                        Point dropPoint = dtde.getLocation();
                        ResearchNode targetNode = elementResearchPanel.getNodeAtPoint(dropPoint);

                        if (targetNode != null) {
                            if (researchPool.getResearchPointCount(draggedElement) >= 1) {
                                if (researchPool.consumeResearchPoint(draggedElement, 1)) {
                                    elementResearchPanel.handleElementDrop(draggedElement, targetNode);
                                    elementPoolPanel.updatePoolPanel();
                                    JOptionPane.showMessageDialog(GameUI.this,
                                            "已将 " + draggedElement.getName() + " 放置到节点",
                                            "操作成功",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(GameUI.this,
                                            "研究点数不足，无法放置 " + draggedElement.getName(),
                                            "资源不足",
                                            JOptionPane.WARNING_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(GameUI.this,
                                        "研究点数不足，无法放置 " + draggedElement.getName(),
                                        "资源不足",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }

                        dtde.dropComplete(true);
                    } else {
                        dtde.rejectDrop();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    dtde.rejectDrop();
                } finally {
                    // 仅刷新受影响区域，而非整个面板
                    ResearchNode targetNode = elementResearchPanel.getNodeAtPoint(dtde.getLocation());
                    if (targetNode != null) {
                        Point pixel = elementResearchPanel.hexToPixel(
                                targetNode.getX(), targetNode.getY(),
                                elementResearchPanel.getWidth()/2, elementResearchPanel.getHeight()/2
                        );
                        // 计算刷新区域（略大于节点尺寸）
                        int refreshSize = elementResearchPanel.NODE_SIZE * 2;
                        elementResearchPanel.repaint(
                                pixel.x - refreshSize,
                                pixel.y - refreshSize,
                                refreshSize * 2,
                                refreshSize * 2
                        );
                    }
                }
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                Point point = dtde.getLocation();
                ResearchNode node = elementResearchPanel.getNodeAtPoint(point);

                // 仅当节点可放置时更新目标节点
                if (node != null &&
                        (node.getElement() == null || node.getElement() == Element.PLACEHOLDER)) {
                    elementResearchPanel.targetNode = node;
                } else {
                    elementResearchPanel.targetNode = null;
                }
//                elementResearchPanel.targetNode = null; // 强制不设置目标节点
                elementResearchPanel.repaint();
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                elementResearchPanel.targetNode = null;
                elementResearchPanel.repaint();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                elementResearchPanel.updateHexagonDimensions();
                elementResearchPanel.repaint();
            }
        });
    }

    // 辅助方法：创建新研究笔记实例
    private ResearchNote createNewResearchNoteInstance() {
        String[] researchTopics = {"元素本质", "魔法基础", "神秘学研究", "元素融合"};
        String topic = researchTopics[new Random().nextInt(researchTopics.length)];
        ResearchNote newNote = new ResearchNote(topic, "");
        long seed = new Random().nextLong();
        Element[] baseElements = {Element.AIR, Element.EARTH, Element.FIRE, Element.WATER,Element.ORDER, Element.ENTROPY};
        newNote.initializeHoneycombLayout(4, 100, 3, 0, seed, baseElements);
        return newNote;
    }

    // 新增：提交研究并生成新研究笔记的函数
    public void submitResearchAndGenerateNewNote(ResearchNote newNote) {
        // 1. 验证当前研究是否完成
        if (!currentResearchNote.isFullyConnected()) {
            JOptionPane.showMessageDialog(this,
                    "当前研究尚未完成，无法提交！",
                    "提交失败",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. 应用研究奖励
        ResearchScroll scroll = currentResearchNote.generateResearchScroll();
        scroll.applyReward(player);

        // 3. 显示研究完成提示
        JOptionPane.showMessageDialog(this,
                "恭喜！研究完成！\n获得奖励：" + scroll.getDescription(),
                "研究成功",
                JOptionPane.INFORMATION_MESSAGE);

        // 4. 设置并同步新研究笔记
        currentResearchNote = newNote;
        updatePanelsWithNewResearchNote(currentResearchNote);

        // 5. 刷新界面组件
        refreshUIComponents();
    }

    // 界面刷新方法
    private void refreshUIComponents() {
        elementResearchPanel.repaint();
        elementInfoPanel.updateInfoPanel();
        elementBoxPanel.updatePointCounts();
        elementPoolPanel.updatePoolPanel();
    }
}
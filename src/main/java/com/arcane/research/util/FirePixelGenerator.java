package com.arcane.research.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class FirePixelGenerator {
    // 常量定义
    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private static final int PIXEL_COUNT = WIDTH * HEIGHT;
    private static final int BITS_PER_PIXEL = 25; // 24位RGB + 1位透明度

    // 火焰颜色渐变
    private static final Color[] FIRE_COLORS = {
            new Color(255, 255, 255), // 白色（火焰尖端）
            new Color(255, 255, 200), // 浅黄色
            new Color(255, 240, 100), // 黄色
            new Color(255, 180, 50),  // 橙色
            new Color(255, 100, 0),   // 红色
            new Color(180, 30, 0)     // 深红色（底部）
    };

    /**
     * 生成火焰像素数据
     */
    public static Object[] generateFirePixels() {
        int[][][] rgbData = new int[HEIGHT][WIDTH][3];
        boolean[][] alphaData = new boolean[HEIGHT][WIDTH];

        // 创建火焰基础数据（底部随机热源）
        int[][] heatMap = new int[HEIGHT][WIDTH];

        // 初始化底部热源（随机值）
        for (int x = 0; x < WIDTH; x++) {
            heatMap[HEIGHT - 1][x] = (int) (Math.random() * 100);
        }

        // 从底部向上传播热量
        for (int y = HEIGHT - 2; y >= 0; y--) {
            for (int x = 0; x < WIDTH; x++) {
                // 计算周围热量平均值并减少（模拟热量扩散和衰减）
                int left = (x > 0) ? heatMap[y + 1][x - 1] : 0;
                int center = heatMap[y + 1][x];
                int right = (x < WIDTH - 1) ? heatMap[y + 1][x + 1] : 0;

                // 热量扩散和衰减
                heatMap[y][x] = Math.max(0, (left + center + right) / 3 - (int) (Math.random() * 3));
            }
        }

        // 根据热量图生成颜色和透明度
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int heat = heatMap[y][x];

                // 热量值映射到颜色
                if (heat > 0) {
                    // 计算颜色索引（0-5）
                    double colorIndex = (heat / 100.0) * (FIRE_COLORS.length - 1);
                    int index = (int) Math.floor(colorIndex);
                    int nextIndex = Math.min(FIRE_COLORS.length - 1, index + 1);
                    double ratio = colorIndex - index;

                    // 颜色插值
                    Color color = interpolateColor(FIRE_COLORS[index], FIRE_COLORS[nextIndex], ratio);

                    rgbData[y][x][0] = color.getRed();
                    rgbData[y][x][1] = color.getGreen();
                    rgbData[y][x][2] = color.getBlue();

                    // 透明度基于热量值（热量越高越不透明）
                    alphaData[y][x] = true;
                } else {
                    // 无热量，完全透明
                    rgbData[y][x][0] = 0;
                    rgbData[y][x][1] = 0;
                    rgbData[y][x][2] = 0;
                    alphaData[y][x] = false;
                }
            }
        }

        return new Object[]{rgbData, alphaData};
    }

    /**
     * 颜色插值
     */
    private static Color interpolateColor(Color c1, Color c2, double ratio) {
        int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * ratio);
        int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * ratio);
        int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * ratio);
        return new Color(r, g, b);
    }

    /**
     * 将像素数据编码为种子
     */
    public static BigInteger encodeToSeed(int[][][] rgbData, boolean[][] alphaData) {
        // 每个像素需要25位：24位RGB + 1位透明度
        // 总共需要16×16×25 = 6400位
        // 使用BigInteger存储这个大数

        BigInteger seed = BigInteger.ZERO;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                // 获取像素数据
                int r = rgbData[y][x][0];
                int g = rgbData[y][x][1];
                int b = rgbData[y][x][2];
                boolean alpha = alphaData[y][x];

                // 构建25位像素数据：[A:1][R:8][G:8][B:8]
                long pixelData = ((alpha ? 1L : 0L) << 24) |
                        ((long) r << 16) |
                        ((long) g << 8) |
                        b;

                // 将像素数据添加到种子中
                int position = (y * WIDTH + x) * BITS_PER_PIXEL;
                seed = seed.or(BigInteger.valueOf(pixelData).shiftLeft(position));
            }
        }

        return seed;
    }

    /**
     * 从种子解码像素数据
     */
    public static Object[] decodeFromSeed(BigInteger seed) {
        int[][][] rgbData = new int[HEIGHT][WIDTH][3];
        boolean[][] alphaData = new boolean[HEIGHT][WIDTH];

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                // 计算像素在种子中的位置
                int position = (y * WIDTH + x) * BITS_PER_PIXEL;

                // 提取25位像素数据
                BigInteger pixelBits = seed.shiftRight(position).and(BigInteger.valueOf((1L << BITS_PER_PIXEL) - 1));
                long pixelData = pixelBits.longValue();

                // 解析像素数据
                boolean alpha = ((pixelData >> 24) & 0x01) == 1;
                int r = (int) ((pixelData >> 16) & 0xFF);
                int g = (int) ((pixelData >> 8) & 0xFF);
                int b = (int) (pixelData & 0xFF);

                rgbData[y][x][0] = r;
                rgbData[y][x][1] = g;
                rgbData[y][x][2] = b;
                alphaData[y][x] = alpha;
            }
        }

        return new Object[]{rgbData, alphaData};
    }

    /**
     * 保存像素数据为图片
     */
    public static void saveAsImage(int[][][] rgbData, boolean[][] alphaData, String filePath) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int r = rgbData[y][x][0];
                int g = rgbData[y][x][1];
                int b = rgbData[y][x][2];
                int alpha = alphaData[y][x] ? 255 : 0;

                int argb = (alpha << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, argb);
            }
        }

        try {
            File output = new File(filePath);
            ImageIO.write(image, "png", output);
            System.out.println("图像已保存至: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主方法：生成火焰像素图并编码为种子
     */
    public static void main(String[] args) {
        // 生成火焰像素数据
        Object[] firePixels = generateFirePixels();
        int[][][] rgbData = (int[][][]) firePixels[0];
        boolean[][] alphaData = (boolean[][]) firePixels[1];

        // 保存原始火焰图像
        saveAsImage(rgbData, alphaData, "fire_original.png");

        // 将像素数据编码为种子
        BigInteger seed = encodeToSeed(rgbData, alphaData);
        System.out.println("火焰像素图种子: " + seed.toString());
        System.out.println("十六进制表示: " + seed.toString(16));

        // 从种子解码回像素数据
        Object[] decodedPixels = decodeFromSeed(seed);
        int[][][] decodedRgb = (int[][][]) decodedPixels[0];
        boolean[][] decodedAlpha = (boolean[][]) decodedPixels[1];

        // 保存解码后的图像
        saveAsImage(decodedRgb, decodedAlpha, "fire_decoded.png");

        // 验证原始数据和解码数据是否一致
        boolean isIdentical = true;
        for (int y = 0; y < HEIGHT && isIdentical; y++) {
            for (int x = 0; x < WIDTH && isIdentical; x++) {
                if (rgbData[y][x][0] != decodedRgb[y][x][0] ||
                        rgbData[y][x][1] != decodedRgb[y][x][1] ||
                        rgbData[y][x][2] != decodedRgb[y][x][2] ||
                        alphaData[y][x] != decodedAlpha[y][x]) {
                    isIdentical = false;
                }
            }
        }

        System.out.println("解码验证: " + (isIdentical ? "完全一致" : "存在差异"));
    }
}

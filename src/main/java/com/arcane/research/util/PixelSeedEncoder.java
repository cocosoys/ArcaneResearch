package com.arcane.research.util;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class PixelSeedEncoder {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private static final int PIXEL_COUNT = WIDTH * HEIGHT;
    private static final int BITS_PER_PIXEL = 24; // RGB各8位
    private static final int BITS_PER_ALPHA = 1;  // 透明度1位
    private static final int BITS_PER_PIXEL_WITH_ALPHA = BITS_PER_PIXEL + BITS_PER_ALPHA;

    /**
     * 从种子生成像素图数据
     */
    public static Object[] generateFromSeed(long seed) {
        int[][][] rgbData = new int[HEIGHT][WIDTH][3];
        boolean[][] alphaData = new boolean[HEIGHT][WIDTH];

        // 使用种子初始化随机数生成器，确保可重复
        java.util.Random random = new java.util.Random(seed);

        // 生成RGB数据
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                rgbData[y][x][0] = random.nextInt(256); // R
                rgbData[y][x][1] = random.nextInt(256); // G
                rgbData[y][x][2] = random.nextInt(256); // B
                alphaData[y][x] = random.nextBoolean(); // 透明度
            }
        }

        return new Object[]{rgbData, alphaData};
    }

    /**
     * 将像素图数据编码为种子
     */
    public static long encodeToSeed(int[][][] rgbData, boolean[][] alphaData) {
        validateData(rgbData, alphaData);

        // 创建一个足够大的数组来存储所有像素数据
        byte[] pixelData = new byte[PIXEL_COUNT * 4]; // 每个像素4字节 (RGB+Alpha)

        int index = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                pixelData[index++] = (byte) rgbData[y][x][0];
                pixelData[index++] = (byte) rgbData[y][x][1];
                pixelData[index++] = (byte) rgbData[y][x][2];
                pixelData[index++] = (byte) (alphaData[y][x] ? 1 : 0);
            }
        }

        // 使用像素数据计算种子
        return calculateSeed(pixelData);
    }

    /**
     * 从像素数据计算种子
     */
    private static long calculateSeed(byte[] data) {
        Checksum checksum = new CRC32();
        checksum.update(data, 0, data.length);
        return checksum.getValue();
    }

    /**
     * 验证输入数据有效性
     */
    private static void validateData(int[][][] rgbData, boolean[][] alphaData) {
        if (rgbData.length != HEIGHT || rgbData[0].length != WIDTH) {
            throw new IllegalArgumentException("RGB数据尺寸必须为16x16");
        }
        if (alphaData.length != HEIGHT || alphaData[0].length != WIDTH) {
            throw new IllegalArgumentException("透明度数据尺寸必须为16x16");
        }

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                for (int c = 0; c < 3; c++) {
                    if (rgbData[y][x][c] < 0 || rgbData[y][x][c] > 255) {
                        throw new IllegalArgumentException("颜色值必须在0-255之间");
                    }
                }
            }
        }
    }
}
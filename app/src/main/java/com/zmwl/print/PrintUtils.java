package com.zmwl.print;

import android.annotation.SuppressLint;

import java.nio.charset.Charset;

/**
 * Created by 周旭 on 2017/3/1.
 * 打印格式工具类
 */

public class PrintUtils {
    /**
     * 打印纸一行最大的字节
     */
    private static final int LINE_BYTE_SIZE = 32;

    /**
     * 打印三列时，中间一列的中心线距离打印纸左侧的距离
     */
    private static final int LEFT_LENGTH = 16;

    /**
     * 打印三列时，中间一列的中心线距离打印纸右侧的距离
     */
    private static final int RIGHT_LENGTH = 16;

    /**
     * 打印三列时，第一列汉字最多显示几个文字
     */
    private static final int LEFT_TEXT_MAX_LENGTH = 5;


    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }


    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     * @return
     */

    public static String printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        // 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
        }
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        // 计算左侧文字和中间文字的空格长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);

        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2 - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }

        // 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        return sb.toString();
    }

    /**
     * 打印星号
     * 例如     *****************海鲜类*************************
     *
     * @param middleText 中间的文字
     * @return
     */
    public static String printStar(String middleText) {
        StringBuilder sb = new StringBuilder();

        int middleTextLength = getBytesLength(middleText);

        // 计算左侧和中间文字的长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append("*");
        }
        sb.append(middleText);

        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2;

        for (int i = 0; i < marginBetweenMiddleAndRight - 2; i++) {
            sb.append("*");
        }
        return sb.toString();
    }

    /**
     * 打印之间的标题
     * <p>
     * 桌号：001
     *
     * @param title 居中的标题
     * @return
     */
    public static String printTitle(String title) {
        StringBuilder sb = new StringBuilder();

        int middleTextLength = getBytesLength(title);

        // 计算左侧和中间文字的长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(title);

        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2;

        for (int i = 0; i < marginBetweenMiddleAndRight - 2; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }


    //打印空白的一行
    public static String print() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LINE_BYTE_SIZE; i++) {
            sb.append("*");
        }
        return sb.toString();
    }
}

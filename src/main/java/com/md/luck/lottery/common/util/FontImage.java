package com.md.luck.lottery.common.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 水印图片生成工具 文字图片
 */
public class FontImage {
    public static void main(String[] args) throws Exception {
//        createImage("～ lottery ～", new Font("宋体", Font.BOLD, 26), new File(
//                BasePath.getBasePath() + "/static/loicon.png"), 84, 64);
        String drawStr = "～ lottery ～";
        int width = 110;
        int height = 24;
        Integer fontHight = 18;
        drawTranslucentStringPic(width, height, fontHight, drawStr, new File(
                BasePath.getBasePath() + "/static/loicon.png"));
        System.out.println(BasePath.getBasePath() + "/static/loicon.png");
//        createImage("请A1002到2号窗口", new Font("黑体", Font.BOLD, 35), new File(
//                "e:/a1.png"), 4096, 64);
//        createImage("请A1001到1号窗口", new Font("黑体", Font.PLAIN, 40), new File(
//                "e:/a2.png"), 4096, 64);

    }

    /**
     * 根据str,font的样式以及输出文件目录
     * @param str	字符串
     * @param font	字体
     * @param outFile	输出文件位置
     * @param width	宽度
     * @param height	高度
     * @throws Exception
     */
    public static void createImage(String str, Font font, File outFile,
                                   Integer width, Integer height) throws Exception {
        // 创建图片
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setClip(0, 0, width, height);
//        g.setColor(new Color(0, 0, 0, 0.0f));
//        // 先用黑色填充整张图片,也就是背景
//        g.fillRect(0, 0, width, height);


        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        g = image.createGraphics();
        // 在换成红色
        g.setColor(Color.LIGHT_GRAY);
        // 设置画笔字体
        g.setFont(font);
        /** 用于获得垂直居中y */
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        // 256 340 0 680
        for (int i = 0; i < 6; i++) {
            // 画出字符串
            g.drawString(str, i * 680, y);
        }
        g.dispose();
        // 输出png图片
        ImageIO.write(image, "png", outFile);
    }

    public static void drawTranslucentStringPic(int width, int height, Integer fontHeight, String drawStr, File outFile)
    {
        try
        {
            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D gd = buffImg.createGraphics();
            //设置透明  start
            buffImg = gd.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            gd=buffImg.createGraphics();
            //设置透明  end
            gd.setFont(new Font("微软雅黑", Font.PLAIN, fontHeight)); //设置字体
            gd.setColor(Color.LIGHT_GRAY); //设置颜色
//            gd.drawRect(0, 0, width - 1, height - 1); //画边框
//            gd.drawString(drawStr, width/2-fontHeight*drawStr.length()/2, fontHeight); //输出文字（中文横向居中）
            gd.drawString(drawStr, width/2 - fontHeight/2 * drawStr.length()/2, fontHeight); //输出文字（中文横向居中）
            ImageIO.write(buffImg, "png", outFile);
//            return buffImg;
        } catch (Exception e) {
//            return null;
        }
    }


}

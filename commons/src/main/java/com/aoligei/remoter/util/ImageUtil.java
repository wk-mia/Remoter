package com.aoligei.remoter.util;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author wk-mia
 * 2020-10-9
 * 图像处理工具类
 */
public class ImageUtil {

    /**
     * 压缩图像
     * @param bufferedImage 图像
     * @param quality 质量比例：0 < quality <= 1
     * @return
     */
    public static byte[] compress(BufferedImage bufferedImage, float quality){
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            Thumbnails.of(bufferedImage)
                    /**原图宽高比例*/
                    .scale(1f)
                    .outputFormat("jpg")
                    /**质量比例*/
                    .outputQuality(quality)
                    /**输出到OutputStream*/
                    .toOutputStream(outputStream);
            return outputStream.toByteArray();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}

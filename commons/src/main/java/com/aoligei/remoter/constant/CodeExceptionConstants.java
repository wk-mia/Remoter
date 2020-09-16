package com.aoligei.remoter.constant;

/**
 * @author wk-mia
 * 2020-9-15
 * 编码/解码异常信息常量配置类
 */
public class CodeExceptionConstants {
    /**
     * 解码器无法将数据转换为指定的类型
     */
    public static final String DECODER_ERROR_TYPE = "the decoder cannot be converted to the specified type";
    /**
     * 编码器无法将数据转换为指定的类型
     */
    public static final String ENCODER_ERROR_TYPE = "the encoder cannot be converted to the specified type";
    /**
     * 输入的对象类型不能被转换为Object
     */
    public static final String INPUT_OBJ_CAST_ERROR = "input object type cannot be forwarded to 'Object'";
}

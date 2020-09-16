package com.aoligei.remoter.convert;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.constant.CodeExceptionConstants;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wk-mia
 * 2020-9-15
 * 编码器
 */
public class RemoterEncoder extends MessageToByteEncoder {

    private static final Logger log = LoggerFactory.getLogger(RemoterDecoder.class);
    /**
     * 编码器输出的对象类型
     */
    private Class definedClass;
    /**
     * 构造器
     * @param definedClass 解码的输出对象
     */
    public RemoterEncoder(Class definedClass) {
        this.definedClass = definedClass;
    }

    /**
     * 解码方法，将出站的definedClass对象转换为byteBuf对象
     * @param channelHandlerContext 通道上下文
     * @param obj 入站definedClass对象
     * @param byteBuf 字节流缓冲区
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        if(definedClass.isInstance(obj)){
            /**
             * 序列化
             */
            byte[] data = SerializationUtil.serialize(obj);
            /**
             * 类型检查
             */
            if(definedClass == BaseRequest.class){
                log.debug("send data to server,size:{}", data.length);
            }else if(definedClass == BaseResponse.class){
                log.debug("send data to client,size:{}", data.length);
            }else {
                throw new RemoterException(CodeExceptionConstants.ENCODER_ERROR_TYPE);
            }
            /**
             * 写入数据到字节流缓存区中
             */
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }else {
            throw new RemoterException(CodeExceptionConstants.INPUT_OBJ_CAST_ERROR);
        }
    }
}

package com.aoligei.remoter.convert;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.constant.CodeExceptionConstants;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * @author wk-mia
 * 2020-9-15
 * 解码器
 */
public class RemoterDecoder extends ByteToMessageDecoder {


    private static final Logger log = LoggerFactory.getLogger(RemoterDecoder.class);
    /**
     * 单次请求传送的数据上线
     */
    private static final int MAX_FRAME_SIZE = 1024*1024;
    /**
     * 解码器输入的对象类型
     */
    private Class definedClass;
    /**
     * 构造器
     * @param definedClass 解码的输出对象
     */
    public RemoterDecoder(Class definedClass) {
        this.definedClass = definedClass;
    }

    /**
     * 解码方法，将入站的byteBuf对象转换为definedClass对象
     * @param channelHandlerContext 通道上下文
     * @param byteBuf 入站字节
     * @param list 缓存区
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() <= MAX_FRAME_SIZE){
            /**
             * 检查可读的字节是否至少有4个(int是4个字节长度)
             * 从入站的ByteBuf中读取int，添加到解码消息的List中
             */
            if(byteBuf.readableBytes() >= 4){
                /**
                 * 标记读索引
                 */
                byteBuf.markReaderIndex();
                int length = byteBuf.readInt();
                /**
                 * 当可读长度比要读的长度小时，恢复读索引位置并退出本次读取，等待下一次字节流进来后再
                 * 进行读取。可避免因可读字节的长度小于本次应读的长度而抛出IndexOutOfBoundsException。
                 */
                if(byteBuf.readableBytes() < length){
                    byteBuf.resetReaderIndex();
                    return;
                }

                /**
                 * 类型检查
                 */
                if(definedClass == BaseRequest.class){
                    log.debug("receive data from client,size:{}", length);
                }else if(definedClass == BaseResponse.class){
                    log.debug("receive data from server,size:{}", length);
                }else {
                    throw new RemoterException(CodeExceptionConstants.DECODER_ERROR_TYPE);
                }
                /**
                 * 读取数据、反序列化并加入到缓存中
                 */
                byte[] data = new byte[length];
                byteBuf.readBytes(data);
                Object obj = SerializationUtil.deserialize(data, definedClass.getClass());
                list.add(obj);
            }
        }else {
            /**
             * 单次请求中传输的数据过大保护机制：
             * 跳过解码，直接向ChannelHandler抛出异常信息
             */
            byteBuf.skipBytes(byteBuf.readableBytes());
            throw new TooLongFrameException("too big frame");
        }
    }
}

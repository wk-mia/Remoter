package com.aoligei.remoter.util;

import com.aoligei.remoter.exception.RemoterException;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wk-mia
 * 2020-9-15
 * 序列化工具类，使用谷歌的插件，jdk自带的序列化处理起来相当麻烦，需要在很多地方实现
 * Serializable接口，还要考虑继承的问题。用protostuff则无需考虑这些问题，并且
 * protostuff转出的字节流占用更小。
 */
public final class SerializationUtil {

    private static final Logger log = LoggerFactory.getLogger(SerializationUtil.class);

    /**
     * 避免每次序列化都重新申请Buffer空间
     */
    private static LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    /**
     * 缓存Schema
     */
    private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<Class<?>, Schema<?>>();

    /**
     * 序列化
     * @param obj 待序列化的对象
     * @param <T> 类型
     * @return 字节数组
     */
    public static <T> byte[] serialize(T obj) throws RemoterException {
        Class<T> defineClass = (Class<T>) obj.getClass();
        try {
            log.debug("serialize start...");
            Schema<T> schema = getSchema(defineClass);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        }catch (Exception e){
            log.debug(e.getMessage());
            throw new RemoterException(e.getMessage(),e);
        }finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化成指定类型的对象
     * @param bytes 对象序列化后的字节数组
     * @param defineClass 反序列化后的对象类型
     * @param <T> 类型
     * @return 指定类型的对象
     */
    public static <T> T deserialize (byte[] bytes, Class<T> defineClass) throws RemoterException{
        try{
            log.debug("deserialize start...");
            Schema<T> schema = getSchema(defineClass);
            T obj = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
            return obj;
        }catch (Exception e){
            log.debug(e.getMessage());
            throw new RemoterException(e.getMessage(),e);
        }
    }

    /**
     * 获取Scheme
     * @param defineClass 对象类型
     * @param <T> 类型
     * @return Scheme
     */
    private static <T> Schema<T> getSchema(Class<T> defineClass) {
        Schema<T> schema = (Schema<T>) schemaCache.get(defineClass);
        if (schema == null) {
            /**
             * 这个schema通过RuntimeSchema进行懒创建并缓存,
             * RuntimeSchema.getSchema()方法是线程安全的
             */
            schema = RuntimeSchema.getSchema(defineClass);
            if (schema != null) {
                schemaCache.put(defineClass, schema);
            }
        }
        return schema;
    }
}

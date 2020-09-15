package com.aoligei.remoter.util;

import java.io.*;

/**
 * @author wk-mia
 * 2020-9-15
 * 序列化工具类
 */
public class SerializationUtil {

    /**
     * 序列化
     * @param obj 待序列化的对象
     * @param <T> 类型
     * @return 字节数组
     * @throws IOException
     */
    public static <T> byte[] serialize(T obj) throws IOException {
        if (null == obj) {
            return null;
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);) {
            out.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * 反序列化成指定类型的对象
     * @param bytes 对象序列化后的字节数组
     * @param cls 反序列化后的对象类型
     * @return 指定类型的对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> T deserialize(byte[] bytes, Class<T> cls) throws ClassNotFoundException, IOException {
        if (null == bytes) {
            return null;
        }
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);) {
            return cls.cast(in.readObject());
        }
    }
}

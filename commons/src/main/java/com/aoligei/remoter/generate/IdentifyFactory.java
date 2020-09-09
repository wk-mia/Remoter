package com.aoligei.remoter.generate;

import com.aoligei.remoter.generate.impl.UUIDIdentifier;

/**
 * 身份识别码工厂
 */
public class IdentifyFactory {

    /**
     * 生成客户端身份识别码
     * @return 客户端身份识别码
     */
    public static String createClientId(){
        /**
         * 自定义生成方法时，需要实现IIdentifierProvider接口的provide方法
         */
        IIdentifierProvider iIdentifierProvider = new UUIDIdentifier();
        String clientId = iIdentifierProvider.provide();
        return clientId;
    }

    /**
     * 生成连接编码
     * @return 客户端身份识别码
     */
    public static String createConnectionId(){
        IIdentifierProvider iIdentifierProvider = new UUIDIdentifier();
        String connectionId = iIdentifierProvider.provide();
        return connectionId;
    }
}

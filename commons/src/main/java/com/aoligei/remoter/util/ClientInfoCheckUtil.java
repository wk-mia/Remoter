package com.aoligei.remoter.util;

import com.aoligei.remoter.dto.ClientInformation;
import com.aoligei.remoter.generate.IIdentifierProvider;
import com.aoligei.remoter.generate.impl.UUIDIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * @author wk-mia
 * 2020-8-31
 * 客户端信息校验
 */
public class ClientInfoCheckUtil {

    private static Logger log = LoggerFactory.getLogger(ClientInfoCheckUtil.class);

    /**
     * 客户端身份识别码是否已经存在
     * @param clientInformationList 账册
     * @param clientId 客户端身份识别码
     * @return true/false
     */
    public static boolean isClientIdExist(List<ClientInformation> clientInformationList,String clientId){
        return clientInformationList.stream().filter(item -> clientId.equals(item.getClientId()))
                .findAny().isPresent();
    }

    /**
     * 客户端信息是否已经存在
     * @param clientInformationList 账册
     * @param ip 客户端IP
     * @return true/false
     */
    public static boolean isClientIpExist(List<ClientInformation> clientInformationList,String ip){
        return clientInformationList.stream().filter(item -> ip.equals(item.getClientIp()))
                .findAny().isPresent();
    }

    /**
     * 客户端信息是否为空
     * @param clientInformation 客户端信息
     * @return true/false
     */
    public static boolean isInfoNull(ClientInformation clientInformation){
        return clientInformation == null;
    }

    /**
     * 客户端身份识别码是否为空
     * @param clientId 客户端身份识别码
     * @return true/false
     */
    public static boolean isClientIdNull(String clientId){return (clientId == null || clientId.isEmpty()) ? true : false;}

    /**
     * 客户端信息是否完备
     * @param clientInformation 客户端信息
     * @return true/false
     */
    public static boolean isInfoComplete(ClientInformation clientInformation){
        return (clientInformation.getClientName() == null || clientInformation.getClientName().isEmpty()) ||
                (clientInformation.getClientIp() == null || clientInformation.getClientIp().isEmpty()) ||
                (clientInformation.getClientPort() == null);
    }


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
     * 通过IP查找客户端信息
     * @param clientInformationList 账册
     * @param clientIp 客户端IP
     * @return 客户端信息
     */
    public static ClientInformation getClientInformationByClientIp(List<ClientInformation> clientInformationList,String clientIp){
        /**
         * 返回找到的第一个匹配项，若没有匹配项，返回null
         */
        Optional<ClientInformation> first = clientInformationList.stream().filter(item -> clientIp.equals(item.getClientIp())).findFirst();
        if(first.isPresent()){
            return first.get();
        }else {
            return null;
        }
    }

    /**
     * 通过编码查找客户端信息
     * @param clientInformationList 账册
     * @param clientId 客户端编码
     * @return 客户端信息
     */
    public static ClientInformation getClientInformationByClientId(List<ClientInformation> clientInformationList,String clientId){
        /**
         * 返回找到的第一个匹配项，若没有匹配项，返回null
         */
        Optional<ClientInformation> first = clientInformationList.stream().filter(item -> clientId.equals(item.getClientId())).findFirst();
        if(first.isPresent()){
            return first.get();
        }else {
            return null;
        }
    }
}

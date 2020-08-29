package com.aoligei.remoter.generate;

/**
 * @author wk-mia
 * 2020-8-28
 * 身份识别码提供接口
 */
public interface IIdentifierProvider {

    /**
     * 获取身份识别码
     * @return 身份识别码
     */
    String provide();
}

package com.aoligei.remoter.generate.impl;

import com.aoligei.remoter.generate.IIdentifierProvider;

/**
 * @author wk-mia
 * 2020-8-28
 * UUID生成器
 */
public class UUIDIdentifier implements IIdentifierProvider {

    @Override
    public String provide() {
        return java.util.UUID.randomUUID().toString();
    }
}

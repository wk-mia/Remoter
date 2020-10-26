package com.aoligei.remoter.service.driver;

import java.awt.*;

/**
 * @author wk-mia
 * 2020-10-26
 * Robot工厂
 */
public class RobotFactory {

    private volatile static Robot instance;

    private RobotFactory(){}

    /**
     * 获取一个Robot对象
     * @return
     */
    public static Robot getInstance(){
        if(instance == null){
            synchronized (RobotFactory.class){
                if(instance == null){
                    try {
                        instance = new Robot();
                    }catch (AWTException e){
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return instance;
    }

}

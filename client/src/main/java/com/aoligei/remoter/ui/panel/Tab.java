package com.aoligei.remoter.ui.panel;

import com.aoligei.remoter.util.AccessConfigUtil;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * @author wk-mia
 * 2020-9-26
 * 主窗体选项卡
 */
@Component
public class Tab extends JTabbedPane {

    /**
     * 标签名称
     */
    private final String[] tabNames = {
            AccessConfigUtil.getValue(AccessConfigUtil.Config.PARAM,"component.tab.name.control"),
            AccessConfigUtil.getValue(AccessConfigUtil.Config.PARAM,"component.tab.name.register"),
            AccessConfigUtil.getValue(AccessConfigUtil.Config.PARAM,"component.tab.name.setting")
    };

    /**
     * 图标
     */
    private final String[] icons = {
            AccessConfigUtil.getValue(AccessConfigUtil.Config.PARAM,"component.icons.dir") + "\\control.png",
            AccessConfigUtil.getValue(AccessConfigUtil.Config.PARAM,"component.icons.dir") + "\\register.png",
            AccessConfigUtil.getValue(AccessConfigUtil.Config.PARAM,"component.icons.dir") + "\\setting.png"
    };

    private final String labelIcon = AccessConfigUtil.getValue(AccessConfigUtil.Config.PARAM,"component.icons.dir") + "\\404.png";

    public Tab(){
        initStyle();
    }

    /**
     * 初始化窗体样式
     */
    private void initStyle(){
//        this.setSize(600,400);
        this.setTabPlacement(JTabbedPane.LEFT);
        for (int i = 0; i < tabNames.length; i++) {
            this.addTab(tabNames[i],
                    new ImageIcon(icons[i]),
                    new JLabel(){{
                setIcon(new ImageIcon(labelIcon));
                setHorizontalAlignment(SwingConstants.CENTER);
            }});
        }
    }


}

package com.aoligei.remoter.ui.service.listener;

import com.aoligei.remoter.netty.NettyClient;
import com.aoligei.remoter.ui.form.DialogPage;
import com.aoligei.remoter.ui.form.SlaverPage;
import com.aoligei.remoter.ui.panel.SlaverScreenPanel;
import com.aoligei.remoter.ui.service.action.IInteract;

import com.aoligei.remoter.util.ImageUtil;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-10-13
 * 远程窗口事件监听
 * 包含：启动、关闭
 */
@Component
public class SlaverPageActionListener implements WindowListener, IInteract {

    private static Logger log = LoggerFactory.getLogger(SlaverPageActionListener.class);

    /**
     * Netty客户端
     */
    @Autowired
    private NettyClient nettyClient;

    /**本地远程的窗体列表*/
    private Map<String,SlaverPage> slavers = new ConcurrentHashMap<>();

    /**
     * 启动远程窗口
     * @param pageTitle 窗口标题
     */
    @Override
    public void call(String pageTitle) {
        SlaverScreenPanel panel = new SlaverScreenPanel();
        SlaverPage page = new SlaverPage(pageTitle,panel);
        this.addListener(page,panel);

        slavers.put(pageTitle,page);
        /**展示窗体*/
        try {
            SwingUtilities.invokeAndWait(() -> {
                page.setVisible(true);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放屏幕截图
     * @param connectionId 连接编码
     * @param bytes 屏幕截图字节流
     */
    @Override
    public void play(String connectionId, byte[] bytes) {
        SlaverPage page = slavers.get(connectionId);
        if(page != null && page.getPanel() != null){
            /**转换图像格式，刷新屏幕*/
            BufferedImage image = ImageUtil.decode(bytes);
            page.getPanel().setImage(image);
            SwingUtilities.invokeLater(() -> {
                page.getPanel().repaint();
            });
        }else {
            log.error("no such slaverPage, please check the connectionId");
        }
    }

    /**
     * 给面板及窗体注册监听器
     * @param page 窗体
     * @param panel 面板
     */
    private void addListener(SlaverPage page, SlaverScreenPanel panel){
        page.addWindowListener(this);
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        SlaverPage page = (SlaverPage) e.getSource();
        String connectId = page.getTitle();
        /**发送停止远程控制命令*/
        try{
            nettyClient.stopControl(connectId);
        }catch (Exception ex){
            DialogPage.errorDialog("connect-error",ex.getMessage());
        }
        /**移除远程窗口*/
        slavers.remove(connectId);
        /**关闭远程窗口*/
        page.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

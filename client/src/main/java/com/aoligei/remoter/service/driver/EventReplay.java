package com.aoligei.remoter.service.driver;

import com.aoligei.remoter.event.KeyBoardEvent;
import com.aoligei.remoter.event.MouseActionEvent;
import com.aoligei.remoter.service.action.IReplay;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * @author wk-mia
 * 2020-10-26
 * 事件重播器，重播MASTER的事件。包括键盘事件和鼠标事件。
 */
@Component
public class EventReplay implements IReplay {

    /**
     * Robot
     */
    private Robot robot = RobotFactory.getInstance();

    /**
     * 重放键盘事件
     * @param event 键盘事件
     */
    @Override
    public void replayKeyBoardEvent(KeyBoardEvent event) {
        if(event.getKeyStatus() == true){
            /**键按下*/
            robot.keyPress(event.getKeyCode());
        }else {
            /**键释放*/
            robot.keyRelease(event.getKeyCode());
        }
    }

    /**
     * 重放鼠标事件
     * @param event 鼠标事件
     */
    @Override
    public void replayMouseActionEvent(MouseActionEvent event) {
        MouseActionEvent.MouseActionEnum mouseAction = event.getMouseAction();
        if(mouseAction == MouseActionEvent.MouseActionEnum.LEFT
                || mouseAction == MouseActionEvent.MouseActionEnum.MIDDLE
                ||mouseAction == MouseActionEvent.MouseActionEnum.RIGHT){
            this.mouseClick(event);
        }else if(mouseAction == MouseActionEvent.MouseActionEnum.DOUBLE_CLICK){
            this.leftDoubleClick(event);
        }else if(mouseAction == MouseActionEvent.MouseActionEnum.DRAGGED){
            this.dragged(event);
        }else if(mouseAction == MouseActionEvent.MouseActionEnum.WHEEL){
            this.wheelMove(event);
        }else {

        }
    }

    /**
     * 单击事件回放
     * @param event
     */
    private void mouseClick(MouseActionEvent event){
        int[] site = event.getSite();
        robot.mouseMove(site[0],site[1]);

        final int mouseButton = MouseActionEvent.deConvert(event.getMouseAction());
        robot.mousePress(mouseButton);
        robot.mouseRelease(mouseButton);
    }

    /**
     * 双击事件回放
     * @param event
     */
    private void leftDoubleClick(MouseActionEvent event){
        int[] site = event.getSite();
        robot.mouseMove(site[0],site[1]);

        final int mouseButton = MouseActionEvent.deConvert(event.getMouseAction());
        robot.mousePress(mouseButton);
        robot.mouseRelease(mouseButton);
        robot.mousePress(mouseButton);
        robot.mouseRelease(mouseButton);
    }

    /**
     * 拖拽事件回放
     * @param event
     */
    private void dragged(MouseActionEvent event){
        final int mouseButton1 = MouseActionEvent.deConvert(event.getMouseAction());
        robot.mousePress(mouseButton1);
        int[] site = event.getSite();
        robot.mouseMove(site[0],site[1]);
    }

    /**
     * 滚轮事件回放
     * @param event
     */
    private void wheelMove(MouseActionEvent event){
        robot.mouseWheel(event.getWheelSize());
    }
}

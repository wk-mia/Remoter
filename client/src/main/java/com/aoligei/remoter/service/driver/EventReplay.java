package com.aoligei.remoter.service.driver;

import com.aoligei.remoter.event.KeyBoardEvent;
import com.aoligei.remoter.event.MouseActionEvent;
import com.aoligei.remoter.event.MouseActionEvent.MouseActionEnum;
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
        if(mouseAction == MouseActionEnum.CLICK){
            this.mouseClick(event);
        }else if(mouseAction == MouseActionEnum.PRESSED){
            this.pressed(event);
        }else if(mouseAction == MouseActionEnum.RELEASED){
            this.released(event);
        }else if(mouseAction == MouseActionEnum.DRAGGED) {
            this.dragged(event);
        }else if(mouseAction == MouseActionEnum.WHEEL) {
            this.wheelMove(event);
        }else if(mouseAction == MouseActionEnum.MOVE) {
            this.move(event);
        }else {
        }
    }

    /**
     * 单击事件回放
     * @param event
     */
    private void mouseClick(MouseActionEvent event){
        final int mouseButton = event.getMouseButton().getCode();
        robot.mousePress(mouseButton);
        robot.mouseRelease(mouseButton);
    }


    /**
     * 鼠标按下回放
     * @param event
     */
    private void pressed(MouseActionEvent event){
        final int mouseButton = event.getMouseButton().getCode();
        robot.mousePress(mouseButton);
    }

    /**
     * 鼠标松开回放
     * @param event
     */
    private void released(MouseActionEvent event){
        final int mouseButton = event.getMouseButton().getCode();
        robot.mouseRelease(mouseButton);
    }

    /**
     * 拖拽事件回放
     * @param event
     */
    private void dragged(MouseActionEvent event){
        if(event.getMouseButton() != null){
            final int mouseButton = event.getMouseButton().getCode();
            robot.mousePress(mouseButton);
        }
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

    /**
     * 鼠标移动回放
     * @param event
     */
    private void move(MouseActionEvent event){
        int[] site = event.getSite();
        robot.mouseMove(site[0],site[1]);
    }
}

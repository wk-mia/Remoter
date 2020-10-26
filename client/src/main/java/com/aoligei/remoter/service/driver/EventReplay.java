package com.aoligei.remoter.service.driver;

import com.aoligei.remoter.event.KeyBoardEvent;
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
}

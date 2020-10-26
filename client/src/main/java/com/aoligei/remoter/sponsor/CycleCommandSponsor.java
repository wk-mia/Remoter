package com.aoligei.remoter.sponsor;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.manage.CycleTaskManage;
import com.aoligei.remoter.util.AccessConfigUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author wk-mia
 * 2020-9-28
 * 点对点模式客户端-周期性命令发起处理器
 * 负责客户端向服务器发起命令，包括MASTER以及SLAVE的各种周期性业务命令；
 * 如：屏幕截图、声音、心跳任务等。
 */
@Component(value = "CycleCommandSponsor")
public class CycleCommandSponsor extends AbstractCommandSponsor  {


    /**周期性任务管理器*/
    private CycleTaskManage cycleTaskManage;
    @Autowired
    public CycleCommandSponsor(CycleTaskManage cycleTaskManage){
        this.cycleTaskManage = cycleTaskManage;
    }


    /**
     * 周期性向服务器发起命令
     * @param request 请求体
     * @throws SponsorException 发起命令异常
     */
    @Override
    protected void particularSponsor(BaseRequest request) throws SponsorException {
        cycleTaskManage.schedule(context,request.getConnectionId());
    }
}

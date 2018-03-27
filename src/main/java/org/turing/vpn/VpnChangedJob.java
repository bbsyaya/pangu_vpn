package org.turing.vpn;


import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/**  
 * 需要执行的任务  
 * @author lhy  
 *  
 */  
public class VpnChangedJob implements Job {  
    //把要执行的操作，写在execute方法中  
    public void execute(JobExecutionContext arg0) throws JobExecutionException {  
        PanguVpnMng.getInstance().triggerTimes();
    }  
}  
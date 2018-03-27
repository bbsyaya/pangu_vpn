package org.turing.vpn.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.SwingWorker;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.turing.vpn.ForFile;
import org.turing.vpn.GetComputer;
import org.turing.vpn.HttpUtils;
import org.turing.vpn.PanguVpnMng;
import org.turing.vpn.PingUtils;
import org.turing.vpn.SMSEngine;
import org.turing.vpn.VpnChangedJob;
import org.turing.vpn.VpnUtil;
import org.turing.vpn.bean.ConnectInfo;
import org.turing.vpn.bean.Const;
import org.turing.vpn.bean.PGResponse;
import org.turing.vpn.bean.VpnConnectInfo;
import org.turing.vpn.request.VpnLoginReq;
import org.turing.vpn.request.VpnOperUpdateReq;
import org.turing.vpn.request.VpnSwitchFinishReq;
import org.turing.vpn.response.VpnLoginRsp;
import org.turing.vpn.response.VpnOperUpdateRsp;
import org.turing.vpn.response.VpnTaskStatistics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class DynamicVpnEngine implements OperaterIF{
	private ConnectInfo info = null;
	private UpdataUIListen listen = null;
	private VpnLoginRsp vpnLoginInfo = null;
	private static DynamicVpnEngine mInstance = new DynamicVpnEngine();
	private final int PING_RESUME_TIMES = 500; // ping 所费时间
	private Scheduler mScheduler = null;
	private boolean isCutVpn = false;
	private final int CONTINUE_CONNECT = 5; // 连续
	private int continueConnectCount = 0; // 连续
	private String vpnName = "";
	private int NOT_TASK_TIME = 10*60*1000; // 允许不执行任务时长
	private int notTaskTime = 0; // 计时
	public static DynamicVpnEngine getInstance() {
		if (null == mInstance) {
			mInstance = new DynamicVpnEngine();
		}
		return mInstance;
	}
	private int vpnConnectCount = 0;
	private List<VpnConnectInfo> vpnConnectInfoList = null;
	public void setInitInfo(ConnectInfo info,List<VpnConnectInfo> vpnConnectInfoList,UpdataUIListen ltn){
		listen = ltn;
		this.info = info;
		this.vpnConnectInfoList = vpnConnectInfoList;
		writeRebootFile("0");
	}
	public void buttonConnectVpn(){
		isCutVpn = false;
		VpnUtil.getInstance().rootComputerByTime();
		connectVpn();
	}
	public void buttonDisConnectVpn(){
		isCutVpn = true;
		disConnectVpn();
		listen.updateUI(false);
	}
	public void login() {
		// TODO Auto-generated method stub
		listen.printLog("\n 登录中...");
		SwingWorker<Void, Void> vpnWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				String url = PanguVpnMng.host + "vpnLogin.pangu";
				VpnLoginReq req = new VpnLoginReq();
				req.setOperType(info.getPurpose());
				req.setDeviceId(info.getDeviceId());
				String json = JSON.toJSONString(req);
				String result = HttpUtils.doPost(url, json, HttpUtils.UTF8);
				PGResponse<VpnLoginRsp> rsp = JSON.parseObject(result,
						new TypeReference<PGResponse<VpnLoginRsp>>() {
						});
				if (null == rsp || rsp.getStatus() != Const.common_ok) {
					return null;
				}
				vpnLoginInfo = rsp.getData();
				return null;
			}

			@Override
			public void done() {
				if (vpnLoginInfo != null) {
					listen.printLog("\n 登录成功 远程IP:"
							+ vpnLoginInfo.getRemoteIp());
					Integer time = vpnLoginInfo.getLoopTime();
					String condition = "0/" + time.toString() + " * * * * ?";
					startJob(condition);
				} else {
					listen.printLog("\n 登录失败");
					connectVpn(); //重连
				}
			}
		};
		vpnWorker.execute();
	
	}
	private void stopJob() {
		try {
			if (null != mScheduler) {
				mScheduler.shutdown();
			}

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void startJob(String condition) {
		CronTrigger mCornTrigger = null;
		// 通过schedulerFactory获取一个调度器
		SchedulerFactory schedulerfactory = new StdSchedulerFactory();
		try {
			// 通过schedulerFactory获取一个调度器
			mScheduler = schedulerfactory.getScheduler();
			// 创建jobDetail实例，绑定Job实现类
			// 指明job的名称，所在组的名称，以及绑定job类
			JobDetail jobDetail = new JobDetailImpl("job1", "jgroup1",
					(Class<? extends Job>) VpnChangedJob.class);

			// 定义调度触发规则，每天上午10：15执行
			mCornTrigger = new CronTriggerImpl("cronTrigger", "triggerGroup");
			// 执行规则表达式
			((CronTriggerImpl) mCornTrigger).setCronExpression(condition);
			// 把作业和触发器注册到任务调度中
			mScheduler.scheduleJob(jobDetail, mCornTrigger);
			// 启动调度
			mScheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean connectVpn() {
		disConnectVpn();
		VpnConnectInfo netInfo = vpnConnectInfoList.get(0);
		netInfo.setTunnelType(info.getTunnelType());
		VpnUtil.getInstance().editVpn(netInfo);
		try {
			vpnName = netInfo.getVpnName();
			String result = VpnUtil.getInstance().connectVpn(netInfo);
			listen.printLog("\n" + result);
			// 判断是否连接成功
			if (result.indexOf("已连接")>0) {
				
				listen.updateUI(true);
				listen.printLog("已成功建立连接,正在ping www.qq.com 直至 vpn 稳定 \n");
				for(int index = 0; index < 5;index++){
					Thread.sleep(3000);
					listen.printLog("第 " + index + "次 ping");
					if(true == PingUtils.ping("www.qq.com", 3, 3000)){
						break;
					}
				}
				int time = PingUtils.pingNeedTime("www.qq.com", 3, 5000);
				listen.printLog("ping 平均所消耗时间 :" + time + "ms");
				if( PING_RESUME_TIMES < time){
					listen.printLog("时间太久,重连VPN \n");
					continueConnectCount++;
					if(continueConnectCount >= CONTINUE_CONNECT){
						listen.printLog("连续5次频繁连接,5分钟后再试!!! \n");
						Thread.sleep(5*60*1000);
						connectVpn();
						listen.connectState(1);
					}else{
						listen.printLog("测试太慢，断开重连! \n");
						connectVpn();
						listen.connectState(1);
					}
				}else{
					listen.connectState(0);
					continueConnectCount = 0;
					listen.getCurrentIp();
					login();
				}
			} else {
				Thread.sleep(3*1000);
				listen.printLog("连接失败,等待3S重连 \n");
				connectVpn();
				listen.connectState(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public void disConnectVpn() {
		// TODO Auto-generated method stub
		stopJob();
		try {
			String result = VpnUtil.getInstance().cutVpn(vpnName);
			if (result.indexOf("没有连接") != -1) {
				listen.printLog("\n 连接不存在!");
			} else {
				listen.printLog("\n 连接已断开");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void writeTimeOut(VpnOperUpdateRsp rsp){
		int choosedMoniterCount = info.getDeviceCount();
		int runningCount = rsp.getRunningCount();
		if( choosedMoniterCount == runningCount){
			notTaskTime = 0; 
		}else{
			notTaskTime += 10*1000;
			// 判断有台电脑出故障了
			if(notTaskTime > NOT_TASK_TIME){
				listen.printLog("\n需重启模拟器...");
				writeRebootFile("1");
				VpnUtil.getInstance().rootComputer();
		        String[] param1 = new String[]{GetComputer.getConfig()};
				SMSEngine.getInstance().sendCCPRestSDK(param1);
				notTaskTime = 0;
			}
		}
	}
	private void writeRebootFile(String type){
		ForFile.writeFile("reboot", type);
	} 
	public void operUpdate() {
		listen.printLog("operUpdate-- time:"+new Date());  
		SwingWorker<Void, Void> vpnWorker = new SwingWorker<Void, Void>() {
			VpnOperUpdateRsp operRsp = null;
			@Override
			protected Void doInBackground() throws Exception {
				String url = PanguVpnMng.host + "vpnOperUpdate.pangu";
				VpnOperUpdateReq req = new VpnOperUpdateReq();
				req.setToken(vpnLoginInfo.getToken());
				String json = JSON.toJSONString(req);
				String result = HttpUtils.doPost(url, json, HttpUtils.UTF8);
				PGResponse<VpnOperUpdateRsp> rsp = JSON.parseObject(result,
						new TypeReference<PGResponse<VpnOperUpdateRsp>>() {
						});
				if (null == rsp || rsp.getStatus() != Const.common_ok) {
					return null;
				}
				operRsp = rsp.getData();
				return null;
			}

			@Override
			public void done() {
				if (null == operRsp || null == operRsp.getStatistics()){
					listen.printLog("\n 异常,operUpdate 返回为null");
					connectVpn(); // 这时也要切换vpn
					return;
				}
				listen.refreshTaskCount(operRsp.getStatistics());
				if (operRsp.getIsSwitchVpn() == 1) { // 需要切换VPN了
					listen.printLog("\n 收到后台切换vpn命令 \n");
					connectVpn();
				}else{
					listen.printLog("\n 不用切换VPN... \n");
				}
				writeTimeOut(operRsp);
			}
		};
		vpnWorker.execute();
	}
}

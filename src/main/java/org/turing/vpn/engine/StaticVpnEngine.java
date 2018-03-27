package org.turing.vpn.engine;

import java.util.ArrayList;
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
import org.turing.vpn.HttpUtils;
import org.turing.vpn.PanguVpnMng;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class StaticVpnEngine implements OperaterIF{
	private ConnectInfo info = null;
	private UpdataUIListen listen = null;
	private VpnLoginRsp vpnLoginInfo = null;
	private static StaticVpnEngine mInstance = new StaticVpnEngine();
	private Scheduler mScheduler = null;
	private boolean isCutVpn = false;
	public static StaticVpnEngine getInstance() {
		if (null == mInstance) {
			mInstance = new StaticVpnEngine();
		}
		return mInstance;
	}
	private int vpnConnectCount = 0;
	private List<VpnConnectInfo> vpnConnectInfoList = null;
	public void setInitInfo(ConnectInfo info,List<VpnConnectInfo> vpnConnectInfoList,UpdataUIListen ltn){
		listen = ltn;
		this.info = info;
		this.vpnConnectInfoList = vpnConnectInfoList;
		vpnConnectCount = 0;
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
	public void updateListIp(String ip){
		vpnConnectCount = 0;
		for(VpnConnectInfo info: vpnConnectInfoList){
			info.setIp(ip);
		}
	}

	public boolean connectVpn() {
		if(isCutVpn == true){
			return false;
		}
		disConnectVpn();
		VpnConnectInfo netInfo = null;
		if (vpnConnectCount >= vpnConnectInfoList.size()) { // 下发信息已经用完
			updateListIp(PanguVpnMng.getInstance().getSwitchIp());//切换IP
			netInfo = vpnConnectInfoList.get(vpnConnectCount);
			listen.printLog("\n ip:" + netInfo.getIp() + "第" + vpnConnectCount + "次连接");
		}else{
			netInfo = vpnConnectInfoList.get(vpnConnectCount);
			listen.printLog("\n ip:" + netInfo.getIp() + "第" + vpnConnectCount + "次连接");
		}
		netInfo.setTunnelType(info.getTunnelType());
		VpnUtil.getInstance().editVpn(netInfo);
		try {
			String result = VpnUtil.getInstance().connectVpn(netInfo);
			listen.printLog("\n" + result);
			// 判断是否连接成功
			if (result.indexOf("已连接") > 0) {
				listen.printLog("已成功建立连接,等待5S");
				listen.updateUI(true);
				Thread.sleep(3 * 1000);
				listen.getCurrentIp();
				if(null == vpnLoginInfo){
					login();
				}else{
					switchFinish();
				}
			} else {
				vpnConnectCount++;
				Thread.sleep(5*1000);
				listen.printLog("连接失败,等待5S重连");
				connectVpn();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public void buttonConnectVpn(){
		isCutVpn = false;
		connectVpn();
	}
	public void buttonDisConnectVpn(){
		isCutVpn = true;
		disConnectVpn();
		listen.updateUI(false);
	}
	public void disConnectVpn() {
		// TODO Auto-generated method stub
		VpnConnectInfo netInfo = vpnConnectInfoList.get(0);
		stopJob();
		try {
			String result = VpnUtil.getInstance().cutVpn("");
			if (result.indexOf("没有连接") != -1) {
				listen.printLog("\n 连接不存在!");
			} else {
				listen.printLog("\n 连接已断开");
			}
			Thread.sleep(5*1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    // vpn 切换完成
    private void switchFinish(){
    	listen.printLog("\n 完成切换vpn命令");
        SwingWorker<Void, Void> vpnWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                String url = PanguVpnMng.host + "vpnSwitchFinish.pangu";
                VpnSwitchFinishReq req = new VpnSwitchFinishReq();
                req.setToken(vpnLoginInfo.getToken());
                String json = JSON.toJSONString(req);
                String result = HttpUtils.doPost(url, json, HttpUtils.UTF8);
                PGResponse<String> rsp = JSON.parseObject(result,
                        new TypeReference<PGResponse<String>>() {
                        });
                if (null == rsp || rsp.getStatus() != Const.common_ok) {
                    return null;
                }
                return null;
            }

            @Override
            public void done() {
                if(vpnLoginInfo != null){
                    Integer time = vpnLoginInfo.getLoopTime();
                    String condition = "0/" +time.toString()+" * * * * ?";
                    startJob(condition);
                }
            }
        };
        vpnWorker.execute();
    }
	public void operUpdate() {
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
				if (null == operRsp)
					return;
				//listen.refreshTaskCount(operRsp.getTaskTotal(),operRsp.getFinishedTaskCount());
				if (operRsp.getIsSwitchVpn() == 1) { // 需要切换VPN了
					listen.printLog("\n 收到后台切换vpn命令");
					updateListIp(PanguVpnMng.getInstance().getSwitchIp());//切换这时IP
					connectVpn();
				}
			}
		};
		vpnWorker.execute();
	}

}

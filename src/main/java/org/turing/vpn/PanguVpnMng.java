package org.turing.vpn;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.turing.vpn.bean.ConnectInfo;
import org.turing.vpn.bean.Const;
import org.turing.vpn.bean.DynamicVpn;
import org.turing.vpn.bean.DynamicVpnExtend;
import org.turing.vpn.bean.PGResponse;
import org.turing.vpn.bean.RemainVpn;
import org.turing.vpn.bean.VpnConnectInfo;
import org.turing.vpn.engine.DynamicVpnEngine;
import org.turing.vpn.engine.StaticVpnEngine;
import org.turing.vpn.engine.UpdataUIListen;
import org.turing.vpn.request.GetBlackIpListReq;
import org.turing.vpn.request.GetDynamicVpnListReq;
import org.turing.vpn.request.GetRemainIpListReq;
import org.turing.vpn.request.GetRemainVpnListReq;
import org.turing.vpn.request.VpnConnectInfoReq;
import org.turing.vpn.request.VpnLoginReq;
import org.turing.vpn.request.VpnOperUpdateReq;
import org.turing.vpn.request.VpnSwitchFinishReq;
import org.turing.vpn.response.VpnConnectInfoRsp;
import org.turing.vpn.response.VpnLoginRsp;
import org.turing.vpn.response.VpnOperUpdateRsp;
import org.turing.vpn.response.VpnTaskStatistics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class PanguVpnMng implements UpdataUIListen {
	private static PanguVpnMng mInstance = new PanguVpnMng();
	private List<RemainVpn> staticVpnList = null;
	private List<DynamicVpnExtend> dynamicVpnList = null;
	
	public static String host = "http://pangu.u-app.cn/pc/";
	//public static String host = "http://127.0.0.1:8080/pc/";
	JList jdymanicVpnList = null;
	JList jdymanicIpList = null;
	JList jstaticVpnList = null;
	JList jstaticIpList = null;
	JComboBox jvpnType = null;
	JComboBox jvpnName = null;
	JLabel jpcNumber = null;
	JLabel jokCount = null;
	JLabel jnoCount = null;
	JLabel jcurIP = null;
	JLabel jinitIP = null;
	JButton jstartConnect = null;
	JButton jcloseConnect = null;
	JButton jstartConnectMoney = null;
	JButton jstartConnectWaterAmy = null;
	JButton jbtnL2tp = null;
	JButton jbtnPptp = null;
	JLabel jincreTotalCount = null;
	JLabel jstockTotalCount = null;
	JLabel jincreAllocCount = null;
	JLabel jstockAllocCount = null;
	JLabel jfinishedOKCount = null;
	JLabel jfinishedFailCount = null;
	JButton jforceChangVpn = null;
	JScrollPane jscrollConnectState = null;
	JComboBox jvpnFunction = null;
	JLabel jchoosedRet = null;
	ChoosedInfo choosdInfo = new ChoosedInfo();
	private RemainVpn choosedStaticVpn = null;
	private DynamicVpn choosedDynamicVpn = null;
	private List<VpnConnectInfo> vpnConnectInfoList = new ArrayList<VpnConnectInfo>();
	public static final int INCREMENT_MONEY_TYPE = 0;// 操作类型 0:增量赚钱 1:增量水军
														// 2:存量赚钱 3:存量水军
	public static final int INCREMENT_WATERAMY_TYPE = 1;// 操作类型 0:增量赚钱 1:增量水军
														// 2:存量赚钱 3:存量水军
	public static final int STOCK_MONEY_TYPE = 2;// 操作类型 0:增量赚钱 1:增量水军 2:存量赚钱
													// 3:存量水军
	public static final int STOCK_WATERAMY_TYPE = 3;// 操作类型 0:增量赚钱 1:增量水军 2:存量赚钱
													// 3:存量水军
	private ConnectInfo info = new ConnectInfo();
	private int okCount = 0;
	private int noCount = 0;
	private int vpnType = 0; // 0:留存 1:固定 2:动态
	private int playType = 0;// 0:顺序循环 1:随机
	private int vpnPurpose = 0;
	private int vpnName = 0; //

	private int vpnFunction = 0; //
	private boolean isUsedStaticVpn = false;
	private boolean isRelease = true;
	private boolean isVpnIsOK = true;
	// -------------------------------------------------

	private String mBlackIpList = "";
	private String mCurrentIp = "";
	private String mNotUseVpnIp = "";
	private int ipListIndex = 0; //
	public static PanguVpnMng getInstance() {
		if (null == mInstance) {
			mInstance = new PanguVpnMng();
		}
		return mInstance;
	}

	public void setHost() {
		if (true == isRelease) {
			int n = JOptionPane.showConfirmDialog(null, "当前在正式环境,确实切换至开发环境",
					"环境切换", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				host = "http://127.0.0.1:8080/pc/";
				isRelease = !isRelease;
			}
		} else {
			int n = JOptionPane.showConfirmDialog(null, "当前在开发环境,确实切换至正式环境",
					"环境切换", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				host = "http://pangu.u-app.cn/pc/";
				isRelease = !isRelease;
			}
		}
	}

	public void init(JLabel choosedRet,JButton btnL2tp,JButton btnPptp,JLabel pcNumber,JButton startConnect,JButton startConnectWaterAmy,JLabel increAllocCount,JLabel stockAllocCount,JLabel finishedOKCount,JLabel finishedFailCount,
			JList staticVpnlist,JList staticIplist,JComboBox vpnFunction, JButton startConnectMoney,
			JButton closeConnect, JList dymanicVpnList, JComboBox vpnType,JComboBox vpnName,JList dymanicIpList, JLabel okCount,
			JLabel noCount, JLabel curIP, JLabel initIp, JLabel increTotalCount,JLabel stockTotalCount, JScrollPane scrollConnectState) {
		this.jchoosedRet = choosedRet;
		this.jbtnL2tp = btnL2tp;
		this.jbtnPptp = btnPptp;
		this.jpcNumber = pcNumber;
		this.jstaticVpnList = staticVpnlist;
		this.jstaticIpList = staticIplist;
		this.jvpnFunction = vpnFunction;
		this.jstartConnect = startConnect;
		this.jstartConnectMoney = startConnectMoney;
		this.jstartConnectWaterAmy = startConnectWaterAmy;
		this.jcloseConnect = closeConnect;
		this.jdymanicVpnList = dymanicVpnList;
		this.jvpnType = vpnType;
		this.jvpnName = vpnName;
		this.jdymanicIpList = dymanicIpList;
		this.jokCount = okCount;
		this.jnoCount = noCount;
		this.jcurIP = curIP;
		this.jinitIP = initIp;
		this.jincreAllocCount = increAllocCount;
		this.jstockAllocCount = stockAllocCount;
		this.jfinishedOKCount = finishedOKCount;
		this.jfinishedFailCount = finishedFailCount;
		this.jincreTotalCount = increTotalCount;
		this.jstockTotalCount = stockTotalCount;
		this.jscrollConnectState = scrollConnectState;
		getDynamicVpnList();
		getStaticVpnList();
		updataUI();
		getBlackIpList();
		getInitIp();
		disableComcopent(false);
		jpcNumber.setText(GetComputer.getConfig());
	}

	public void refreshDymanicVpn() {
		getDynamicVpnList();
		getStaticVpnList();
	}

	public void dynamicVpnValueChanged(ListSelectionEvent e) {
		if (jdymanicVpnList.getSelectedIndex() == -1)
			return;
		choosedDynamicVpn = dynamicVpnList.get(jdymanicVpnList
				.getSelectedIndex()).getVpn();
		setDymanicIpList();
	}
	public void staticVpnValueChanged(ListSelectionEvent e) {
		if (jstaticVpnList.getSelectedIndex() == -1)
			return;
		ipListIndex = 0;
		choosedStaticVpn = staticVpnList.get(jstaticVpnList
				.getSelectedIndex());
		setStaticIpList();
	}
	public void activeVpnFunction() {
		if(null == jvpnFunction) return;
		
		vpnFunction = jvpnFunction.getSelectedIndex();
		if (vpnFunction == -1){
			vpnFunction = 0;
		}
		info.setPurpose(vpnFunction);
	}
	public void activeMonitorCount(int count){
		info.setDeviceCount(count);
		choosdInfo.setMonitorCount(count + "台");
		jchoosedRet.setText(choosdInfo.getPrint());
	}
	public void activeVpnType() {
		String[] strList = { "赚钱", "水军" };
		DefaultComboBoxModel typeModel = new DefaultComboBoxModel();
		DefaultComboBoxModel functionModel = new DefaultComboBoxModel();
		if (jvpnType == null) return;
		
		if(null == staticVpnList || null == dynamicVpnList)
			return;
		vpnType = jvpnType.getSelectedIndex();
		if(-1 == vpnType) return;
		
		jvpnName.removeAllItems();
		jvpnFunction.removeAllItems();
		if (vpnType == 0) {
			for (RemainVpn vpn : staticVpnList) {
				typeModel.addElement(vpn.getName());
			}
		} else if (vpnType == 1) {
			for (DynamicVpnExtend vpn : dynamicVpnList) {
				typeModel.addElement(vpn.getVpn().getName());
			}
		}
		for (int i = 0; i < strList.length ; i++) {
			functionModel.addElement(strList[i]);
		}
		jvpnName.setModel(typeModel);
		jvpnFunction.setModel(functionModel);
	}
	public void activeDeviceName() {
		//jdeviceCount.getSelectedItem().toString()
	}
	public void activeVpnName() {
		ipListIndex = 0;
		if(null == jvpnName) return;
		vpnName = jvpnName.getSelectedIndex();
		
		if(-1 == vpnName) return;
		
		if(null == staticVpnList || null == dynamicVpnList)
			return;
		
		if (vpnType == 0) {
			ipListIndex = 0;
			choosedStaticVpn = staticVpnList.get(vpnName);
			jstaticVpnList.setSelectedIndex(vpnName);
			setStaticIpList();
		} else if (vpnType == 1) {
			choosedDynamicVpn = dynamicVpnList.get(vpnName).getVpn();
			jdymanicVpnList.setSelectedIndex(vpnName);
			ipListIndex = 0;
			setDymanicIpList();
		}
	}

	// 切换IP
	public String getSwitchIp(){
		if (isUsedStaticVpn == true){
			ipListIndex++;
			ipListIndex = ipListIndex % jstaticIpList.getModel().getSize();
			String ip = jstaticIpList.getModel().getElementAt(ipListIndex).toString();
			jstaticIpList.setSelectedIndex(ipListIndex);
			System.out.print("--改变--IP:"+ip);
			return ip;
		}
		return "";
	}
	public void selectL2tp(){
		info.setTunnelType("L2tp");
		choosdInfo.setVpnConnectType("L2tp");
		jchoosedRet.setText(choosdInfo.getPrint());
	}
	public void selectPptp(){
		info.setTunnelType("Pptp");
		choosdInfo.setVpnConnectType("Pptp");
		jchoosedRet.setText(choosdInfo.getPrint());
	}
	private DynamicVpn getVpnByNumber(){
		String number = GetComputer.getConfig();
		for(DynamicVpnExtend vpn:dynamicVpnList){
			if(number.equals(vpn.getComputer().getDeviceSerial())){
				return vpn.getVpn();
			}
		}
		return null;
	}
	private void autoConnect(int purpose){
		info.setPurpose(purpose);
		choosedDynamicVpn = getVpnByNumber();
		choosdInfo.setDynmaticName(choosedDynamicVpn.getName());
		jchoosedRet.setText(choosdInfo.getPrint());
		vpnConnectInfoList.clear();
		vpnConnectInfoList = JSON.parseObject(choosedDynamicVpn.getConfigure(),
				new TypeReference<List<VpnConnectInfo>>() {
				});
		
		DynamicVpnEngine.getInstance().setInitInfo(info,vpnConnectInfoList, this);
		DynamicVpnEngine.getInstance().buttonConnectVpn();
	}
	public void startConnectMoney(){
		autoConnect(0);
	}
	public void startConnectWaterAmy(){
		autoConnect(1);
	}
	public void startConnect() {
		int n = JOptionPane.showConfirmDialog(null, "当前各选型选择是否无误？", "开始连接",
				JOptionPane.YES_NO_OPTION);		
		if (n == 0) {
			disableComcopent(true);
			info.setDeviceId(RandomUtils.getRandom(16));
			if (jvpnType.getSelectedIndex() == 0) {
				isUsedStaticVpn = true;
				StatusTextRefresh("\n" + "选择了固态IP");
				setStaticIpList();
				// 取第一个IP出来跑
				ipListIndex = ipListIndex%jstaticIpList.getModel().getSize();
				for(VpnConnectInfo conInfo:vpnConnectInfoList){
					conInfo.setIp((jstaticIpList.getModel().getElementAt(ipListIndex).toString()));
				}
				StaticVpnEngine.getInstance().setInitInfo(info,vpnConnectInfoList, this);
				StaticVpnEngine.getInstance().buttonConnectVpn();
			} else {
				isUsedStaticVpn = false;
				StatusTextRefresh("\n" + "选择了动态IP");
				setDymanicIpList();
				DynamicVpnEngine.getInstance().setInitInfo(info,vpnConnectInfoList, this);
				DynamicVpnEngine.getInstance().buttonConnectVpn();
			}
		}
	}

	public void closeConnect() {
		StatusTextRefresh("\n" + "结束连接");
		disableComcopent(false);
		if(isUsedStaticVpn == true){
			StaticVpnEngine.getInstance().buttonDisConnectVpn();
		}else{
			DynamicVpnEngine.getInstance().buttonDisConnectVpn();
		}
		
	}

	// 连接前参数准备
	private void setParmBeforeConnect() {
		String ip = "";
		info.setDeviceId(RandomUtils.getRandom(16));
		if (vpnType == 0) { // 固定IP
			setStaticIpList();
		} else {// 动态IP
			String serverAddress = (String) jdymanicIpList.getModel()
					.getElementAt(0);
			String name = (String) jdymanicIpList.getModel().getElementAt(1);
			String password = (String) jdymanicIpList.getModel()
					.getElementAt(2);
			setDymanicIpList();
		}
	}

	private void StatusTextRefresh(String str) {
		System.out.print(str);
	}

	private void refreshTaskInfo(VpnTaskStatistics statistics) {
		if( null == statistics){
			StatusTextRefresh("\n" + "statistics == null");
			return;
		}
		jincreTotalCount.setText("" + statistics.getTaskIncrementTotalCount());
		jstockTotalCount.setText("" + statistics.getTaskStockTotalCount());
		jincreAllocCount.setText("" + statistics.getTaskAllocIncrementCount());
		jstockAllocCount.setText("" + statistics.getTaskAllocStockCount());
		jfinishedOKCount.setText("" + statistics.getTaskReportFinishedCount());
		jfinishedFailCount.setText("" + statistics.getTaskReportNotFinishedCount());
		
		jincreTotalCount.updateUI();
		jstockTotalCount.updateUI();
		jincreAllocCount.updateUI();
		jstockAllocCount.updateUI();
		jfinishedOKCount.updateUI();
		jfinishedFailCount.updateUI();
	}
	private void disableComcopent(boolean isDisable) {

		if (false == isDisable) {
			jvpnType.setEnabled(true);
			jvpnName.setEnabled(true);

		} else {
			jvpnType.setEnabled(false);
			jvpnName.setEnabled(false);

		}
	}
	private void connectButtonDisable(boolean isDisable) {
		if (false == isDisable) {
			jstartConnect.setEnabled(true);
			jcloseConnect.setEnabled(true);
		}else{
			jstartConnect.setEnabled(false);
			jcloseConnect.setEnabled(true);
		}
	}
	public void triggerTimes() {
		if(isUsedStaticVpn == true){
			StaticVpnEngine.getInstance().operUpdate();
		}else{
			DynamicVpnEngine.getInstance().operUpdate();
		}
	}

	private void updataUI() {
		jokCount.setText(String.valueOf(okCount));
		jnoCount.setText(String.valueOf(noCount));
	}

	// 获取最新IP
	private void getInitIp() {
		SwingWorker<Void, Void> vpnWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				String url = host + "remoteIp.pangu";
				String json = HttpUtils.doGet(url,HttpUtils.UTF8);
				if (json != null && !json.equals("")) {
					JSONObject ob = JSON.parseObject(json);
					mNotUseVpnIp = ob.getString("data");
					if (null == mNotUseVpnIp || mNotUseVpnIp.equals(""))
						return null;
				}
				return null;
			}

			@Override
			public void done() {
				if (null == mNotUseVpnIp || mNotUseVpnIp.equals("")) {
					jinitIP.setText("请求IP失败");
				} else {
					jinitIP.setText(mNotUseVpnIp);
				}
			}
		};
		vpnWorker.execute();
	}
	private void setStaticIpList(){
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		String[] ipList = choosedStaticVpn.getIpList().split("\\|");
		for(String ip:ipList){
			listModel.addElement(ip);
		}
		jstaticIpList.setModel(listModel);
		jstaticIpList.setSelectedIndex(0);
		vpnConnectInfoList.clear();
		vpnConnectInfoList = JSON.parseObject(choosedStaticVpn.getConfigure(),
				new TypeReference<List<VpnConnectInfo>>() {
				});
	}
	private void setDymanicIpList() {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		listModel.addElement(choosedDynamicVpn.getDomain());
		listModel.addElement(choosedDynamicVpn.getUser());
		listModel.addElement(choosedDynamicVpn.getPassword());
		jdymanicIpList.setModel(listModel);
		jdymanicIpList.setSelectedIndex(0);
		vpnConnectInfoList.clear();
		vpnConnectInfoList = JSON.parseObject(choosedDynamicVpn.getConfigure(),
				new TypeReference<List<VpnConnectInfo>>() {
				});
	}
	public List<VpnConnectInfo> getConnectInfoList(){
		return vpnConnectInfoList;
	}
	private void getDynamicVpnList() {
		SwingWorker<Void, Void> vpnWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				String url = host + "getDynamicVpnList.pangu";
				GetDynamicVpnListReq req = new GetDynamicVpnListReq();
				String json = JSON.toJSONString(req);
				String result = HttpUtils.doPost(url, json, HttpUtils.UTF8);
				PGResponse<List<DynamicVpnExtend>> rsp = JSON.parseObject(result,
						new TypeReference<PGResponse<List<DynamicVpnExtend>>>() {
						});
				if (null == rsp || rsp.getStatus() != Const.common_ok) {
					return null;
				}
				dynamicVpnList = rsp.getData();
				return null;
			}

			@Override
			public void done() {
				if (null == dynamicVpnList || dynamicVpnList.size() == 0)
					return;
				DefaultListModel listModel = new DefaultListModel();
				for (DynamicVpnExtend vpn : dynamicVpnList) {
					listModel.addElement(vpn.getVpn().getName());
					//createConnect(vpn.getVpn().getName(),vpn.getVpn().getDomain(),"L2tp");
					System.out.print(vpn.getVpn().getConfigure());
					List<VpnConnectInfo> list = JSON.parseObject(vpn.getVpn().getConfigure(),
							new TypeReference<List<VpnConnectInfo>>() {
							});
					for(VpnConnectInfo info:list){
						info.setVpnName(vpn.getVpn().getName());
						info.setPassword(vpn.getVpn().getPassword());
						info.setIp(vpn.getVpn().getDomain());
						info.setUserName(vpn.getVpn().getUser());
						info.setAuthenticationMethod("Chap, MsChapv2, Pap");
						info.setEncryptionLevel("Optional");
					}	
					String json = JSON.toJSONString(list);
					vpn.getVpn().setConfigure(json);
				}
				jdymanicVpnList.setModel(listModel);
				jdymanicVpnList.setSelectedIndex(0);
				choosedDynamicVpn = dynamicVpnList.get(0).getVpn();
				setDymanicIpList();
				activeVpnType();
			}
		};
		vpnWorker.execute();
	}
	private void getStaticVpnList() {
		SwingWorker<Void, Void> vpnWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				String url = host + "getStaticVpnList.pangu";
				GetDynamicVpnListReq req = new GetDynamicVpnListReq();
				String json = JSON.toJSONString(req);
				String result = HttpUtils.doPost(url, json, HttpUtils.UTF8);
				PGResponse<List<RemainVpn>> rsp = JSON.parseObject(result,
						new TypeReference<PGResponse<List<RemainVpn>>>() {
						});
				if (null == rsp || rsp.getStatus() != Const.common_ok) {
					return null;
				}
				staticVpnList = rsp.getData();
				return null;
			}

			@Override
			public void done() {
				if (null == staticVpnList || staticVpnList.size() == 0)
					return;
				DefaultListModel listModel = new DefaultListModel();
				int index = 0;
				String authenticationMethod = "";
				for (RemainVpn vpn : staticVpnList) {
					listModel.addElement(vpn.getName());
					String ip = RandomUtils.getRandom(0, 255)+"."+RandomUtils.getRandom(0, 255)+"."+
							RandomUtils.getRandom(0, 255)+"."+RandomUtils.getRandom(0, 255);
					//createConnect(vpn.getName(),ip,"L2tp");
					System.out.print(vpn.getConfigure());
					List<VpnConnectInfo> list = JSON.parseObject(vpn.getConfigure(),
							new TypeReference<List<VpnConnectInfo>>() {
							});
					// 只是为改个名字
					if(index == 0){
						authenticationMethod = "Chap, MsChapv2";
					}else{
						authenticationMethod = "Chap, MsChapv2, Pap";
					}
					for(VpnConnectInfo info:list){
						info.setVpnName(vpn.getName());
						info.setAuthenticationMethod(authenticationMethod);
					}	
					String json = JSON.toJSONString(list);
					vpn.setConfigure(json);
					index ++;
				}
				jstaticVpnList.setModel(listModel);
				jstaticVpnList.setSelectedIndex(0);
				choosedStaticVpn = staticVpnList.get(0);
				setStaticIpList();
				activeVpnType();
			}
		};
		vpnWorker.execute();
	}
	private void createConnect(String name,String ip,String tunnelType){
		VpnConnectInfo info = new VpnConnectInfo();
		info.setVpnName(name);
		info.setIp(ip);
		info.setTunnelType(tunnelType);
		String vpnName = VpnUtil.getInstance().getVpn(info);
		if (null == vpnName || vpnName.equals("")) {
			VpnUtil.getInstance().createVpn(info);
		} else {
			//VpnUtil.getInstance().editVpn(info);
		}
	}
	private void getBlackIpList() {
		SwingWorker<Void, Void> vpnWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				String url = host + "getBlackIpList.pangu";
				GetBlackIpListReq req = new GetBlackIpListReq();
				req.setPlatformId(1L);
				String json = JSON.toJSONString(req);
				String result = HttpUtils.doPost(url, json, HttpUtils.UTF8);
				PGResponse<String> rsp = JSON.parseObject(result,
						new TypeReference<PGResponse<String>>() {
						});
				if (null == rsp || rsp.getStatus() != Const.common_ok) {
					return null;
				}
				mBlackIpList = rsp.getData();
				return null;
			}

			@Override
			public void done() {
			}
		};
		vpnWorker.execute();
	}

	public void updateUI(boolean isDisable) {
		// TODO Auto-generated method stub
		if(isDisable == true){
			connectButtonDisable(true);
		}else{
			connectButtonDisable(false);
		}
	}

	public void getCurrentIp() {
		// TODO Auto-generated method stub
		SwingWorker<Void, Void> vpnWorker = new SwingWorker<Void, Void>() {
			String json = null;

			@Override
			protected Void doInBackground() throws Exception {
				mCurrentIp = "";
				String url = host + "remoteIp.pangu";
				json = HttpUtils.doGet(url,HttpUtils.UTF8);
				if (json != null && !json.equals("")) {
					JSONObject ob = JSON.parseObject(json);
					mCurrentIp = ob.getString("data");
					if (null == mCurrentIp || mCurrentIp.equals(""))
						return null;

					jcurIP.setText(mCurrentIp);
				} else {
					jcurIP.setText("请求IP失败");
				}
				return null;
			}

			@Override
			public void done() {

			}
		};
		vpnWorker.execute();

	}

	public void printLog(String str) {
		// TODO Auto-generated method stub
		StatusTextRefresh(str);
	}

	public void connectState(int state) {
		// TODO Auto-generated method stub

	}

	public void refreshTaskCount(VpnTaskStatistics statistics) {
		// TODO Auto-generated method stub
		refreshTaskInfo(statistics);
	}

	public void changeIp() {
		// TODO Auto-generated method stub
		
	}
}

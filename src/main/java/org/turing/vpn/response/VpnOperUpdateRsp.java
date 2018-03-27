package org.turing.vpn.response;


public class VpnOperUpdateRsp extends BaseRsp{
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public String getRealIp() {
		return realIp;
	}
	public void setRealIp(String realIp) {
		this.realIp = realIp;
	}
	public int getIsSwitchVpn() {
		return isSwitchVpn;
	}
	public void setIsSwitchVpn(int isSwitchVpn) {
		this.isSwitchVpn = isSwitchVpn;
	}
	public int getLoopTime() {
		return loopTime;
	}
	public void setLoopTime(int loopTime) {
		this.loopTime = loopTime;
	}
	public int getTaskTotal() {
		return taskTotal;
	}
	public void setTaskTotal(int taskTotal) {
		this.taskTotal = taskTotal;
	}
	
	public VpnConnectInfoRsp getConnectInfo() {
		return connectInfo;
	}
	public void setConnectInfo(VpnConnectInfoRsp connectInfo) {
		this.connectInfo = connectInfo;
	}


	public VpnTaskStatistics getStatistics() {
		return statistics;
	}
	public void setStatistics(VpnTaskStatistics statistics) {
		this.statistics = statistics;
	}
	public int getRunningCount() {
		return runningCount;
	}
	public void setRunningCount(int runningCount) {
		this.runningCount = runningCount;
	}

	private String remoteIp;	//getRemoteAddr
	private String realIp; 		//x-forwarded-for
	private int isSwitchVpn;    //1: 是 0:否
	private int loopTime;		// 下次询问时长 单位秒
	private int taskTotal = 0;		// 任务总数
	private int runningCount = 0; // 正在运行任务数，正常始终等于模拟器的数量
	private VpnTaskStatistics statistics;
	private VpnConnectInfoRsp connectInfo;
}

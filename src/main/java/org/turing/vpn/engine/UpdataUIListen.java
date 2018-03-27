package org.turing.vpn.engine;

import org.turing.vpn.response.VpnTaskStatistics;

public interface UpdataUIListen {
	public void updateUI(boolean isDisable);
	public void changeIp();
	public void getCurrentIp();
	public void printLog(String str);
	public void connectState(int state); // 0:成功,1:失败
	public void refreshTaskCount(VpnTaskStatistics statistics);
}

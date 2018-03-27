package org.turing.vpn.response;

import java.util.ArrayList;
import java.util.List;

import org.turing.vpn.bean.VpnConnectInfo;

public class VpnConnectInfoRsp extends BaseRsp{	
	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	public List<VpnConnectInfo> getVpnList() {
		return vpnList;
	}
	public void setVpnList(List<VpnConnectInfo> vpnList) {
		this.vpnList = vpnList;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	private String ip = "";
	private List<VpnConnectInfo> vpnList = new ArrayList<VpnConnectInfo>();
	private Long groupId;
	private boolean isUsed = false;
}

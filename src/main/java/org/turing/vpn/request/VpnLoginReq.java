package org.turing.vpn.request;

public class VpnLoginReq extends BaseReq{
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public int getOperType() {
		return operType;
	}
	public void setOperType(int operType) {
		this.operType = operType;
	}
	private String deviceId;
	private int operType;	//操作类型  0:增量赚钱 1:增量水军 2:存量赚钱 3:存量水军
}

package org.turing.vpn.response;


public class VpnLoginRsp extends BaseRsp{
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getLoopTime() {
		return loopTime;
	}
	public void setLoopTime(int loopTime) {
		this.loopTime = loopTime;
	}
	private String remoteIp;	//getRemoteAddr
	private String realIp; 		//x-forwarded-for
	private String token;		// 下发token
	private int loopTime;		// 下次询问时长 单位秒
}

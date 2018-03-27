package org.turing.vpn.engine;

public interface OperaterIF {
	public void login();
	public boolean connectVpn(); 
	public void disConnectVpn(); 
	public void operUpdate();
}

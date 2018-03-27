package org.turing.vpn.request;

public class GetRemainIpListReq extends BaseReq{
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}

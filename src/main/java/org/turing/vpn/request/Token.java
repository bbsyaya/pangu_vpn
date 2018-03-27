package org.turing.vpn.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token implements Serializable{
	
    /**
	 * @author turing
	 */
	private static final Long serialVersionUID = 4139355991176419542L;
	private String timestamp;    //时间long值
    private String random;       //10位数的随机值
    private String signature;    //数据签名
    private String accessToken;  //访问token,后台没有下发时填 ""
    
    @JsonProperty
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	@JsonProperty
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	
	@JsonProperty
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	@JsonProperty
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}

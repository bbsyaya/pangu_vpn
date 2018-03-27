/**
 * 
 * Title：Computer
 * Copyright: Copyright (c) 2016
 * Company: turing
 * @author turing
 * @version 1.0, 2017年03月08日 
 * @since 2017年03月08日 
 */

package org.turing.vpn.bean;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

 /**Computer*/
 public class Computer extends BaseModel<Computer>
 {
    
  //自动生成区域开始
  private static final long serialVersionUID = 4228931488933792837L;

  /***/
  private Long userId = 1L;

  /***/
  private Long vpnId = 1L;

  /***/
  private Integer isValid = 1;

  /***/
  private String deviceSerial = "";

  /***/
  private String deviceToken = "";

  /***/
  private String address = "";

  /***/
  private Date createDate = new Date();

  /***/
  private Date updateDate = new Date();


  
  /**获取*/
  @JsonProperty
  @NotNull(groups = {Default.class,Save.class})
  public Long getUserId()
  {
   return this.userId;
  }

  /**设置*/
  public void setUserId(Long userId)
  {
    this.userId=userId;
  }

  
  /**获取*/
  @JsonProperty
  @NotNull(groups = {Default.class,Save.class})
  public Long getVpnId()
  {
   return this.vpnId;
  }

  /**设置*/
  public void setVpnId(Long vpnId)
  {
    this.vpnId=vpnId;
  }

  
  /**获取*/
  @JsonProperty
  public Integer getIsValid()
  {
   return this.isValid;
  }

  /**设置*/
  public void setIsValid(Integer isValid)
  {
    this.isValid=isValid;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =255 )
  @NotEmpty(groups = {Default.class,Save.class})
  public String getDeviceSerial()
  {
   return this.deviceSerial;
  }

  /**设置*/
  public void setDeviceSerial(String deviceSerial)
  {
    this.deviceSerial=deviceSerial;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =255 )
  public String getDeviceToken()
  {
   return this.deviceToken;
  }

  /**设置*/
  public void setDeviceToken(String deviceToken)
  {
    this.deviceToken=deviceToken;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =255 )
  public String getAddress()
  {
   return this.address;
  }

  /**设置*/
  public void setAddress(String address)
  {
    this.address=address;
  }

  
  /**获取*/
  @JsonProperty
  public Date getCreateDate()
  {
   return this.createDate;
  }

  /**设置*/
  public void setCreateDate(Date createDate)
  {
    this.createDate=createDate;
  }

  
  /**获取*/
  @JsonProperty
  public Date getUpdateDate()
  {
   return this.updateDate;
  }

  /**设置*/
  public void setUpdateDate(Date updateDate)
  {
    this.updateDate=updateDate;
  }

  //自动生成区域结束
 }

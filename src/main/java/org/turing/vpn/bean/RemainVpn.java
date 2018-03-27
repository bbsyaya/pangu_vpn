/**
 * 
 * Title：RemainVpn
 * Copyright: Copyright (c) 2016
 * Company: turing
 * @author turing
 * @version 1.0, 2017年02月24日 
 * @since 2017年02月24日 
 */

package org.turing.vpn.bean;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

 /**RemainVpn*/
 public class RemainVpn extends BaseModel<RemainVpn>
 {
    
  //自动生成区域开始
  private static final long serialVersionUID = 6952241487901338886L;

  /***/
  private Long groupId;

  /***/
  private String ipList;

  /***/
  private String name;

  /***/
  private String password;

  /***/
  private String user;

  /***/
  private String configure;

  /***/
  private Integer isValid;

  /***/
  private Date createDate;

  /***/
  private Date updateDate;


  
  /**获取*/
  @JsonProperty
  @NotNull(groups = {Default.class,Save.class})
  public Long getGroupId()
  {
   return this.groupId;
  }

  /**设置*/
  public void setGroupId(Long groupId)
  {
    this.groupId=groupId;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =5592405 )
  public String getIpList()
  {
   return this.ipList;
  }

  /**设置*/
  public void setIpList(String ipList)
  {
    this.ipList=ipList;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =255 )
  public String getName()
  {
   return this.name;
  }

  /**设置*/
  public void setName(String name)
  {
    this.name=name;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =255 )
  public String getPassword()
  {
   return this.password;
  }

  /**设置*/
  public void setPassword(String password)
  {
    this.password=password;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =255 )
  public String getUser()
  {
   return this.user;
  }

  /**设置*/
  public void setUser(String user)
  {
    this.user=user;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =255 )
  public String getConfigure()
  {
   return this.configure;
  }

  /**设置*/
  public void setConfigure(String configure)
  {
    this.configure=configure;
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

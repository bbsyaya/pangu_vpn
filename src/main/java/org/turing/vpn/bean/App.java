/**
 * 
 * Title：App
 * Copyright: Copyright (c) 2016
 * Company: turing
 * @author turing
 * @version 1.0, 2017年01月15日 
 * @since 2017年01月15日 
 */

package org.turing.vpn.bean;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

 /**App*/
 public class App extends BaseModel<App>
 {
    
  //自动生成区域开始
  private static final long serialVersionUID = 2099241484482629751L;

  /***/
  private Long userId;

  /***/
  private Long platformId;

  /***/
  private String name;

  /***/
  private String packageName;

  /***/
  private String apkPath;

  /***/
  private String token;

  /***/
  private Date createDate;

  /***/
  private Date updateDate;


  
  /**获取*/
  @JsonProperty
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
  public Long getPlatformId()
  {
   return this.platformId;
  }

  /**设置*/
  public void setPlatformId(Long platformId)
  {
    this.platformId=platformId;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =255 )
  @NotEmpty(groups = {Default.class,Save.class})
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
  @NotEmpty(groups = {Default.class,Save.class})
  public String getPackageName()
  {
   return this.packageName;
  }

  /**设置*/
  public void setPackageName(String packageName)
  {
    this.packageName=packageName;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =255 )
  public String getApkPath()
  {
   return this.apkPath;
  }

  /**设置*/
  public void setApkPath(String apkPath)
  {
    this.apkPath=apkPath;
  }

  
  /**获取*/
  @JsonProperty
  @Length(max =255 )
  @NotEmpty(groups = {Default.class,Save.class})
  public String getToken()
  {
   return this.token;
  }

  /**设置*/
  public void setToken(String token)
  {
    this.token=token;
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

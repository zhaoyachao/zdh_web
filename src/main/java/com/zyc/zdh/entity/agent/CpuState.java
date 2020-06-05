package com.zyc.zdh.entity.agent;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 *
 * @ClassName:CpuState.java     
 * @version V2.3
 * @author: wgcloud     
 * @date: 2019年11月16日
 * @Description: 查看CPU使用情况
 * @Copyright: 2019-2022 wgcloud. All rights reserved.
 *
 */
@Table(name="cpu_state")
public class CpuState {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2913111613773445949L;

	@Column(name="ID")
	private String id;

	/**
	 * host名称
	 */
	@Column(name="HOST_NAME")
	private String hostname;

	/**
	 * 用户态的CPU时间（%）废弃
	 */
	@Column(name="USER")
    private String user;

    /**
	 * cpu使用率
	 */
	@Column(name="SYS")
    private Double sys;
    
    /**
	 * 当前空闲率
	 */
	@Column(name="IDLE")
    private Double idle;
    
    /**
   	 * cpu当前等待率
   	 */
	@Column(name="IOWAIT")
    private Double iowait;
    
    /**
   	 * 硬中断时间（%） 废弃
   	 */
	@Column(name="IRQ")
    private String irq;
    
    /**
     * 软中断时间（%） 废弃
     */
	@Column(name="SOFT")
    private String soft;
    
    /**
     * 添加时间
     * MM-dd hh:mm:ss
     */
	@Column(name="DATE_STR")
    private String dateStr;
    
    /**
     * 创建时间
     */
	@Column(name="CREATE_TIME")
    private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Double getSys() {
		return sys;
	}

	public void setSys(Double sys) {
		this.sys = sys;
	}

	public Double getIdle() {
		return idle;
	}

	public void setIdle(Double idle) {
		this.idle = idle;
	}

	public Double getIowait() {
		return iowait;
	}

	public void setIowait(Double iowait) {
		this.iowait = iowait;
	}

	public String getIrq() {
		return irq;
	}

	public void setIrq(String irq) {
		this.irq = irq;
	}

	public String getSoft() {
		return soft;
	}

	public void setSoft(String soft) {
		this.soft = soft;
	}

	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDateStr() {
		if(!StringUtils.isEmpty(dateStr)&&dateStr.length()>16){
			return dateStr.substring(5);
		}
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

    
   
}
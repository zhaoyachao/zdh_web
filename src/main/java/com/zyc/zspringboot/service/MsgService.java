package com.zyc.zspringboot.service;

import java.util.List;

import com.zyc.zspringboot.netty.MsgInfo;

/**
 * ClassName: MsgService
 *
 * @author zyc-admin
 * @date 2017年12月26日
 * @Description: TODO
 */
public interface MsgService {

	public MsgInfo selectById(long id);

	public int insert(MsgInfo msgInfo);

	public int delete(long id);

	public int update(MsgInfo msgInfo);

	public List<MsgInfo> selectByExample(MsgInfo msgInfo);
}

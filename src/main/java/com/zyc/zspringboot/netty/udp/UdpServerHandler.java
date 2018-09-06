package com.zyc.zspringboot.netty.udp;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.zyc.zspringboot.netty.MsgInfo;
import com.zyc.zspringboot.service.MsgService;
import com.zyc.zspringboot.shiro.RedisUtil;
import com.zyc.zspringboot.util.SpringContext;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * ClassName: UdpServerHandler
 * 
 * @author zyc-admin
 * @param <DefaultChannelHandlerContext>
 * @date 2017年12月27日
 * @Description: TODO
 */
public class UdpServerHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {

	private static Logger logger = LoggerFactory
			.getLogger(UdpServerHandler.class);

	private static Map<String, ChannelHandlerContext> hash = new HashMap<>();
	private static Map<String, InetSocketAddress> hashIp = new HashMap<>();
	private static final String USER_IP_KEY="user:ip:";

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg)
			throws Exception {
		//ctx每次都是同一个对象
		String req = msg.content().toString(CharsetUtil.UTF_8);
		MsgInfo m = JSON.parseObject(req, MsgInfo.class);
		logger.info("ctx.hashCode====" + ctx.hashCode());
		if ("server".equals(m.getToUser())) {
			logger.debug("服务器：" + req);
			insert2Redis(m.getFormUser(), msg.sender());
		} else {
			logger.debug("接收到消息：" + m.getMsg());
			dealMsg(ctx, m);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.debug("错误：" + cause.getMessage());
		ctx.close();
	}

	/**
	 * 处理消息
	 * 
	 * @param ctx
	 * @param msg
	 */
	public void dealMsg(ChannelHandlerContext ctx, MsgInfo msgInfo) {

		// 固化消息
		insert2Db(msgInfo);
		// 获取收消息人地址
		if (getRedisUtil().exists(USER_IP_KEY+msgInfo.getToUser())) {
			String ip=getRedisUtil().get(USER_IP_KEY+msgInfo.getToUser()).toString();
			InetSocketAddress address = new InetSocketAddress(
					ip, 8767);
			ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
					JSON.toJSONString(msgInfo), CharsetUtil.UTF_8), address));
		}
	}

	/**
	 * 信息入库
	 * 
	 * @param msgInfo
	 */
	public void insert2Db(MsgInfo msgInfo) {
		// 是否是组信息
		if (msgInfo.getGroup().equals("true")) {
			getMsgService().insert(msgInfo);
		} else {
			getMsgService().insert(msgInfo);
		}
	}

	/**
	 * 获取信息入库service
	 * @return
	 */
	public MsgService getMsgService() {
		return ((MsgService) SpringContext.getBean("msgService"));
	}
	
	/**
	 * 登录用户ip保存到redis
	 * @param formUser
	 * @param sender
	 */
	private void insert2Redis(String formUser, InetSocketAddress sender) {
		logger.debug(USER_IP_KEY+formUser+sender.getHostString());
		getRedisUtil().set(USER_IP_KEY+formUser,sender.getHostString(), 100L,TimeUnit.SECONDS);
	}
	
	/**
	 * 获取redisUtil对象
	 * @return
	 */
	public RedisUtil getRedisUtil(){
		return (RedisUtil) SpringContext.getBean("redisUtil");
	}
}

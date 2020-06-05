package com.zyc.zdh.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.netty.MsgInfo;
import com.zyc.zdh.service.MsgService;
import com.zyc.zdh.util.SpringContext;

/**
 * ClassName: NettyUdpTestClient
 * 
 * @author zyc-admin
 * @date 2017年12月27日
 * @Description: TODO
 */
public class NettyUdpTestClient2 {

	EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
	static Channel channel;
	boolean run = true;

	public void connection() {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup).channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.option(ChannelOption.SO_RCVBUF, 1024 * 1024)
				.option(ChannelOption.SO_SNDBUF, 1024 * 1024)
				.handler(new MySimpleChannelInboundHandler2());
		try {
			channel = bootstrap.bind(8767).sync().channel();
			final MsgInfo msg = new MsgInfo();
			msg.setMsgId(SnowflakeIdWorker.getInstance().nextId());
			msg.setMsg("你好啊?");
			msg.setToUser("server");
			msg.setFormUser("zzz");
			msg.setGroup("true");
			String m = JSON.toJSONString(msg);
			System.out.println(m.getBytes().length);
			channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
					m.toString(), CharsetUtil.UTF_8), new InetSocketAddress(
					"10.50.7.24", 8766)));
			while (run) {
				System.out.println("channel.isActive()===="
						+ channel.isActive());
				channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
						m.toString(), CharsetUtil.UTF_8),
						new InetSocketAddress("10.50.7.24", 8766)));
				try {
					Thread.sleep(1000 * 20);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					run = false;
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		final NettyUdpTestClient2 nutc = new NettyUdpTestClient2();

		new Thread(new Runnable() {
			@Override
			public void run() {
				nutc.connection();
			}
		}).start();
		MsgInfo msg = new MsgInfo();
		msg.setMsgId(SnowflakeIdWorker.getInstance().nextId());
		msg.setMsg("this is my first message");
		msg.setToUser("zyc");
		msg.setFormUser("zzz");
		msg.setGroup("true");
		String m = JSON.toJSONString(msg);
		System.out.println(m.getBytes().length);
		while (channel == null) {
			System.out.println("channel is dead");
			try {
				Thread.sleep(1000*2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
				m.toString(), CharsetUtil.UTF_8), new InetSocketAddress(
				"10.50.7.24", 8766)));
	}

}

final class MySimpleChannelInboundHandler2 extends
		SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg)
			throws Exception {
		String m = msg.content().toString(CharsetUtil.UTF_8);
		MsgInfo msgInfo = JSON.parseObject(m, MsgInfo.class);
		System.out.println("client2====" + msgInfo.getMsg());
		msgInfo.setState(1);
		((MsgService) SpringContext.getBean("msgService"))
				.update(((MsgInfo) msgInfo));
		System.out.println("已结束信息");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e)
			throws Exception {
		ctx.close();
	}

}

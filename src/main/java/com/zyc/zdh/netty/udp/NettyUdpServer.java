package com.zyc.zdh.netty.udp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;


/**
 * ClassName: NettyUdpServer
 * 
 * @author zyc-admin
 * @date 2017年12月27日
 * @Description:udp netty服务端
 */
@Service("nettyUdpServer")
public class NettyUdpServer {
	
	private static Logger logger = LoggerFactory.getLogger(NettyUdpServer.class);

	@Value("${udp.ip}")
	private String udpIp;
	@Value("${udp.port}")
	private int port;

	EventLoopGroup workGroop = new NioEventLoopGroup();

	public void startServer() {
		Bootstrap serverBootStrap = new Bootstrap();
		serverBootStrap.group(workGroop).channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.option(ChannelOption.SO_RCVBUF, 1024 * 1024)
				.option(ChannelOption.SO_SNDBUF, 1024 * 1024)
				.handler(new ChannelInitializer<NioDatagramChannel>() {
					@Override
					protected void initChannel(NioDatagramChannel ch)
							throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new UdpServerHandler());
					}
				});

		ChannelFuture cf;
		try {
			logger.debug(udpIp + "==========" + port);
			cf = serverBootStrap.bind(udpIp, port).sync();
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} finally {
			workGroop.shutdownGracefully();
		}
	}

	public void shutdown() {
		workGroop.shutdownGracefully();
	}
}
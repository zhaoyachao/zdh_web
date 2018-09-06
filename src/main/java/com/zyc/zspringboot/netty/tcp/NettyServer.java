package com.zyc.zspringboot.netty.tcp;



import org.springframework.stereotype.Service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * ClassName: NettyServer   
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description: TODO  
 */
@Service("nettyServer")
public class NettyServer {
	EventLoopGroup bossGroop=new NioEventLoopGroup(); 
	EventLoopGroup workGroop=new NioEventLoopGroup(); 
	
	
	public void startServer(){
	
		ServerBootstrap serverBootStrap=new ServerBootstrap();
		serverBootStrap.group(bossGroop, workGroop)
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 1024)
		/*.option(ChannelOption.SO_SNDBUF, 32*1024)
		.option(ChannelOption.SO_RCVBUF, 32*1024)*/
		.option(ChannelOption.SO_KEEPALIVE,true)
		.childHandler(new ChannelInitializer<SocketChannel>(){

			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
//				//设置特殊分隔符
//				ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
//				sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
//				//设置字符串形式的解码
//				sc.pipeline().addLast(new StringDecoder());
				
				//设置传递参数类型为对象
				sc.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
				sc.pipeline().addLast(new ObjectEncoder());
				//设置心态检测,心跳检测handler必须放在自定义handler之前
				sc.pipeline().addLast(new IdleStateHandler(100,60,30),new ServerHandler());
				
			}
		});
		
		try {
			ChannelFuture cf=serverBootStrap.bind(8765).sync();
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bossGroop.shutdownGracefully();
			workGroop.shutdownGracefully();
		}
	}
	
	public void shutdown(){
		bossGroop.shutdownGracefully();
		workGroop.shutdownGracefully();
	}
}

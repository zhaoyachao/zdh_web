package com.zyc.zspringboot.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import com.zyc.zspringboot.jdbc.MySqlUtil;
import com.zyc.zspringboot.job.SnowflakeIdWorker;
import com.zyc.zspringboot.netty.MsgInfo;

/**
 * ClassName: NettyTestClient
 * 
 * @author zyc-admin
 * @date 2017年12月26日
 * @Description: TODO
 */
public class NettyTestClient2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sendMsg();
	}

	public static void sendMsg() {
		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap b = new Bootstrap();
		b.group(group).option(ChannelOption.SO_KEEPALIVE, true)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel sc)
							throws Exception {
						//
						sc.pipeline().addLast(
								new ObjectDecoder(ClassResolvers
										.cacheDisabled(this.getClass()
												.getClassLoader())));
						sc.pipeline().addLast(new ObjectEncoder());
						sc.pipeline().addLast(new MyClientHandler2());
					}
				});

		ChannelFuture cf;
		try {
			cf = b.connect("127.0.0.1", 8765).sync();
			
				MsgInfo msg = new MsgInfo();
				msg.setMsgId(SnowflakeIdWorker.getInstance().nextId());
				msg.setMsg("消息1");
				msg.setGroup("false");
				msg.setFormUser("zzz");
				msg.setToUser("zyc");
				cf.channel().writeAndFlush(msg);
			

			// 等待客户端端口关闭
			cf.channel().closeFuture().sync();
             while(true){}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}

	}
}

class MyClientHandler2 extends ChannelHandlerAdapter{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if(msg instanceof MsgInfo){
			//server 心跳信息
			if(((MsgInfo) msg).getFormUser().equals("server")){
				MsgInfo m=new MsgInfo();
				m.setFormUser("zyc");
				m.setToUser("server");
				m.setMsg("心跳连接信息");
				m.setGroup("false");
				m.setState(0);
				m.setType("");
				ctx.writeAndFlush(m);
			}else{
				MySqlUtil mySqlUtil=new MySqlUtil();
				((MsgInfo) msg).setState(1);
				System.out.println(((MsgInfo) msg).getMsg());
				mySqlUtil.update((MsgInfo) msg);
			}
			
		}
	}
}
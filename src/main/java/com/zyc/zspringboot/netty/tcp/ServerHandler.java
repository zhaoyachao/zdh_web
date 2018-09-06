package com.zyc.zspringboot.netty.tcp;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyc.zspringboot.netty.MsgInfo;
import com.zyc.zspringboot.service.MsgService;
import com.zyc.zspringboot.util.SpringContext;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * ClassName: ServerHandler   
 * @author zyc-admin
 * @date 2017年12月26日  
 * @Description:
 */
public class ServerHandler extends ChannelHandlerAdapter {

	private static Logger logger=LoggerFactory.getLogger(ServerHandler.class);
	private static Map<String,Channel> hash=new HashMap<String,Channel>();
	private static Map<String,String> hashUser=new HashMap<String,String>();
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		logger.debug("心跳监测");
		 if (evt instanceof IdleStateEvent) {  
	            IdleStateEvent event = (IdleStateEvent) evt;  
	            if (event.state() == IdleState.READER_IDLE) {  
	                /*读超时*/  
	                System.out.println("READER_IDLE 读超时");
	                MsgInfo msgInfo=new MsgInfo();
	                msgInfo.setFormUser("server");
	                msgInfo.setMsg("读超时");
	               ctx.writeAndFlush(msgInfo);
	               // ctx.disconnect();  
	            } else if (event.state() == IdleState.WRITER_IDLE) {  
	                /*写超时*/     
	                System.out.println("WRITER_IDLE 写超时");
	                MsgInfo msgInfo=new MsgInfo();
	                msgInfo.setFormUser("server");
	                msgInfo.setMsg("写超时");
	               ctx.writeAndFlush(msgInfo);
	               // ctx.disconnect();  
	            } else if (event.state() == IdleState.ALL_IDLE) {  
	                /*总超时*/  
	                System.out.println("ALL_IDLE 总超时");  
	                MsgInfo msgInfo=new MsgInfo();
	                msgInfo.setFormUser("server");
	                msgInfo.setMsg("读写超时");
	               ctx.writeAndFlush(msgInfo);
	                //ctx.disconnect();  
	            }  
	        }  
		
	}
	
    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug(" server channel active... ");
		
	}
    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
    	System.out.println("ctx.hashCode()===="+ctx.hashCode());
    	System.out.println("ctx.channel().id()===="+ctx.channel().id());
    	if(msg instanceof MsgInfo){
    		if(((MsgInfo) msg).getToUser().equals("server")){
    			//心跳发送过来的，需要保存自己channel
    			hash.put(ctx.channel().id().toString(), ctx.channel());
    			hashUser.put(((MsgInfo) msg).getFormUser(), ctx.channel().id().toString());
    		}else{
    			//用户和用户的信息，需要找到toUser用户的channel 然后写数据
    			String channelId=hashUser.get(((MsgInfo) msg).getToUser());
    			Channel channel=hash.get(channelId);
    			((MsgService) SpringContext.getBean("msgService")).insert(((MsgInfo) msg));
    			channel.writeAndFlush(msg);
    		}
    	
    		
    		/*if(((MsgInfo) msg).getGroup().equals("true")){
    			logger.debug(((MsgInfo) msg).getMsg());
    			((MsgService) SpringContext.getBean("msgService")).insert(((MsgInfo) msg));
    		}else{
    			logger.debug(((MsgInfo) msg).getMsg());
    			((MsgService) SpringContext.getBean("msgService")).insert(((MsgInfo) msg));
    		}*/
    	}
		
		//ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
//    	((MsgInfo) msg).setState(1);
//		((MsgService) SpringContext.getBean("msgService")).update(((MsgInfo) msg));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t)
			throws Exception {
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("logout");
	}

	public void insert2Redis(ChannelHandlerContext ctx, MsgInfo msg){
		
	}
}

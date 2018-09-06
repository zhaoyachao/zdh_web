package com.zyc.zspringboot.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.Serializable;

public class ZookeeperTest implements Serializable{

	private static final long serialVersionUID = -4551346599411638603L;

	public static void main(String[] args){
		ZooKeeper zk= null;
		try {
			zk = new ZooKeeper("10.31.2.153:2181",30000,null);
			zk.create("/zyc","zhaoyachao".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			Stat stat = new Stat();
			System.out.println(new String(zk.getData("/zyc",true,stat)));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		}

	}
}

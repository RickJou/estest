package com.niiwo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.niiwoo.zk.CuratorListens;
import com.niiwoo.zk.InitBusinessDataToZk;

@RunWith(SpringJUnit4ClassRunner.class) // 使用junit4进行测试
@ContextConfiguration(locations = { "classpath:/spring-context.xml" })
public class TestStartApp {

	@Test
	public void addUserBasicInfo() {
		try {
			//监听总节点
			CuratorListens.initListen();
			//初始化时间节点
			InitBusinessDataToZk.initMonitorAndWarningDataToZK();
			
			Thread.currentThread().sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

package com.niiwoo.zk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.log4j.Logger;

public class CuratorListens {
	private static Logger log = Logger.getLogger(CuratorListens.class);

	public static void initListen() {
		// 监听主节点,相当于将这个主节点下的所有子节点递归同步到本地内存,在设计时注意粒度,不要设计太多的层次.
		listenZK(ZkToLocalStore.LISTEN_BASE_PATH);
	}

	/**
	 * 监听节点和子节点从创建
	 * 
	 * @param generalType
	 */
	public static void listenZK(String listenPath) {
		TreeCache tc = CuratorUtil.getTreeCaheListen(listenPath);

		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		TreeCacheListener listen = new TreeCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				ChildData data = event.getData();
				if (data != null) {
					String path = data.getPath();
					switch (event.getType()) {
					case INITIALIZED:
						break;
					case NODE_ADDED:
						log.info("监听到zookeeper添加节点" + path);
						ZkToLocalStore.addPath(path, new String(data.getData()));
						break;
					case NODE_UPDATED:
						log.info("监听到zookeeper修改子节点" + path);
						ZkToLocalStore.updatePath(path, new String(data.getData()));
						break;
					case NODE_REMOVED:
						log.info("监听到zookeeper移除子节点" + path);
						ZkToLocalStore.removePath(path);
						break;
					default:
						break;
					}
				}
			}
		};
		tc.getListenable().addListener(listen,pool);
		try {
			tc.start();
			log.info("curator 开启对zookeeper:[" + listenPath + "]的TreeCache类监听");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	};

}

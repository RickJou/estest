package com.niiwoo.es;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Elastic Search Hight Lever 操作执行类
 * @author alan
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EsRestExcute {

	private static EsConnection lowerLeverRestConnection;
	private static RestHighLevelClient hightLeverRestConnection;
	private static Object initLock = new Object();

	private static RestClient getLowerConnection() {
		if (lowerLeverRestConnection == null) {// doubel check
			lowerLeverRestConnection = new EsConnection();
		}
		return lowerLeverRestConnection.getConnection();
	}

	private static RestHighLevelClient getConnection() {
		if (hightLeverRestConnection == null) {
			synchronized (initLock) {
				if (hightLeverRestConnection == null) {// double check
					hightLeverRestConnection = new RestHighLevelClient(getLowerConnection());
				}
			}
		}
		return hightLeverRestConnection;
	}

	public static void InsertData(String indexName, String indexType, String dataId, Map<String,Object> param) {
		IndexRequest request = new IndexRequest(indexName.toLowerCase(), indexType, dataId);
		request.source(param);
		try {
			IndexResponse indexResponse = getConnection().index(request);
			if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
			    //新增成功
				 System.out.println("新增成功");
			} else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
			    //更新成功
				System.out.println("更新成功");
			}
			ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
			if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
			    //返回分片总数不等于总分片数
				System.out.println("返回分片总数不等于总分片数");
			}
			if (shardInfo.getFailed() > 0) {
			    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
			        String reason = failure.reason(); 
			        //错误
			        System.out.println(reason);
			    }
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void InsertDataSync(String indexName, String indexType, String dataId, Map<String,Object> param,ActionListener<IndexResponse> listener) {
		IndexRequest request = new IndexRequest(indexName.toLowerCase(), indexType, dataId);
		request.source(param);
		getConnection().indexAsync(request, listener);
	}

	public static void main(String[] args) throws InterruptedException {
		Map param = new HashMap();
		param.put("username", "张三");
		param.put("age", "20");
		param.put("address", "零丁洋");
		
		//异步监听
		ActionListener<IndexResponse> listen = new ActionListener<IndexResponse>() {
		    @Override
		    public void onResponse(IndexResponse indexResponse) {
		    	if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
				    //新增成功
					 System.out.println("新增成功");
				} else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
				    //更新成功
					System.out.println("更新成功");
				}
				ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
				if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
				    //返回分片总数不等于总分片数
					System.out.println("返回分片总数不等于总分片数");
				}
				if (shardInfo.getFailed() > 0) {
				    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
				        String reason = failure.reason(); 
				        //错误
				        System.out.println(reason);
				    }
				}
		    }
		    @Override
		    public void onFailure(Exception e) {
		        e.printStackTrace();
		    }
		};
		
		
		
		long time1 = System.currentTimeMillis();
		List<Thread> listThread = new ArrayList<Thread>();
		for (int i = 0; i < 100; i++) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					long time = System.currentTimeMillis();
					//EsRestExcute.InsertData("hightLeverIndexName"+time, "hightIndexType"+time, time+"", param);
					EsRestExcute.InsertDataSync("hightLeverIndexName"+time, "hightIndexType"+time, time+"", param,listen);
				}
			});
			listThread.add(t);
			try {
				Thread.currentThread().sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("开始并发");
		for (Thread t : listThread) {
			t.start();
			//t.join();
		}
		System.out.println("耗时:"+(System.currentTimeMillis()-time1));

	}
	
	
}

package com.niiwoo.es;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;

import com.alibaba.fastjson.JSON;

/**
 * Elastic Search Lower Rest操作执行类
 * @author alan
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EsLowerRestExcute {

	private static EsConnection connectioin;

	private static RestClient getConnection() {
		if (connectioin == null) {// doubel check
			connectioin = new EsConnection();
		}
		return connectioin.getConnection();
	}

	public static void InsertData(String indexName, String indexType, String dataId, final String jsonString,ResponseListener listener) {
		final StringBuilder filter = new StringBuilder();
		filter.append("/").append(indexName.toLowerCase()).append("/").append(indexType).append("/").append(dataId);

		HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
		Map<String, String> catParam = Collections.singletonMap("pretty", "true");
		getConnection().performRequestAsync(RestApi.PUT, filter.toString(), catParam, entity, listener);
	}

	public static void getDataById(String indexName, String indexType, String dataId, ResponseListener listener) {
		StringBuilder filter = new StringBuilder();
		filter.append("/").append(indexName.toLowerCase()).append("/").append(indexType).append("/").append(dataId);
		getConnection().performRequestAsync(RestApi.GET, filter.toString(), listener);

	}


	public static void main(String[] args) {
		Map param = new HashMap();
		param.put("username", "张三");
		param.put("age", "20");
		param.put("address", "零丁洋");
		
		ResponseListener addDataListener = new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				// 新增成功
				if (response.getStatusLine().getStatusCode() == 200) {
					System.out.println("新增成功");
					System.out.println(response.toString());
				}
			}

			@Override
			public void onFailure(Exception exception) {
				// 补偿处理
				exception.printStackTrace();
			}
		};
		
		ResponseListener queryListener = new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				// 新增成功
				if (response.getStatusLine().getStatusCode() == 200) {
					System.out.println("查询成功");
					System.out.println(response.toString());
				}
			}

			@Override
			public void onFailure(Exception exception) {
				if(exception instanceof ResponseException){
					
				}
				// 查询失败
				exception.printStackTrace();
			}
		};
		
		for (int i = 0; i <= 1; i++) {
			EsLowerRestExcute.InsertData("testIndex", "testType1", i + "", JSON.toJSONString(param),queryListener);
			EsLowerRestExcute.getDataById("testIndex", "testType1", "3",addDataListener);
		}

	}

}

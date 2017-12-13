package com.niiwoo.es;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

public class EsConnection {
	private HttpHost[] hosts;
	private RestClient restClient = null;

	public EsConnection() {
		init();
	}

	/**
	 * 初始化连接
	 */
	@SuppressWarnings("unused")
	private synchronized void init() {
		if (restClient == null) {
			// 默认vm调试环境
			if (hosts == null) {
				HttpHost host1 = new HttpHost("192.168.188.20", 9200, "http");
				HttpHost host2 = new HttpHost("192.168.188.21", 9200, "http");
				HttpHost host3 = new HttpHost("192.168.188.22", 9200, "http");
				hosts = new HttpHost[] { host1, host2, host3 };
			}

			RestClientBuilder builder = RestClient.builder(hosts);
			// 连接超时默认为1秒,网络超时默认为30秒,最大重试超时时间默认为30秒
			builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
				@Override
				public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
					return requestConfigBuilder.setConnectTimeout(30000).setSocketTimeout(60000);
				}
			}).setMaxRetryTimeoutMillis(60000);
			// Apache Http异步客户端默认启动一个调度程序线程和与本地检测到的处理器数量（取决于
			// Runtime.getRuntime().availableProcessors()返回的数量）一样多的工作线程数量。线程数可以修改如下：
			builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
				@Override
				public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
					int count  = Runtime.getRuntime().availableProcessors()*100;
					System.out.println("线程数:"+count);
					return httpClientBuilder.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(count).build());
				}
			});

			// 先占式身份验证可以被禁用，这意味着每个请求都将被发送出去而没有授权头，以查看它是否被接受，并且当收到HTTP 401响应时，
			// 它将重新发送与基本身份验证头完全相同的请求。如果你想这样做，那么你可以通过以下方法禁用它HttpAsyncClientBuilder：
			// x-pack是收费的,所以默认不使用认证
			if (false) {
				final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("user", "password"));
				builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
					@Override
					public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
						httpClientBuilder.disableAuthCaching();
						return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
					}
				});
			}
			restClient = builder.build();
			System.out.println("初始化连接成功");
		}
	}

	/**
	 * 获取es连接
	 * @return
	 */
	public RestClient getConnection() {
		if (restClient == null) {
			init();
		}
		return restClient;
	}

	/**
	 * 释放资源,关闭连接,慎用!
	 */
	public synchronized void closeClient() {
		if (restClient != null) {
			try {
				restClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

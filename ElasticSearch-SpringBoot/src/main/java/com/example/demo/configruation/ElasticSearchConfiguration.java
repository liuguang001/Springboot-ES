package com.example.demo.configruation;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfiguration {

	@Value("${elasticsearch.hostlist}")
	private String hostList;
	
	@Bean
	public RestHighLevelClient restHighLevelClient(){
		String[] splitHost = hostList.split(",");
		HttpHost [] htpHostArray=new HttpHost[splitHost.length];
		for (int i = 0; i < splitHost.length; i++) {
			htpHostArray[i]=new HttpHost(splitHost[i].split(":")[0],Integer.parseInt(splitHost[i].split(":")[1]));
		}
		return new RestHighLevelClient(RestClient.builder(htpHostArray));
	}
	
	@Bean
	public RestClient restClient(){
		String[] splitHost = hostList.split(",");
		HttpHost [] htpHostArray=new HttpHost[splitHost.length];
		for (int i = 0; i < splitHost.length; i++) {
			htpHostArray[i]=new HttpHost(splitHost[i].split(":")[0],Integer.parseInt(splitHost[i].split(":")[1]));
		}
		return RestClient.builder(htpHostArray).build();
	}
	
	
}

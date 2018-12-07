package com.example.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Dto.PropertiesDto;

/**增删改操作
 * @author Administrator
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticSearchSpringBootApplication.class)
public class ElasticSearchSpringBootApplicationTests {

	@Autowired
	private RestHighLevelClient highLevelClient;
	@Autowired
	private RestClient restClient;

	/**创建索引&添加映射
	 * @throws IOException
	 */
	@Test
	public void createIndex() throws IOException {
		Map<String, Object> mapping = new HashMap<>();
		Map<String, Object> properties = new HashMap<>();

		PropertiesDto propertiesDto = new PropertiesDto();
		propertiesDto.setType("text");
		properties.put("name", propertiesDto);

		PropertiesDto propertiesDto1 = new PropertiesDto();
		propertiesDto1.setType("text");
		properties.put("discription", propertiesDto);

		PropertiesDto propertiesDto2 = new PropertiesDto();
		propertiesDto2.setType("text");
		properties.put("studymodel", propertiesDto);

		mapping.put("properties", properties);

		String jsonString = JSON.toJSONString(mapping, SerializerFeature.DisableCircularReferenceDetect);
		System.out.println(jsonString);
		CreateIndexRequest createRequest = new CreateIndexRequest("xc_course");
		createRequest.settings(Settings.builder().put("number_of_shards", "1").put("number_of_replicas", "0"));
		//创建索引时添加映射
		createRequest.mapping("doc", jsonString, XContentType.JSON);
		IndicesClient indices = highLevelClient.indices();
		CreateIndexResponse createIndexResponse = indices.create(createRequest);
		boolean acknowledged = createIndexResponse.isAcknowledged();
		System.out.println(acknowledged);
	}

	/**删除索引库
	 * @throws IOException
	 */
	@Test
	public void deleteIndex() throws IOException {
		DeleteIndexRequest deleteRequest = new DeleteIndexRequest("xc_course");
		DeleteIndexResponse response = highLevelClient.indices().delete(deleteRequest);
		boolean acknowledged = response.isAcknowledged();
		System.out.println(acknowledged);
	}
	
	/**添加文档
	 * @throws IOException
	 */
	@Test
	public void addDocument() throws IOException{
		Map<String,Object> source=new HashMap<>();
		source.put("name", "Bootstrap开发框架");
		source.put("description", "Bootstrap是由Twitter推出的一个前台页面开发框架，在行业之中使用较为广泛。此开发框架包含了大量的CSS、JS程序代码，可以帮助开发者（尤其是不擅长页面开发的程序人员）轻松的实现一个不受浏览器限制的精美界面效果。");
		source.put("studymodel", "201001");
		IndexRequest indexRequest=new IndexRequest("xc_course","doc");
		indexRequest.source(source);
		IndexResponse index = highLevelClient.index(indexRequest);
		Result result = index.getResult();
		System.out.println(result);
	}
	
	/**删除文档
	 * @throws IOException
	 */
	@Test
	public void deleteDocument() throws IOException{
		DeleteRequest deleteRequest=new DeleteRequest("xc_course","doc","HWIbh2cB4oXJbZiisjEc");
		DeleteResponse delete = highLevelClient.delete(deleteRequest);
		System.out.println(delete.getResult());
	}
	
	/**
	 * 更新文档
	 * @throws IOException 
	 */
	@Test
	public void updateDocument() throws IOException{
		UpdateRequest updateRequest=new UpdateRequest("xc_course","doc","HWIbh2cB4oXJbZiisjEc");
		Map<String,Object> doc=new HashMap<>();
		doc.put("name", "java开发框架");
		doc.put("description", "Spring Boot是由Pivotal团队提供的全新框架，其设计目的是用来简化新Spring应用的初始搭建以及开发过程。该框架使用了特定的方式来进行配置，从而使开发人员不再需要定义样板化的配置。通过这种方式，Spring Boot致力于在蓬勃发展的快速应用开发领域(rapid application development)成为领导者。");
		doc.put("studymodel", "201702");
		updateRequest.doc(doc);
		UpdateResponse update = highLevelClient.update(updateRequest);
		System.out.println(update.getResult());
	}
}

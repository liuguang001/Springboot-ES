package com.example.demo;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**搜索相关api演示
 * @author Administrator
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticSearchSpringBootApplication.class)
public class ElasticSearchSpringBootApplicationSearchs {

	@Autowired
	private RestHighLevelClient highLevelClient;
	@Autowired
	private RestClient restClient;
	
	/**普通查询&&分页查询
	 * @throws IOException
	 */
	@Test
	public void seachAndPage() throws IOException{
		SearchRequest searchRequest=new SearchRequest("xc_course");
		searchRequest.types("doc");
		//创建搜索源
		SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());//设置搜索参数
		searchSourceBuilder.fetchSource(new String[]{"description","name"}, new String[]{"studymodel"});//设置(显示哪些字段,不显示哪些字段)
		//分页参数(可附加)
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(1);
		//设置搜索源
		searchRequest.source(searchSourceBuilder);
		SearchResponse search = highLevelClient.search(searchRequest);
		//获取匹配结果
		SearchHits hits = search.getHits();
		//总条数
		long totalHits = hits.totalHits;
		System.out.println("total="+totalHits);
		for (SearchHit searchHit : hits) {
			System.out.println(searchHit.getId());
			System.out.println(searchHit.getSourceAsMap());
		}
	}
	
	
	
	
	/**termQuery 匹配分词,精确查询,不会对查询关键词分词(匹配词库中已经过分词的词)
	 * @throws IOException
	 */
	@Test
	public void termSearch() throws IOException{
		SearchRequest searchRequest=new SearchRequest("xc_course");
		searchRequest.types("doc");
		
		SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("name", "java"));//设置查询模式
		searchSourceBuilder.fetchSource(new String[]{"description","name"}, new String[]{"studymodel"});
		
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		SearchHits searchHits = searchResponse.getHits();
		long totalHits = searchHits.totalHits;
		System.out.println("totalHits="+totalHits);
		for (SearchHit searchHit : searchHits) {
			System.out.println(searchHit.getSourceAsMap());
		}
	}
	
	/**termsQuery通过id查询
	 * @throws IOException
	 */
	@Test
	public void termSearchByIds() throws IOException{
		SearchRequest searchRequest=new SearchRequest("xc_course");
		searchRequest.types();
		
		String [] ids={"HWIbh2cB4oXJbZiisjEc","HmIwh2cB4oXJbZii7zGm"};
		
		SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termsQuery("_id", ids));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		System.out.println("total="+hits.getTotalHits());
		for (SearchHit searchHit : hits) {
			System.out.println(searchHit.getSourceAsMap());
		}
	}

	/**
	 * matchQuery全文检索,会将关键词分词
	 * @throws IOException 
	 */
	@Test
	public void searchAndMatch() throws IOException{
		SearchRequest searchRequest=new SearchRequest("xc_course");
		searchRequest.types("doc");
		SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("description","Pivotal Bootstrap").operator(Operator.OR));//条件查询 or和and
		//searchSourceBuilder.query(QueryBuilders.matchQuery("description","Pivotal").minimumShouldMatch("80%"));//设置分词出现的比例
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		System.out.println(hits.getTotalHits());
		for (SearchHit searchHit : hits) {
			System.out.println(searchHit.getSourceAsMap());
		}
	}
	
	
	/**boolean查询添加多个查询模式
	 * @throws IOException
	 */
	@Test
	public void searchAndBoolean() throws IOException{
		SearchRequest searchRequest=new SearchRequest("xc_course");
		searchRequest.types("doc");
		
		SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
		MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("description","Pivotal Bootstrap").operator(Operator.OR);
		TermQueryBuilder termQuery = QueryBuilders.termQuery("name", "java");
		searchSourceBuilder.query(QueryBuilders.boolQuery().must(matchQuery).must(termQuery));
		
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		System.out.println(hits.getTotalHits());
		for (SearchHit searchHit : hits) {
			System.out.println(searchHit.getSourceAsMap());
		}
	}
	
	/**filter过滤器查询(对搜索结果进行过滤)(不计算匹配得分,效率高)
	 * @throws IOException
	 */
	@Test
	public void searchAndFilter() throws IOException{
		SearchRequest searchRequest=new SearchRequest("xc_course");
		searchRequest.types("doc");
		
		//filter必须用在boolean查询基础上
		SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
		MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("description","Pivotal Bootstrap").operator(Operator.OR);
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(matchQuery);
		boolQuery.filter(QueryBuilders.rangeQuery("studymodel").gte("201001").lte("201010"));
		searchSourceBuilder.query(boolQuery);
		
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		System.out.println(hits.getTotalHits());
		for (SearchHit searchHit : hits) {
			System.out.println(searchHit.getSourceAsMap());
		}
	}
	
	/**Sort查询结果排序
	 * @throws IOException
	 */
	@Test
	public void searchAndSort() throws IOException{
		SearchRequest searchRequest=new SearchRequest("xc_course");
		searchRequest.types("doc");
		
		SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());//设置搜索参数
		searchSourceBuilder.fetchSource(new String[]{"description","name"}, new String[]{"studymodel"});//设置(显示哪些字段,不显示哪些字段)
		searchSourceBuilder.sort("_id", SortOrder.ASC);
		
		searchRequest.source(searchSourceBuilder);
		SearchResponse search = highLevelClient.search(searchRequest);
		SearchHits hits = search.getHits();
		System.out.println("total="+hits.totalHits);
		for (SearchHit searchHit : hits) {
			System.out.println(searchHit.getSourceAsMap());
		}
	}


	/**Sort查询结果高亮显示
	 * @throws IOException
	 */
	@Test
	public void searchAndHighlight() throws IOException{
		SearchRequest searchRequest=new SearchRequest("xc_course");
		searchRequest.types("doc");
		
		SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("name", "开发"));//设置查询模式
		searchSourceBuilder.fetchSource(new String[]{"description","name"}, new String[]{"studymodel"});
		HighlightBuilder highlightBuilder=new HighlightBuilder();
		highlightBuilder.preTags("<tag>");
		highlightBuilder.postTags("</tag>");
		highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
		searchSourceBuilder.highlighter(highlightBuilder);
		
		
		searchRequest.source(searchSourceBuilder);
		SearchResponse search = highLevelClient.search(searchRequest);
		SearchHits hits = search.getHits();
		System.out.println("total="+hits.totalHits);
		for (SearchHit searchHit : hits) {
			System.out.println(searchHit.getHighlightFields());
		}
	}
}

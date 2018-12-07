package com.example.demo.Dto;

/**
 * es定义类型字段映射
 * @author Administrator
 *
 */
public class PropertiesDto {
	/*
	数据类型 :
	文本类型(text,keyword[索引时不会分词,搜索时精确查询])
	日期类型(data)
	数值类型(long,integer,short,byte,double,float,half_float,scaled_float)
	*/
	private String type;//数据类型
	private Boolean index;//是否将该字段索引
	private String analyzer="ik_max_word";//指定索引分词器(默认为细粒度分词)
	private String search_analyzer="ik_smart";//指定搜索分词器(默认为粗粒度分词)
	private String format;//格式参数(例如类型为dada时 yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis)
	private Integer scaling_factor;//比例因子,当参数为数值类型时,es会在存储时乘以该值
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getIndex() {
		return index;
	}
	public void setIndex(Boolean index) {
		this.index = index;
	}
	public String getAnalyzer() {
		return analyzer;
	}
	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}
	public String getSearch_analyzer() {
		return search_analyzer;
	}
	public void setSearch_analyzer(String search_analyzer) {
		this.search_analyzer = search_analyzer;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public Integer getScaling_factor() {
		return scaling_factor;
	}
	public void setScaling_factor(Integer scaling_factor) {
		this.scaling_factor = scaling_factor;
	}
	
	
}

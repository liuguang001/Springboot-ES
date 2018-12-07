  #1 elasticsearch-6.2.1 : es集成了ik分词器的安装包(windows系统,jdk版本为1.8)
  
  
  
 #2 ES安装包 : es原始安装包和ik分词器安装包(动手能力强的同学可以下载本包自行按需配置
  
            ES配置在elasticsearch-6.2.1\config\elasticsearch.yml中
            
            ik分词器安装只需把ik安装包里的elasticsearch目录拷贝到es的plugins文件夹下(更名为ik以便于日后维护)
            
            配置额外的分词字典:elasticsearch-6.2.1\plugins\ik\config\IKAnalyzer.cfg.xml,
            在elasticsearch-6.2.1\plugins\ik\config\ 文件夹下加上一个自己的词典文件(格式参照ik自带的main.dic)
            然后在<entry key="ext_dict">加上自己的词典文件名</entry>
  
            
  
  
 #3 ElasticSearch-SpringBoot : springboot集成es,演示代码在test下,稍加修改即可用于开发

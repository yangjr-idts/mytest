package com.itheima.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class solrj {
	private HttpSolrServer httpSolrjService;
	@Test
	public void SaveDocument() throws Exception {
		String url = "http://localhost:8095/solr";
		httpSolrjService = new HttpSolrServer(url);
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", "4");
		document.addField("name","小明");
		httpSolrjService.add(document);
		httpSolrjService.commit();
		
	}
	@Test
	public void UpdateDocument() throws Exception {
		String url = "http://localhost:8095/solr";
		httpSolrjService = new HttpSolrServer(url);
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", "5");
		document.addField("title","小红");
		httpSolrjService.add(document);
		httpSolrjService.commit();
	}
	@Test
	public void DeleteDocument() throws Exception {
		String url = "http://localhost:8095/solr";
		httpSolrjService = new HttpSolrServer(url);
		//按id删除
		//httpSolrjService.deleteById("5");
		//httpSolrjService.commit();
		//根据条件删除
		httpSolrjService.deleteByQuery("*:*");
		httpSolrjService.commit();
		
	}
	@Test
	public void searchIndex() throws Exception {
		String url = "http://localhost:8095/solr";
		httpSolrjService = new HttpSolrServer(url);
		//创建搜索对象
		SolrQuery query = new SolrQuery();
		//设置搜索条件
		query.setQuery("*:*");
		//发起搜索请求
		QueryResponse response = httpSolrjService.query(query);
		//处理搜寻结果
		SolrDocumentList results = response.getResults();
		System.out.println("结果条数" + results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println("======================");
			System.out.println("id" + solrDocument.get("id"));
			System.out.println("name" + solrDocument.get("name"));
		}
		
	}
	@Test
	public void searchIndex2() throws Exception {
		//复杂查询
		String url = "http://localhost:8095/solr";
		httpSolrjService = new HttpSolrServer(url);
		//创建搜索对象
		SolrQuery query = new SolrQuery();
		//设置搜索条件
		query.setQuery("钻石");
		//设置过滤条件
		query.setFilterQueries("product_catalog_name:幽默杂货");
		//排序
		query.setSort("product_price",ORDER.desc);
		//设置分页
		query.setStart(0);
		query.setRows(10);
		//设置显示列表
		query.setFields("id","product_name","product_price","product_catalog_name","product_picture");
		//设置默认域
		query.set("df","product_keywords");
		//是否高亮
		query.setHighlight(true);
		query.addHighlightField("product_name");
		query.setHighlightSimplePre("<font style='color:red'>");
		query.setHighlightSimplePost("</font>");
		//发起搜索请求
		QueryResponse response = httpSolrjService.query(query);
		//处理搜寻结果
		SolrDocumentList results = response.getResults();
		System.out.println("结果条数" + results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println("======================");
			System.out.println("id:" + solrDocument.get("id"));
			String productName = "";
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("product_name");
			System.out.println(list);
			//判断是否有高亮内容
			if (null != list) {
				productName = list.get(0);
			} else {
				productName = (String) solrDocument.get("product_name");
			}
			System.out.println("product_name:" + productName);
			System.out.println("product_price:" + solrDocument.get("product_price"));
			System.out.println("product_catalog_name:" + solrDocument.get("product_catalog_name"));
			System.out.println("product_picture:" + solrDocument.get("product_picture"));
		}
		
	}
}

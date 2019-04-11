package cn.itcast.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Solrj的使用
 * 添加
 * 查询
 * 删除
 * 修改
 *
 * 云计算
 *
 * 机器非常多 很多机器一起算 云计算
 */
public class SolrJDemo {

    //添加  修改 
    @Test
    public void testAdd() throws Exception{
        //String baseUrl="http://192.168.200.128:8080/solr/collection2";
        String baseUrl="http://192.168.200.128:8080/solr";
        SolrServer solrServer = new HttpSolrServer(baseUrl);

        //添加
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id","234");
        doc.setField("name","haha");

        solrServer.add(doc, 1000);
        //solrServer.commit();
    }

    //删除
    @Test
    public void testDelete() throws Exception{
        String baseUrl="http://192.168.200.128:8080/solr";
        SolrServer solrServer = new HttpSolrServer(baseUrl);
        //solrServer.deleteById("234",1000);
        //慎用
        solrServer.deleteByQuery("*:*", 100);
    }

    //查询
    @Test
    public void testFind() throws Exception{
        String baseUrl="http://192.168.200.128:8080/solr";
        SolrServer solrServer = new HttpSolrServer(baseUrl);
        //Apache SolrJ
        //SpringDataSolr


        SolrQuery solrQuery = new SolrQuery();

        //查询所有
        solrQuery.set("q","家天下");
        solrQuery.set("df", "product_keywords");

        //指定查询的域 fl field list
        solrQuery.set("fl", "id,product_price");

        //分页处理
        solrQuery.setStart(0);
        solrQuery.setRows(6);

        //排序
        solrQuery.setSort("product_price", SolrQuery.ORDER.asc);

        //打开高亮的开关
        solrQuery.setHighlight(true);
        //设置需要高亮的域
        solrQuery.addHighlightField("product_name");
        //前缀
        solrQuery.setHighlightSimplePre("<em style='color:red'>");
        //后缀
        solrQuery.setHighlightSimplePost("</em>");


        //执行查询
        QueryResponse response = solrServer.query(solrQuery);

        //获取高亮
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();


        //查询结果集
        SolrDocumentList docs = response.getResults();

        long numFound = docs.getNumFound();
        System.out.println("总条数: "+numFound);

        for (SolrDocument doc : docs) {
            System.out.println("id:"+doc.get("id"));

            Map<String, List<String>> map = highlighting.get(doc.get("id"));
            List<String> list = map.get("product_name");
            if (null!=list&&list.size()>0){
                System.out.println("高亮的名称: "+list.get(0));
            }

            System.out.println("product_name:" + doc.get("product_name"));
            System.out.println("product_price:" + doc.get("product_price"));
        }


    }

}


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Solr管理
 * 添加
 * 修改
 * 删除
 * 查询
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:*.xml"})
public class TestSpringDataSolr {

    @Autowired
    private SolrTemplate solrTemplate;

    //添加 //修改
    @Test
    public void testAdd() throws Exception {
        //索引库
        Item item = new Item();
        item.setId(1L);
        item.setBrand("华为");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为2号专卖店");
        item.setTitle("华为Mate9");
        item.setPrice(new BigDecimal(2000));
        item.setUpdateTime(new Date());

        solrTemplate.saveBean(item, 1000);
    }

    //删除
    @Test
    public void testDelete() throws Exception {

        //solrTemplate.deleteById("1");
        SolrDataQuery query = new SimpleQuery("item_title:Mate");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    //循环插入100条数据
    @Test
    public void testAddList(){
        List<Item> list=new ArrayList<Item>();
        for(int i=0;i<100;i++){
            Item item=new Item();
            item.setId(i+1L);
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为2号专卖店");
            item.setTitle("华为Mate"+i);
            item.setPrice(new BigDecimal(2000+i));
            list.add(item);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }


    //查询
    @Test
    public void testFind() throws Exception {
        //Query query = new SimpleQuery("*:*");
        //华为
        //Mate0
        //Mate
        //0
        //select * from 表 where item_title = 0
       /* Query query = new SimpleQuery("item_title:0");*/
        Criteria criteria = new Criteria("item_title").contains("0");
        Query query = new SimpleQuery(criteria);

        //开始行 Spring offset 偏移量
        //假如有100条数据 分成10页 第一页从第0条数据开始 下一页从10开始 0 - 9 , 10 - 19
        query.setOffset(0);
        //每页数
        query.setRows(8);

        //执行查询
        ScoredPage<Item> page = solrTemplate.queryForPage(query, Item.class);

        //总条数
        System.out.println("总条数:"+page.getTotalElements());
        System.out.println("总页数:"+page.getTotalPages());
        List<Item> itemList = page.getContent();
        for (Item item : itemList) {

            System.out.println(item);
        }

    }

}

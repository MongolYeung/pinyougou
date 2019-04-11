package cn.itcast.solrUtils;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * 批量导入数据 从mysql到索引库
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/*.xml"})
public class DataImport {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void dataImport(){
        //查询所有
        List<Item> itemList = itemDao.selectByExample(null);// jdk 动态 代理  cglib 反射

        for (Item item : itemList) {
            item.setSpecMap(JSON.parseObject(item.getSpec(), Map.class));
        }


        //tb_item  String spec = {"网络":"电信4G","机身内存":"128G"}

        //          Map<String,String> specMap = JSON.parseObject(spec,Map.class)

        /**
         *  item_spec_网络:电信4G
         *  item_spec_机身内存:128G
         *
         * 	<dynamicField name="item_spec_*" type="string" indexed="true" stored="true" />
         */
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }
}

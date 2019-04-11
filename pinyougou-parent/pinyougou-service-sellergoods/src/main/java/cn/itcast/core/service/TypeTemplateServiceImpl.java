package cn.itcast.core.service;

import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 模块管理
 */
@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {
    @Autowired
    private TypeTemplateDao typeTemplateDao;
    @Autowired
    private SpecificationOptionDao specificationOptionDao;
    @Autowired
    private RedisTemplate redisTemplate;

    //查询分页
    @Override
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate) {

        //查询所有模板结果集
        List<TypeTemplate> typeTemplates = typeTemplateDao.selectByExample(null);
        for (TypeTemplate template : typeTemplates) {


            //品牌结果集字符串  转换为对象
            //[{"id":1,"text":"联想"},{"id":3,"text":"三星"},{"id":2,"text":"华为"},{"id":4,"text":"小米"}]
            List<Map> brandList = JSON.parseArray(template.getBrandIds(), Map.class);
            redisTemplate.boundHashOps("brandList").put(template.getId(),brandList);

            //规格结果集字符串  转换为对象  直接调方法
            List<Map> specList = findBySpecList(template.getId());
            redisTemplate.boundHashOps("specList").put(template.getId(),specList);
        }


        //Mybatis 分页插件
        PageHelper.startPage(page, rows);
        //创建查询对象
        TypeTemplateQuery typeTemplateQuery = new TypeTemplateQuery();
        //判断是否有条件需要查询
        if (null != typeTemplate) {
            //生成条件对象
            TypeTemplateQuery.Criteria criteria = typeTemplateQuery.createCriteria();
            if (null != typeTemplate.getName() && !"".equals(typeTemplate.getName().trim())) {
                criteria.andNameLike("%" + typeTemplate.getName().trim() + "%");
            }
        }
        Page<TypeTemplate> p = (Page<TypeTemplate>) typeTemplateDao.selectByExample(typeTemplateQuery);
        return new PageResult(p.getTotal(), p.getResult());

    }

    //添加
    @Override
    public void add(TypeTemplate typeTemplate) {
        typeTemplateDao.insertSelective(typeTemplate);
    }

    //修改
    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
    }

    //删除
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            typeTemplateDao.deleteByPrimaryKey(id);
        }
    }

    //查询一个
    @Override
    public TypeTemplate findOne(Long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    //根据模板ID查询规格List<Map> 每一个Map要有规格选项结果集
    @Override
    public List<Map> findBySpecList(Long id) {
        //模板对象
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        // [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();
        //转成List<Map>
        List<Map> listMap = JSON.parseArray(specIds, Map.class);
        for (Map map : listMap) {
            SpecificationOptionQuery query = new SpecificationOptionQuery();
            //报错：Object --> Integer String  基本类型 --> Long特殊类型 长整
            query.createCriteria().andSpecIdEqualTo(Long.parseLong(String.valueOf(map.get("id"))));
            List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(query);
            map.put("options", specificationOptions);
        }
        return listMap;
    }

    //Mysql 索引库 消息 队列  分布式文件系统 Redis缓存
}

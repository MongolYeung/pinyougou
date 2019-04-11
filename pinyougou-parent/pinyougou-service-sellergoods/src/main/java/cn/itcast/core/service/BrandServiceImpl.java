package cn.itcast.core.service;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 品牌管理
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    //直接Controller Service
    @Autowired
    private BrandDao brandDao;

    //查询所有
    @Override
    public List<Brand> findAll() {
        //5个字段
        //update tb_tb set name = #{name} where ... 选择性
        //update tb_tb set name = #{name}, age=null, where ... 选择性
        //
        return brandDao.selectByExample(null);
    }

    //查询分页对象 无条件
    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize) {
        //Mybatis分页插件
        PageHelper.startPage(pageNo, pageSize);
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //查询分页对象 有条件
    @Override
    public PageResult search(Integer pageNo, Integer pageSize, Brand brand) {
        //Mybatis分页插件
        PageHelper.startPage(pageNo, pageSize);
        // 判断是否有条件需要查询
        BrandQuery brandQuery = new BrandQuery();
        if (null != brand) {
            BrandQuery.Criteria createCriteria = brandQuery.createCriteria();
            if (null != brand.getName() && !"".equals(brand.getName().trim())) {
                createCriteria.andNameLike("%" + brand.getName().trim() + "%");
            }
            if (null != brand.getFirstChar() && !"".equals(brand.getFirstChar().trim())) {
                createCriteria.andFirstCharEqualTo(brand.getFirstChar().trim());
            }
        }
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //添加
    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
        //insert into tb_tb (id,name,98个都是null) values (1,haha,null,null 98个null           100个字段
        //insert into tb_tb (id,name) values (1,haha)
    }

    //修改
    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    //删除
    @Override
    public void delete(Long[] ids) {
        //delete from tb_brand where id = 5;
        /*for (Long id : ids) {
            brandDao.deleteByPrimaryKey(id);
        }*/

        //delete from tb_brand where id in (3,5,7,1)
        BrandQuery brandQuery = new BrandQuery();//入参
        //条件对象中 内部对象  保存条件  放在  where id in (3,5,7,1)
        brandQuery.createCriteria().andIdIn(Arrays.asList(ids));
        brandDao.deleteByExample(brandQuery);
    }

    //查询一个
    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

}

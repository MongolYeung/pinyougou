package cn.itcast.core.service;

import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import cn.itcast.core.vo.SpecificationVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 规格管理
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecificationDao specificationDao;

    @Autowired
    private SpecificationOptionDao specificationOptionDao;

    //查询分页 无条件
    @Override
    public PageResult findPage(Integer page, Integer rows) {
        //插件
        PageHelper.startPage(page, rows);

        Page<Specification> p = (Page<Specification>) specificationDao.selectByExample(null);
        return new PageResult(p.getTotal(), p.getResult());

    }

    //查询分页 有条件
    @Override
    public PageResult search(Integer page, Integer rows, Specification specification) {
        //Mybatis分页插件
        PageHelper.startPage(page, rows);
        // 判断是否有条件需要查询
        SpecificationQuery specificationQuery = new SpecificationQuery();
        if (null != specification) {
            SpecificationQuery.Criteria createCriteria = specificationQuery.createCriteria();
            if (null != specification.getSpecName() && !"".equals(specification.getSpecName().trim())) {
                createCriteria.andSpecNameLike("%" + specification.getSpecName().trim() + "%");
            }
        }
        Page<Specification> p = (Page<Specification>) specificationDao.selectByExample(specificationQuery);
        return new PageResult(p.getTotal(), p.getResult());
    }

    //添加
    @Override
    public void add(SpecificationVo specificationVo) {
        //规格表
        specificationDao.insertSelective(specificationVo.getSpecification());
        //规格选项表 多的一方
        List<SpecificationOption> specificationOptionList = specificationVo.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            //外键
            specificationOption.setSpecId(specificationVo.getSpecification().getId());
            specificationOptionDao.insertSelective(specificationOption);
        }
    }

    //回显一个
    @Override
    public SpecificationVo findOne(Long id) {
        SpecificationVo vo = new SpecificationVo();
        vo.setSpecification(specificationDao.selectByPrimaryKey(id));
        SpecificationOptionQuery query = new SpecificationOptionQuery();

        query.createCriteria().andSpecIdEqualTo(id);
        vo.setSpecificationOptionList(specificationOptionDao.selectByExample(query));
        return vo;
    }


    //修改
    @Override
    public void update(SpecificationVo vo) {
        //1.规格表  修改
        specificationDao.updateByPrimaryKeySelective(vo.getSpecification());

        //2.规格选项表
        //先删除 通过外键 批量删除
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(vo.getSpecification().getId());
        specificationOptionDao.deleteByExample(query);
        //再添加
        List<SpecificationOption> specificationOptionList = vo.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            //外键
            specificationOption.setSpecId(vo.getSpecification().getId());
            specificationOptionDao.insertSelective(specificationOption);
        }

    }

    //删除规格
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //删除规格表  删除规格属性表
            specificationDao.deleteByPrimaryKey(id);
            SpecificationOptionQuery query = new SpecificationOptionQuery();
            query.createCriteria().andSpecIdEqualTo(id);
            specificationOptionDao.deleteByExample(query);
        }

    }


}

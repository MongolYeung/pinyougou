package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.vo.SpecificationVo;

import java.util.List;

public interface SpecificationService {

    PageResult search(Integer page, Integer rows, Specification specification);

    void add(SpecificationVo specificationVo);

    SpecificationVo findOne(Long id);


    void update(SpecificationVo vo);

    void delete(Long[] ids);

    PageResult findPage(Integer page, Integer rows);
}

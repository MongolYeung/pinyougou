package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;

import java.util.List;

public interface BrandService {
    //查询所有
    List<Brand> findAll();

    //查询分页对象
    PageResult findPage(Integer pageNo,Integer pageSize);

    //条件查询分页对象
    PageResult search(Integer pageNo, Integer pageSize, Brand brand);

    //添加
    void add(Brand brand);

    //修改
    void update(Brand brand);

    //删除
    void delete(Long[] ids);

    //查询一个
    Brand findOne(Long id);
}

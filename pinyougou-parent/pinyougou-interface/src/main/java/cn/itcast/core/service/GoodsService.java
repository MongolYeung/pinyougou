package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.vo.GoodsVo;

public interface GoodsService {
    void add(GoodsVo vo);

    PageResult search(Integer page, Integer rows, Goods goods);

    void update(GoodsVo vo);

    GoodsVo findOne(Long id);

    void updateStatus(Long[] ids, String status);

    void delete(Long[] ids);
}

package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.GoodsService;
import cn.itcast.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.WebResult;

/**
 * 商品管理
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private GoodsService goodsService;

    //商品录入
    @RequestMapping("/add")
    public Result add(@RequestBody GoodsVo vo) {
        //设置商家 ID
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        vo.getGoods().setSellerId(name);
        try {
            goodsService.add(vo);
            return new Result(true, "录入成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "录入失败");
        }
    }

    //查询分页对象
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Goods goods) {
        //运营商后台管理 对所有商家进行统一管理
        return goodsService.search(page, rows, goods);
    }

    //修改
    @RequestMapping("/update")
    public Result update(@RequestBody GoodsVo vo) {
        try {
            goodsService.update(vo);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    //查询一个商品组合对象
    @RequestMapping("/findOne")
    public GoodsVo findOne(Long id) {
        return goodsService.findOne(id);
    }

    //更新状态 审核通过或驳回
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }

    //删除
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }

    }
}

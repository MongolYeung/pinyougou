package cn.itcast.core.service;

import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 商家管理
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerDao sellerDao;

    //添加
    public void add(Seller seller) {
        if (null != seller.getSellerId() && !"".equals(seller.getSellerId())) {
            //判断商家名是否含有空格
            if (!seller.getSellerId().contains(" ")) {
                seller.setCreateTime(new Date());
                seller.setPassword(new BCryptPasswordEncoder().encode(seller.getPassword()));
                seller.setStatus("0");
                sellerDao.insertSelective(seller);
            } else {
                throw new RuntimeException("商家名称不能有空格");
            }
        } else {
            throw new RuntimeException("商家名称不能为空");
        }
    }

    //修改 审核
    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        sellerDao.updateByPrimaryKeySelective(seller);
    }

    //查询分页
    @Override
    public PageResult search(Integer page, Integer rows, Seller seller) {
        //分页助手
        PageHelper.startPage(page, rows);
        //创建查询对象
        SellerQuery sellerQuery = new SellerQuery();
        //添加条件 查询未审核状态
        sellerQuery.createCriteria().andStatusEqualTo(seller.getStatus());
        //传入查询对象 返回 页面
        Page<Seller> p = (Page<Seller>) sellerDao.selectByExample(sellerQuery);
        //返回结果集
        return new PageResult(p.getTotal(), p.getResult());

    }

    //查询一个
    @Override
    public Seller findOne(String sellerId) {
        return sellerDao.selectByPrimaryKey(sellerId);
    }

}

package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.seller.Seller;

public interface SellerService {
    void add(Seller seller);

    void updateStatus(String sellerId,String status);

    PageResult search(Integer page, Integer rows, Seller seller);

    Seller findOne(String sellerId);
}

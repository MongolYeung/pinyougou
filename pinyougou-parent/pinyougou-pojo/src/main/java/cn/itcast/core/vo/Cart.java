package cn.itcast.core.vo;

import cn.itcast.core.pojo.order.OrderItem;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 购物车对象  购物车没有表
 */
public class Cart implements Serializable{


    //商家名称
    private String SellerName;
    //商家ID
    private String SellerId;
    //商品集合
    private List<OrderItem> orderItemList;

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getSellerId() {
        return SellerId;
    }

    public void setSellerId(String sellerId) {
        SellerId = sellerId;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(SellerId, cart.SellerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(SellerId);
    }
}

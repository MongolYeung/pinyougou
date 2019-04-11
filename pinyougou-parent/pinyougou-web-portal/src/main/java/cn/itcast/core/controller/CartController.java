package cn.itcast.core.controller;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.CartService;
import cn.itcast.core.vo.Cart;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车管理
 *
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;


    //加入购物车   入参: 库存ID itemId  购买数量 num
    @RequestMapping("/addGoodsToCartList")
    //@CrossOrigin(origins={"http://localhost:9003"},allowCredentials="true")
    @CrossOrigin(origins={"http://localhost:9003"})
    public Result addGoodsToCartList(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        try {

            List<Cart> cartList = null;

            //1:获取Cookie
            Cookie[] cookies = request.getCookies();
            if (null != cookies && cookies.length > 0){
                for (Cookie cookie : cookies) {
                    //2:获取Cookie中购物车集合
                    if ("CART".equals(cookie.getName())){
                        String value = cookie.getValue();
                        cartList = JSON.parseArray(value, Cart.class);
                        break;
                    }
                }
            }

            //3:没有 创建购物车集合
            if (null == cartList){
                cartList = new ArrayList<>();
            }
            //4:追加当前款  库存ID 数量

            //准备工作 根据库存ID查询库存对象(商家ID)
            Item item = cartService.findItemById(itemId);
            //新的购物车对象
            Cart newCart = new Cart();
            //商家ID
            newCart.setSellerId(item.getSellerId());
            //商家名称 不给
            //商品集合
            //一个商品
            OrderItem newOrderItem = new OrderItem();
            //库存ID
            newOrderItem.setItemId(itemId);
            //数量
            newOrderItem.setNum(num);
            //商品集合
            List<OrderItem> newOrderItemList = new ArrayList<>();
            //添加新商品
            newOrderItemList.add(newOrderItem);
            //添加到购物车
            newCart.setOrderItemList(newOrderItemList);

            //1)判断新购物车所属商家 在当前购物车集合中是否已经存在
            int newIndexOf = cartList.indexOf(newCart);//newIndexOf == -1 不存在  >= 0 存在 同时 newIndexOf 角标位置
            if (newIndexOf != -1){
                //--存在
                //找到相同的商家对象的购物车 获取出此商家对应的所有商品集合
                Cart oldCart = cartList.get(newIndexOf);
                List<OrderItem> oldOrderItemList = oldCart.getOrderItemList();

                //2)判断当前新商品 在上面的商品集合中是否已经存在
                int indexOf = oldOrderItemList.indexOf(newOrderItem);
                if (indexOf != -1){
                    //---存在
                    //找出对应的相同商品 追加数量
                    OrderItem oldOrderItem = oldOrderItemList.get(indexOf);
                    oldOrderItem.setNum(oldOrderItem.getNum()+newOrderItem.getNum());
                }else {
                    //---不存在
                    //直接添加新商品即可
                    oldOrderItemList.add(newOrderItem);
                }

            }else {
                //--不存在
                //创建新的购物车对象 当作新购物车添加到购物车集合中
                cartList.add(newCart);
            }


            //判断当前用户是否登录
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!"anonymousUser".equals(name)){
                //登录了
                //5:将上面合并后的购物车集合再次追加到Redis缓存 清空Cookie
                cartService.addCartListToRedis(cartList,name);
                Cookie cookie = new Cookie("CART", null);
                //存活时间
                cookie.setMaxAge(0);
                //路径  http://localhost:9103/cart/addGoodsToCartList.do
                //路径  http://localhost:9103/haha/addGoodsToCartList.do
                cookie.setPath("/");
                response.addCookie(cookie);

            }else {
                //未登录
                //5:创建Cookie保存购物车 并回显浏览器
                Cookie cookie = new Cookie("CART", JSON.toJSONString(cartList));
                //设置存活时间
                cookie.setMaxAge(60*60*24*7);
                //设置路径  http://localhost:9103/cart/addGoodsToCartList.do
                //设置路径  http://localhost:9103/haha/addGoodsToCartList.do
                cookie.setPath("/");
                //回显浏览器
                response.addCookie(cookie);
            }

            return new Result(true, "加入购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "加入购物车失败");
        }

    }

    //查询所有购物车结果集
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(HttpServletRequest request,HttpServletResponse response){
        List<Cart> cartList = null;
        //未登录  从Cookie查询
        //1:获取Cookie
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0){
            for (Cookie cookie : cookies) {
                //2:获取Cookie中购物车集合
                if ("CART".equals(cookie.getName())){
                    String value = cookie.getValue();
                    cartList = JSON.parseArray(value, Cart.class);
                    break;
                }
            }
        }

        //判断当前用户是否登录
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(name)){
            //登录了
            //3:有 追加到Redis缓存中 清空Cookie
            if (null != cartList){
                cartService.addCartListToRedis(cartList,name);
                Cookie cookie = new Cookie("CART", null);
                //设置存活时间
                cookie.setMaxAge(0);
                //设置路径  http://localhost:9103/cart/addGoodsToCartList.do
                //设置路径  http://localhost:9103/haha/addGoodsToCartList.do
                cookie.setPath("/");
                //回显浏览器
                response.addCookie(cookie);
            }
            //4:从缓存中获取购物车结果集
            cartList = cartService.findCartListFromRedis(name);

        }

        //5:有 将购物车装满
        if (null != cartList){
            cartList = cartService.findCartList(cartList);
            //库存ID  数量 商家ID
        }

        //6:回显

        return cartList;
    }

}



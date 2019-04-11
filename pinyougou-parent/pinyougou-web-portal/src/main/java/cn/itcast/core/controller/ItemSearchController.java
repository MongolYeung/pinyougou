package cn.itcast.core.controller;

import cn.itcast.core.service.ItemSearchService;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 搜索管理
 */
@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    //搜索开始
    @RequestMapping("/search")
    public Map<String, Object> search(@RequestBody Map<String, String> searchMap) {
        return itemSearchService.search(searchMap);
    }
}

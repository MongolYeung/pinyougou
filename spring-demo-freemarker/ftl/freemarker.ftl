<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<#include "freemarker2.ftl"/>

Hello ${name!}


<#assign linkman=lm/>
联系人:${linkman}

<#assign info={"mobile":"13301231212",'address':'北京市昌平区王府街'} >
手机:${info.mobile}
地址:${info.address}

<#if success??>
    <#if success>
        我们成功了
    <#else>
        我们失败了
    </#if>
</#if>

    <#--List goodsList=new ArrayList();

    Map goods1=new HashMap();
    goods1.put("name", "苹果");
    goods1.put("price", 5.8);
    Map goods2=new HashMap();
    goods2.put("name", "香蕉");
    goods2.put("price", 2.5);
    Map goods3=new HashMap();
    goods3.put("name", "橘子");
    goods3.put("price", 3.2);
    goodsList.add(goods1);
    goodsList.add(goods2);
    goodsList.add(goods3);
    root.put("goodsList", goodsList);-->

    <#list goodsList as goods>
        水果: ${goods.name}
        价格: ${goods.price}

    </#list>

    集合的长度: ${goodsList?size}

    <#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
    <#assign info = text?eval />

    银行:${info.bank}
    账户:${info.account}

    当前时间:${cur_time?date}     年月日
    当前时间:${cur_time?time}     时分秒
    当前时间:${cur_time?datetime} 年月日时分秒
    当前时间:${cur_time?string('yy/MM_dd HH-mm:ss')}  年月日时分秒

    id:${id?c}

</body>
</html>
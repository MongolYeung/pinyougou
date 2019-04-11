package cn.itcast.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

/**
 * Freemarker
 * 测试
 */
public class FMDemo {

    public static void main(String[] args) throws Exception {
        //静态化程序  看成是Freemarker对象
        Configuration configuration = new Configuration(Configuration.getVersion());

        //ftl Freemarker官方  模板文件 .ftl    .java  .xml  .txt ....properties...  .html

        String dir = "E:\\Project\\pinyougou\\spring-demo-freemarker\\ftl\\";
        //模板 + 数据 == 输出
        configuration.setDirectoryForTemplateLoading(new File(dir));

        //加载模板  从磁盘上读取文件到内存 IO 绝对路径 不认识相对路径
        Template template = configuration.getTemplate("freemarker.ftl");

        //数据
        Map root = new HashMap();
        root.put("name", null);

        root.put("lm", "刘德华");

        root.put("success", true);

        List goodsList=new ArrayList();

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
        root.put("goodsList", goodsList);

        //当前时间
        root.put("cur_time", new Date());//格林威治

        //数字123,456,789       整数123456789
        //字符串
        root.put("id", 149187842867916L);


        //输出
        Writer out = new FileWriter(new File(dir + "hello.html"));

        //处理模板
        template.process(root,out);

        System.out.println("生成完成");


    }
}

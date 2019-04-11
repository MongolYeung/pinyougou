package cn.itcast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 * 测试FastDFS的Java接口  客户端
 */
public class FastDFSDemo {

    public static void main(String[] args) throws Exception{

        //0:准备工作
        ClientGlobal.init("E:\\Project\\pinyougou\\fastDFSdemo\\src\\main\\resources\\fdfs_client.conf");

        //1:创建Tracker客户端  ip:port
        TrackerClient trackerClient = new TrackerClient();

        //2:连接服务器
        TrackerServer trackerServer = trackerClient.getConnection();

        //3:创建 Storage
        StorageClient1 storageClient1 = new StorageClient1(trackerServer,null);

        //4:上传图片
        // http://192.168.200.128/
        // group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
        String path = storageClient1.upload_file1("D:\\Pictures\\Saved Pictures\\preview.jpg", "jpg", null);

        System.out.println("http://192.168.200.128/" + path);
    }
}

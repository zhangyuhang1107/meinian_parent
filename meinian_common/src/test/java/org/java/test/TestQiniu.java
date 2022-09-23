package org.java.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * @author zyhstart
 */
/* 七牛云文件上传测试类
 * Zone.zone0:华东
 * Zone.zone1:华北
 * Zone.zone2:华南
 * 自动识别上传区域
 * Zone.autoZone
 */
public class TestQiniu {

    // 上传本地文件
    @Test
    public void uploadFile() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());//华南
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "FVsZVx0HsN6LIXbCcVaFC7xFsMAaetvYWzsZoapj";
        String secretKey = "U-rdmDlr3cFe-U4qkfhQOK_LPOB4nRpepF5_AnAn";
        String bucket = "meinian-zyh";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png，可支持中文
        String localFilePath = "E:/pic/mm.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null; // 自定义文件名称
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);// Fiu8dW7YfHvuCexmt93ndyiU_-4t
            System.out.println(putRet.hash);// Fiu8dW7YfHvuCexmt93ndyiU_-4t
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    @Test
    public void deleteFile() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        String accessKey = "FVsZVx0HsN6LIXbCcVaFC7xFsMAaetvYWzsZoapj";
        String secretKey = "U-rdmDlr3cFe-U4qkfhQOK_LPOB4nRpepF5_AnAn";
        String bucket = "meinian-zyh";//空间
        String key = "Fiu8dW7YfHvuCexmt93ndyiU_-4t";//文件名称
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

}


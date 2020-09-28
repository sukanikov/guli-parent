package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @program: guli-parent
 * @author: bailiang
 * @create: 2020-09-24 15:14
 */
@Service
public class FileServiceImpl implements FileService{
    @Autowired
    private OssProperties ossProperties;

    //创建oss实例
    private OSS createOss(){
        String endpoint = ossProperties.getEndpoint();
        String keyId = ossProperties.getAccessKeyId();
        String keySecret = ossProperties.getAccessKeySecret();
        String bucketName = ossProperties.getBucketName();

        //判断oss实例是否存在：如果不存在则创建，如果存在则获取
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);
        if (!ossClient.doesBucketExist(bucketName)) {
            //创建bucket
            ossClient.createBucket(bucketName);
            //设置oss实例的访问权限：公共读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }

        return ossClient;
    }

    @Override
    public String upload(InputStream inputStream, String originalFilename, String module){
        OSS ossClient = createOss();

        //目录的处理：分类 + 日期
        String folder = module + new DateTime().toString("/yyyy/MM/dd/");

        //文件名的处理：UUID.获取到的文件的扩展名
        String fileName = UUID.randomUUID().toString();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String key = folder + fileName + ext;

        // 上传文件流。
        ossClient.putObject(ossProperties.getBucketName(), key, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

//        https://guliyunclass-file.oss-cn-shanghai.aliyuncs.com/avatar/default.jpg
//        文件名：bucketName            endpoint                  originalFilename
//        return "https://" + bucketName + "." + endpoint + "/" + originalFilename;
//        初步上传：阿里云oss的文件名：https://guliyunclass-file-01.oss-cn-shanghai.aliyuncs.com/01.jpg
        return "https://" + ossProperties.getBucketName() + "." + ossProperties.getEndpoint() + "/" + key;

    }

    @Override
    public void removeFile(String url){
        OSS ossClient = createOss();

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        //avatar/日期路径/文件名.扩展名
        // url = https://bucketName.endpoint/objectName
        String host = "https://" + ossProperties.getBucketName() + "." + ossProperties.getEndpoint() + "/";
        String objectName = url.substring(host.length());   //刚好取的是host之后的字符串
        ossClient.deleteObject(ossProperties.getBucketName(), objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}

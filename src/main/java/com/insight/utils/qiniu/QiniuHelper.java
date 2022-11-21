package com.insight.utils.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author 作者
 * @date 2017年9月5日
 * @remark 七牛存储服务
 */
public class QiniuHelper {
    private final QiniuConfig config;

    private final Auth auth;
    private final UploadManager uploadManager;
    private final BucketManager bucketManager;

    /**
     * 构造方法
     *
     * @param config QiniuConfig
     */
    public QiniuHelper(QiniuConfig config) {
        this.config = config;

        auth = Auth.create(config.accessKey, config.secretKey);
        Configuration configuration = new Configuration(Region.region0());
        uploadManager = new UploadManager(configuration);
        bucketManager = new BucketManager(auth, configuration);
    }

    /**
     * 获取上传token
     *
     * @param fileName 文件名
     * @return token
     */
    public String getUploadToken(String fileName) {
        return auth.uploadToken(config.bucketName, fileName);
    }

    /**
     * 上传 路径上传
     *
     * @param path     上传路径
     * @param fileName 文件名
     * @return 返回信息
     */
    public String upload(String path, String fileName) throws QiniuException {
        String token = getUploadToken(fileName);
        Response response = uploadManager.put(path, fileName, token);
        DefaultPutRet result = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

        return config.qiniuUrl + result.key;
    }

    /**
     * 上传并返回下载地址信息
     *
     * @param data 文件字节数组
     * @return 下载地址
     */
    public String upload(byte[] data) throws QiniuException {
        return upload(data, null);
    }

    /**
     * 上传并返回下载地址信息
     *
     * @param data     文件字节数组
     * @param fileName 文件名
     * @return 下载地址
     */
    public String upload(byte[] data, String fileName) throws QiniuException {
        String token = auth.uploadToken(fileName == null ? config.bucketName : fileName);
        Response response = uploadManager.put(data, fileName, token);
        DefaultPutRet result = Json.decode(response.bodyString(), DefaultPutRet.class);

        return config.qiniuUrl + result.key;
    }

    /**
     * 读文件内容
     *
     * @param fileName 文件名
     * @return 字节数组
     */
    public byte[] read(String fileName) throws IOException {
        bucketManager.stat(config.bucketName, fileName);

        String url = config.qiniuUrl + fileName;
        URL u = new URL(url);
        InputStream in = u.openStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(in, output);
        return output.toByteArray();
    }

    /**
     * 获取文件hash
     *
     * @param fileName 文件名
     * @return 文件hash
     */
    public String info(String fileName) throws QiniuException {
        FileInfo info = bucketManager.stat(config.bucketName, fileName);

        return info.hash;
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     */
    public void delete(String fileName) throws QiniuException {
        bucketManager.delete(config.bucketName, fileName);
    }
}

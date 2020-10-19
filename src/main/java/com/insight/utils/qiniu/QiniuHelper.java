package com.insight.utils.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author 作者
 * @date 2017年9月5日
 * @remark 七牛存储服务
 */
public final class QiniuHelper {
    @Value("${qiniu.accessKey}")
    private static String accessKey;

    @Value("${qiniu.secretKey}")
    private static String secretKey;

    @Value("${qiniu.bucketName}")
    private static String bucketName;

    @Value("${qiniu.url}")
    private static String qiniuUrl;

    private static final Auth AUTH = Auth.create(accessKey, secretKey);
    private static final Configuration CONFIG = new Configuration(Zone.autoZone());
    private static final UploadManager UPLOAD_MANAGER = new UploadManager(CONFIG);
    private static final BucketManager BUCKET_MANAGER = new BucketManager(AUTH, CONFIG);

    /**
     * 获取上传token
     *
     * @param fileName 文件名
     * @return token
     */
    public static String getUploadToken(String fileName) {
        return AUTH.uploadToken(bucketName, fileName);
    }

    /**
     * 上传 路径上传
     *
     * @param path     上传路径
     * @param fileName 文件名
     * @return 返回信息
     */
    public static Response upload(String path, String fileName) throws QiniuException {
        return UPLOAD_MANAGER.put(path, fileName, getUploadToken(fileName));
    }

    /**
     * 上传 字节流上传
     *
     * @param data     上传字节流
     * @param fileName 文件名
     * @return 返回信息
     */
    public static Response upload(byte[] data, String fileName) throws QiniuException {
        return UPLOAD_MANAGER.put(data, fileName, AUTH.uploadToken(bucketName));
    }

    /**
     * 下载
     *
     * @param fileName 文件名
     * @return 下载URL
     */
    @Deprecated
    public static String downLoad(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");

        return qiniuUrl + encodedFileName;
    }

    /**
     * 读文件内容
     *
     * @param fileName 文件名
     * @return 字节数组
     */
    public static byte[] read(String fileName) throws IOException {
        BUCKET_MANAGER.stat(bucketName, fileName);

        String url = qiniuUrl + fileName;
        URL u = new URL(url);
        InputStream in = u.openStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(in, output);
        byte[] bytes = output.toByteArray();
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(output);

        return bytes;
    }

    /**
     * 获取文件hash
     *
     * @param fileName 文件名
     * @return 文件hash
     */
    public static String info(String fileName) throws QiniuException {
        FileInfo info = BUCKET_MANAGER.stat(bucketName, fileName);

        return info.hash;
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     */
    public static void delete(String fileName) {
        try {
            BUCKET_MANAGER.delete(bucketName, fileName);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传并下载接口，返回下载地址信息
     *
     * @param data 文件字节数组
     * @return 下载地址
     */
    public static String upAndDownload(byte[] data) throws QiniuException, UnsupportedEncodingException {
        Response response = QiniuHelper.upload(data, null);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

        return QiniuHelper.downLoad(putRet.hash);
    }
}

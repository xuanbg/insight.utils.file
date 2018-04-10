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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author 作者
 * @date 2017年9月5日
 * @remark 七牛存储服务
 */
public final class QiniuHelper {
    private static String ak = "yFnb1L-yqxkEjfjOwiQzb5wsRcIQRoaZUbrhFupD";
    private static String sk = "vfmYoka7B74ikLzGcgeCqfqlytOskqwU7mMu3QgX";
    private static String bucketName = "apin-voucher";
    private static String publicURi = "http://voucher.apin.com/";

    private static Auth auth = Auth.create(ak, sk);
    private static Configuration c = new Configuration(Zone.autoZone());
    private static UploadManager uploadManager = new UploadManager(c);
    private static BucketManager bucketManager = new BucketManager(auth, c);

    private QiniuHelper() {
    }

    /**
     * 获取上传token
     *
     * @param fileName 文件名
     * @return token
     */
    public static String getUploadToken(String fileName) {
        return auth.uploadToken(bucketName, fileName);
    }

    /**
     * 上传 路径上传
     *
     * @param path     上传路径
     * @param fileName 文件名
     * @return 返回信息
     */
    public static Response upload(String path, String fileName) {
        try {
            return uploadManager.put(path, fileName, getUploadToken(fileName));
        } catch (QiniuException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 上传 字节流上传
     *
     * @param data     上传字节流
     * @param fileName 文件名
     * @return 返回信息
     */
    public static Response upload(byte[] data, String fileName) {
        try {
            return uploadManager.put(data, fileName,  auth.uploadToken(bucketName));
        } catch (QiniuException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 下载
     *
     * @param fileName 文件名
     * @return
     */
    @Deprecated
    public static String downLoad(String fileName) {
        try {
            String encodedFileName = URLEncoder.encode(fileName, "utf-8");
            return  publicURi+encodedFileName;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读文件内容
     *
     * @param fileName 文件名
     * @return
     */
    public static byte[] read(String fileName) {
        try {
            bucketManager.stat(bucketName, fileName);

            String url = publicURi + fileName;
            URL u = new URL(url);
            InputStream in = u.openStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            IOUtils.copy(in, output);
            byte[] bytes =output.toByteArray();

            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(output);

            return bytes;
        } catch (QiniuException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取文件hash
     *
     * @param fileName 文件名
     * @return 文件hash
     */
    public static String info(String fileName) {
        try {
            FileInfo info = bucketManager.stat(bucketName, fileName);
            return info.hash;
        } catch (QiniuException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     */
    public static void delete(String fileName) {
        try {
            bucketManager.delete(bucketName, fileName);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传并下载接口，返回下载地址信息
     *
     * @param data
     * @return 下载地址
     */
    public static String upAndDownload(byte[] data){
        try {
            Response response = QiniuHelper.upload(data,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return QiniuHelper.downLoad(putRet.hash);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }
}

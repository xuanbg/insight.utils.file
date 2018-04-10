package com.insight.utils.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
     * 上传
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
     * 下载
     *
     * @param fileName 文件名
     * @return
     */
    @Deprecated
    public static String downLoad(String fileName) {
        return publicURi + fileName;
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
}

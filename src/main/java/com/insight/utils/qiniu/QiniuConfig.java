package com.insight.utils.qiniu;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author 宣炳刚
 * @date 2020/10/19
 * @remark 七牛配置类
 */
@Configuration
public class QiniuConfig {

    /**
     * accessKey
     */
    @Value("${qiniu.accessKey}")
    public String accessKey;

    /**
     * secretKey
     */
    @Value("${qiniu.secretKey}")
    public String secretKey;

    /**
     * bucketName
     */
    @Value("${qiniu.bucketName}")
    public String bucketName;

    /**
     * 七牛URL
     */
    @Value("${qiniu.url}")
    public String qiniuUrl;

    /**
     * 构造方法
     */
    public QiniuConfig() {
    }
}

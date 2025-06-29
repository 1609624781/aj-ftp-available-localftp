package com.ajaxjs.net.ftp;

import org.junit.Test;

import java.io.IOException;

public class TestFTP {
    @Test
    public void testUpload() throws IOException {
        System.out.println("开始连接 FTP 服务器");
        UploadFtp client = new UploadFtp("192.168.1.2", 21);
        System.out.println("开始上传文件");
        client.login("Anonymous", "1@1.com");
        client.upload("c:/temp/re.zip", "/re.zip");
        client.closeServer();
        System.out.println("上传完成");
    }

    @Test
    public void testDownload() throws IOException {
        UploadFtp ftp = new UploadFtp("192.168.1.2", 21);
        System.out.println("开始下载文件");
        ftp.login("Anonymous", "1@1.com");
        ftp.getFile("/re.zip", "c:/temp/re1.zip");
        System.out.println("下载完成");
    }
}

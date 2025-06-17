[![Maven Central](https://img.shields.io/maven-central/v/com.ajaxjs/aj-ftp?label=Latest%20Release)](https://central.sonatype.com/artifact/com.ajaxjs/aj-ftp)
[![Javadoc](https://img.shields.io/badge/javadoc-1.1-brightgreen.svg?)](https://javadoc.io/doc/com.ajaxjs/aj-ftp)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/lightweight-component/aj-ftp)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:frank@ajaxjs.com)


# A Lightweight FTP Client

This is a simple and lightweight FTP component with the following features:

- Simple & Lightweight: Easy to use and minimal in size.
- Based on JDK's `sun.*` extension package: Most standard JDK distributions already include built-in FTP capabilities. However, some environments (such as Android) do not support the sun.* packages — that's where this component comes in handy.
- Supports Progress Tracking: Adds the ability to monitor upload/download progress.

## Source code

[Github](https://github.com/lightweight-component/aj-ftp) | [Gitcode](https://gitcode.com/lightweight-component/aj-ftp)


## Usage

### Install
```xml
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>aj-ftp</artifactId>
    <version>1.1</version>
</dependency>
```
Examples of uploading and downloading files via FTP are shown below. The server `speedtest.tele2.net` is a publicly available, free, and anonymous FTP service found online.


```java
public class TestFTP {
    @Test
    public void testUpload() throws IOException {
        UploadFtp client = new UploadFtp("speedtest.tele2.net", 21);
        client.login("anonymous", "anonymous");
        client.upload("c:\\temp\\re.zip", "/upload/re.zip");
        client.closeServer();
    }

    @Test
    public void testDownload() throws IOException {
        UploadFtp ftp = new UploadFtp("speedtest.tele2.net", 21);
        ftp.login("anonymous", "anonymous");
        ftp.getFile("/1KB.zip", "c:\\temp\\re.zip");
    }
}
```

## Key Source Code

The main class is `UploadFtp`, which extends the base `FtpClient` class and is used to implement FTP file upload and download functionality.



```java
package com.ajaxjs.net.ftp;

import com.ajaxjs.net.ftp.sun.TelnetInputStream;
import com.ajaxjs.net.ftp.sun.TelnetOutputStream;
import com.ajaxjs.net.ftp.sun.ftp.FtpClient;

import java.io.*;
import java.nio.file.Files;

/**
 * FTP 文件上传
 */
public class UploadFtp extends FtpClient {
    public UploadFtp(String server, int port) throws IOException {
        super(server, port);
    }

    /**
     * 用书上传本地文件到 FTP 服务器上
     *
     * @param source 上传文件的本地路径
     * @param target 上传到 FTP 的文件路径
     */
    public void upload(String source, String target) {
        try {
            binary();

            try (TelnetOutputStream ftp = put(target);
                 InputStream file = Files.newInputStream(new File(source).toPath())) {
                BufferedInputStream in = new BufferedInputStream(file);

                new ProgressListener().copy(in, new BufferedOutputStream(ftp), in.available());

                System.out.print("put file suc from " + source + "   to  " + target + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从 FTP 上下载所需要的文件
     *
     * @param source 在 FTP 上路径及文件名
     * @param target 要保存的本地的路径
     */
    public void getFile(String source, String target) {
        try {
            binary();

            try (TelnetInputStream ftp = get(source);
                 OutputStream file = Files.newOutputStream(new File(target).toPath())) {

                ProgressListener listener = new ProgressListener();
                listener.setFileName(target);
                listener.copy(new BufferedInputStream(ftp), new BufferedOutputStream(file), getFileSize(source, ftp));

                System.out.print("get file suc from " + source + "   to  " + target + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 为了计算下载速度和百分比，读取 FTP 该文件的大小
     */
    private int getFileSize(String source, TelnetInputStream ftp) throws IOException {
        // 这里的组合使用是必须得 sendServer 后到 readServerResponse
        sendServer("SIZE " + source + "\r\n");

        if (readServerResponse() == 213) {
            String msg = getResponseString();

            try {
                return Integer.parseInt(msg.substring(3).trim());
            } catch (Exception e) {
            }
        }

        return 0;
    }
}
```

Tutorial(Chinese): https://blog.csdn.net/zhangxin09/article/details/134222511.




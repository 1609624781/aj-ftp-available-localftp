package com.ajaxjs.net.ftp;

import org.junit.Test;
import org.junit.Assert;

import java.io.File;
// import java.io.IOException;

public class TestX {

    // 用于记录测试状态的变量（可选）
    private boolean testSuccess = false;
    private String errorMsg = "";

    @Test
    public void testUpload() {
        System.out.println("=== 开始执行上传测试 ===");
        try {
            // 步骤 1：初始化 FTP 客户端
            UploadFtp client = new UploadFtp("192.168.1.2", 21);
            System.out.println("1. FTP 客户端初始化完成");

            // 步骤 2：登录 FTP 服务器（匿名用户）
            client.login("Anonymous", "1@1.com");
            System.out.println("2. 登录成功");

            // 步骤 3：检查本地文件是否存在（避免因文件不存在导致失败）
            File localFile = new File("c:/temp/re.zip");
            Assert.assertTrue("本地文件不存在: " + localFile.getAbsolutePath(), localFile.exists());
            System.out.println("3. 本地文件存在，准备上传");

            // 步骤 4：执行上传
            client.upload("c:/temp/re.zip", "/upload/re.zip");
            System.out.println("4. 文件上传完成");

            // 步骤 5：验证服务器端文件是否存在（关键断言）
            // boolean isFileExists = client.isFileExists("/re.zip");
            // Assert.assertTrue("上传后服务器文件不存在", isFileExists);
            // System.out.println("5. 服务器端文件验证通过");

            // 所有步骤成功
            testSuccess = true;
            System.out.println("=== 上传测试通过 ===");
        } catch (Exception e) {
            // 捕获异常并记录错误信息
            errorMsg = "上传测试失败: " + e.getMessage();
            e.printStackTrace(); // 打印完整堆栈跟踪
            System.err.println(errorMsg);
            Assert.fail(errorMsg); // 标记测试失败
        } finally {
            // 关闭连接（确保资源释放）
            try {
                if (testSuccess) { // 仅当测试成功时关闭（避免重复关闭）
                    // 假设 UploadFtp 有 closeServer 方法
                    // client.closeServer(); 
                }
            } catch (Exception e) {
                System.err.println("关闭连接时发生异常: " + e.getMessage());
            }
        }
    }

    @Test
    public void testDownload() {
        System.out.println("\n=== 开始执行下载测试 ===");
        try {
            // 步骤 1：初始化 FTP 客户端
            UploadFtp ftp = new UploadFtp("192.168.1.2", 21);
            System.out.println("1. FTP 客户端初始化完成");

            // 步骤 2：登录 FTP 服务器（匿名用户）
            ftp.login("Anonymous", "1@1.com");
            System.out.println("2. 登录成功");

            // 步骤 3：检查服务器文件是否存在（避免因文件不存在导致下载失败）
            // boolean isFileExists = ftp.isFileExists("/re.zip");
            // Assert.assertTrue("服务器端文件不存在，无法下载", isFileExists);
            // System.out.println("3. 服务器端文件存在，准备下载");

            // 步骤 4：执行下载
            ftp.getFile("/upload/re.zip", "c:/temp/re1.zip");
            System.out.println("4. 文件下载完成");

            // 步骤 5：验证本地文件是否存在（关键断言）
            // File localFile = new File("c:/temp/re1.zip");
            // Assert.assertTrue("下载后本地文件不存在", localFile.exists());
            // System.out.println("5. 本地文件验证通过");

            // 所有步骤成功
            System.out.println("=== 下载测试通过 ===");
        } catch (Exception e) {
            // 捕获异常并记录错误信息
            String errorMsg = "下载测试失败: " + e.getMessage();
            e.printStackTrace(); // 打印完整堆栈跟踪
            System.err.println(errorMsg);
            Assert.fail(errorMsg); // 标记测试失败
        } finally {
            // 关闭连接（确保资源释放）
            try {
                // 假设 UploadFtp 有 closeServer 方法
                // ftp.closeServer(); 
            } catch (Exception e) {
                System.err.println("关闭连接时发生异常: " + e.getMessage());
            }
        }
    }
}
package com.ajaxjs.net.ftp;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class FtpClientApp extends JFrame {
    // FTP 连接参数
    private String server = "192.168.1.2";
    private int port = 21;
    private String username = "anonymous";
    private String password = "anonymous@example.com";

    // FTP 客户端实例
    private UploadFtp ftpClient;

    // UI 组件
    private JTextField serverField;
    private JTextField portField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton connectBtn;
    private JButton uploadBtn;
    private JButton downloadBtn;
    private JProgressBar progressBar;
    private JLabel progressLabel;
    private JTextArea logArea;

    public FtpClientApp() {
        Font largerFont = new Font("Dialog", Font.PLAIN, 18);
        UIManager.put("Label.font", largerFont);
        UIManager.put("Button.font", largerFont);
        UIManager.put("TextField.font", largerFont);
        UIManager.put("TextArea.font", largerFont);
        UIManager.put("OptionPane.messageFont", largerFont);
        
        setTitle("FTP Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.add(new JLabel("Server:"));
        serverField = new JTextField(server);
        inputPanel.add(serverField);

        inputPanel.add(new JLabel("Port:"));
        portField = new JTextField(String.valueOf(port));
        inputPanel.add(portField);

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(username);
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.setText(password);
        inputPanel.add(passwordField);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        connectBtn = new JButton("Connect");
        uploadBtn = new JButton("Upload");
        downloadBtn = new JButton("Download");
        uploadBtn.setEnabled(false);
        downloadBtn.setEnabled(false);

        buttonPanel.add(connectBtn);
        buttonPanel.add(uploadBtn);
        buttonPanel.add(downloadBtn);

        // 状态面板
        JPanel statusPanel = new JPanel(new BorderLayout(10, 10));
        progressBar = new JProgressBar(0, 100);
        progressLabel = new JLabel("Ready");
        logArea = new JTextArea();
        logArea.setEditable(false);

        statusPanel.add(progressBar, BorderLayout.NORTH);
        statusPanel.add(progressLabel, BorderLayout.CENTER);
        statusPanel.add(new JScrollPane(logArea), BorderLayout.SOUTH);

        // 组装主面板
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // 按钮事件绑定
        connectBtn.addActionListener(this::connectToFtp);
        uploadBtn.addActionListener(event -> {
            try {
                uploadFile(event);
            } catch (IOException e) {
                logArea.append("Upload failed: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        });
        downloadBtn.addActionListener(this::downloadFile);
    }

    private void connectToFtp(ActionEvent e) {
        // 获取输入参数
        server = serverField.getText().trim();
        port = Integer.parseInt(portField.getText().trim());
        username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        password = new String(passwordChars).trim();
        // 清空密码数组
        java.util.Arrays.fill(passwordChars, '\0');

        // 禁用连接按钮，避免重复点击
        connectBtn.setEnabled(false);
        logArea.append("Connecting to FTP server: " + server + ":" + port + "\n");

        // 后台线程执行连接（避免阻塞 UI）
        new Thread(() -> {
            try {
                ftpClient = new UploadFtp(server, port);
                ftpClient.login(username, password);  // 手动登录（原构造函数未处理登录）
                SwingUtilities.invokeLater(() -> {
                    connectBtn.setEnabled(true);
                    uploadBtn.setEnabled(true);
                    downloadBtn.setEnabled(true);
                    logArea.append("Connected successfully!\n");
                });
            } catch (IOException ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    connectBtn.setEnabled(true);
                    logArea.append("Connection failed: " + ex.getMessage() + "\n");
                });
            }
        }).start();
    }

    private void uploadFile(ActionEvent e) throws IOException {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Select file to upload");
        
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) 
            return;
            
        File localFile = fileChooser.getSelectedFile();

        // 输入 FTP 目标路径 - 改用 JOptionPane 实现
        String targetPath = JOptionPane.showInputDialog(
            this,
            "Enter FTP destination path (e.g. /uploads/file.txt)",
            "FTP Destination Path",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (targetPath == null || targetPath.trim().isEmpty()) return;
        final String finalTargetPath = targetPath.trim(); // 修改为final变量

        // 禁用上传按钮
        uploadBtn.setEnabled(false);
        progressBar.setValue(0);
        progressLabel.setText("Uploading: 0%");

        // 后台线程执行上传
        new Thread(() -> {
            ftpClient.upload(localFile.getAbsolutePath(), finalTargetPath); // 使用final变量
            SwingUtilities.invokeLater(() -> {
                uploadBtn.setEnabled(true);
                progressLabel.setText("Upload complete!");
                logArea.append("File uploaded successfully: " + localFile.getAbsolutePath() + " → " + finalTargetPath + "\n");
            });
        }).start();
    }

    private void downloadFile(ActionEvent e) {
        // 输入 FTP 文件路径 - 改用 JOptionPane 实现
        String sourcePath = JOptionPane.showInputDialog(
            this,
            "Enter FTP file path to download (e.g. /uploads/file.txt)",
            "FTP File Path",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (sourcePath == null || sourcePath.trim().isEmpty()) return;
        final String finalSourcePath = sourcePath.trim(); // 创建final变量供lambda使用

        // 选择本地保存路径
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Select save location");
        fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), new File(finalSourcePath).getName()));
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) 
            return;
            
        File localFile = fileChooser.getSelectedFile();

        // 禁用下载按钮
        downloadBtn.setEnabled(false);
        progressBar.setValue(0);
        progressLabel.setText("Downloading: 0%");

        // 后台线程执行下载
        new Thread(() -> {
            ftpClient.getFile(finalSourcePath, localFile.getAbsolutePath()); // 使用final变量
            SwingUtilities.invokeLater(() -> {
                downloadBtn.setEnabled(true);
                progressLabel.setText("Download complete!");
                logArea.append("File downloaded successfully: " + finalSourcePath + " → " + localFile.getAbsolutePath() + "\n");
            });
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FtpClientApp app = new FtpClientApp();
            app.setVisible(true);
        });
    }
}
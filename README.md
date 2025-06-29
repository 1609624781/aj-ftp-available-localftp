# A Lightweight FTP Client

This is a simple and lightweight FTP component with the following features:

- Simple & Lightweight: Easy to use and minimal in size.
- Based on JDK's `sun.*` extension package: Most standard JDK distributions already include built-in FTP capabilities. However, some environments (such as Android) do not support the `sun.*` packages — that's where this component comes in handy.
- Supports Progress Tracking: Adds the ability to monitor upload/download progress.

## Usage

Requires Java8+.

### Install
```xml
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>aj-ftp</artifactId>
    <version>1.1</version>
</dependency>
```

## Key Source Code

src\main\java\com\ajaxjs\net\ftp\FtpClientApp.java


useing localhost as ftp server

could be modified to use other ftp server, set in the code above


`*.java` are used to test the component, modify them to test if you want use other ftp server

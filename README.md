[![Maven Central](https://img.shields.io/maven-central/v/com.ajaxjs/aj-ftp?label=Latest%20Release)](https://central.sonatype.com/artifact/com.ajaxjs/aj-ftp)
[![Javadoc](https://img.shields.io/badge/javadoc-1.1-brightgreen.svg?)](https://javadoc.io/doc/com.ajaxjs/aj-ftp)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/lightweight-component/aj-ftp)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:frank@ajaxjs.com)


# A Lightweight FTP Client

Tutorial(Chinese): https://blog.csdn.net/zhangxin09/article/details/134222511.

## Source code

[Github](https://github.com/lightweight-component/aj-ftp) | [Gitcode](https://gitcode.com/lightweight-component/aj-ftp)


## Install
```xml
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>aj-ftp</artifactId>
    <version>1.1</version>
</dependency>
```

## Usage

FTP upload:

```java
UploadFtp client = new UploadFtp("speedtest.tele2.net", 21);
client.login("anonymous", "anonymous");
client.upload("c:\\temp\\re.zip", "/upload/re.zip");
client.closeServer();
```

FTP download:
```java
UploadFtp ftp = new UploadFtp("speedtest.tele2.net", 21);
ftp.login("anonymous", "anonymous");
ftp.getFile("/1KB.zip", "c:\\temp\\re.zip");
```

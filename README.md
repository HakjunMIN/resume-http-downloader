# HTTP Downloader

HTTP Downloader with resume and retry logic. Target HTTP has to support `Range` in request header. This repo has both of `java net` and `java apache http client` implementations.

## Quick start

1.  Change File download URL, file name and size in test file of [FileDownloaderTest.java](./src/test/java/com/techblogon/httpdownloader/FileDownloaderTest.java) and [FileDownloaderApacheTest.java](./src/test/java/com/techblogon/httpdownloader/FileDownloaderApacheTest.java)

```java
    long FILE_SIZE = 104857602L;
    // The file URL to download
    String FILE_URL = "your blob url";
    // The local file name to save
    String FILE_NAME = "100mb.test";
```

2. Run test

```bash 
$ mvn clean test
```

## Core Description

Below `offset` variable should be declared as `Long` in case of supporting large file size 

```java
 // If the file already exists, resume the download from the end of the file
        if (file.exists()) {
            offset = file.length();
            httpGet.setHeader("Range", "bytes=" + offset + "-");
        }
```


# HTTP Downloader Resume and Retry

HTTP Downloader with resume and retry logic. Target HTTP has to support `Range` in request header. This repo has both of `java net` and `java apache http client` implementations.

## Quick start

1.  Change File download URL, file name and size in test file of [FileDownloaderTest.java](./src/test/java/com/example/FileDownloaderTest.java) and [FileDownloaderHttpClientTest.java](./src/test/java/com/example/FileDownloaderHttpClientTest.java)

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

## cURL

1. Try to use `--retry` and `--retry-max-time` to retry download and `-C` to resume download

```bash
curl -L -O --retry 3 --retry-max-time 0 -C - http://url

```

2. If specify `Range` in request header, it will download the file from the new offset

```bash
curl -O -C --retry 3 - -L -J -H "Range: bytes=<offset>-" "your blob url"
```

## Python3

```python
import requests
import os
import time

url = "<your-blob-url>"
file_name = "100mb.test"

retries = 3
delay = 5

# Define the chunk size for streaming the file
chunk_size = 1024

# Check if the file already exists and get its size
if os.path.exists(file_name):
    file_size = os.path.getsize(file_name)
else:
    file_size = 0

session = requests.Session()

# If file exists, add request header of `Raange` for resuming downloading 
session.headers.update({"Range": f"bytes={file_size}-"})

for i in range(retries):
    try:
        response = session.get(url, stream=True)
        # Check if the response status code is OK or Partial Content
        if response.status_code in (200, 206):
            # Open the file in append mode
            with open(file_name, "ab") as f:
                for chunk in response.iter_content(chunk_size):
                    f.write(chunk)
            print(f"Downloaded {file_name} successfully.")
            break
        else:
            raise Exception(f"Invalid status code: {response.status_code}")
    except Exception as e:
        print(f"Error: {e}")
        print(f"Retrying in {delay} seconds...")
        time.sleep(delay)
else:
    print(f"Failed to download {file_name} after {retries} retries.")
```
package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class FileDownloaderHttpClient {
    private static final int MAX_RETRIES = 3;
    private static final int BUFFER_SIZE = 4096;

    public void downloadFile(String url, String filePath) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        int currentRetry = 0;
        boolean finished = false;
        File file = new File(filePath);
        long offset = 0;

        // If the file already exists, resume the download from the end of the file
        if (file.exists()) {
            offset = file.length();
            httpGet.setHeader("Range", "bytes=" + offset + "-");
        }

        while (!finished && currentRetry < MAX_RETRIES) {
            try {
                HttpResponse response = client.execute(httpGet);

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200 || statusCode == 206) { 
                    InputStream input = response.getEntity().getContent();                    
                    FileOutputStream output = new FileOutputStream(file, true); 
                    
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead = -1;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                    output.close();
                    input.close();
                    finished = true;
                } else { 
                    throw new HttpException("Unexpected status code: " + statusCode);
                }
            } catch (HttpException | IOException e) {        
                e.printStackTrace();
                currentRetry++;

                System.out.println("An error occurred while downloading file: " + e.getMessage());
                System.out.println("Retrying in 10 seconds...");
                
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

            } finally {
                httpGet.releaseConnection();
            }
        }

        if (!finished) {
            throw new IOException("Failed to download file after " + MAX_RETRIES + " attempts");
        }
    }
}
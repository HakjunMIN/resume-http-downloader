package com.example;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class FileDownloader {

    private static final int BUFFER_SIZE = 4096;
    private static final int MAX_RETRY = 3;

    public void downloadFile(String urlString, String filePath) throws IOException {    
        // The current offset of the file
        long offset = 0;
        int retry = 0;
        boolean done = false;

        File file = new File(filePath);

        while (!done && retry < MAX_RETRY) {
            try {
                URL url = new URL(urlString);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // If the offset is not zero, set the Range header to resume from that offset
                if (offset != 0 || file.exists()) {
                    connection.setRequestProperty("Range", "bytes=" + file.length() + "-");
                }
                connection.connect();

                BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                FileOutputStream out = new FileOutputStream(filePath, true);
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    offset += bytesRead;
                }
                in.close();
                out.close();
                done = true;
            } catch (IOException e) {
                System.out.println("An error occurred while downloading file: " + e.getMessage() + " Retrying...");
                retry++;

                if (retry > MAX_RETRY) {
                    throw new RuntimeException("Failed to download file after " + MAX_RETRY + " attempts", e);
                }

                System.out.println("An error occurred while downloading file: " + e.getMessage());
                System.out.println("Retrying in 10 seconds...");

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        System.out.println("File downloaded successfully");
    }
}


package com.example;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FileDownLoaderHtpClientTest {

    
    @Test
    public void shouldDownloadFileForHttpClient() throws IOException
    {
        System.out.println( "Starting file download via FileDownloaderHttpClient" );

        long FILE_SIZE = 104857602L;
        // The file URL to download
        String FILE_URL = "your blog url";
        // The local file name to save
        String FILE_NAME = "100mb.test";

        FileDownloaderHttpClient fileDownloaderHttpClient = new FileDownloaderHttpClient();

        fileDownloaderHttpClient.downloadFile(FILE_URL, FILE_NAME);

        File file = new File(FILE_NAME);
        assertEquals(file.exists(), true);
        assertEquals(file.length(), FILE_SIZE);
        file.delete();
    
    }
    
}

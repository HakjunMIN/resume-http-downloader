package com.example;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FileDownloaderTest 
{
    
    @Test
    public void shouldDownloadFile() throws IOException
    {        
        System.out.println( "Starting file download via FileDownloader" );
        long FILE_SIZE = 104857602L;
        // The file URL to download
        String FILE_URL = "your blob url";
        // The local file name to save
        String FILE_NAME = "100mb.test";

        FileDownloader fileDownloader = new FileDownloader();

        fileDownloader.downloadFile(FILE_URL, FILE_NAME);

        File file = new File(FILE_NAME);
        assertEquals(file.exists(), true);
        assertEquals(file.length(), FILE_SIZE);
        file.delete();
    }

}

package com.example.mykitchen;

import androidx.annotation.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FileDownloader {
    private OkHttpClient client;

    public FileDownloader() {
        client = new OkHttpClient();
    }

    public void downloadFile(String url, String destinationPath, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    long downloadedBytes = 0;
                    long totalBytes = response.body().contentLength();

                    try (InputStream inputStream = response.body().byteStream();
                         OutputStream outputStream = new FileOutputStream(destinationPath)) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                            downloadedBytes += bytesRead;
                            callback.onDownloadProgress(downloadedBytes, totalBytes);
                        }

                        callback.onDownloadFinished();
                    } catch (IOException e) {
                        callback.onDownloadFailed();
                    }
                } else {
                    callback.onDownloadFailed();
                }
                response.close();
            }
        });
    }

    public interface Callback {
        void onDownloadProgress(long downloadedBytes, long totalBytes);

        void onDownloadFinished();

        void onDownloadFailed();
    }
}

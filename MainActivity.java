package com.example.mykitchen;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static String getInstalledVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo("com.mango.mango_tv", 1);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void checkVersionAndUpdate() {
        String latestVersion = "1.0"; //версия из БД
        String installedVersion = getInstalledVersion(this);
        if (installedVersion != null && latestVersion != null) {
            if (!installedVersion.equals(latestVersion)) {
                showUpdateNotification();
            } else {
            }
        }
    }

    public void showUpdateNotification() {
        Toast.makeText(this, "Пожалуйста обновите версию", Toast.LENGTH_LONG).show();
    }

    public void downloadAndInstalledNewVersion() {
        String url = "http://example.com/iptvcore.apk";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setVisibleInDownloadsUi(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "iptvcore_update.apk");
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == downloadId) {
                    Uri apkUri = downloadManager.getUriForDownloadedFile(downloadId);
                    if (apkUri != null) {
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(installIntent);
                    }
                }
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}

        // Дальше идут способы возможных решений, их тоже можно использовать, какое удобнее смотри сам, хотя делают они тоже самое...


/*public static String getInstalledVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo("com.exemple.iptvcore", 0);// пакетное имя
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void checkVersionAndUpdate() {
        String latestVersion = "1.0"; //версия из БД
        String installedVersion = getInstalledVersion(this);
        if (installedVersion != null && latestVersion != null && !installedVersion.equals(latestVersion)) {
            showUpdateNotification();
        }
    }

    public void showUpdateNotification() {
        Toast.makeText(this, "Пожалуйста обновите версию", Toast.LENGTH_LONG).show();
    }

    public void downloadAndInstalledNewVersion() {
        String apkUrl = "https://example.com/iptvcore.apk";
        String apkDestination = getFilesDir() + "/iptvcore.apk"; //Куда сохранить

        FileDownloader fileDownloader = new FileDownloader();
        fileDownloader.downloadFile(apkUrl, apkDestination, new FileDownloader.Callback() {
            @Override
            public void onDownloadFinished() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(FileProvider.getUriForFile(MainActivity.this, "com.example.fileprovider", new File(apkDestination)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }

            @Override
            public void onDownloadFailed() {
            }
        });
    }*/

    /*public void downloadAndInstalledNewVersion() {
        FileDownloader fileDownloader = new FileDownloader();

        String apkUrl = "https://example.com/iptvcore.apk";

        String apkDestination = getApplicationContext().getCacheDir().getPath() + "/iptvcore.apk"; //Куда сохранить

        fileDownloader.downloadFile(apkUrl, apkDestination, new FileDownloader.Callback() {
            @Override
            public void onDownloadProgress(long downloadedBytes, long totalBytes) {
            }

            @Override
            public void onDownloadFinished() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(apkDestination)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onDownloadFailed() {
            }
        });
    }*/
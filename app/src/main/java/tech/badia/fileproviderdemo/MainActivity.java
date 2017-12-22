package tech.badia.fileproviderdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button mBtnLegacy = null;
    Button mBtnFileProvider = null;
    Button mBtnResource = null;


    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnLegacy = (Button) findViewById(R.id.btn_legacy);
        mBtnFileProvider = (Button) findViewById(R.id.btn_file_provider);
        mBtnResource = (Button) findViewById(R.id.btn_resource);


        mBtnLegacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSoundLegacy();
            }
        });

        mBtnFileProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSoundFileProvider();
            }
        });

        mBtnResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSoundResource();
            }
        });


        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        copyFromAssetToInternalStorage("notification.mp3");

    }


    private void copyFromAssetToInternalStorage(String fileName){
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        byte[] buffer = new byte[1024];
        int read;

        try {
            in = assetManager.open(fileName);
            out = new FileOutputStream(getApplicationContext().getFilesDir().toString() + "/" + fileName  );

            while( (read = in.read(buffer)) != -1) {
                out.write(buffer,0,read);
            }

            in.close();
            out.flush();
            out.close();



        }catch (Exception e){
            Log.d("FileProviderDemo", e.toString());
        }

    }


    private void playSoundLegacy(){
        Uri uri = Uri.parse("file://asset_folder/notification.mp3");
        generateNotification(uri);
    }


    private void playSoundFileProvider(){
        File imagePath = new File(getApplicationContext().getFilesDir(), "");
        File newFile = new File(imagePath, "notification.mp3");
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "tech.badia.fileprovider", newFile);
        getApplicationContext().grantUriPermission("com.android.systemui", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        generateNotification(uri);
    }


    private void playSoundResource(){
        // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);

        Resources resources = getApplicationContext().getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.raw.notification))
                .appendPath(resources.getResourceTypeName(R.raw.notification))
                .appendPath(resources.getResourceEntryName(R.raw.notification))
                .build();


        generateNotification(uri);
    }




    private void generateNotification(Uri uri) {
        NotificationChannel channel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                    .build();

            channel = new NotificationChannel("ch:" + (new Date()).getTime(), "channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(uri, audioAttributes);

            mNotificationManager.createNotificationChannel(channel);

            mBuilder =
                    new NotificationCompat.Builder(getApplicationContext(), channel.getId())
                            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                            .setContentTitle("Notification Title")
                            .setContentText("Notification Text")
                            .setVibrate(new long[500])
                            .setAutoCancel(true);

        }

        mNotificationManager.notify(1, mBuilder.build());
    }

}

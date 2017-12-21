package tech.badia.fileproviderdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mBtnLegacy = null;
    Button mBtnFileProvider = null;


    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnLegacy = (Button) findViewById(R.id.btn_legacy);
        mBtnFileProvider = (Button) findViewById(R.id.btn_file_provider);


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


        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);



    }


    private void playSoundLegacy(){

        Uri uri = Uri.parse("file://asset_folder/notification.mp3");

        mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "A")
                        .setContentTitle("Notification with legacy sound")
                        .setContentText("Notification with legacy sound")
                        .setSound(uri)
                        .setVibrate(new long[500])
                        .setAutoCancel(true);

        mNotificationManager.notify(1, mBuilder.build());


    }


    private void playSoundFileProvider(){

    }



}

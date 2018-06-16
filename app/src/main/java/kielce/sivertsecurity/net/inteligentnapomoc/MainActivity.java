package kielce.sivertsecurity.net.inteligentnapomoc;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity  {

    public static final String PHONE_NUMBER = "885920718";
    private final int PERMISSION_SMS_NUMBER = 6161;
    private final int PERMISSION_CALL_NUMBER = 6262;
    private final int PERMISSION_PHONE_STATE = 6363;
    private final int NOTIFICATION_ID = 6464;
    private  boolean AVAILABILITY = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestPermissions();

        checkAvailability();


        final Button b1 = (Button) findViewById(R.id.button8);
        final Button b2 = (Button) findViewById(R.id.button10);
        final Button b3 = (Button) findViewById(R.id.button11);

        if(!AVAILABILITY) {
            b1.setAlpha(.5f);
            b1.setClickable(false);
        } else {
           notification();
        }



        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!AVAILABILITY || !b1.isClickable()) {
                    Toast.makeText(getApplicationContext(), "Nie jestes objety programem stalego nadzoru!", Toast.LENGTH_LONG).show();
                    return;
                }


                Toast.makeText(getApplicationContext(), R.string.call_emergency_sent, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Wybieranie numeru: 112. \nPrzekazujemy informacje: - Pacjent cierpi na cukrzyce - Grupa krwi A. \nPrzekazywanie lokalizacji GPS", Toast.LENGTH_LONG).show();

                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(PHONE_NUMBER, null, "Sytuacja zagrozenia zycia. UWAGA:Pacjent ma cukrzyce. Lokalizacja: 78.52 E, 45.91 N", null, null);
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + PHONE_NUMBER));
                getApplicationContext().startActivity(intent);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAlert();
            }
        });


    }





    private static final int MY_NOTIFICATION_ID = 1;

    private Notification myNotification;

    void notification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("yourpackage.notifyId", NOTIFICATION_ID);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(this)
                .setContentTitle("POMOC")
                .setSmallIcon(R.drawable.szwajcaria)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("NACISNIJ ABY WEZWAC SLUZBY RATUNKOWE"))
                .addAction(R.drawable.szwajcaria, "WEZWIJ", pIntent)
                .setContentIntent(pIntent)
                .setContentText("NACISNIJ ABY WEZWAC SLUZBY RATUNKOWE")
                .setOngoing(true);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }



    public void sendMessage(View view) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    public void requestPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_PHONE_STATE);

            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        PERMISSION_SMS_NUMBER);

                // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_CALL_NUMBER);

            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }



    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CALL_NUMBER: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.exit(0);
                }

                break;
            }
            case PERMISSION_PHONE_STATE: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.exit(0);
                }
                break;
            }

            case PERMISSION_SMS_NUMBER: {

                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.exit(0);
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("UWAGA!")
                .setMessage("Czy napewno wybrać numer alarmowy?.\nLokalizacja zostanie przesłana do służb")
                .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Toast.makeText(getApplicationContext(), R.string.call_emergency_sent, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Wybieranie numeru: 112.\nPrzekazywanie lokalizacji GPS", Toast.LENGTH_LONG).show();

                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(PHONE_NUMBER, null, "Sytuacja zagrozenia zycia. Lokalizacja: 78.52 E, 45.91 N", null, null);
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + PHONE_NUMBER));
                        getApplicationContext().startActivity(intent);

                    }
                })
                .setNegativeButton("Nie chce", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //
                    }
                });
        dialog.show();
    }



    private void checkAvailability() {
        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getDeviceId();

        if(mPhoneNumber.equalsIgnoreCase("353560084816285")) {
            AVAILABILITY = true;
            return;
        }
        AVAILABILITY = false;
    }

}

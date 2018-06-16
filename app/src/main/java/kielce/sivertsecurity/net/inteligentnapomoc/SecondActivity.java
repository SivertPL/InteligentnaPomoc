package kielce.sivertsecurity.net.inteligentnapomoc;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);


        final Button b1 = (Button) findViewById(R.id.button19);
        final Button b2 = (Button) findViewById(R.id.button18);
        final Button b3 = (Button) findViewById(R.id.button17);
        final Button b4 = (Button) findViewById(R.id.button16);
        final Button b5 = (Button) findViewById(R.id.button15);
        final Button b6 = (Button) findViewById(R.id.button14);


        final Button exit = (Button) findViewById(R.id.button14);


        View.OnClickListener w = new View.OnClickListener() {
            public void onClick(View v) {
               showAlert();
            }
        };

        b1.setOnClickListener(w);
        b2.setOnClickListener(w);
        b3.setOnClickListener(w);
        b4.setOnClickListener(w);
        b5.setOnClickListener(w);
        b6.setOnClickListener(w);


        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(v);
            }
        });

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        startActivity(intent);
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
                        sms.sendTextMessage(MainActivity.PHONE_NUMBER, null, "Sytuacja zagrozenia zycia. Lokalizacja: 78.52 E, 45.91 N", null, null);
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + MainActivity.PHONE_NUMBER));
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

}
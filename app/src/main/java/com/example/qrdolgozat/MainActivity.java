package com.example.qrdolgozat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button scanneles,kiiratas;
    TextView textView;
    private Timer timer;
    private TimerTask timerTask;
    private Naplozas naplozas;
    String szoveg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();



        scanneles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("QR Code Scanning by Valaki");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);       //!!
                integrator.initiateScan();
            }
        });
        timer = new Timer();
        timerTask = new TimerTask(){

            @Override
            public void run() {
                TimerMethod();
            }
        };
        timer.schedule(timerTask, 1000,1000);
    }
    //időzített keresztfüggvény
    public void TimerMethod() {
        this.runOnUiThread(Timer_Tick);
    }
    public Runnable Timer_Tick = new Runnable() {
        @Override
        public void run() {
            kiiratas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    szoveg=textView.getText().toString();
                    try
                    {
                        naplozas.kiiras(szoveg);
                        Toast.makeText(MainActivity.this, "scanneltDatok lementve a készülékre", Toast.LENGTH_SHORT).show();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            });
            /*szoveg=textView.getText().toString();
            try
            {
                naplozas.kiiras(szoveg);
                Toast.makeText(MainActivity.this, "scanneltDatok lementve a készülékre", Toast.LENGTH_SHORT).show();
            }catch (IOException e)
            {
                e.printStackTrace();
            }*/
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(this, "Kiléptünk a scannelésből", Toast.LENGTH_SHORT).show();
            }else
            {
                textView.setText("QR Code eredmény:" + result.getContents());


                //ha van benne link akkor menjen rá
                Uri uri = Uri.parse(result.getContents());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init(){

        scanneles = findViewById(R.id.gomb1);
        kiiratas = findViewById(R.id.gomb2);
        textView = findViewById(R.id.textView);

    }
}

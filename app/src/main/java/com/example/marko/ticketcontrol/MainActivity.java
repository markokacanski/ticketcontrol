package com.example.marko.ticketcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Exchanger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    IntentIntegrator mIntegrator = new IntentIntegrator(this);

    TextView txt;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = (TextView) findViewById(R.id.textView);
        img = (ImageView) findViewById(R.id.imageView);
    }

    public void handleClick(View v){
        mIntegrator.initiateScan();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {

            String haystack = scanResult.getContents();

            Pattern userIdPattern = Pattern.compile("uid:(.*);");
            Pattern beaconIdPattern = Pattern.compile("bid:(.*)");

            Matcher userIdMatcher;
            Matcher beaconIdMatcher;
            userIdMatcher = userIdPattern.matcher(haystack);
            beaconIdMatcher = beaconIdPattern.matcher(haystack);

            String userId = "you suck at programming";
            String beaconId = "you suck at programming";

            if (userIdMatcher.find()){
                 userId = userIdMatcher.group(1);

            }

            if(beaconIdMatcher.find()) {
                 beaconId = beaconIdMatcher.group(1);
            }

            if (checkTicket(userId,beaconId)){
                txt.setText("");
                img.setImageResource(R.drawable.uservalid);
            }else{
                txt.setText("");
                img.setImageResource(R.drawable.usernotvalid);
            }

            //txt.setText("user: "+userId+" and beacon: "+beaconId);
        }
    }

    private boolean checkTicket(String userId, String beaconId) {

        String requestUrl = "http://gocarinthia.cicika.info/user/" + userId + "/" + beaconId;

        String apiResult = getApiResponse(requestUrl);


        if (!apiResult.isEmpty()) {
          return true;
        } else {
          return false;
        }
    }

    private String getApiResponse(String url){

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(url)
                .build();

        try{
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch(Exception e){
            //oh no shit happend
            return "you suck at programming";
        }

    }
}

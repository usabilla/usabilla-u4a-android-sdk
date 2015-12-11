package com.usabilla.sdk.ubfeedback;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.usabilla.sdk.ubform.UBFormClient;

public class MainActivity extends ActionBarActivity {

    private UBFormClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        client = new UBFormClient(this);

        //Opens the form when the user shakes the device
        client.setShakeFeedbackForm("xxxYOUR_APP_IDxxx");

        //Opens the form when the user presses the button
        this.findViewById(R.id.btnRunForm).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                client.takeScreenshot();
                client.openFeedbackForm("xxxYOUR_APP_IDxxx");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        client.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        client.onPause();
    }
}

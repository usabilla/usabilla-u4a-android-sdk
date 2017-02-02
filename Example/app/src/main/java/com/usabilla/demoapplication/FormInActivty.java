package com.usabilla.demoapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.usabilla.sdk.ubform.UBFormClient;
import com.usabilla.sdk.ubform.UBFormInterface;
import com.usabilla.sdk.ubform.controllers.Form;
import com.usabilla.sdk.ubform.utils.ThemeConfig;

public class FormInActivty extends AppCompatActivity implements UBFormInterface {

    private Form form;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_in_activty);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UBFormClient.loadFeedbackForm("58930de09172337051e8d3cc", getApplicationContext(), FormInActivty.this);

        setUpBroadcastReceivers();
    }

    private void setUpBroadcastReceivers() {
        BroadcastReceiver mCloser, mPlayStore;

        mCloser = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                FormInActivty.this.finish();
            }
        };

        mPlayStore = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "I should redirect to the play store now", Toast.LENGTH_SHORT).show();
            }
        };
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mPlayStore, new IntentFilter("com.usabilla.redirectToPlayStore"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mCloser, new IntentFilter("com.usabilla.closeForm"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        item = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == this.item && form != null) {
            form.navigationButtonPushed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void formLoadedSuccessfully(Form form, boolean b) {
        this.form = form;

        //I will use the action bar to hold the navigation button
        form.hideDefaultNavigationButton(true);
        form.hideCancelButton(true);

        //I can customise font and drawables here before displaying the form
        ThemeConfig themeConfig = form.getThemeConfig();
        //form.getThemeConfig().setFont("MiloOT.otf", getApplicationContext());

        getSupportFragmentManager().beginTransaction().add(R.id.container, form).commit();
    }

    @Override
    public void formFailedLoading(Form form) {

    }

    @Override
    public void textForMainButtonUpdated(String text) {
        if (item != null)
            item.setTitle(text);
    }
}

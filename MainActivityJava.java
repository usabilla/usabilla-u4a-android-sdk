package com.usabilla.android.testandroidx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.usabilla.sdk.ubform.UbConstants;
import com.usabilla.sdk.ubform.Usabilla;
import com.usabilla.sdk.ubform.UsabillaFormCallback;
import com.usabilla.sdk.ubform.UsabillaReadyCallback;
import com.usabilla.sdk.ubform.sdk.entity.FeedbackResult;
import com.usabilla.sdk.ubform.sdk.form.FormClient;
import com.usabilla.sdk.ubform.sdk.form.model.UbFonts;
import com.usabilla.sdk.ubform.sdk.form.model.UbImages;
import com.usabilla.sdk.ubform.sdk.form.model.UsabillaTheme;

import java.util.Arrays;

public class MainActivityJava extends AppCompatActivity implements UsabillaFormCallback, UsabillaReadyCallback {

    private static final String FRAGMENT_TAG = "MyFragment";

    private BroadcastReceiver usabillaReceiverClosePassive;
    private BroadcastReceiver usabillaReceiverCloseCampaign;
    private IntentFilter closerFilter = new IntentFilter(UbConstants.INTENT_CLOSE_FORM);
    private IntentFilter closerCampaignFilter = new IntentFilter(UbConstants.INTENT_CLOSE_CAMPAIGN);

    private FormClient formClient;
    private Usabilla usabilla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupPassiveFormBroadcastReceiver();
        setupCampaignFormBroadcastReceiver();
        usabilla = Usabilla.INSTANCE;

        initializeSdk();
        //Optional
        setUsabillaTheme();

        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG) != null) {
            formClient = (FormClient) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            attachFragment();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(usabillaReceiverClosePassive, closerFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(usabillaReceiverCloseCampaign, closerCampaignFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaReceiverClosePassive);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaReceiverCloseCampaign);
    }

    @Override
    public void formLoadSuccess(FormClient form) {
        formClient = form;
        attachFragment();
    }

    @Override
    public void formLoadFail() {
        Toast.makeText(this, "Form load fail", Toast.LENGTH_LONG).show();
    }

    @Override
    public void mainButtonTextUpdated(String text) {
        // Use this text for your own navigation button.
        // Usually returns "Next" or "Submit".
    }

    @Override
    public void onUsabillaInitialized() {
        // This callback will be called once the initialization process of the SDK finishes.
        // In case an appId was provided during initialization, then the SDK starts to be able
        // to process events right after this callback is called.
    }

    private void attachFragment() {
        getSupportFragmentManager().beginTransaction().replace("use frame layout here", formClient.getFragment(), FRAGMENT_TAG).commit();
    }

    private void setupPassiveFormBroadcastReceiver() {
        usabillaReceiverClosePassive = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (formClient != null) {
                    getSupportFragmentManager().beginTransaction().remove(formClient.getFragment()).commit();
                }
            }
        };
    }

    private void setupCampaignFormBroadcastReceiver() {
        usabillaReceiverCloseCampaign = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final FeedbackResult res = intent.getParcelableExtra(FeedbackResult.INTENT_FEEDBACK_RESULT_CAMPAIGN);
                String feedbackInfo = "Rating " + res.getRating() + "\n";
                feedbackInfo += "Abandoned page " + res.getAbandonedPageIndex() + "\n";
                feedbackInfo += "Is sent " + res.isSent();
                Toast.makeText(context, feedbackInfo, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void initializeSdk() {
        // In the initialize method the third parameter defines a custom http client that can replace
        // the default one used by the SDK (Volley).
        // If `null` is passed then the default client will be used.
        usabilla.initialize(this, "use your personal AppId here", null, this);
        usabilla.setDebugEnabled(true);
        usabilla.updateFragmentManager(getSupportFragmentManager());
    }

    private void giveFeedback() {
        // Optional screenshot
        final Bitmap screenshot = usabilla.takeScreenshot(this);

        // Optional theme specific for that passive form
        final UbFonts newFonts = new UbFonts(R.font.indie_flower);
        final UsabillaTheme theme = new UsabillaTheme(newFonts, null);
        usabilla.loadFeedbackForm("use your personal FormId here", screenshot, theme, this);
    }

    private void sendEvent() {
        usabilla.sendEvent(getApplicationContext(), "MyEvent");
    }

    private void resetCampaign() {
        // Reset campaign progression deleting them from memory.
        // It also fetches the campaigns associated to the appId once again
        // and calls the initialisation callback once the process is finished.
        usabilla.resetCampaignData(this, this);
    }

    private void resetPassiveFeedbackForms() {
        // Deletes preloaded passive feedback forms from memory.
        usabilla.removeCachedForms();
    }

    private void setUsabillaTheme() {
        // Create images
        final UbImages newImages = new UbImages(
                Arrays.asList(R.drawable.mood_1_bw,
                        R.drawable.mood_2_bw,
                        R.drawable.mood_3_bw,
                        R.drawable.mood_4_bw,
                        R.drawable.mood_5_bw),
                null,
                R.drawable.ic_star_yellow,
                R.drawable.ic_star_red);
        // Set the theme
        final UsabillaTheme theme = new UsabillaTheme(null, newImages);
        usabilla.setTheme(theme);
    }
}

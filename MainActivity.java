public class MainActivity extends AppCompatActivity implements UsabillaFormCallback, UsabillaReadyCallback {

    private static final String FRAGMENT_TAG = "MyFragment";
    
    private BroadcastReceiver usabillaReceiverClosePassive;
    private BroadcastReceiver usabillaReceiverCloseCampaign;
    private IntentFilter closerFilter = new IntentFilter(Constants.INTENT_CLOSE_FORM);
    private IntentFilter closerCampaignFilter = new IntentFilter(Constants.INTENT_CLOSE_CAMPAIGN);
    
    private Usabilla usabilla;
    private FormClient formClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupPassiveFormBroadcastReceiver();
        setupCampaignFormBroadcastReceiver();

        usabilla = new Usabilla.Companion().getInstance(this);
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
        if (form.getFragment() != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_frame, form.getFragment(), FRAGMENT_TAG).commit();
        }
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
        if (formClient.getFragment() != null) {
            getSupportFragmentManager().beginTransaction().replace("use frame layout here", formClient.getFragment(), FRAGMENT_TAG).commit();
        }
    }

    private void setupPassiveFormBroadcastReceiver() {
        usabillaReceiverClosePassive = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (formClient != null && formClient.getFragment() != null) {
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
        
        // Optional theme
        final Fonts themeFonts = new Fonts();
        themeFonts.setRegular("fonts/IndieFlower.ttf");
        final UsabillaTheme theme = new UsabillaTheme.Builder()
                .setFonts(themeFonts)
                .build();
        
        usabilla.loadFeedbackForm(this, "use your personal FormId here", screenshot, theme, this);
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
        usabilla.removeCachedForms(this);
    }

    private void setUsabillaTheme() {
        // Create images
        Images themeImages = new Images();
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(R.drawable.mood_1_bw);
        ids.add(R.drawable.mood_2_bw);
        ids.add(R.drawable.mood_3_bw);
        ids.add(R.drawable.mood_4_bw);
        ids.add(R.drawable.mood_5_bw);
        themeImages.setEnabledEmoticons(ids);

        themeImages.setStarOutline(R.drawable.ic_star_red);
        themeImages.setStar(R.drawable.ic_star_yellow);

        // Build theme
        UsabillaTheme.Builder themeBuilder = new UsabillaTheme.Builder();
        themeBuilder.setImages(themeImages);

        // Set the theme
        usabilla.setTheme(themeBuilder.build());
    }
}
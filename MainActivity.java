public class MainActivity extends AppCompatActivity implements UBFeedbackForm {

    private static final String FRAGMENT_TAG = "MyFragment";
    private FormClient formClient;
    private BroadcastReceiver usabillaCloser;
    private BroadcastReceiver usabillaCampaignCloser;
    private IntentFilter closerFilter = new IntentFilter(Constants.INTENT_CLOSE_FORM);
    private IntentFilter closerCampaignFilter = new IntentFilter(Constants.INTENT_CLOSE_CAMPAIGN);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Optional
        setUpUsabillaTheme();

        setupCloserBroadcastReceiver();
        setupCampaignCloserBroadcastReceiver();
        initSDK();

        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG) != null) {
            formClient = (FormClient) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            attachFragment();
        }
    }

    private void attachFragment() {
        if (formClient.getFragment() != null) {
            getSupportFragmentManager().beginTransaction().replace("use frame layour here", formClient.getFragment(), FRAGMENT_TAG).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(usabillaCloser, closerFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(usabillaCampaignCloser, closerCampaignFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaCloser);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaCampaignCloser);
    }

    private void setupCloserBroadcastReceiver() {
        usabillaCloser = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (formClient != null && formClient.getFragment() != null) {
                    getSupportFragmentManager().beginTransaction().remove(formClient.getFragment()).commit();
                }
            }
        };
    }

    private void setupCampaignCloserBroadcastReceiver() {
        usabillaCampaignCloser = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                FeedbackResult res = intent.getParcelableExtra(Constants.INTENT_FEEDBACK_RESULTS_CAMPAIGN);
                String feedbackInfo = "Rating " + res.getRating() + "\n";
                feedbackInfo += "Abandoned page " + res.getAbandonedPageIndex() + "\n";
                feedbackInfo += "Is sent " + res.isSent();
                Toast.makeText(context, feedbackInfo, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void initSDK() {
        Usabilla.initialize(this, "MyAppID");
        Usabilla.setDebugEnabled(true);
        Usabilla.updateFragmentManager(getSupportFragmentManager());
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
        // Use this text for your own navigation button
        // Usually returns "Next" or "Submit"
    }

    private void giveFeedback() {
        // Optional screenshot
        Usabilla.takeScreenshot(this);
        Usabilla.loadFeedbackForm(this, "MyFormID", this);
    }

    private void sendEvent() {
        Usabilla.sendEvent(getApplicationContext(), "MyEvent");
    }

    private void resetCampaign() {
        // Used to reset campaign progression
        Usabilla.resetCampaignData(this);
    }

    private void setUpUsabillaTheme() {
        // Create fonts
        Fonts themeFonts = new Fonts();
        themeFonts.setRegular("fonts/IndieFlower.ttf");

        // Create images
        Images themeImages = new Images();
        ArrayList<Integer> ids = new ArrayList<Integer>();
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
        themeBuilder.setFonts(themeFonts);

        // Set the theme
        Usabilla.setTheme(themeBuilder.build());
    }
}
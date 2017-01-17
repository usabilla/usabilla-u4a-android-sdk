public class MainActivity extends AppCompatActivity implements UBFormInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       
       //UBFormClient is now static
        //This is used just to get the name of the hostin app, for reporting
        UBFormClient.initClient(getApplicationContext());
        //The SDK uses broadcast to communicate with the main application. 
        BroadcastReceiver usabillaCloser = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //remove fragment
            }
        };
         BroadcastReceiver asd = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //This will be called if the user asks to go to the playstore
                //Redirect to play store
            }
        };
        //This one is used when the user wishes to go to the play store. In this case "com.usabilla.closeForm" will be called immediately after as well.
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(asd, new IntentFilter("com.usabilla.redirectToPlayStore"));
        //This one is called when the user wants to close the form
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(usabillaCloser, new IntentFilter("com.usabilla.closeForm"));

    }
   
    protected void onButtonClick() {
       
        final JSONObject customVars = new JSONObject();
        try {
            customVars.put("user", "Mari_o.");
            customVars.put("user_id", 12345);
        } catch (JSONException e) {
            //WHOPS
        }
   
        UBFormClient.takeScreenshot(this);
        //Loading a form with an ID, custom variables (if any), context and the object implementing the UBFormInterface for the callback
        UBFormClient.loadFeedbackForm("ID", customVars, getApplicationContext(), this);
       
    }

    @Override
    public void formLoadedSuccessfully(Form form, boolean active) {
        //Form has been loaded, you can configure the behaviour of the buttons. Being a fragment, the navigation button ( next, submit) and the cancel one 
        //are displayed on the bottom of the screen
        form.hideCancelButton(false);
        form.hideGiveMoreFeedback(true);
        //You can edit the form styling here
        ThemeConfig themeConfig = form.getThemeConfig();
        themeConfig.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        themeConfig.setTitleColor(getResources().getColor(android.R.color.holo_purple));
        themeConfig.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        themeConfig.setAccentColor(getResources().getColor(android.R.color.white));
        themeConfig.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        themeConfig.setAccentTextColor(getResources().getColor(android.R.color.black));
        //At this point the fragment is loaded and ready to be displayed. It's up to yo when or how
    }
    @Override
    public void formFailedLoading(Form defaultForm) {
        //This is called when some kind of error occurs. You get a default form
    }

    @Override
    public void textForMainButtonUpdated(String text) {
        //this is  useful if you want to hide the default navigation button and provide your own (ex. in action bar)
        //In this case, a few more steps are necessary:
        //1) call  form.hideDefaultNavigationButton(true); to hide the fragment navigation. You can also hide the cancel button if you want to use the device physical back button.
        //2) call form.getTextForMainButton() to get the text your button should have at the beginning.
        //3) call form.navigationButtonPushed(); when your external button is pushed. The form will update itself and that method will also return the string for the new button text.

    }
}

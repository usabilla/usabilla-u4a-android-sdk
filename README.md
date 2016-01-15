 [ ![Download](https://api.bintray.com/packages/usabilla/maven/ubform/images/download.svg) ](https://bintray.com/usabilla/maven/ubform/_latestVersion)

# Usabilla FeedbackSDK for Android
This repository contains the FeedbackSDK for Android.


Take a look at our [Wiki](https://github.com/usabilla/usabilla-u4a-android-sdk/wiki) for a complete and in depth guide on how to install and customize the SDK.

# WARNING
Version 2.0.0+ is incompatible with the implementation of version 1.*
Please check MainActivity.java to see how the SDK could be used.

**Starting from version 2.1.0 the SDK uses AppCompatActivity declared in support:appcompat-v7:22.1+. Please make sure you are using a version of support:appcompat-v7 that contains AppCompatActivity**

# Installation

## Gradle instructions
The Usabilla SDK are currently served through [JCenter](https://bintray.com/usabilla/maven/ubform/view):
- make sure `jcenter()` is included in your repositories
- add `compile 'com.usabilla.sdk:ubform:2.0.3'` to the dependencies of your gradle build script.

## Manual instructions
Download the packaged `.aar` library and include it in your project according to the IDE of your choice.

## How to get started
### On the web
- Create a new app on your [Usabilla](https://app.usabilla.com/member/) Live for Apps section.
- Copy th AppId from the app you wish to use in your SDK.

### On the SDK
- import the SDK in your activity `import com.usabilla.sdk.ubform.UBFormClient;`
- init the UbFormClient in the onCreate() method of your activity.
- call `openFeedbackForm` passing your AppId to show the form you created in the web interface.
- use `setShakeFeedbackForm` to automatically open the form if the device is shaken.
- remember to call onResume and onPause on the UbFormClient according to the state of your activity.

```
public class MainActivity{

    private UBFormClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new UBFormClient(this);
        //Open the feedback form on device shake
        client.setShakeFeedbackForm("AppId");

        this.findViewById(R.id.feedbackButton).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                client.takeScreenshot();
                client.openFeedbackForm("AppID");
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
```

## Screenshot
In order to attach a screenshot to the feedback item you can either let our SDK generate it for you by calling `client.takeScreenshot()`
or you can pass it as the view you want to screenshot.

## Custom variables
You can pass along custom variables that will be attached to the feedback users send.
Currently custom variables are represented by a JSON object attached to the activity intent as extra:
```
    JSONObject customVars = new JSONObject();
    try {
        customVars.put("user", "mario");
        customVars.put("uid", 12345);
    } catch (JSONException e) {
        // WHOPS
    }
    openFeedbackForm(AppId, customVars)
```
## ProGuard 
If you are using Proguard, add the following line to your configuration
```
    -keep class com.usabilla.sdk.ubform.data.** { *; }
```

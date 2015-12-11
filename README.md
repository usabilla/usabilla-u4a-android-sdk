 [ ![Download](https://api.bintray.com/packages/usabilla/maven/ubform/images/download.svg) ](https://bintray.com/usabilla/maven/ubform/_latestVersion)

# Usabilla FeedbackSDK for Android
This repository contains the FeedbackSDK for Android.

# WARNING
Version 2.0.0+ is incompatible with the implementation of version 1.*
Please check MainActivity.java to see how the SDK could be used.

**Starting from version 2.0.3 the SDK uses AppCompatActivity declared in appcompat-v7:23+. Using a version of appcompat-v7 lower that 23 will cause the SDK to crash**

## Manual instructions
Download the packaged `.aar` library and include it in your project according to the IDE of your choice.

## Gradle instructions
The Usabilla SDK are currently served through [JCenter](https://bintray.com/usabilla/maven/ubform/view):
 - make sure `jcenter()` is included in your repositories
 - add `compile 'com.usabilla.sdk:ubform:2.0.3'` to the dependencies of your gradle build script.

## Integration instructions
 - import the SDK in your activity `import com.usabilla.sdk.ubform.UBFormClient;`
 - onCreate init the UbFormClient passing the current activity with `new UBFormClient(this)`
 - call `openFeedbackForm` passing your APP_ID to show the form

The *UbFormClient* exposes a couple of interesting methods:
 - setShakeFeedbackForm(APP_ID)
 - openFeedbackForm(APP_ID)
 - takeScreenshot()
together with `onResume` and `onPause` that needs to be called from the corresponding overrides.


## Screenshot
In order to attach a screenshot to the feedback item you can either let our SDK generate it for you by calling `client.takeScreenshot()`
or you can pass it the view you want to screenshot.

## Custom variables
You can pass along custom variables that will be attached to the feedback users send.
Currently custom variables are represented by a JSON object attached to the activity intent as extra:
```
    JSONObject customVars = new JSONObject();
    try {
        customVars.put("user", "mario");
        customVars.put("uid", 12345);
    } catch (JSONException e) {
        // OPS
    }
    openFeedbackForm(APP_ID, customVars)
```

## Shake gesture
You can activate the feedback form on shake passing your APP_ID to `setShakeFeedbackForm`.

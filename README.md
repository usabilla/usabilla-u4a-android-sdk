 [ ![Download](https://api.bintray.com/packages/usabilla/maven/ubform/images/download.svg) ](https://bintray.com/usabilla/maven/ubform/_latestVersion)

# Usabilla FeedbackSDK for Android
This repository contains the FeedbackSDK for Android.

## Manual instructions
Download the packaged `.aar` library and include it in your project according to the IDE of your choice.

## Gradle instructions
The Usabilla SDK are currently served through [JCenter](https://bintray.com/usabilla/maven/ubform/view):
 - make sure `jcenter()` is included in your repositories
 - add `compile 'com.usabilla.sdk:ubform:VERSION_NUMBER'` to the dependencies of your gradle build script.

## Integration instructions
 - import the SDK in your activity `import com.usabilla.sdk.ubform.*;`
 - Invoke a new Intent: `Intent usabillaIntent = new Intent(MyActivity.this, ubForm.class);`
 - Pass along your APP_ID obtained from the Usabilla backend `usabillaIntent.putExtra("appId", "APP_ID");`
 - Start the new activity `startActivity(usabillaIntent);`

## Screenshot
In order to attach a screenshot to the feedback item you can either use our SDK to auto generate a snapshot of the current view:
```
    ubScreenshot.getInstance().takeScreenshot(getWindow().getDecorView().getRootView());
```
Or you can simply pass a bitmap:
```
    ubScreenshot.getInstance().setScreenshotBmp(YOUR_BITMAP);
```

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
    usabillaIntent.putExtra("customVars", customVars.toString());
```

## Shake gesture
At the moment our SDK does not facilitate a "shake to give feedback" gesture. This will be available in the following version.
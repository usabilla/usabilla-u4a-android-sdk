 [ ![Download](https://api.bintray.com/packages/usabilla/maven/ubform/images/download.svg) ](https://bintray.com/usabilla/maven/ubform/_latestVersion)

# Usabilla for Apps - Android SDK
This repository contains the SDK for Android.


Take a look at our [Wiki](https://github.com/usabilla/usabilla-u4a-android-sdk/wiki) for a complete and in depth guide on how to install and customize the SDK.

## Latest changes in v3.4.4

#### Fixed
- Error when multiple ending pages were present in the form

#### Added
- Add possibility to preload a Form for off-line use


## Old SDK versions
Version 3.+ is incompatible with the implementation of version 2.*
Please check the wiki to see how to implement the SDK.

**In version < 3.0 the SDK uses AppCompatActivity declared in support:appcompat-v7:22.1+. Please make sure you are using a version of support:appcompat-v7 that contains AppCompatActivity**

# Installation

## Supported version
The Usabilla SDK required the minSdkVersion of the application to be 16 (Android 4.1).

## Gradle instructions
The Usabilla SDK are currently served through [JCenter](https://bintray.com/usabilla/maven/ubform/view):
- make sure `jcenter()` is included in your repositories
- add `compile 'com.usabilla.sdk:ubform:3.4.+'` to the dependencies of your gradle build script.

## Manual instructions
Download the packaged `.aar` library and include it in your project according to the IDE of your choice.

## How to get started
### On the web
- Create a new app on your [Usabilla](https://app.usabilla.com/member/) Live for Apps section.
- Copy th AppId from the app you wish to use in your SDK.

### In the code

* Import the SDK in your activity using `import com.usabilla.sdk.ubform.UBFormClient;`
* Implement the `UBFormInterface` in your activity or fragment
* Call `openFeedbackForm` passing your Form ID to show the form you created in the web interface.
* The SDK will return an instance of `Form`, which is a subclass of `android.support.v4.app.Fragment`
* The form needs to be placed somewhere in your layout. A good approach is to insert a `FrameLayout` in your layout and use it to host the form. It is also possible to have a dedicated activity for the form. For a practical example, see the example application in this repository.


This is an example of how your activity should look like:

```
public class MainActivity implements UBFormInterface {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UBFormClient.initClient(getApplicationContext());

        // optional preload form for off-line use
        UBFormClient.preloadFeedbackForm("FORM ID", getApplicationContext());

        // normal loading on demand
        UBFormClient.loadFeedbackForm("FORM ID", getApplicationContext(), MainActivity.this);
    }

    //This will be called when the form has finished loading and is ready to be displayed
    @Override
    public void formLoadedSuccessfully(Form form, boolean b) {
       //your code
    }

    //This will be called in case the form ID was not found in our server or there was some kind of error during the request
    //It will return a default form
    @Override
    public void formFailedLoading(Form form) {
      //your code
    }


    @Override
    public void textForMainButtonUpdated(String s) {

    }
}
```

## Preloading
If you know you will need to display a feedback form when the user is offline, you can preload it a cache it in the SDK to make it available at any given moment.
To preload a form use

`UBFormClient.preloadFeedbackForm("FORM ID", getApplicationContext());`

This will fetch the latest version of the form and cache it in the SDK.
When you will request that form, if there is no network connectivity, the SDK will use the cached version and your user will be able to submit his feedback

Feedback submitted while offline will be sent when connectivity is restored.

## Screenshot
You can decide wether or not to attach a screenshot in your form.
In order to attach a screenshot to the feedback item you can either:
* Let our SDK generate a screenshot of the currently displayed screen you by calling `client.takeScreenshot()`
* Take a screenshot of a particular view using `UBFormClient.takeScreenshot(view)`
* Specify a custom image to be used as screenshot using `UBFormClient.setCustomScreenshot(bitmap);`


## Communication with the App
The SDK communicates with the main app trough broadcasts.
Specifically, the SDK will send out these broadcasts:
- `"com.usabilla.closeForm"` when the user wants the form to be closed or to disappear
- `"com.usabilla.redirectToPlayStore"` when the user wants to open the Play Store on the app's page

To receive these broadcast use the `LocalBroadcastManager` as such   

`LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(closeButtonReceiver, new IntentFilter("com.usabilla.closeForm"));`

## Submission status

 The `"com.usabilla.closeForm"` broadcast bundles some information regarding what the user has done with the form.
 The broadcast will contain a pareled array of FeedbackResult under the `feedbackResults` key.

 A FeedbackResult object contains:
 * The `rating` left by the user (smilies or star rating). If the user hasn't set one, the default value will be 0.
 * A `sent` boolean that is true if the form has been submitted, false otherwise.
 * An `abandonedPageIndex`, containig the page the user left the form from. If the form has been submitted this value will be -1.


A possible implementation of the broadcasts receiver looks like this:

 ```
 usabillaCloser = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                FeedbackResult[] result = (FeedbackResult[]) intent.getParcelableArrayExtra("feedbackResults");
                for (FeedbackResult aResult : result)
                    Log.i("feedback results", "Rating left: " + aResult.getRating() + " form was sent: " + aResult.isSent() + " dropped out in page number: " + aResult.getAbandonedPageIndex());
            }
        };
 ```

 The broadcast bundles an array because the user might submit the form multiple time, but this broadcast will be called only when the form needs to be removed from the UI.


## External Navigation
It is possible to hide the default navigation and cancel button in the SDK and provide your own (ex. in the action bar).

To do so you must:
- set `form.hideDefaultNavigationButton(true);`  and `form.hideCancelButton(true);`
- call `form.getTextForMainButton()` to get the text for your navigation button.
- when the user presses your navigation button, call `form.navigationButtonPushed()`. This method will also return the text for the navigation button (it could change from "Next" to "Submit")



## ProGuard
If you are using Proguard, add the following line to your configuration
```
    -keep class com.usabilla.sdk.ubform.data.** { *; }
```

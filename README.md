 [ ![Download](https://api.bintray.com/packages/usabilla/maven/ubform/images/download.svg) ](https://bintray.com/usabilla/maven/ubform/_latestVersion)

# Usabilla for Apps - Android SDK
This repository contains the SDK for Android.


Take a look at our [Wiki](https://github.com/usabilla/usabilla-u4a-android-sdk/wiki) for a complete and in depth guide on how to install and customize the SDK.

## Old SDK versions
Version 3.+ is incompatible with the implementation of version 2.*
Please check the wiki to see how to implement the SDK.

**In version < 3.0 the SDK uses AppCompatActivity declared in support:appcompat-v7:22.1+. Please make sure you are using a version of support:appcompat-v7 that contains AppCompatActivity**

# Installation

## Gradle instructions
The Usabilla SDK are currently served through [JCenter](https://bintray.com/usabilla/maven/ubform/view):
- make sure `jcenter()` is included in your repositories
- add `compile 'com.usabilla.sdk:ubform:3.0'` to the dependencies of your gradle build script.

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

 
This is an example of how your activity should look like:

```
public class MainActivity implements UBFormInterface {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UBFormClient.initClient(getApplicationContext());
        UBFormClient.loadFeedbackForm("57adda5864ad10cab9875b91", getApplicationContext(), MainActivity.this);

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

### Screenshot
You can decide wether or not to attach a screenshot in your form.
In order to attach a screenshot to the feedback item you can either:
* Let our SDK generate a screenshot of the currently displayed screen you by calling `client.takeScreenshot()`
* Take a screenshot of a particular view using `UBFormClient.takeScreenshot(view)`
* Specify a custom image to be used as screenshot using `UBFormClient.setCustomScreenshot(bitmap);`

By default, the user can remove any screenshot attached to the feedback form.  
You can force the user to submit a screenshot in his feedback form by calling `UBFormClient.setIsScreenshotForced(true);`.   


## ProGuard 
If you are using Proguard, add the following line to your configuration
```
    -keep class com.usabilla.sdk.ubform.data.** { *; }
```

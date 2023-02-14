[![Download](https://maven-badges.herokuapp.com/maven-central/com.usabilla.sdk/ubform/badge.svg)](https://mvnrepository.com/artifact/com.usabilla.sdk/ubform)

# Usabilla for Apps - Android SDK
Usabilla for Apps allows you to collect feedback from your users with great ease and flexibility.

***
- [Requirements](#requirements)
  - [TLS1.2](#tls1.2)
  - [Java 8](#java-8)
- [Android API limitations](#android-api-limitations)
- [Installation](#installation)
- [Public properties](#public-properties)
  - [Custom variables](#custom-variables)
  - [Debug mode](#debug-mode)
  - [External navigation](#external-navigation-passive-feedback-only)
  - [Telemetry data submission](#telemetry-data-submission)
  - [Theme](#theme)
- [Public functions](#public-functions)
  - [Initialize](#initialize)
  - [Load a passive form](#load-a-passive-form)
  - [Preload a passive form](#preload-a-passive-form)
  - [Remove cached forms](#remove-cached-forms)
  - [Capture a screenshot](#capture-a-screenshot)
  - [Send events](#send-events)
  - [Update fragment manager](#update-fragment-manager)
  - [Reset campaigns data](#reset-campaigns-data)
  - [Dismiss form](#dismiss-form)
  - [Mask PII](#mask-pii)
  - [Set footer logo clickable](#set-footer-logo-clickable)
- [Miscellaneous](#miscellaneous)
  - [Close a passive form](#close-a-passive-form)
  - [Access form data](#access-form-data)
  - [App review on the PlayStore](#app-review-on-the-playstore)
  - [Custom http client](#custom-http-client)
  - [Telemetry](#telemetry)
  - [Localization](#localization)
  - [Accessibility](#accessibility)
  - [Devices with notch](#devices-with-notch)
***

## Requirements

- Android API: Minimum 19 - Target 30
- Use of AndroidX support libraries
- Use of TLS1.2 protocol for network connections (automatically enabled for Android API >= 21)
- Project targeting Java8 in the `build.gradle` `compileOptions`

### TLS1.2

If your app supports Android API 19, in order to enable TLS1.2 on those devices you need to update your security provider as follows

Include the following dependency in your `build.gradle` file

```
implementation `com.google.android.gms:play-services-safetynet:17.0.0`
```

Call the following function in your app before any action with the Usabilla SDK

```kotlin
fun upgradeSecurityProvider(context: Context) {
    ProviderInstaller.installIfNeededAsync(context, object : ProviderInstallListener {
        override fun onProviderInstalled() {
            // You are good to go
        }

        override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent) {
            // Something went wrong. For more details please refer to https://developer.android.com/training/articles/security-gms-provider
        }
    })
}
```

### Java 8

Our SDK targets Java8 and uses components available from Android API 26, therefore if your app targets previous Android versions please do enable desugaring support as explained in the [official Google guidelines](https://developer.android.com/studio/write/java8-support.html)

## Android API limitations
The following functionalities will only be available on phones running Android API >= 21

- The cursor on text fields is tinted with the accent color
- The progress bar at the top of the form is tinted with the accent color
- The star component allow custom drawables to be applied to it

## Installation
Grab the latest version using

```
implementation 'com.usabilla.sdk:ubform:7.6.5'
```

If you have obfuscation enabled (ProGuard/R8) and you use a version of our SDK <= 6.4.0 you need to add this line to your obfuscation configuration
```
-keep public class com.usabilla.sdk.ubform.eventengine.TargetingOptionsModel
```

## Public properties

### Custom variables
Custom variables are represented by a non mutable Map of objects and are attached to each feedback sent

```kotlin
 Usabilla.customVariables = mapOf(
    Pair("tier", "premium"),
    Pair("loggedIn", true)
  )
 ```

There are a few limitations to the kind of objects you can add to the custom variables

* Custom objects need to override the `toString()` function.
* Arrays are not allowed.
* The name (key) of each custom variable must not be `blank` and it must not contain `.` or `$`.

⚠️ **Custom variables can be used as targeting options for campaigns, as long as their value is a `String`.**

### Debug mode
Local logging (disabled by default) can be enabled using

```kotlin
Usabilla.debugEnabled = true
```

### External navigation (**passive feedback only**)

It is possible to hide the default navigation buttons our forms use and provide your own (e.g. in the Toolbar).

To do so a couple of steps are required:
* Set the standard navigation buttons invisible

    ```kotlin
    Usabilla.setDefaultNavigationButtonsVisibility(false)
    ```

* From your custom trigger call the function `navigationButtonPushed` on the form fragment to continue.

    ```kotlin
    myButton.setOnClickListener { formFragment.navigationButtonPushed() }
    ```

### Telemetry data submission
Telemetry data submission (enabled by default) can be disabled using

```kotlin
Usabilla.submitTelemetryData = false
```

### Theme
A custom theme can be applied to both passive forms and campaign forms using

```kotlin
Usabilla.theme = UsabillaTheme(UbFonts(), UbImages())
```

## Public functions

### Initialize
The first function called on the Usabilla SDK should be `initialize`. 
Failure to call `initialize` before any other public function in the SDK can cause erratic behaviour.

```kotlin
Usabilla.initialize(context: Context, appId: String?, httpClient: UsabillaHttpClient?, callback: UsabillaReadyCallback?)
```

### Load a passive form
Passive feedback form are loaded using

```kotlin
Usabilla.loadFeedbackForm(formId: String, screenshot: Bitmap?, theme: UsabillaTheme? , callback: UsabillaFormCallback?)
```

### Preload a passive form
Preloading the form will fetch and store it locally. It can be then shown by calling **loadFeedbackForm** with the preloaded formId.

```kotlin
Usabilla.preloadFeedbackForms(formIds: List<String>)
```

### Remove cached forms
Preloaded forms can be removed from cache using

```kotlin
Usabilla.removeCachedForms()
```

### Capture a screenshot
We offer two utility functions to capture a screenshot that can then be added to the passive form load request

``` kotlin
val myScreenshot = Usabilla.takeScreenshot(view: View)
val myScreenshot = Usabilla.takeScreenshot(activity: Activity)
```

### Send events
Campaigns are triggered by events sent using

```kotlin
Usabilla.sendEvent(context: Context, event: String)
```

⚠️ **A campaign will only be triggered once for the same user.**

### Update fragment manager
To show campaigns it's necessary to provide a reference to the FragmentManager using

```kotlin
Usabilla.updateFragmentManager(fragmentManager: FragmentManager)
```

⚠️ **When the fragmentManager is updated from inside a child-fragment the right instance to pass is `requireActivity().supportFragmentManager`.**

### Reset campaigns data
Campaign data stored locally can be removed (and fetched again, effectively losing any trace whether they already triggered or not) using

```kotlin
Usabilla.resetCampaignData(context: Context, callback: UsabillaReadyCallback?)
```

### Dismiss form
Forms showing on screen can be programmatically dismissed using

```kotlin
Usabilla.dismiss(context: Context)
```

Campaigns are dismissed directly by the SDK, whereas passive forms assume the proper broadcast receiver is implemented.

### Mask PII
PII (Personally Identifiable Information) present in all input text fields can be masked (on submission) using

```kotlin
Usabilla.setDataMasking(masks: List<String>, maskCharacter: Char)
```

⚠️ **The email field does not apply masking since it explicitly asks for a sensitive data.**

### Set footer logo clickable
Setting whether the footer logo is clickable or not can be done using

``` kotlin
Usabilla.setFooterLogoClickable(clickable: Boolean)
```

## Miscellaneous

### Back button intercept
Our forms (campaign banner included) intercepts the phone's back button clicks and remove themselves from the screen

### Access form data
You can optionally register broadcast receivers with any of the following filters

```kotlin
IntentFilter(UbConstants.INTENT_CLOSE_FORM)
IntentFilter(UbConstants.INTENT_CLOSE_CAMPAIGN)
IntentFilter(UbConstants.INTENT_ENTRIES)
```

they will trigger respectively when either a campaign or a passive form is closed/dismissed and in their `onReceive` function you can get access to form data (FeedbackResult) or to the String version of all fields present in the form (with their value)

```kotlin
private val usabillaGeneralReceiver: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val passiveResult: FeedbackResult? = intent.getParcelableExtra(FeedbackResult.INTENT_FEEDBACK_RESULT)
        val campaignResult: FeedbackResult? = intent.getParcelableExtra(FeedbackResult.INTENT_FEEDBACK_RESULT_CAMPAIGN)
        // String version of a map `fieldId` -> `fieldValue` covering all form components
        val entries: String? = intent.getStringExtra(FeedbackResult.INTENT_ENTRIES)
    }
}
```

### App review on the PlayStore
When a form is given a 4 or 5 rating in the mood/star component, once closed the official [In-App review API](https://developer.android.com/guide/playcore/in-app-review) prompt to rate the app on the PlayStore will be presented.

This will show only on devices that have the Play Store installed.

For information regarding testing/checking this feature, please refer to the [official guidelines](https://developer.android.com/guide/playcore/in-app-review/test).

### Custom http client
We enable the injection of a custom http client in the `initialize` function to handle all the network connections in our SDK.

A sample http client implementation can be seen in the classes `CustomHttpClient.java` or `CustomHttpClient.kt`

### Telemetry

The SDK collects diagnostic data to improve the performance and optimise usage on the variety of devices it can be run on.

⚠️ **The information collected is not used to identify users, does not contain PII and it's not shared with external parties**

Adding the SDK to your project will not send us any information; information is sent only in the following cases:
- Call to `initialise()` completes
- Call to `loadFeedbackForm()` completes
- Call to `sendEvent()` completes
- Exiting a feedback form

The data collected content is as follows:

```json
{
  "appVersion": "1.0.0",
  "appName": "AppName",
  "device": "Android SDK built for x86",
  "freeMemory": "831172",
  "freeSpace": "582800",
  "orientation": "Portrait",
  "osVersion": "8.1.0",
  "reachability": "WiFi",
  "rooted": false,
  "screenSize": "1440x2392",
  "sdkVersion": "7.6.5",
  "system": "android",
  "totalMemory": "1530604",
  "totalSpace": "793488",
  "id": "ms since Unix Epoch",
  "timestamp": "human readable date",
  "originClass": "com.usabilla.Usabilla",
  "action": {
    "duration": "102",
    "errorCode": "0",
    "errorMessage": "",
    "name": "function or property invoked on our public interface"
    ...
    function parameters are also collected here
    ...
  }
}
```

### Localization
To provide your own translation of some of the strings our SDK uses just overwrite them in your `strings.xml` file

```xml
<string name="ub_field_error">Please check this field</string>
<string name="ub_element_screenshot_title">Screenshot (optional)</string>
<string name="ub_element_screenshot_message">Add an image</string>
<string name="ub_button_close_default">Close</string>
<string name="ub_button_continue_default">Continue</string>
<string name="ub_button_playStore_default">Rate on the play store</string>
<string name="ub_button_submit_default">Submit</string>
<string name="ub_dialog_playStore_title">Rate</string>
<string name="ub_dialog_playStore_message">Thank you for your feedback! Would you like to leave a review?</string>
<string name="ub_dialog_playStore_negative">No, thanks</string>
<string name="ub_dialog_playStore_positive">Rate now</string>
<string name="ub_sdk_permission_disabled_label">Permission disabled!\nEnable it from Settings -> app info</string>

// Accessibility labels
<string name="ub_element_mood_select_rating">Select a rating out of %1$d</string>
<string name="ub_element_mood_adjust_instructions">Swipe up or swipe down to adjust</string>
<string name="ub_element_slider_select_rating">Select a rating from %1$d (%2$s) to %3$d (%4$s)</string>
<string name="ub_element_required">This field is required</string>
<string name="ub_element_screenshot_delete">Delete Screenshot</string>
<string name="ub_element_screenshot_edit">Change Screenshot</string>
<string name="ub_element_mood_hate">I really don\'t like it!</string>
<string name="ub_element_mood_dislike">I don\'t like it</string>
<string name="ub_element_mood_neutral">I feel neutral</string>
<string name="ub_element_mood_like">I like it!</string>
<string name="ub_element_mood_love">I love it!</string>
<string name="ub_element_mood_one_star">One star</string>
<string name="ub_element_mood_two_star">Two stars</string>
<string name="ub_element_mood_three_star">Three stars</string>
<string name="ub_element_mood_four_star">Four stars</string>
<string name="ub_element_mood_five_star">Five stars</string>
<string name="ub_usabilla_logo">Powered by Usabilla</string>

<string name="ub_take_picture">Take Picture</string>
<string name="ub_screenshot_preview">Screenshot Preview</string>
<string name="ub_menu_add">Add</string>
<string name="ub_menu_undo">Undo</string>
<string name="ub_menu_done">Done</string>

<string name="ub_camera_access_denied">No access to camera</string>
<string name="ub_camera_access_denied_details">Allowing access lets you take photos to add to your feedback.</string>
<string name="ub_camera_access_allow">Allow access to camera</string>

<string name="ub_edit_title">Edit</string>
```

### Accessibility
For Android TalkBack to work properly with our passive feedback form you have to make sure that the `Activity` (or `Fragment`) holding it is not considered for TalkBack.

This can be achieved using

```kotlin
view.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
```
where `view` is the `ViewGroup` you want to exclude from the TalkBack.

When the form is dismissed then you can set it back using

```kotlin
view.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
```

### Devices with notch
There might be some display issues in devices with a notch, especially if no action bar theme is used. 

This can be handled for devices running Android 9 (API Level 28) and above following [google guidelines](https://developer.android.com/guide/topics/display-cutout).

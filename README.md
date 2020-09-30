[![Download](https://api.bintray.com/packages/usabilla/maven/ubform/images/download.svg)](https://bintray.com/usabilla/maven/ubform/_latestVersion)

# Usabilla for Apps - Android SDK
Usabilla for Apps allows you to collect feedback from your users with great ease and flexibility.

***
- [Requirements](#requirements)
- [Permissions](#permissions)
- [Android API limitations](#android-api-limitations)
- [Installation](#installation)
- [Public properties](#public-properties)
  - [Custom variables](#custom-variables)
  - [Debug mode](#debug-mode)
  - [External navigation](#external-navigation)
  - [Theme](#theme)
    - [Custom fonts](#custom-fonts)
    - [Custom images](#custom-images)
- [Public functions](#public-functions)
  - [Initialize](#initialise)
  - [Load a passive form](#load-a-passive-form)
  - [Preload a passive form](#preload-a-passive-form)
  - [Remove cached forms](#remove-cached-forms)
  - [Capture a screenshot](#capture-a-screenshot)
  - [Send events](#send-events)
  - [Update fragment manager](#update-fragment-manager)
  - [Reset campaigns data](#reset-campaigns-data)
  - [Dismiss form](#dismiss-form)
  - [Mask PII](#mask-pii)
- [Miscellaneous](#miscellaneous)
  - [Close a form](#close-a-form)
  - [Feedback result](#feedback-result)
  - [App review on the PlayStore](#app-review-on-the-playstore)
  - [Custom http client](#custom-http-client)
  - [Localization](#localization)
  - [Accessibility](#accessibility)
***

## Requirements

- Minimum Android API 19 (Android 4.4) 
- Use of AndroidX support libraries
- The use of TLS1.2 protocol for network connections (automatically enabled for Android API >= 21)

If your app supports API 19, in order to enable TLS1.2 you need to update your security provider as follows

Include the following dependency in your `build.gradle` file

```
implementation `com.google.android.gms:play-services-safetynet:17.0.0`
```

Call the following method in your app before any action with the Usabilla SDK

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

## Permissions
The SDK uses the permissions `READ_EXTERNAL_STORAGE` and `CAMERA`.

They are only needed during the flow to attach a new screenshot to a passive form.

Images selected this way allow the user to annotate them directly on the phone with a pen/pencil tool.

## Android API limitations
The following functionalities will only be available on phones running a version of Android equal to or higher than API 21

- The cursor on text fields is tinted with the accent color
- The progress bar at the top of the form is tinted with the accent color
- The star component allow custom drawables to be applied to it

## Installation
Grab the latest version via Gradle:

```
implementation 'com.usabilla.sdk:ubform:7.0.7'
```

or Maven:

```
<dependency>
  <groupId>com.usabilla.sdk</groupId>
  <artifactId>ubform</artifactId>
  <version>7.0.7</version>
  <type>pom</type>
</dependency>
```

If you have ProGuard enabled you will have to add this line to your ProGuard configuration
```
-keep public class com.usabilla.sdk.ubform.eventengine.TargetingOptionsModel
```

## Public properties

### Custom variables
Custom variables are represented by a Map containing key-value pairs that are attached to the feedback users will send (both from a passive feedback or a campaign)

⚠️ **Setting the custom variables overwrites the previously present ones**

```kotlin
// Setting a whole object
 val myVariables = hashMapOf(
    Pair("tier", "premium"),
    Pair("loggedIn", true)
  )
 Usabilla.customVariables = myVariables

 // Modifying the object
 Usabilla.customVariables["tier"] = "platinum"
 ```

There are a few limitations to the kind of objects you can add to the custom variables

* Custom objects need to override the `toString()` method to be able to read them from the Usabilla dashboard.
* Arrays of objects are not accepted and will be removed by our servers.
* Custom variables are translated into a `JSONObject` during submission, so any non parseable value will break their submission.

⚠️ **Custom variables can be used as targeting options for campaigns, as long as the `value` is a `String`.**

### Debug mode
Local logging (disabled by default) can be enabled using

```kotlin
Usabilla.debugEnabled = true
```

### External navigation

⚠️ **Passive feedback only!**

It is possible to hide the default navigation buttons our forms use from the SDK and provide your own (e.g. in the Toolbar).

To do so a couple of steps are required:
* Set the Usabilla standard navigation buttons invisible
    
    ```kotlin
    Usabilla.setDefaultNavigationButtonsVisibility(false)
    ```

* Call the method `navigationButtonPushed` on the fragment representing the form you received from the SDK in the `onClickListener` of your custom button (e.g. the one you have placed in the Toolbar). This will tell the form to proceed with a 'continue' action, you should provide handling for the cancellation of the form yourself.
    
    ```kotlin
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.page_content -> formClient.navigationButtonPushed()
        }
        return true
    }
    ```

* (OPTIONAL) Use the `mainButtonTextUpdated` method present in the callback provided when loading a passive form to grab the text for the navigation button you want to use
    ```kotlin
    override fun mainButtonTextUpdated(text: String) {
        // Use this text for your own navigation button.
        // Usually returns "Next" or "Submit".
    }
    ```

### Theme
Setting a general theme will apply it to both passive forms and campaign forms, and can be done as follows

```kotlin
Usabilla.theme = UsabillaTheme(UbFonts(), UbImages())
```

**Both parameters are nullable, and when not provided will use the SDK default values**
- `UbFonts`:
  - text and title size 18sp
  - note text size 16dp
  - boldness enabled
- `UbImages`:
  - default mood and stars as seen in the Usabilla dashboard

#### Custom fonts
`UbFonts` can be instantiated with five different parameters:
- `regular` resourceId for the font to be applied to the feedback form.
- `bold` enables text to be shown bold (for titles for example).
- `titleSize` size in `sp` for the titles in the form
- `textSize` size in `sp` for the text in the form
- `miniSize` size in `sp` for the small text in the form (e.g. NPS text, Error labels or top page required field note)

⚠️ The font resource needs to be placed in the `res/font` folder of the project and be in format `.otf` or `.ttf`

#### Custom images
`UbImages` can be instantiated with four different parameters:
- `selectedEmoticons` list of resourceIds of images to replace selected emoticons in the Mood component.
- `unselectedEmoticons` list of resourceIds of images to replace unselected emoticons in the Mood component.
- `star` resourceId of the image to be used in place of the default star for the Mood component (when in star version).
- `starOutline` resource ID of the image to be used in place of the default star outline for the Mood component (when in star version).

To use custom emoticons you must provide two lists of **five image IDs**  (selected and unselected) to be used instead of the Usabilla default emoticons.

Passing only a valid list for the seelcted emoticons will show them emoticons with an alpha value of 0.5 when unselected.

In order to display the Star Rating in your form instead of the Mood, you must switch it in the [Usabilla Web Interface](https://app.usabilla.com/member/apps/).

## Public functions

### Initialize
The first method called on the Usabilla SDK should be `initialize`. Failure to call `initialise` first before any other public method in the SDK will prevent the SDK from running properly.

```kotlin
Usabilla.initialize(context: Context, appId: String?, httpClient: UsabillaHttpClient?, callback: UsabillaReadyCallback?)
```

Optional parameters
- `appId` Id to enable the Campaign feature (please refer to the [Campaigns](#campaigns) section).
- `httpClient` Custom client to handle all network connections performed by the SDK (please refer to the [Custom Http Client](#custom-http-client) section).
- `callback` Callback used to be notified when the initialization process ends. When using campaigns it will indicate that the SDK is ready to receive events.

This method takes care of
- Initialising the internals of our SDK.
- Submitting any pending feedback items.
- Fetching and updating all campaigns associated with the provided appId.

### Load a passive form
To load a passive feedback form you should use

```kotlin
Usabilla.loadFeedbackForm(formId: String, screenshot: Bitmap?, theme: UsabillaTheme? , callback: UsabillaFormCallback?)
```

Optional parameters
- `screenshot` Bitmap that will be attached to the feedback form.
- `theme` Theme that will be applied solely to the passive form here requested.
- `callback` Callback used to take actions when the loading ends to be able to show the form.

### Preload a passive form
Preloading the form will fetch and store it locally. In order to show it to the user the **loadFeedbackForm** method needs to be called specifying a preloaded formId

```kotlin
Usabilla.preloadFeedbackForms(formIds: List<String>)
```

### Remove cached forms
Preloaded forms can be removed from cache using

```kotlin
Usabilla.removeCachedForms()
```

### Capture a screenshot
We offer two methods to capture a Bitmap that can be then added to the passive form

``` kotlin
// Returns a bitmap showing the View
val myScreenshot = Usabilla.takeScreenshot(view: View)

// Returns a bitmap showing the full screen
val myScreenshot = Usabilla.takeScreenshot(activity: Activity)
```

### Send events
Campaigns, our proactive surveys targeted to a specific set of users, are triggered by events and events are sent using

```kotlin
Usabilla.sendEvent(context: Context, event: String)
```

⚠️ **A campaign will never be triggered more than once for the same user.**

⚠️ **Custom variables can be used to specify some traits of the user and target the campaign only to a specific subset.**

### Update fragment manager
Campaigns are Fragments, therefore to properly display it's important that you provide a reference to the latest FragmentManager using

```kotlin
Usabilla.updateFragmentManager(fragmentManager: FragmentManager)
```

### Reset campaigns data
Campaign data stored locally can be removed using

```kotlin
Usabilla.resetCampaignData(val context: Context, val callback: UsabillaReadyCallback?)
```

The method removes all campaigns stored locally and fetches them again from our remote API, effectively losing any trace whether they already triggered or not.
The optional parameter `callback` is used to communicate when the fetching of the campaigns has ended and the campaign events can start being processed by the Usabilla SDK.

## Dismiss form
Forms showing n screen can be programmatically dismissed using

```kotlin
Usabilla.dismiss(context: Context)
```

Campaigns are dismissed directly by the SDK, whereas passive forms assume the proper broadcast receiver is implemented.

## Mask PII
PII (Private Identifiable Information) present in input text fields can be masked (on the back-end side) using

```kotlin
Usabilla.setDataMasking(masks: List<String>, maskCharacter: Char)
```

with parameters
 - `masks` List of RegExes to mask data in input fields.
 - `maskCharacter` Caracter to replace the matched RegExes.

default values for the masks are:
- Email Addresses
  ```regexp
  [a-zA-Z0-9\+\.\_\%\-\+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+
  ```
- Numbers 4 or more digits long
  ```regexp
  [0-9]{4,}
  ```

default value for the maskCharacter is `X`

⚠️ **The email field does not apply masking since it explicitely asks for a sensitive data.**

## Miscellaneous

### Close a form
In order to know when to remove a passive form you need to implement a broadcast receiver listening for its closing event

Moreover, inside the `onReceive` method of the receiver it's possible to obtain a `FeebackResult` object containing some information about the form such as
- The **rating**, set as soon as the user interacts with the mood/star component.
- The **abandonedPageIndex**, set only if the user cancels the form before submission, otherwise -1.
- The **isSent** property, telling whether the feedback item was sent or not.

```kotlin
private val usabillaReceiverClosePassive: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // The passive feedback form needs to be closed and the feedback result is returned
        val res: FeedbackResult? = intent.getParcelableExtra(FeedbackResult.INTENT_FEEDBACK_RESULT)
    }
}

private val usabillaReceiverCloseCampaign: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // The campaign feedback form has been closed and the feedback result is returned
        val res: FeedbackResult? = intent.getParcelableExtra(FeedbackResult.INTENT_FEEDBACK_RESULT_CAMPAIGN)
    }
}

override fun onStart() {
    super.onStart()
    LocalBroadcastManager.getInstance(this).registerReceiver(usabillaReceiverClosePassive, IntentFilter(UbConstants.INTENT_CLOSE_FORM))
    LocalBroadcastManager.getInstance(this).registerReceiver(usabillaReceiverCloseCampaign, IntentFilter(UbConstants.INTENT_CLOSE_CAMPAIGN))
}

override fun onStop() {
    super.onStop()
    LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaReceiverClosePassive)
    LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaReceiverCloseCampaign)
}
```

### App review on the PlayStore
If the form has the "Show rating prompt after feedback submit" setting enabled (found in the form-creation dashboard under `Advanced settings`) and the mood/star rating given by the user is 4 or 5 then the official <a href="https://developer.android.com/guide/playcore/in-app-review" target="_blank">In-App review API</a> prompt to rate the app on the PlayStore will be presented.

This will show after the form is closed (dismissed or finished) only on devices that have the Play Store installed (i.e. physical devices).

For information regarding testing/checking this feature, please refer to the <a href="https://developer.android.com/guide/playcore/in-app-review/test" target="_blank">official guidelines</a>

### Custom http client
We allow to specify a parameter in the `initialize` method to introduce a custom http client to handle all the connections to retrieve and send data through our SDK.

A sample http client implementation can be seen in the classes `CustomHttpClient.java` or `CustomHttpClient.kt`

### Localization
To provide your own translationof some of the strings our SDK uses just overwrite them in your `strings.xml` file

```xml
<string name="ub_field_error">Please check this field</string>
<string name="ub_element_screenshot_title">Screenshot (optional)</string>
<string name="ub_element_screenshot_message">Add screenshot</string>
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
To have Android TalkBack work properly with the passive feedback form, you have to make sure that the `Activity` (or `Fragment`) holding the `FormClient.fragment` received from our SDK stops being considered for TalkBack.

This can be achieved using
```kotlin
view.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
```
where `view` is the `ViewGroup` you want to exclude from the TalkBack.

Of course when the form is dismissed then you can reset the TalkBack
```kotlin
view.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
```

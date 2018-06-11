[![Download](https://api.bintray.com/packages/usabilla/maven/ubform/images/download.svg)](https://bintray.com/usabilla/maven/ubform/_latestVersion)

# Usabilla for Apps - Android SDK
Usabilla for Apps allows you to collect feedback from your users with great ease and flexibility.

The new Usabilla SDK Version 4 comes with two major advancements:
1. The new feature [Actively targeted surveys](#campaigns) (referred to as **Campaigns** in this document).
2. A more stabilised [Passive feedback forms](#passive-feedback).

* * *

- [Usabilla for Apps - Android SDK](#usabilla-for-apps---android-sdk)
    - [Requirements](#requirements)
    - [Installation](#installation)
    - [Initialization](#initialization)
        - [Debug Mode](#debug-mode)
    - [Campaigns](#campaigns)
        - [The App Id](#the-app-id)
        - [Targeting options](#targeting-options)
        - [Managing an existing campaign](#managing-an-existing-campaign)
        - [Campaign results](#campaign-results)
        - [Update fragment manager](#update-fragment-manager)
        - [Reset campaigns data](#reset-campaigns-data)
    - [Passive feedback](#passive-feedback)
        - [Loading a form](#loading-a-form)
        - [Preloading a form](#preloading-a-form)
        - [Adding a screenshot](#adding-a-screenshot)
        - [Feedback submission callback](#feedback-submission-callback)
            - [Feedback Result](#feedback-result)
    - [Play Store link](#play-store-link)
        - [Reset passive forms](#reset-passive-forms)
    - [Custom http client](#custom-http-client)
    - [Custom variables](#custom-variables)
            - [Limitations](#limitations)
    - [UI Customisations](#ui-customisations)
        - [Custom Emoticons Rating](#custom-emoticons-rating)
            - [Provide only the selected version](#provide-only-the-selected-version)
            - [Provide both the selected and unselected version](#provide-both-the-selected-and-unselected-version)
        - [Custom Star Rating](#custom-star-rating)
        - [Custom Fonts](#custom-fonts)
        - [Custom colors](#custom-colors)
        - [Apply different themes to different passive forms](#apply-different-themes-to-different-passive-forms)
    - [Localization](#localization)
    - [External Navigation](#external-navigation)
    - [Permissions](#permissions)
    - [Proguard](#proguard)

* * *

## Requirements
- The Usabilla SDK requires the minSdkVersion of the application to be 16 (Android 4.1).

## Installation
- You can find the latest version of our SDK [here](https://bintray.com/usabilla/maven/ubform) and add it as a Maven or a Gradle dependency (`implementation 'com.usabilla.sdk:ubform:5.1.0'`).
- If you don't want to use a dependency manager you can also import the .aar library independently.
Our SDK uses the following dependencies. If your project doesn't use them already you might need to add it as well in your gradle file.
```
dependencies {
    compile 'com.android.volley:volley:1.1.0'
    compile 'com.android.support:appcompat-v7:27.1.0'
    compile "org.jetbrains.kotlin:kotlin-stdlib-jre7:1.2.31"
}
```

## Initialization
The first thing you need to do is to get an instance of our SDK with the command:

```java
final Usabilla usabilla = Usabilla.Companion.getInstance(context);
```

or in Kotlin

```kotlin
val usabilla = Usabilla.getInstance(context)
```

On that instance you should then proceed to initialise it using the **initialize** method in one of its four flavours:

```java
usabilla.initialize(Context context);
usabilla.initialize(Context context, @Nullable String appId);
usabilla.initialize(Context context, @Nullable String appId, @Nullable UsabillaHttpClient httpClient);
usabilla.initialize(Context context, @Nullable String appId, @Nullable UsabillaHttpClient httpClient, @Nullable UsabillaReadyCallback callback);
```

The method **initialize** has three optional parameters:
- `appId` needs to be used if you want to use the Campaign feature (please read the [Campaigns](#campaigns) section for more information).
- `httpClient` gives the possibility to inject a custom client to handle all the connections performed by the SDK (please read the [Custom Http Client](#custom-http-client) section for more information).
- `callback` is a callback used to communicate when the initialization process ends.

If you are using Campaigns, the callback will indicate that the SDK is ready to receive events.

The **initialize** method will take care of:
* Submitting any pending feedback items.
* Fetching and updating all campaigns associated with the appId.
* Initializing a few background processes of the SDK.

>⚠️ **Failure to call this method before using the SDK will prevent it from running properly.**

### Debug Mode
In order to obtain more insights form the SDK while developing, logging can be enabled with:
```java
usabilla.setDebugEnabled(true);
```

This property is by default set to `false`.

## Campaigns
This guide describes the Campaigns feature and all the steps necessary to work with it.

In the Usabilla for Apps Platform, a campaign is defined as a proactive survey targeted to a specific set of users.

Running Campaigns in your mobile app is great because it allows you to collect very specific insights from a targeted set of users. Furthermore, creating new Campaigns and managing existing ones can be done without the need for a new app release. Everything can be managed from the Usabilla web interface.

You can run as many campaigns as you like and target them to be triggered when a specific set of targeting options are met.
The configuration of how a campaign is displayed to the user will be familiar to existing Usabilla customers. You can configure it to suit your needs just like you are used to from the Passive feedback forms.

The most important aspect of running a mobile campaign is the presence of 'Events'. Events are custom triggers that are configured in the SDK. When a pre-defined event occurs, it will allow you to trigger a campaign. A good example of an event is a successful purchase by a user in your app.

### The App Id
The appId is an identifier used to associate campaigns to a mobile app. It is generated by Usabilla and can be found in the Usabilla web interface.    
By loading the SDK with a specific appId, it will fetch all the campaigns connected to it.

We recommend you to use a separate AppId per app. One Campaign can be targeted to multiple apps (e.g. Android production app, Android beta app) by associating it with multiple appIds

### Targeting options
Campaigns are triggered by events. Events are used to communicate with the SDK when something happens in your app. Consequently, the SDK will react to an event depending on the configuration of the Usabilla web interface.
To send an event to the SDK, use :
```java
usabilla.sendEvent(Context context, String event);
```

You can also segment your user base using custom variables.
**Custom variables** can be used to specify some traits of the user and target the campaign only to a specific subset.

For more on how to use custom variables, read [Custom Variables](#custom-variables)

**Note: A campaign will never be triggered for the same user more than once.**

### Managing an existing campaign
You can start collecting campaign results right after you create a new campaign in the Usabilla for Apps [Campaign Editor](https://app.usabilla.com/member/live/apps/campaigns/add).
By default, new campaigns are marked as inactive. On the Usabilla for Apps [Campaign Overview](https://app.usabilla.com/member/#/apps/campaigns/overview/) page, you can activate or deactivate an existing campaign at any moment to reflect your specific needs.

Moreover, you can update the content of your campaign (e.g. questions) at any time. Keep in mind that the changes you make to an existing active campaign might affect the integrity of the data you collect (different responses before and after a change).

Furthermore, you can also change the targeting options of a campaign. Keep in mind that updating the targeting options of an active campaign will **reset** any progression previously made on the user's device.

### Campaign results
Aggregated campaign results are available for download from the [Campaign Overview](https://app.usabilla.com/member/#/apps/campaigns/overview/). Here you can download the results per campaign, in the CSV format.

Campaign results will contain the answers that your users provided. Responses from a campaign are collected and sent to Usabilla page by page. This means that even if a user decides to abandon the campaign halfway through, you will still collect valuable insights. When a user continues to the next page, the results of the previous page are submitted to Usabilla. Besides campaign results showing the answers to your campaign questions, you will be able to view the device metadata and custom variables.

As for campaign results. Please note that editing the form of an existing campaign will affect the aggregated campaign results:

- Adding new questions to a form will add additional columns to the CSV file.
- Removing questions from an existing form will not affect the previously collected results. The associated column and its data will still be in the CSV file.
- Replacing the question type with a different question is also possible. When you give the same 'name' in the Usabilla for Apps Campaign Editor, the results are represented in the same column.

### Update fragment manager
⚠️ It's important to note that our campaigns use Android Fragments in order to be displayed to the user; therefore it's important that you provide a reference to the FragmentManager using :

```java
usabilla.updateFragmentManager(FragmentManager fragmentManager);
```

Furthermore, remember to handle properly the device rotation and other cases where a new FragmentManager is created, in order to always provide the latest one to the SDK.

### Reset campaigns data
The Usabilla SDK offers the possibility to reset the campaign data stored locally using the **resetCampaignData** method in one of its two flavours:
```java
usabilla.resetCampaignData(Context context);
usabilla.resetCampaignData(Context context, @Nullable UsabillaReadyCallback callback);
```
The method removes all campaigns stored locally and fetches them again from our remote API, effectively losing any trace whether they triggered or not.
The optional parameter `callback` is used to communicate when the fetching of the campaigns has ended and the campaign events can start being processed by the Usabilla SDK.

## Passive feedback
Passive feedback are feedback forms that are not triggered by events.
They are mostly, but not necessarily, initiated directly by the user.

### Loading a form
The SDK uses the Form ID you get from [Usabilla](https://app.usabilla.com/member/apps/list) after creating a new form, to fetch and display the form inside your app.

A basic implementation of the SDK would be the following:

```java
public class MainActivity extends AppCompatActivity implements UsabillaFormCallback {

    public void loadForm() {
        // After initialising our SDK
        // The null parameters are for the screenshot and theme, both explained in their own chapters
        Usabilla.loadFeedbackForm(this, "the form id to load", null, null, this);
    }

    @Override
    public void formLoadSuccess(FormClient form) {
        if (form.getFragment() != null) {
            getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.main_activity_frame, form.getFragment(), FRAGMENT_TAG)
              .commit();
        }
    }

    @Override
    public void formLoadFail() {
        // The form did not load (internet connection, invalid formId...)
    }

    @Override
    public void mainButtonTextUpdated(String text) {
        // See section External Navigation
    }
}
```

### Preloading a form
Our SDK offers the possibility to preload forms. The form will be fetched and stored locally. This could be useful in case the user is offline and the form is requested.

```java
usabilla.preloadFeedbackForms(Context context, List<String> formIds);
```

### Adding a screenshot
It is possible to attach a screenshot to a feedback form.

You can take a screenshot at any moment by calling

``` java
// Passing a View to take the screenshot and attach it the form
Bitmap myScreenshot = usabilla.takeScreenshot(View view);

// Passing an Activity to take the screenshot and attach it the form
Bitmap myScreenshot = usabilla.takeScreenshot(Activity activity);
```

These methods will return a bitmap containing a screenshot of the view or activity you have passed.

You can then attach this image, or any other bitmap of your choosing, to the feedback form you're loading by specifying the screenshot parameter in the `loadFeedbackForm` method.

```java
Bitmap myScreenshot = usabilla.takeScreenshot(this);
usabilla.loadFeedbackForm(this, "formId", myScreenshot, this);
```

### Feedback submission callback
It is possible to know when the passive feedback form has been closed
using the following BroadcastReceiver.

```java
private BroadcastReceiver usabillaCloser;
private IntentFilter closerFilter = new IntentFilter("com.usabilla.closeForm");


@Override
protected void onStart() {
    super.onStart();
    LocalBroadcastManager.getInstance(this).registerReceiver(usabillaCloser, closerFilter);
}

@Override
protected void onStop() {
    super.onStop();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(usabillaCloser);
}

private void setupCloserBroadcastReceiver() {
    usabillaCloser = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // The feedback form has been closed
            // And following result has been returned                
            FeedbackResult result = (FeedbackResult) intent.getParcelableArrayExtra(FeedbackResult.INTENT_FEEDBACK_RESULT);
        }
    };
}
```

#### Feedback Result
```java
public class FeedbackResult {
    int rating;
    int abandonedPageIndex;
    boolean isSent;
}
```

The **rating** value is set as soon as the user interacts with it and will be reported even if the form is not submitted.

The **abandonedPageIndex** property is only set if the user cancels the form before submission.

The **isSent** property is false if the user dropped out while filling the form.

## Play Store link
Enable "App Store redirect" in the Advanced panel of the feedback form editor in order to redirect the user to the Play Store after leaving a positive feedback item.

This will show a button "Go to the app store" on the last page of the passive feedback form or a native Dialog after the campaign form was displayed. In both cases, they will be displayed only if the mood rating is 4 or 5.

### Reset passive forms
The Usabilla SDK offers the possibility to reset the database from previosuly fetched passive forms using the **removeCachedForms** method:

```java
usabilla.removeCachedForms(Context context);
```
The method removes all passive forms stored locally.

## Custom http client
We allow the use of a custom client to handle all the connections to retrieve and send data through our SDK.

To do so we allow to specify a parameter in the `initialize` method adhering to the `UsabillaHttpClient` interface, which in turns depends on three other interfaces:

- `UsabillaHttpRequest`: encapsulating a HTTP request
- `UsabillaHttpResponse`: encapsulating a HTTP response
- `UsabillaHttpListener`: providing callbacks for success and failure of a request

A sample http client implementation done leveraging the library `OkHttp` can be seen in the class `CustomHttpClient.java` 

## Custom variables
You can pass along custom variables that will be attached to the feedback users send.
Custom variables are held in a `HashMap<String, Object>` in the public interface of the SDK.

You can set custom variables by manipulating the `customVariables` object, adding or removing entries as you need. An example is as follow:

```java
// Setting a whole object
 HashMap<String, Object> myVariables = new HashMap<>();
 myVariables.put("tier", "premium");
 myVariables.put("loggedIn", true);
 usabilla.setCustomVariables(myVariables);

 // Modifying the object
 usabilla.getCustomVariables().put("tier", "premium");
 ```

Custom variables are added as extra feedback data with every feedback item sent by the SDK, whether from a passive feedback or a campaign.

**Custom variables can be used as targeting options, as long as the `value` is a `String`.**

#### Limitations
There are a few limitations to the kind of objects you can add to the custom variables:

• You can add custom objects, but you will have to override the `toString()` method to be able to read them from the Usabilla dashboard.    
• Arrays of objects are not accepted and will be removed by our servers    
• The custom variables will be translated into a `JSONObject` during the submission to our servers, so any non parseable value will break their submission.

## UI Customisations

### Custom Emoticons Rating
It is possible to use custom images instead of the ones provided natively in the SDK.

To do so, you must provide a list (or two, depending on what you want to achieve) of five image ids to be used instead of the Usabilla default emoticons.
The first element of the array should be the lowest or leftmost item, while the 5th element will be the highest or rightmost.
At the moment, the SDK does not perform any check to make sure the lists are valid.

#### Provide only the selected version
You can provide only one list containing the selected version of the icons.

The images will be displayed with an alpha value of 0.5 when unselected, and with an alpha value of 1 when selected.

```java
// Create images
Images themeImages = new Images();
ArrayList<Integer> imagesIdList = new ArrayList<Integer>();
imagesIdList.add(R.drawable.your_mood_1);
imagesIdList.add(R.drawable.your_mood_2);
imagesIdList.add(R.drawable.your_mood_3);
imagesIdList.add(R.drawable.your_mood_4);
imagesIdList.add(R.drawable.your_mood_5);
themeImages.setEnabledEmoticons(imagesIdList);

// Build theme
UsabillaTheme.Builder themeBuilder = new UsabillaTheme.Builder();
themeBuilder.setImages(themeImages);
usabilla.setTheme(themeBuilder.build());
```

#### Provide both the selected and unselected version
You can provide two lists containing the selected and the unselected version of the icons.

The icons drawable will be selected from one of the two lists according to its state.

```java
themeImages.setEnabledEmoticons(enabledEmoticonsImagesIdList);
themeImages.setDisabledEmoticons(disabledEmoticonsImagesIdList);
```

### Custom Star Rating
You can change the appearance of the star in the Star Rating by setting both `star` and `starOutline` in the Usabilla Theme object.

Keep in mind that, in order to display the Star Rating in your form, you must first enable it in the [Usabilla Web Interface](https://app.usabilla.com/member/apps/).

```java
Images themeImages = new Images();

themeImages.setStarOutline(R.drawable.ic_star_outline);
themeImages.setStar(R.drawable.ic_star);

UsabillaTheme.Builder themeBuilder = new UsabillaTheme.Builder();
themeBuilder.setImages(themeImages);
usabilla.setTheme(themeBuilder.build());
```

### Custom Fonts
It is possible to change the font of the feedback form by setting the `regular` property of the `UsabillaTheme.fonts`.

```java
// Create fonts
Fonts themeFonts = new Fonts();
themeFonts.setRegular("fonts/your_font");

// Build theme
UsabillaTheme.Builder themeBuilder = new UsabillaTheme.Builder();
themeBuilder.setFonts(themeFonts);
usabilla.setTheme(themeBuilder.build());
```

⚠️ It's important for your font files to be stored either in the asset folder of the project
or in a subfolder of asset called `"fonts/"`

### Custom colors
All colors are set from the Usabilla website and not from the SDK. You can find a detailed explanation of the color's name and role in our [knowledge base](https://support.usabilla.com/hc/en-us/articles/211588989-How-do-I-change-the-feedback-form-colors-in-Usabilla-for-Apps).

### Apply different themes to different passive forms
Setting a general theme will apply it to both passive forms and campaign forms.

Additionally, it is possible to specify a theme when requesting a passive form, which will be solely applied to the form upon receiving it.

This can be done as follows:

```java
Fonts themeFonts = new Fonts();
themeFonts.setRegular("fonts/yourFont.ttf");
UsabillaTheme.Builder themeBuilder = new UsabillaTheme.Builder().setFonts(themeFonts);
final UsabillaTheme theme = themeBuilder.build();

Usabilla.loadFeedbackForm(this, formId, null, theme, this);
```

Multiple requests of multiple forms done with different themes will result in each form having its own dedicated theme.

## Localization
If you want to provide your own translation, you need to override the strings in the default Usabilla SDK. You can do so by providing a string with the same name in your main application string resource file. This will override the SDK default value and yours will be displayed instead.

The string resources you can override and their default value are the following
```xml
<string name="usa_field_error">Please check this field</string>
<string name="usa_screenshot_title">Screenshot (optional)</string>
<string name="usa_screenshot_message">Add screenshot</string>
<string name="usa_thank_you_dialog_more_feedback">Give more feedback</string>
<string name="usa_rate_on_play_store">Rate on the play store</string>
<string name="usa_dialog_playstore_title">Rate</string>
<string name="usa_dialog_playstore_message">Thank you for your feedback! Would you like to leave a review?</string>
<string name="usa_dialog_playstore_negative">NO, THANKS</string>
<string name="usa_dialog_playstore_positive">RATE NOW</string>
<string name="usa_toolbar_continue">Continue</string>
<string name="usa_close_form">Close</string>
<string name="usa_submit_text">Submit</string>
<string name="sdk_permission_disabled_label">Permission disabled!\nEnable it from Settings -> app info</string>
```

## External Navigation
It is possible to hide the default navigation and cancel button in the SDK (for the passive feedback form) and provide your own (ex. in the action bar).

```java
usabilla.setDefaultNavigationButtonsVisibility(false);

@Override
public void mainButtonTextUpdated(String text) {
    // Use this text for your own navigation button
    // Usually returns "Next" or "Submit"
}
```

## Permissions
If the user tries to set a custom screenshot, the SDK will ask for the permission to access the external storage.    
No other permission is needed to run the SDK.

## Proguard
If you are using resource proguards in your project, add these files to your configuration(whitelist) to keep these resources

 ```
 "R.drawable.mood_1", "R.drawable.mood_2", "R.drawable.mood_3", "R.drawable.mood_4", "R.drawable.mood_5",
 "R.drawable.star_full", "R.drawable.star_empty"
 ```
 
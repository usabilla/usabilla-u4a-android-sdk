[![Download](https://api.bintray.com/packages/usabilla/maven/ubform/images/download.svg)](https://bintray.com/usabilla/maven/ubform/_latestVersion)

# Usabilla for Apps - Android SDK
Usabilla for Apps allows you to collect feedback from your users with great ease and flexibility.

The new Usabilla SDK Version 4 comes with two major advancements:
1. The new feature [Actively targeted surveys](#campaigns) (referred as **Campaigns** in this document).
2. A more stabilized [Passive feedback forms](#passive-feedback).

* * *

- [Requirements](#requirements)
- [Installation](#installation)
- [Initialization](#initialization)
    - [Debug mode](#debug-mode)
- [Campaigns](#campaigns)
    - [The App Id](#the-app-id)
    - [Targeting options](#targeting-options)
    - [Managing an existing campaign](#managing-an-existing-campaign)
    - [Campaign callback](#campaign-callback)
    - [Campaign results](#campaign-results)
    - [Update fragment manager](#update-fragment-manager)
- [Passive feedback](#passive-feedback)
    - [Loading a form](#loading-a-form)
    - [Preloading a form](#preloading-a-form)
    - [Adding a screenshot](#adding-a-screenshot)
    - [Feedback submission callback](#feedback-submission-callback)
        - [Feedback Result](#feedback-result)
    - [Give more feedback](#give-more-feedback)
- [Custom variables](#custom-variables)
- [Play Store link](#play-store-link)
- [UI Customisations](#ui-customisations)
    - [Custom Emoticons Rating](#custom-emoticons-rating)
    - [Custom Fonts](#custom-fonts)
- [Localisation](#localisation)
- [External navigation](#external-navigation)
- [Proguard](#proguard)

* * *

## Requirements
- The Usabilla SDK requires the minSdkVersion of the application to be 16 (Android 4.1).


## Installation
- You can find the latest version of our SDK [here](https://bintray.com/usabilla/maven/ubform) and add it as a Maven or a Gradle dependency (`implementation 'com.usabilla.sdk:ubform:4.0.3'`).
- If you don't want to use a dependency manager you can also import the .aar library independently.
Our SDK uses the following dependencies. If your project doesn't use them already you might need to add it as well in your gradle file.
```
dependencies {
    compile 'com.mcxiaoke.volley:library:1.0.19'
    compile 'com.android.support:appcompat-v7:25.1.1'
    compile "org.jetbrains.kotlin:kotlin-stdlib-jre7:1.2.0"
}
```

## Initialization

Once the installation step is done the SDK can be initialized using the method:
```java
Usabilla.initialize(Context context, @Nullable String appId);
```
where `appId` can be `null` if you are not using the Campaign feature.
Please read the [Campaigns](#campaigns) section for more information.

The **initialize** method will take care of:
* Submitting any pending feedback items.
* Fetching and updating all campaigns associated with the app ID.
* Initializing a few background processes of the SDK.

>⚠️ **Failure to call this method before using the SDK will prevent it from running properly.**

### Debug Mode
In order to have more insights from the SDK while developing, you can enable logging with:
```java
Usabilla.setDebugEnabled(true)
```
This property is by default set to `false`.

## Campaigns
Version 4 of the Usabilla for Apps SDK introduces the new Campaigns feature.
This guide describes the Campaigns feature and all the steps necessary to work with it.

In the Usabilla for Apps Platform, a campaign is defined as a proactive survey targeted to a specific set of users.

Being able to run campaigns in your mobile app is great because it allows you to collect more specific insights from your targeted users. What is even better is that creating new and managing existing campaigns can be done without the need for a new release of your app. Everything can be managed from the Usabilla web interface.

You can run as many campaigns as you like and target them to be triggered when a specific set of targeting options are met.
The configuration of how a campaign is displayed to the user will be familiar to existing Usabilla customers. You can configure it to suit your needs just like you are used to from the Passive feedback forms.

The most important aspect of running a mobile campaign is the presence of 'Events'. Events are custom triggers that are configured in the SDK. When a pre-defined event occurs, it will allow you to trigger a campaign. A good example of an event is a successful purchase by a user in your app.

### The App Id
The app Id is an identifier used to associate campaigns to a mobile app.
By loading the SDK with a specific App Id, it will fetch all the campaigns connected to the given App Id.

It is possible to target a campaign to more than one app (e.g. Android Production App, Android Beta App) by associating it with multiple App Ids.

### Targeting options
Campaigns are triggered by events. Events are used to communicate with the SDK when something happens in your app. Consequently, the SDK will react to an event depending on the configuration of the Usabilla web interface.
To send an event to the SDK, use :
```java
Usabilla.sendEvent(Context context, String event);
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

### Campaign callback



### Campaign results

Aggregated campaign results are available for download from the [Campaign Overview](https://app.usabilla.com/member/#/apps/campaigns/overview/). Here you can download the results per campaign, in the CSV format.

Campaign results will contain the answers that your users provided. Responses from a campaign are collected and sent to Usabilla page by page. This means that even if a user decides to abandon the campaign halfway through, you will still collect valuable insights. When a user continues to the next page, the results of the previous page are submitted to Usabilla. Besides campaign results showing the answers to your campaign questions, you will be able to view the device metadata and custom variables.

As for campaign results. Please note that editing the form of an existing campaign will affect the aggregated campaign results:

- Adding new questions to a form will add additional columns to the CSV file.
- Removing questions from an existing form will not affect the previously collected results. The associated column and its data will still be in the CSV file.
- Replacing the question type with a different question is also possible. When you give the same 'name' in the Usabilla for Apps Campaign Editor, the results are represented in the same column.

### Update fragment manager

⚠️ It's important to note that our Campaigns uses Android Fragments in order to be displayed to the user; therefore it's important that you provide a reference to the FragmentManager using :

```java
Usabilla.updateFragmentManager(FragmentManager fragmentManager)
```

Furthermore, remember to handle properly the device rotation and other cases where a new FragmentManager is created, in order to always provide the latest one to the SDK.

## Passive feedback
Passive feedback are feedback forms that are not triggered by events.
They are mostly, but not necessarily, initiated directly by the user.

### Loading a form

The SDK uses the Form ID you get from [Usabilla](https://app.usabilla.com/member/apps/list) after creating a new form, to fetch and display the form inside your app.

A basic implementation of the SDK would be the following:

```java
public class MainActivity extends AppCompatActivity implements UBFeedbackForm {

    public void loadForm() {
        public static void loadFeedbackForm(this, "the form id to load", this) {
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

```
Usabilla.preloadFeedbackForms(Context context, List<String> formIds)
```

### Adding a screenshot
It is possible to attach a screenshot to a feedback form.

You can take a screenshot at any moment calling

``` java
// Passing a View to take the screenshot and attach it the form
Usabilla.takeScreenshot(View view)

// Passing an Activity to take the screenshot and attach it the form
Usabilla.takeScreenshot(Activity activity)

// Passing a screenshot bitmap to be attached to the form
Usabilla.setCustomScreenshot(@NonNull final Context context, final Bitmap screenshot)
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
            // And following results have been returned                
            FeedbackResult[] result = (FeedbackResult[]) intent.getParcelableArrayExtra("feedbackResults");
        }
    };
}
```

#### Feedback Result

```java
public class FeedbackResult {
    int rating;
    int abandonedPageIndex;
    boolean sent;
}
```

The **rating** value is set as soon as the user interacts with it and will be reported even if the form is not submitted.

The **abandonedPageIndex** property is only set if the user cancels the form before submission.

The **sent** property is false if the user dropped out while filling the form.

### Give more feedback

You can choose to display a button "Give more feedback" (on the last page of the form) to give the user the possibility to change his feedbacks. It can be done by setting the parameter `isVisible` to true.

```
Usabilla.setGiveMoreFeedbackVisibility(boolean isVisible)
```

## Play Store link

To redirect the user to the Play Store, you can switch on "App store redirect" in the ADVANCED panel of the form creation.

This will show a button "Go to the app store" on the last page of the passive feedback form or a native Dialog after the campaign form was displayed. In both cases, they will be displayed only if the mood rating is 4 or 5.


## Custom variables

You can pass along custom variables that will be attached to the feedback users send.
Custom variables are held in a `HashMap<String, Object>` in the public interface of the SDK.

You can set custom variables by manipulating the `customVariables` object, adding or removing entries as you need.

Custom variables are added as extra feedback data with every feedback item sent by the SDK, whether from a passive feedback or a campaign.

**Custom variables can be used as targeting options, as long as the `value` is a `String`.**

#### Limitations

There are a few limitations to the kind of objects you can add to the custom variables:

• You can add custom objects, but you will have to override the `toString()` method to be able to read them from the Usabilla dashboard.    
• Arrays of objects are not accepted and will be removed by our servers    
• The custom variables will be translated into a `JSONObject` during the submission to our servers, so any unparseable value will break their submission.


## UI Customisations

### Custom Emoticons Rating
It is possible to use custom images instead of the one provided natively in the SDK.

To do so, you must provide a list (or two, depending on what you want to achieve) of five images id that will be used instead of the Usabilla's default emoticons.
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
Usabilla.setTheme(themeBuilder.build());
```

#### Provide both the selected and unselected version
You can provide two lists containing the selected and the unselected version of the icons.

The icons drawable will be selected from one of the two lists according to its state.

```java
themeImages.setEnabledEmoticons(enabledEmoticonsImagesIdList);
themeImages.setDisabledEmoticons(disabledEmoticonsImagesIdList);
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
Usabilla.setTheme(themeBuilder.build());
```

⚠️ It's important for your font files to be stored either in the asset folder of the project
or in a subfolder of asset called "fonts/"

## Localisation

If you want to provide your own translation, you need to override the strings in the default Usabilla SDK. You can do so by providing a string with the same name in your main application string resource file. This will override the SDK default value and yours will be displayed instead.

## External Navigation

It is possible to hide the default navigation and cancel button in the SDK (for the passive feedback form) and provide your own (ex. in the action bar).

```java
Usabilla.setDefaultNavigationButtonsVisibility(false);
```

```java
@Override
public void mainButtonTextUpdated(String text) {
    // Use this text for your own navigation button
    // Usually returns "Next" or "Submit"
}
```

## Proguard

If you are using resource proguards in your project, add these files to your configuration(whitelist) to keep these resources

 ```
 "R.drawable.mood_1", "R.drawable.mood_2", "R.drawable.mood_3", "R.drawable.mood_4", "R.drawable.mood_5",
 "R.drawable.star_full", "R.drawable.star_empty"
 ```

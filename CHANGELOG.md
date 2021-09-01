### Changelog:
## 7.3.1
#### Fix
- Exception when app obfuscation is enabled (issue #248)
- Star component visual problems (issue #250)

## 7.3.0
#### Fix
- Return value from the public `dismiss` method
- Prevent returning multiple passive form when the last one requested is still present on screen
- Talkback info on star component
- Logging not showing when enabled
#### Remove
- Permissions `READ_EXTERNAL_STORAGE` and `CAMERA`
#### Update
- Android AppCompat dependency to v1.3.1
- Moshi dependency to v1.12.0
- Kotlin dependency to v1.5.21

## 7.2.2
#### Add
- Optional broadcast receiver to receive user entries when a form or campaign is closed
#### Fix
- Image selection not working properly in ReactNative

## 7.2.1
#### Fix
- Campaign banner not shown properly in full screen apps. (Fixes issue #220)
#### Update
- README to give detailed information about JAVA8 compatibility. (Fixes issue #239)

## 7.2.0
#### Add
- Set java compatibility to Java 8 
- Public property to turn off telemetry data upload
- Upload of Javadocs to Maven Central
#### Fix
- Crash after resuming the app from the background in a multi step campaign
- Bug for concurrent injection (sometimes campaigns would not be shown because of that)
#### Remove
- Dependency from JCenter
- Deprecated kotlin-extensions plugin
#### Update
- Volley dependency to v1.2.0
- Exifinterface dependency to v1.3.2
- Kotlin dependency to v1.4.32
- Kotlin coroutines dependency to v1.4.2
- Moshi dependency to v1.10.0
- Play core dependency to v1.10.0

## 7.1.2
#### Fix
- Campaigns not shown when there is a failure while parsing (Fixes issue #223)
#### Update
- Usabilla brand logo and redirect url with GetFeedback logo and url
- Local database to support downgrading (upon suggestion in issue #225)

## 7.1.1
#### Fix
- Custom variables not set
- Crash when parsing wrongly formatted campaign
#### Update
- Custom variables now use a ConcurrentMap to prevent threading problems

## 7.1.0
#### Fix
- Google play review prompt not being shown (#205)
- Asynchrionous operations done off main thread (#217)
- Campaign events remembered through different app launches
- Banner dimension with long content or big font size
- Sending event simultaneously had campaigns overlap
#### Update
- Campaign banner title max number of rows set to 3

## 7.0.9
#### Fix
- Http 494 Error (Fixes issue #214)
- Campaign data submissions
#### Update
- ConcurrentModificationException crash (upon suggestion in issue #215)

## 7.0.8
#### Add
- Function to toggle logo clickability
#### Fix
- Randomisation of Picker, Checkboxes and Radio components
- Jump rules related to Mood and Slider component
#### Update
- GooglePlay core dependency v1.8.3
- AndroidX exifinterface dependency to v1.3.1
- Target Android 30

## 7.0.7
#### Add
- Moshi dependency v1.9.3
#### Fix
- Duplicate input field (Fixes Issue #201)
- Non-Sdk Greylist warning (Fixes issue #199)
#### Remove
- Gson dependency
#### Update
- Kotlin dependency to v1.4.10

## 7.0.6
#### Fix
- Parcelisation error (Fixes issue #198)
#### Update
- App rating system to adhere to [new Google standard](https://developer.android.com/guide/playcore/in-app-review)
- Kotlin dependency to v1.4.0
- GooglePlay core dependency v1.8.0
- AndroidX appcompat dependency to v1.2.0
- Coroutines dependency to v1.3.9

## 7.0.5
#### Fix
- Custom themes not working with UbImages (Fixes issue #193)

## 7.0.4
#### Fix
- Disabled campaigns not updated in local database
- Campaign banner overlapped by navigation bar (Fixes issue #158)

## v7.0.3
#### Fix
- Issue causes CameraX to be frozen after an image picked from gallery
- Issues with loading feedback forms offline

## v7.0.2
#### Add
- Compatibility for scoped storages introduced on Android API 29
- Gson dependency v2.8.6
#### Fix
- Next button text in the first feedback page (Fixes issue #163)
- FileNotFoundException at UbAnnotationFragment (Fixes issue #172)
- RequestLegacyExternalStorage not found when targeting below android version 29 (Fixes issue #184)

## 7.0.1
#### Fix
- Minor bug
#### Update
- Kotlin dependency to v1.3.72

## 7.0.0
#### Add
- Dependency on coroutines
#### Fix
- Bug on passive form not sending the attached screenshot
- Bug on campaigns not sending results
- Crash using a custom HTTP client
#### Remove
- Reference of TLS1.1 from the codebase
- Support for Android API below 19
#### Update
- Android library dependencies to use AndroidX
- Targeting Android 29
- Kotlin dependency to v1.3.70

## 6.4.4
#### Fix
- Crash caused by parcelable exception on play store dialog (Fixes issue #167)

## 6.4.3
#### Fix
- Form navigation visibility not working as expected
- Cancel/Dismiss button not sending broadcast

## 6.4.2
#### Fix
- Close form broadcast being called while Play Store dialog is active

## 6.4.1
#### Fix
- Crash caused by TargetingOptionsModel when obfuscated
- Crash when the `body` in `UsabillaHttpResponse` is null
- Compatibility with dynamic feature modules
- EditText losing text on rotation

## 6.4.0
#### Add
- User can draw on the screenshot to mask or highlight features
#### Fix
- Crash when resuming the app after its process was killed
- Crash when resuming the apps on Samsung phones
#### Update
- Footer to make use of new Usabilla logo

## 6.3.0
#### Add
- User can choose between camera or image library as source when adding screenshot
#### Fix
- Navigation bar showing in front of campaign banner when the navigation bar is translucent
- Campaign banner element visual aberration. Fixes issue (Fixes issue #151)

## 6.2.0
#### Add
- Method to mask personal identifiable information in the public interface
- Method to remove a displayed form programmatically

## 6.1.0
#### Fix
- Finishing the banner does not send a broadcast. (Fixes issue #128)
- `isSent` property is always false. (Fixes issue #129)
- PlayStore review prompt not displayed. (Fixes issue #130)
- Progress bar does not appear in campaigns
- The abandonedPageIndex property is set on submit
- Crash when opening a campaign without a start page
#### Update
- Secondary button updated to text color
- If you have ProGuard enabled you will have to add this line to your ProGuard configuration:
```
-keep public class com.usabilla.sdk.ubform.eventengine.TargetingOptionsModel
```

## 6.0.0
#### Remove
- Old way of supporting fonts
- `Context` parameter when getting Usabilla instance
#### Update
- UI to card design
- Resource names for uniformity
- `Fonts` changed to `UbFonts`
- `Images` changed to `UbImages`
- `Constants` changed to `UbConstants`
- Accessibility for slider component

## 5.3.0
#### Add
- Accessibility feedback regarding required fields
- Support for custom fonts by resource id
#### Fix
- Required NPS component does not stop users from continuing
#### Update
- Dependencies (Volley to v1.1.1, AppCompat to v28.0.0, Kotlin standard library to v1.2.71)
- Dependency configuration to use `implementation` instead of `compile`

## 5.2.1
#### Fix
- Crash when sending an event after upgrading to v5.2.0
#### Update
- Drawable names to have a `ub_` prefix

## 5.2.0
#### Add
- Accessibility labels on screenshot components
- Accessibility labels on mood and star rating components
#### Fix
- Private methods that became public
- Percentage rule to check only the first time it is triggered
#### Remove
- Play Store button at the last page of the Passive Feedback Form
#### Update
- Changed Play Store reminder in Passive Feedback Form to a dialog
- Increased touch area for slider component

## 5.1.0
#### Add
- Possibility to set a custom Http Client to handle the network calls
#### Fix
- Memory leaks concerning the `FragmentManager`, `UsabillaReadyCallback` and `UsabillaFormCallback`
#### Update
- Volley library to the official v1.1.0

## 5.0.0
#### Add
- Possibility to apply a custom theme to a single passive feedback form
- Callback to the reset campaign method to notify when the action is finished
- Possibility to remove cached passive feedback forms
#### Fix
- Crash happening on devices without Google PlayStore installed
- Crash happening when the screenshot file was not found
#### Remove
- Feature to give more feedback at the end of a passive feedback form
- Methods `getAppName`, `setAppName`, `getAppId`, `setCustomScreenshot` and `getAppVersion` from the public interface of our SDK
#### Update
- The way to interact with the SDK from static methods to class instantiation. See the ReadMe for more information
- The method `loadFeedbackForm` to accept as parameters also a Bitmap for the screenshot and a theme
- The way to provide a screenshot to the SDK
- Kotlin `stdlib` to use `jdk7` instead of `jre7`

## 4.1.1
#### Fix
- `IllegalStateException` when displaying the banner
- Punctuation marks not being correctly handled in custom variables

## 4.1.0
#### Add
- Callback parameter to the `initialize` and `resetCampaignData` methods to notify when they finish. See the ReadMe for more information
- Overloaded versions of `initialize` and `resetCampaignData` methods.

## 4.0.3
#### Fix
- Dependency to espresso idling resources incorrectly leaking into the hosting application
- Issue that would cause the redirect to play store button not to appear under certain conditions

## 4.0.2
#### Fix
- Issue with the jump rules not considered with the radio buttons component
- Issue when the submission of the form was blocked by a required hidden field

## 4.0.1
#### Fix
- Issue with data serialisation

## 4.0.0
#### Add
- Targeted active feedback functionalities. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#campaigns) for more details.
- `updateFragmentManager(SupportFragmentManager)` to pass the FragmentManager needed for campaign functionalities. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#update-fragment-manager) for more details
- `isDebugEnabled()` and `setDebugEnabled(boolean)` to enable the logging of information coming from the SDK. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#debug-mode) for more details
- `resetCampaignData(Context)`. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#managing-an-existing-campaign) for more details
#### Remove
- `showCancelButton` property. The cancel button will now be always displayed.
- Possibility to add specialised custom variables to passive forms. From now on, the global custom variables will be used instead
#### Update
- Default smilies and stars
- Renamed `UBForm` to `Usabilla`
- Renamed `UBFormInterface` to `UBFeedbackForm`
- Renamed `initClient()` method to `initialize(Context, String);`. Calling this method is now mandatory to start the SDK
- Changed `formLoadedSuccessfully(Form, boolean)` to `formLoadSuccess(FormClient)`
- Changed `formFailedLoading(Form)` to `formLoadFail()`
- Changed `loadFeedbackForm(String, Context, UBFormInterface);` to `loadFeedbackForm(Context, String, UBFeedbackForm)`
- Changed `setCustomScreenshot(Bitmap)` to `setCustomScreenshot(Context, Bitmap)`
- Changed `textForMainButtonUpdated(String)` to `mainButtonTextUpdated(String)`
- Refactored `UsabillaTheme`. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#ui-customisations) for more details
- Merged `form.hideDefaultNavigationButton(boolean)` and `form.hideCancelButton(boolean);` into `setDefaultNavigationButtonsVisibility(boolean)` and `areNavigationButtonsVisible()`

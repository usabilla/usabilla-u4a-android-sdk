### Changelog:
## 7.0.7
#### Updated
- Kotlin dependency to v1.4.10
#### Removed
- Gson dependency
#### Added
- Moshi dependency v1.9.3
#### Fixed
- Duplicate input field (Fixes Issue #201)
- Non-Sdk Greylist warning (Fixes issue #199)

## 7.0.6
#### Updated
- App rating system to adhere to [new Google standard](https://developer.android.com/guide/playcore/in-app-review)
- Kotlin dependency to v1.4.0
- GooglePlay core dependency v1.8.0
- AndroidX appcompat dependency to v1.2.0
- Coroutines dependency to v1.3.9
#### Fixed
- Parcelisation error (Fixes issue #198)

## 7.0.5
#### Fixed
- Custom themes not working with UbImages (Fixes issue #193)

## 7.0.4
#### Fixed
- Disabled campaigns not updated in local database
- Campaign banner overlapped by navigation bar (Fixes issue #158)

## v7.0.3
#### Fixed
- Issue causes CameraX to be frozen after an image picked from gallery
- Issues with loading feedback forms offline

## v7.0.2
#### Added
- Compatibility for scoped storages introduced on Android API 29
- Gson dependency v2.8.6
#### Fixed
- Next button text in the first feedback page (Fixes issue #163)
- FileNotFoundException at UbAnnotationFragment (Fixes issue #172)
- RequestLegacyExternalStorage not found when targeting below android version 29 (Fixes issue #184)

## 7.0.1
#### Updated
- Kotlin dependency to v1.3.72
#### Fixed
- Minor bug

## 7.0.0
#### Updated
- Android library dependencies to use AndroidX
- Targeting Android 29
- Kotlin dependency to v1.3.70
#### Fixed
- Bug on passive form not sending the attached screenshot
- Bug on campaigns not sending results
- Crash using a custom HTTP client
#### Removed
- Reference of TLS1.1 from the codebase
- Support for Android API below 19
#### Added
- Dependency on coroutines

## 6.4.4
#### Fixed
- Crash caused by parcelable exception on play store dialog (Fixes issue #167)

## 6.4.3
#### Fixed
- Form navigation visibility not working as expected
- Cancel/Dismiss button not sending broadcast

## 6.4.2
#### Fixed
- Close form broadcast being called while Play Store dialog is active

## 6.4.1
#### Fixed
- Crash caused by TargetingOptionsModel when obfuscated
- Crash when the `body` in `UsabillaHttpResponse` is null
- Compatibility with dynamic feature modules
- EditText losing text on rotation

## 6.4.0
#### Added
- User can draw on the screenshot to mask or highlight features
#### Updated
- Footer to make use of new Usabilla logo
#### Fixed
- Crash when resuming the app after its process was killed
- Crash when resuming the apps on Samsung phones

## 6.3.0
#### Added
- User can choose between camera or image library as source when adding screenshot
#### Fixed
- Navigation bar showing in front of campaign banner when the navigation bar is translucent
- Campaign banner element visual aberration. Fixes issue (Fixes issue #151)

## 6.2.0
#### Added
- Method to mask personal identifiable information in the public interface
- Method to remove a displayed form programmatically

## 6.1.0
#### Updated
- Secondary button updated to text color
- If you have ProGuard enabled you will have to add this line to your ProGuard configuration:
```
-keep public class com.usabilla.sdk.ubform.eventengine.TargetingOptionsModel
```
#### Fixed
- Finishing the banner does not send a broadcast. (Fixes issue #128)
- `isSent` property is always false. (Fixes issue #129)
- PlayStore review prompt not displayed. (Fixes issue #130)
- Progress bar does not appear in campaigns
- The abandonedPageIndex property is set on submit
- Crash when opening a campaign without a start page

## 6.0.0
#### Updated
- UI to card design
- Resource names for uniformity
- `Fonts` changed to `UbFonts`
- `Images` changed to `UbImages`
- `Constants` changed to `UbConstants`
- Accessibility for slider component
#### Removed
- Old way of supporting fonts
- `Context` parameter when getting Usabilla instance

## 5.3.0
#### Added
- Accessibility feedback regarding required fields
- Support for custom fonts by resource id
#### Updated
- Dependencies (Volley to v1.1.1, AppCompat to v28.0.0, Kotlin standard library to v1.2.71)
- Dependency configuration to use `implementation` instead of `compile`
#### Fixed
- Required NPS component does not stop users from continuing

## 5.2.1
#### Updated
- Drawable names to have a `ub_` prefix
#### Fixed
- Crash when sending an event after upgrading to v5.2.0

## 5.2.0
#### Added
- Accessibility labels on screenshot components
- Accessibility labels on mood and star rating components
#### Updated
- Changed Play Store reminder in Passive Feedback Form to a dialog
- Increased touch area for slider component
#### Removed
- Play Store button at the last page of the Passive Feedback Form
#### Fixed
- Fixed private methods that became public
- Fixed the percentage rule to check only the first time it is triggered

## 5.1.0
#### Added
- Added the possibility to set a custom Http Client to handle the network calls

#### Updated
- Updated the default Volley library to the official v1.1.0

#### Fixed
- Fixed possible memory leaks concerning the `FragmentManager`, `UsabillaReadyCallback` and `UsabillaFormCallback`

## 5.0.0
#### Added
- Added the possibility to apply a custom theme to a single passive feedback form
- Added a callback to the reset campaign method to notify when the action is finished
- Added the possibility to remove cached passive feedback forms

#### Updated
- Updated the way to interact with the SDK from static methods to class instantiation. See the ReadMe for more information
- Updated the method `loadFeedbackForm` to accept as parameters also a Bitmap for the screenshot and a theme
- Updated the way to provide a screenshot to the SDK
- Updated Kotlin `stdlib` to use `jdk7` instead of `jre7`

#### Removed
- Removed the feature to give more feedback at the end of a passive feedback form
- Removed the methods `getAppName`, `setAppName`, `getAppId`, `setCustomScreenshot` and `getAppVersion` from the public interface of our SDK

#### Fixed
- Fixed a crash happening on devices without Google PlayStore installed
- Fixed a crash happening when the screenshot file was not found

## 4.1.1
#### Fixed
- Fixed a possible `IllegalStateException` when displaying the banner
- Fixed punctuation marks not being correctly handled in custom variables

## 4.1.0
#### Added
- Added callback parameter to the `initialize` and `resetCampaignData` methods to notify when they finish. See the ReadMe for more information
- Added overloaded versions of `initialize` and `resetCampaignData` methods.

## 4.0.3
#### Fixed
- Fixed dependency to espresso idling resources incorrectly leaking into the hosting application
- Fixed an issue that would cause the redirect to play store button not to appear under certain conditions

## 4.0.2
#### Fixed
- Fixed an issue with the jump rules not considered with the radio buttons component
- Fixed an issue when the submission of the form was blocked by a required hidden field

## 4.0.1
#### Fixed
- Fixed a possible issue with data serialisation

## 4.0.0
#### Added
- Added targeted active feedback functionalities. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#campaigns) for more details.
- Added `updateFragmentManager(SupportFragmentManager)` to pass the FragmentManager needed for campaign functionalities. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#update-fragment-manager) for more details
- Added `isDebugEnabled()` and `setDebugEnabled(boolean)` to enable the logging of information coming from the SDK. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#debug-mode) for more details
- Added `resetCampaignData(Context)`. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#managing-an-existing-campaign) for more details

#### Updated
- Updated default smilies and stars
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

#### Removed
- Removed `showCancelButton` property. The cancel button will now be always displayed.
- Removed the possibility to add specialised custom variables to passive forms. From now on, the global custom variables will be used instead

## 3.4.4
#### Fixed
- Error when multiple ending pages were present in the form

#### Added
- Add possibility to preload a Form for off-line use

## 3.4.3
#### Fixed
- The screenshot is now correctly reset after the form has been sumbitted
- Labels in the slider component do not overlap eachother anymore
- Icons in the mood component always resize properly

## 3.4.2
#### Fixed
- Fixed color customisation not applied correctly to the form footer

## 3.4.1
#### Updated
- Updated Volley to 'com.android.volley:volley:1.0.0'
- Update the support:design library to 25.3.0

## 3.4.0
#### Added
- The close form broadcast now contains informations relative to the feedback the user has submitted. See the ReadMe for more information

#### Removed
- Removed unused parameter `boolean active` from `public void formLoadedSuccessfully(Form form)`

## 3.3.1
#### Added
- Possibility to set the titles in bold
- New field error message, displayed under the title when the user tries to submit an incomplete form

#### Updated
- Revamped and polished UI and animations following material guidelines
- `miniTextSize` and its getter / setter have been renamed to `miniFontSize``, to align the naming with the other properties.
- Updated support libraries to `v25.1.0`

#### Removed
- It's not possible anymore to force a user to submit a screenshot

## 3.2.1
#### Added
- Added possibility to change the size of the fonts. See the wiki for more info

#### Updated
- Switched to ContextCompat for loading resources
- Polished the UI of the form. Components are now more aligned with material design guidelines
- Updated screenshot component
- Updated dependencies `appcompat-v7`, `support-annotations`, `support-v4` and `support:design` to `25.0.1`

#### Removed
- Removed 10 buttons version of the NPS component. Now it defaults to slider

## 3.1.2
#### Fixed
- Fixed the wrong callback method being called with the default form. The default form will be now correctly returned only on the `formFailedLoading` method

#### Updated
- Added padding to top and bottom of scrollview

## 3.1.1
#### Added
- It is now possible to set most text fields (like submit, cancel, redirect to play store) from the web interface

#### Updated
- Updated UI of text fields, sliders and drop down picker according to material design guidelines

#### Fixed
- Fixed a bug that could occur when applying customisations on older devices

## 3.0
- This SDK moves from activities to fragments and introduces many major changes. See the Wiki for details on the new implementation
- Reworked theme customisation. Each form now has its own configuration object
- Colour customisation from the Usabilla web interface is now supported

## 2.3.5
- Fixed redirect to play store not working correctly
- Fixed a crash that might occur if the uses minimises the app during the loading of the form

## 2.3.4.2
- Fixed application title not being correctly displayed in the action bar
- Fixed custom font not being correctly applied to the picker component

## 2.3.4.1
- Added the possibility to automatically take a screenshot when opening the form though the shake listener

## 2.3.4
- Fixed a crash that could occur while restoring the fragment after savedInstanceState
- Improved support to the Marshmallow permission request system. Fixed a crash that could occur while choosing a screenshot with third party media application
- Prefixed all resource names to avoid conflict with hosting application

## 2.3.1
- updated appcompat-v7 to 23.4.0
- updated support-annotations to 23.4.0
- updated support-v4 to 23.4.0
- Bug fixes and stability improvements

## 2.3.0
- Removed GSON dependency, GSON rule in pro guard file can also be omitted
- Removed the popup at the end of the feedback. Now there is a dedicated page instead, with all the previous content disposed in a more user friendly way
- Various bug fixes and improvements

## 2.2.0
- Upgraded target SDK version to 23
- Removed deprecated Apache HTTP library.

## 2.1.6
- It is now possible to specify a custom image as the feedback screenshot
- It is now possible to force the user to submit a screenshot

## 2.1.5
- It is now possible to override all the text in the sdks and provide a custom localization
- The SDK can now redirect the users to the Play Store to review your app if they gave a positive rating
- Various bug fixes and improvements

## 2.1.1
- Custom variables are now correctly reported

## 2.1.0
- Enabled colors, images and fonts customization via SDK
- Downgraded Appcompat-v7 to 22.1.0
- Various bug fixes and improvements

## 2.0.3
- Added star rating: It’s now possible to choose between the emoticons or a more traditional 5 star rating
- Raised minimum api level to 16
- Updated Appcompat-v7 to 23.1.1


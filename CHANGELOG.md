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
- Added `updateFragmentManager(SupportFragmentManager)` to pass the FragmentManager needed for campaign functionalities. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#update-fragment-manager) for more details.
- Added `isDebugEnabled()` and `setDebugEnabled(boolean)` to enable the logging of information coming from the SDK. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#debug-mode) for more details.
- Added `resetCampaignData(Context)`. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#managing-an-existing-campaign) for more details.


#### Updated
- Updated default smilies and stars.
- Renamed `UBForm` to `Usabilla`.
- Renamed `UBFormInterface` to `UBFeedbackForm`.
- Renamed `initClient()` method to `initialize(Context, String);`. Calling this method is now mandatory to start the SDK.
- Changed `formLoadedSuccessfully(Form, boolean)` to `formLoadSuccess(FormClient)`.
- Changed `formFailedLoading(Form)` to `formLoadFail()`.
- Changed `loadFeedbackForm(String, Context, UBFormInterface);` to `loadFeedbackForm(Context, String, UBFeedbackForm)`.
- Changed `setCustomScreenshot(Bitmap)` to `setCustomScreenshot(Context, Bitmap)`.
- Changed `textForMainButtonUpdated(String)` to `mainButtonTextUpdated(String)`.
- Refactored `UsabillaTheme`. See the [readme](https://github.com/usabilla/usabilla-u4a-android-sdk#ui-customisations) for more details.
- Merged `form.hideDefaultNavigationButton(boolean)` and `form.hideCancelButton(boolean);` into `setDefaultNavigationButtonsVisibility(boolean)` and `areNavigationButtonsVisible()`

#### Removed
- Removed `showCancelButton` property. The cancel button will now be always displayed.
- Removed the possibility to add specialised custom variables to passive forms. From now on, the global custom variables will be used instead.


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
- Possibility to set the titles in bold.
- New field error message, displayed under the title when the user tries to submit an incomplete form.

#### Updated
- Revamped and polished UI and animations following material guidelines.
- `miniTextSize` and its getter / setter have been renamed to `miniFontSize``, to align the naming with the other properties.
- Updated support libraries to `v25.1.0`.

#### Removed
- It's not possible anymore to force a user to submit a screenshot.

## 3.2.1
#### Added
- Added possibility to change the size of the fonts. See the wiki for more info.

#### Updated
- Switched to ContextCompat for loading resources
- Polished the UI of the form. Components are now more aligned with material design guidelines
- Updated screenshot component
- Updated dependencies `appcompat-v7`, `support-annotations`, `support-v4` and `support:design` to `25.0.1`

#### Removed
- Removed 10 buttons version of the NPS component. Now it defaults to slider.

## 3.1.2
#### Fixed
- Fixed the wrong callback method being called with the default form. The default form will be now correctly returned only on the `formFailedLoading` method.

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
- This SDK mo##es from acti##ities to fragments and introduces many major changes. See the Wiki for details on the new implementation
- Reworked theme customisation. Each form now has its own configuration object.
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
- Fixed a crash that could occur while restoring the fragment after sa##edInstanceState
- Impro##ed support to the Marshmallow permission request system. Fixed a crash that could occur while choosing a screenshot with third party media application
- Prefixed all resource names to a##oid conflict with hosting application


## 2.3.1
- updated appcompat-##7 to 23.4.0
- updated support-annotations to 23.4.0
- updated support-##4 to 23.4.0
- Bug fixes and stability impro##ements

## 2.3.0
- Remo##ed GSON dependency, GSON rule in pro guard file can also be omitted
- Remo##ed the popup at the end of the feedback. Now there is a dedicated page instead, with all the pre##ious content disposed in a more user friendly way.
- ##arious bug fixes and impro##ements

## 2.2.0
- Upgraded target SDK ##ersion to 23
- Remo##ed deprecated Apache HTTP library.

## 2.1.6
- It is now possible to specify a custom image as the feedback screenshot
- It is now possible to force the user to submit a screenshot

## 2.1.5
- It is now possible to o##erride  all the text in the sdks and pro##ide a custom localization
- The SDK can now redirect the users to the Play Store to re##iew your app if they ga##e a positi##e rating
- ##arious bug fixes and impro##ements


## 2.1.1
- Custom ##ariables are now correctly reported

## 2.1.0
- Enabled colors, images and fonts customization ##ia SDK
- Downgraded Appcompat-##7 to 22.1.0
- ##arious bug fixes and impro##ements

## 2.0.3
- Added star rating: It’s now possible to choose between the emoticons or a more traditional 5 star rating.
- Raised minimum api le##el to 16
- Updated Appcompat-##7 to 23.1.1

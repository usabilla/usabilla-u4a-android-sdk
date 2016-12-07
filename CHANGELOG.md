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

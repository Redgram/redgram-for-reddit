# redgram-for-reddit [![Build Status](https://travis-ci.org/Redgram/redgram-for-reddit.svg?branch=dev)](https://travis-ci.org/Redgram/redgram-for-reddit)
An awesome Android Reddit client

--------------

For collaborations, please view [this issue](https://github.com/Redgram/redgram-for-reddit/issues/1) and follow the links, specifically the article on the suggested branching model.

###Latest:

- Upgrades MainActivity to use the CoordinatorLayout and utilies SnackBar for certain operations like unhide.
- Any activity that has CoordinatorLayout should implement CoordinatorLayout Interface because snackbars can only be called from within that activity.
- Implement Report operation. Ask for confirmation, and call hide operation but do not prompt for undo operation on success.
- Fix scroll view in PostPreviewFragment.
- Implement a reliable way to call methods in fragment from activities.
- OAuth 2 implemented for a single account.
- Logging in required before accessing the app.
- Comments Activity that uses a viewpager. Comments now collapse and expand as expected. 
- Upgraded to Retrofit and okHttp to the latest version. 
- Realm for local database.
- Any activity/fragment with a sliding panel should extend from SlidingUpPanelActivity/SlidingUpPanelFragment.
- All custom views should call interfaces to perform their operations, and views talk to presenters for non-UI related operations when needed.


###Installation:

- Clone this repo.
- In AndroidStudio, *File > Open*.
- Choose the top level folder (Redgram).
- Gradle should automatically build the file. If not, navigation to *Build > ReBuild Project*.
- Run project.

###Todo:

- **Always** - Follow the Program to Interface approach. [Read More](http://stackoverflow.com/questions/383947/what-does-it-mean-to-program-to-an-interface).
- Focus on `ThreadActivity`:
	- Up/Down vote, view media, etc
	- Markdown
	- Comments operations - load more, up/down vote on comments
- Make sure the app cannot be rotated to Landscape.
- Implement Settings and a way to integrate it with Realm and throughout the app.
- Submit links and comments.
- Implement Profile (auth user and others). 

###Enhancement Required:

- UI for the nav drawer - but it's functional.
- Smooth scroll when many comments are collpased.
- Avoiding *Bitmap too large to fit* warnings.
- Controlling network calls as state changes.
- UI touch gestures.
- Caching.
- Dagger(2) Integration.

###License:

This project is under **GPL license**

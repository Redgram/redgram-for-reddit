# redgram-for-reddit [![Build Status](https://travis-ci.org/Redgram/redgram-for-reddit.svg?branch=master)](https://travis-ci.org/Redgram/redgram-for-reddit)
An awesome Android Reddit client

--------------



Head to the `master` branch to learn more about the project.

For collaborations, please view [this issue](https://github.com/Redgram/redgram-for-reddit/issues/1) and follow the links, specifically the article on the suggested branching model.

###Latest:

- OAuth 2 implemented for a single account.
- Logging in required before accessing the app.
- Comments Activity that uses a viewpager. Comments now collapse and expand as expected. 
- Upgraded to Retrofit and okHttp to the latest version. 
- Any activity/fragment with a sliding panel should extend from SlidingUpPanelActivity/SlidingUpPanelFragment.

###Installation:

- Clone this repo.
- In AndroidStudio, *File > Open*.
- Choose the top level folder (Redgram).
- Gradle should automatically build the file. If not, navigation to *Build > ReBuild Project*.
- Run project.

###Todo:

- **Always** - Follow the Program to Interface approach. [Read More](http://stackoverflow.com/questions/383947/what-does-it-mean-to-program-to-an-interface).
- Merge changes to `dev` before `master` branch.
- All custom views should call interfaces to perform their operations, and views talk to presenters for non-UI related operations when needed.
- Implement a custom undo action and apply it on hide operation in HomeView.
- Implement Report operation. Ask for confirmation, and call hide operation but do not prompt for undo operation on success.
- Fix scroll view in PostPReviewFragment.
- Implement a reliable way to call methods in fragment from activities.
- Call a new activity every time the user clicks or visits a subreddit or user. No need for MainActivity's `singleTask` tag in manifest.

###Enhancement Required:

- Smooth scroll when many comments are collpased.
- Avoiding *Bitmap too large to fit* warnings.
- Controlling network calls as state changes.
- UI touch gestures.
- Caching.
- Dagger(2) Integration.

###License:

This project is under **GPL license**

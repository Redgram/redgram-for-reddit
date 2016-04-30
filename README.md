# redgram-for-reddit [![Build Status](https://travis-ci.org/Redgram/redgram-for-reddit.svg?branch=dev)](https://travis-ci.org/Redgram/redgram-for-reddit)
An awesome Android Reddit client

--------------

For collaborations, please view [this issue](https://github.com/Redgram/redgram-for-reddit/issues/1) and follow the links, specifically the article on the suggested branching model.

###Todo:

- **Always** - Follow the Program to Interface approach. [Read More](http://stackoverflow.com/questions/383947/what-does-it-mean-to-program-to-an-interface).
- Custom Nav Drawer UI.
- Configure Realm throughout app.
- Support for multiple accounts (use Realm to save different authenticated accounts)
- Focus on `ThreadActivity`:
	- Hide operation
	- Markdown
	- Comments operations - load more, up/down vote on comments
- Make sure the app cannot be rotated to Landscape.
- Implement Settings and a way to integrate it with Realm and throughout the app.
- Submit links and comments.
- Implement Profile (auth user and others). 

###Enhancement Required:

- UI for the nav drawer - but it's functioning properly.
- Smooth scroll when many comments are collpased.
- Avoiding *Bitmap too large to fit* warnings.
- Controlling network calls as state changes.
- Caching with Retrofit 2.

###License:

This project is under **GPL license**

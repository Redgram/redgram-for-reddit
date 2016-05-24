# redgram-for-reddit [![Build Status](https://travis-ci.org/Redgram/redgram-for-reddit.svg?branch=dev)](https://travis-ci.org/Redgram/redgram-for-reddit)
An awesome Android Reddit client

--------------

For collaborations, please view [this issue](https://github.com/Redgram/redgram-for-reddit/issues/1) and follow the links, specifically the article on the suggested branching model.

After this branch is merged to `dev`, it will remain active and updated in order to consider all possible changes to user preferences and global settings.

###Todo:

- **Always** - Follow the Program to Interface approach. [Read More](http://stackoverflow.com/questions/383947/what-does-it-mean-to-program-to-an-interface).
- Change the implementation to ask the users whether they want to use their Reddit preferences.
- Configure Realm throughout app.
- Implement Settings and a way to integrate it with Realm and throughout the app.

### Implementation

When users authenticate for the first time, they shall be prompted an option to use their Reddit preferences in the app.
The only preference that is forcefully synced is the age indication field, `over_18`. 

The update is only made once when the user logs in.

If the user skipped the update, the app settings shall be set to the default values. The user can always sync with the
Reddit preferences from the Settings activity later on.

Each account in the application have the same flow as above. Each user shall have their unique set of settings unless specified to use the same settings from an existing account, which can also be modified (this exception is only made when one or more accounts were previously authenticated).

The user shall be able to set periodic updates from their Reddit preferences in the Settings Activity. If the target account was chosen as a candidate by another one (or more), the target account shall update that one as well.

### Breakdown of [User Preferences](https://www.reddit.com/r/redditdev/comments/21jh28/oauth_2_new_preferences_endpoints_get_patch/) used in the app

| Field   			     | Type        |  Description   |
| -----------------------|:-----------:| :------------  |
| affiliate_links        |  boolean    | (?)			 
| default_comment_sort	 |	String     | The default comment sort of the user
| domain_details 		 |  boolean    | Show additional details in the domain text when available (such as the source subreddit or the content author's url/name)
| email_messages		 |  boolean    | Receive emails on new messages in inbox
| hide_ads				 |  boolean    | **Valid for Gold users only**
| hide_downs			 |  boolean    | Hide posts that users down vote
| hide_ups				 |  boolean    | Hide posts that users up vote
| highlight_controversial|  boolean    | Highlight controversial comments
| highlight_new_comments |  boolean    | **Valid for Gold users only. Remembers visits for 48 hours**
| ignore_suggested_sort	 |  boolean    | Ignore suggested sort if there was no preferred one (test it)
| label_nsfw 			 |  boolean    | Label posts that are over 18 by NSFW tag 
| mark_messages_read	 |  boolean    | Mark inbox messages as read
| media					 |  String     | Values of On, Off, or Subreddit Default. 
| min_comment_score		 |	int        | The min comment score to be displayed (default is -4)
| min_link_score		 |  int        | The min link score to be displayed (default is -4)
| monitor_mentions		 |  boolean	   | Notify user when people say their username
| no_profanity			 |  boolean    | Hide or show blasphemous or obscene language
| num_comments			 |  int        | Default number of comments to be requested at once (1 to 500, 200 default)
| numsites				 |  int        | Default number of comments to be requested at once (10 to 100)
| over_18				 |  boolean    | Indicates that the user is over 18 for NSFW material
| public_votes			 |  boolean    | Show or hide the posts or comments that the user votes on
| show_flair 			 |  boolean    | Show or hide flair next to user comments
| show_gold_expiration	 |  boolean    | **Valid for Gold users only**
| show_link_flair		 |  boolean    | Show or hide flair next to links
| show_trending			 |  boolean    | Show trending subreddits in the frontpage
| store_visits			 |  boolean    | **Valid for Gold users only**

###Global App Settings

*list here*

###License:

This project is under **GPL license**

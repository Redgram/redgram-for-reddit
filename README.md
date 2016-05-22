# redgram-for-reddit [![Build Status](https://travis-ci.org/Redgram/redgram-for-reddit.svg?branch=dev)](https://travis-ci.org/Redgram/redgram-for-reddit)
An awesome Android Reddit client

--------------

For collaborations, please view [this issue](https://github.com/Redgram/redgram-for-reddit/issues/1) and follow the links, specifically the article on the suggested branching model.

After this branch is merged to `dev`, it will remain active and updated in order to consider all possible changes to user preferences and global settings.

###Todo:

- **Always** - Follow the Program to Interface approach. [Read More](http://stackoverflow.com/questions/383947/what-does-it-mean-to-program-to-an-interface).
- Configure Realm throughout app.
- Implement Settings and a way to integrate it with Realm and throughout the app.

###Breakdown of [User Preferences](https://www.reddit.com/r/redditdev/comments/21jh28/oauth_2_new_preferences_endpoints_get_patch/):

| Field   			     | Type        |  Description   |
| -----------------------|:-----------:| :------------: |
| affiliate_links        |  boolean    | 
| beta					 | 	boolean	   |			 
| clickgadget			 | 	boolean	   |
| collapse_read_messages |  boolean    | Collapse messages that are viewed already (show title only)			 
| compress				 |  boolean    |			 
| credit_autorenew		 | 	boolean    | 
| default_comment_sort	 |	String     | The default comment sort of the user
| domain_details 		 |  boolean    | (?)
| email_messages		 |  boolean    | REceive emails on new messages in inbox
| enable_default_themes	 |  boolean    |
| hide_ads				 |  boolean    | Gold users are ad free
| hide_downs			 |  boolean    | Hide posts that users down vote
| hide_ups				 |  boolean    | Hide posts that users up vote
| hide_from_robots		 |  boolean    | 
| hide_locationbar		 |  boolean    |
| highlight_controversial|  boolean    | Highlight controversial comments
| highlight_new_comments |  boolean    | Highlight new comments (period?)
| ignore_suggested_sort	 |  boolean    | Ignore suggested sort if there was no preferred one (test it)
| label_nsfw 			 |  boolean    | Label posts that are over 18 by NSFW tag
| lang					 |  String     | 
| legacy_search			 |  boolean    | 
| mark_messages_read	 |  boolean    | Mark inbox messages as read
| media					 |  String     | Values of On, Off, or Subreddit Default. 
| min_comment_score		 |	int        | The min comment score to be displayed (default is -4)
| min_link_score		 |  int        | The min link score to be displayed (default is -4)
| monitor_mentions		 |  boolean	   |
| newwindow				 |  boolean    |
| no_profanity			 |  boolean    |
| num_comments			 |  int        | Default number of comments to be requested at once (200 is default)
| numsites				 |  int        | (?)
| organic				 |  boolean    |
| other_theme			 |  String     |
| over_18				 |  boolean    | Indicates that the user is over 18 for NSFW material
| private_feeds			 |  boolean    | (?)
| public_votes			 |  boolean    | Show or hide the posts or comments that the user votes on
| research 				 |  boolean    |
| show_flair 			 |  boolean    | Show or hide flair next to user comments
| show_gold_expiration	 |  boolean    |
| show_link_flair		 |  boolean    | Show or hide flair next to links
| show_promote			 |  boolean    |
| show_stylesheets		 |  boolean    |
| show_trending			 |  boolean    | Show trending subreddits in the frontpage
| store_visits			 |  boolean    | Store history of visits to links
| theme_selector		 |  String     |
| threaded_messages		 |  boolean    |
| threaded_modmail		 |  boolean    |
| use_global_defaults	 |  boolean    | (?)

###Global App Settings

*list here*

###License:

This project is under **GPL license**

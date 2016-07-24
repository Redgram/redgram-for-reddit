# redgram-for-reddit [![Build Status](https://travis-ci.org/Redgram/redgram-for-reddit.svg?branch=dev)](https://travis-ci.org/Redgram/redgram-for-reddit)
An awesome Android Reddit client

--------------

For collaborations, please view [this issue](https://github.com/Redgram/redgram-for-reddit/issues/1) and follow the links, specifically the article on the suggested branching model.

After this branch is merged to `dev`, it will remain active and updated in order to consider all possible changes to user preferences and global settings.

### Implementation



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

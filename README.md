# redgram-for-reddit
An Android Reddit client focusing primarily on media images/videos. 

--------------

Please head to the [ABOUT](/ABOUT.md) page to learn more about the project.

**Status**: Under development, and open for public reviews.

**Goal**: Using Android best practices to reach a solid base for further development.

For collaborations, please view [this issue](https://github.com/Redgram/redgram-for-reddit/issues/1) and follow the links, specifically the article on the suggested branching model.

###Latest:

- Subreddits Activity.
- Image Manager with builder for a single image.
- Fixes an issue when loading more leads to duplicate posts by replacing existing ones with the newly added ones.
- Loads the user subreddits once. They can be accessed by clicking on the title of the home view.
- Implements Sliding Up Panel for all dynamic views.
	- Implemented to view images from cache and loading webpages.
	- Created a base class for fragments to implement panel control methods.

###Installation:

- Clone this repo.
- In AndroidStudio, *File > Open*.
- Choose the top level folder (Redgram).
- Gradle should automatically build the file. If not, navigation to *Build > ReBuild Project*.
- Run project.

###Todo:

- **Always** - Follow the Program to Interface approach. [Read More](http://stackoverflow.com/questions/383947/what-does-it-mean-to-program-to-an-interface).

- Add the ability to add/delete new Subreddits to default ones in the Subreddits Activity.
	- If the user is authenticated, subscribe/unsubscribe
		- Make it possible to add/delete subreddits without the need to use the API.
	- <del>Hold click subreddit to view infromation on it (requires new fragment [SubredditDetailsFragment])
	- Ability to update information viewed in SubredditDetailsFragment
- User Authentication (**dev-auth** branch - has to be merged with **dev** before starting).
- String Builder Manager to highlight text
	- <del>Use in headers
	- Use to mark Admins, Mods in borders
	- Tag view, visit source webpage on link click (for default view too)
	- Extend to manipulate mutable text (EditText) for submissions 
- Shared Preferences Manager:
	- <del>Create a Manager Class.
	- Requires a Setting Page for global configuration.
	- Keep track of new preferences.
- Implementing Videos (MP4 & Youtube).
- GFYCAT API.
	- **Open with WebView for now** 
- IMGUR API:
	- Implementing IMGUR images.
	- Implementing IMGUR GALLERIES & ALBUMES.
	-  **Open with WebView for now** 
- Posts options such as view user, view subreddit, hide, share, open with browser, etc.
- Comments section (needs comments presenter like search and home views).

###Enhancement Required:

- Avoiding *Bitmap too large to fit* warnings.
- Controlling network calls as state changes.
- UI touch gestures.
- Checking Network Connection in the background.
- Caching.
- Dagger(2) Integration.

###License:

This project is under **GPL license**

# redgram-for-reddit
An Android Reddit client focusing primarily on media images/videos. 

--------------

Please head to the [ABOUT](/ABOUT.md) page to learn more about the project.

**Status**: Under development, and open for public reviews.

**Goal**: Using Android best practices to reach a solid base for further development.

For collaborations, please view [this issue](https://github.com/Redgram/redgram-for-reddit/issues/1) and follow the links, specifically the article on the suggested branching model.

###Latest:

- Image Manager with builder for a single image.
- Fixes an issue when loading more leads to duplicate posts by replacing existing ones with the newly added ones.
- Loads the user subreddits once. They can be accessed by clicking on the title of the home view.
- Implements Sliding Up Panel for all dynamic views.
	- Implemented to view images from cache and loading webpages.
	- Created a base class for fragments to implement panel control methods.
- Various UI improvements.

###Installation:

- Clone this repo.
- In AndroidStudio, *File > Open*.
- Choose the top level folder (Redgram).
- Gradle should automatically build the file. If not, navigation to *Build > ReBuild Project*.
- Run project.

###Todo:

- Follow the Program to Interface approach. [Read More](http://stackoverflow.com/questions/383947/what-does-it-mean-to-program-to-an-interface).
- User Authentication (needs its own branch).
- String Builder Manager to highlight text in post header (NSFW, username, GOLD, etc).
- Multiple Shared Preferences:
	- Mostly for configuring UI.
	- Requires a Setting Page.
- Implementing Videos (MP4 & Youtube).
- GFYCAT API.
- IMGUR API:
	- Implementing IMGUR images.
	- Implementing IMGUR GALLERIES & ALBUMES.
- Posts options such as view user, view subreddit, hide, share, open with browser, etc.
- Comments section (needs comments presenter like search and home views).

Earlier this summer:

- <del>Fix minor issues with search (mostly UI)
- <del>Create XML Layouts for the different list items.
- <del>Implement advanced search.
- <del>Implement front page with filters for Home fragment.
- <del>Implement a reliable Search functionality.
- <del>Cache doesn't seem to work. Investigate.
- <del>Learn/Integrate Dagger(2).

###License:

This project is under **GPL license**

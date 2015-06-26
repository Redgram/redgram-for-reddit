# redgram-for-reddit
An Android Reddit client focusing primarily on media images/videos. 

--------------

Please head to the [ABOUT](/ABOUT.md) page to learn more about the project. 

**Status**: Under development, and open for public reviews as well as collaboration.

**Goal**: Using Android best practices to reach a solid base for further development.

This project is under **GPL license**. Although other licenses are prefered, no change will be considered for the mean time.

For collaborations, please view [this issue](https://github.com/Redgram/redgram-for-reddit/issues/1) and follow the links, specifically the article on the suggested branching model.

###Latest:

- Caching fixed - [Implementation](/Redgram/app/src/main/java/com/matie/redgram/data/network/api/reddit/base/RedditServiceBase.java)
- Dagger 2 integration.

###Installation:

- Clone this repo.
- In AndroidStudio, *File > Open*.
- Choose the top level folder (Redgram).
- Gradle should automatically build the file. If not, navigation to *Build > ReBuild Project*.
- Run project.

###Todo:

- Implement a reliable Search functionality.
- Create XML Layouts for the different list items.
- <del>Cache doesn't seem to work. Investigate.
- <del>Learn/Integrate Dagger(2).
- Follow the Program to Interface approach. [Read More](http://stackoverflow.com/questions/383947/what-does-it-mean-to-program-to-an-interface).

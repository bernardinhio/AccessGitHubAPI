I developed this App to connect to the Github Api of one account / profile and then extract information and build the profile image in an Android app.

The other goal is to make a responsive and reactive interface dealing with the Items of te RecyclerView that is supposed to show information about a Github account such as his repositories and information about these repositories. But this information should not be shown in one time, but only on demand when the user clicks on each Item = one repository to get more information.

When this request of information happens by the user click, then a new Http request to the Api is created. So I Total in this Ap, there are 3 Http requests 

I made interactive approaches in thse places with a circular progress bar and a RecyclerView that fills itself when the data arrives.

I used the old fashion, not recommended approach, and little bit cumbersome method of using the native SDK class AsynchTask from Android in combination with OkHttp!

Naturally I would use Retrofit for that because I can make reusable Interface methods that all can have different types of requests or different End-points from the same base-url But Retrofit directly uses a parser and convert our json into Java classes so we don’t need to use OkHttp to get every node and element in the json. But OkHttp is very widely used in Android projects, so this is why I finally made the calls with it.

Home to input a github account url

![screenshot_1545286695](https://user-images.githubusercontent.com/20923486/50267425-813cec80-0427-11e9-8591-b910c9da6737.png) 

Calling the API for account and the API Avatar image, the image should load first then the call to account info occures

![screenshot_1545217164](https://user-images.githubusercontent.com/20923486/50216346-d1637280-0385-11e9-9788-79f16bb77258.png) 

Display combination of 2 API Calls

![screenshot_1545217182](https://user-images.githubusercontent.com/20923486/50216384-e8a26000-0385-11e9-9191-6526b25f443e.png) 


Filling RecyclerView using its adapter by a first call to repositories of the account, then on the individual level of each Item call the API ffor the list of commits, and process the commits to find the most early one then show data on the UI

First API call for repositories

![screenshot_1545376906](https://user-images.githubusercontent.com/20923486/50329962-9eda8680-04f9-11e9-9c5f-6478533e577f.png) 

Second API call to find the last commit while showing progress bar during process

![screenshot_1545377380](https://user-images.githubusercontent.com/20923486/50330231-b9f9c600-04fa-11e9-9139-431b07111d43.png) 

Show data when last commit is found and remove progress bar

![screenshot_1545377671](https://user-images.githubusercontent.com/20923486/50330404-54f2a000-04fb-11e9-9b13-c557d71ed3cc.png) 


------------- Technologies I used -------------

- OkHttp
- AsyncTask
- RecyclerView / holder / adapter
- Material design


------------- Future work -------------

- Refactoring reusing code for AsyncTask
- Showing circular progress bar at the RecyclerView each fragment "refreshing"
- changing architecture to use SQLite and Parcelable to avoid loading data but using local one 


------------- GitHub API calls -------------

To get account info
https://api.github.com/users/bernardinhio 

To get repositories of an account
https://api.github.com/users/bernardinhio/repos 

To get the commits of a repository
https://api.github.com/repos/bernardinhio/ScorePinsGame/commits 

Documentation GitHub API
https://developer.github.com/v3/search/ 
https://developer.github.com/v3/users/ 


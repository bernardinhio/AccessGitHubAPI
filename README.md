------------- Aims of this App-------------

The aim is to build an App that connects to the Github API, shows the public repositories of a particular user and then retrieves their respective last commits. The SDK targeted is 19.

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
![screenshot_1545376919](https://user-images.githubusercontent.com/20923486/50329990-c2053600-04f9-11e9-8264-93c9eca08907.png) 

Show data when last commit is found and remove progress bar
![screenshot_1545376931](https://user-images.githubusercontent.com/20923486/50330002-dcd7aa80-04f9-11e9-978d-42e6491126bb.png)


------------- Technologies I used -------------

- OkHttp
- AsyncTask
- RecyclerView / holder / adapter
- Material design
- SQLite (not yet done)


------------- Future work -------------

- collapsing / expanding fragments at the recycler view for more details (or other ui concept)
- Refactoring reusing code for AsyncTask
- Showing circular progress bar at the RecyclerView each fragment "refreshing"
- changing architecture to use SLite and Parcelable to avoid loading data but using local one 


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


------------- Persistence about only repositories -------------

Using SQLITE, the app first shows the content stored on the phone ()SQLite and then requests Github for new data then update the database and the UI


------------- Aims of this App-------------

The aim is to build an App that connects to the Github API, shows the public repositories of a particular user and then retrieves their respective last commits. The SDK targeted is 19.

Home to input url

![screenshot_1545217137](https://user-images.githubusercontent.com/20923486/50216288-a0833d80-0385-11e9-9d26-a3964fceca82.png)

Calling the API for account and Avatar image

![screenshot_1545217164](https://user-images.githubusercontent.com/20923486/50216346-d1637280-0385-11e9-9788-79f16bb77258.png)

Display combination of 2 API Calls

![screenshot_1545217182](https://user-images.githubusercontent.com/20923486/50216384-e8a26000-0385-11e9-9191-6526b25f443e.png)


Filling RecyclerView with data from 2 API calls for last commit and a all repositories of an account

![screenshot_1545217194](https://user-images.githubusercontent.com/20923486/50216439-0cfe3c80-0386-11e9-8fc8-3ea2057611e8.png)


------------- Technologies I used -------------

- OkHttp
- AsyncTask
- RecyclerView / holder / adapter
- Material design
- SQLite (not yet done)


------------- future work -------------

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


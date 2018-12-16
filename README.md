------------- Aims of this App-------------

The aim is to build an App that connects to the Github API, shows the public repositories of a particular user and then retrieves their respective last commits. The SDK targeted is 19.


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


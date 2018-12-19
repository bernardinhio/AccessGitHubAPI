package bernardo.bernardinhio.accessgithubapi.model

class Account(
        var id : String = "",
        var username : String = "",
        var createdAt : String = "",
        var htmlUrl : String = "",
        var apiUrl : String = "",
        var avatarUrl : String = "",
        var repositoriesApiUrl : String = "",
        var email : String = "", // to be completed once the call to repositories API occures
        var arrayListRepositories : ArrayList<Repository> = ArrayList<Repository>() // to be completed once the call to repositories API occures
){

}
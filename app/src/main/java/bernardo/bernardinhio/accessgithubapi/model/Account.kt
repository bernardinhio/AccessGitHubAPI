package bernardo.bernardinhio.accessgithubapi.model

class Account(
        val id : String = "",
        val username : String = "",
        val createdAt : String = "",
        val htmlUrl : String = "",
        val apiUrl : String = "",
        val avatarUrl : String = "",
        val repositoriesApiUrl : String = "",
        val email : String = "", // to be completed once the call to repositories API occures
        val repositories : ArrayList<Repository> = ArrayList<Repository>() // to be completed once the call to repositories API occures
){

}
package bernardo.bernardinhio.accessgithubapi.model

class Account(
        val id : Long = 0,
        val username : String = "",
        val email : String = "",
        val htmlUrl : String = "",  // ends with username
        val apiUrl : String = "",   // ends with username
        val avatarUrl : String = "",
        val repositories : ArrayList<Repository> = ArrayList<Repository>()
){

}
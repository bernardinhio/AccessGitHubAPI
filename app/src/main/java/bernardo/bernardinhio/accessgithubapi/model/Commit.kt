package bernardo.bernardinhio.accessgithubapi.model

class Commit(
        val shaID : String ="",
        val committer : Account = Account(),
        val date : String = "",
        val message : String = "",
        val apiUrl : String = "", // ends with shaID
        val htmlUrl : String = "" // ends with shaID

){

}
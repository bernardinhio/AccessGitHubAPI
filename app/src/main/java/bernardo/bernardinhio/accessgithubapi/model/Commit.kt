package bernardo.bernardinhio.accessgithubapi.model

class Commit(
        var shaID : String ="",
        var date : String = "",
        var message : String = "",
        var htmlUrl : String = "", // ends with shaID,
        var apiUrl : String = "", // ends with shaID
        var committer : Account = Account()
){

}
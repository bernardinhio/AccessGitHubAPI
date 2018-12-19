package bernardo.bernardinhio.accessgithubapi.model

import android.support.v13.view.inputmethod.InputConnectionCompat

class Repository(
        var id : String = "",
        var name : String = "",
        var createdAt : String = "",
        var updatedAt : String = "",
        var htmlUrl : String = "", // ends with name
        var apiUrl : String = "",  // ends with name
        var commitsApiUrl : String = "",
        var programingLanguage : String = "",
        var author : Account = Account(), // to be completed once the last commit is found so the account knows the email
        var lastCommit : Commit = Commit() // to be completed once the last commit is found

){

}
package bernardo.bernardinhio.accessgithubapi.model

import android.support.v13.view.inputmethod.InputConnectionCompat

class Repository(
        val id : Long = 0,
        val name : String = "",
        val apiUrl : String = "",  // ends with name
        val htmlUrl : String = "", // ends with name
        val programingLanguage : String = "",
        val owner : Account = Account(),
        val lastCommit : Commit = Commit()

){

}
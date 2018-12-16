package bernardo.bernardinhio.accessgithubapi;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * API CALLS DOC
 * https://developer.github.com/v3/search/
 * https://developer.github.com/v3/users/
 *
 * EXAMPLE CALLS my account
 * https://api.github.com/users/bernardinhio
 * https://api.github.com/users/bernardinhio/repos
 * https://api.github.com/repos/bernardinhio/ScorePinsGame/commits
 *
 * RETROFIT
 * https://www.simplifiedcoding.net/retrofit-android-example/
 * https://www.youtube.com/watch?v=Vn1vD4oC7sA
 * https://www.journaldev.com/23297/android-retrofit-okhttp-offline-caching
 *
 * OTHER LIBRARY
 * https://github.com/100rabhkr/GetJSON
 *
 * STACKOVERFLOW
 * https://stackoverflow.com/questions/36495004/github-api-integration-in-an-app
 *
 */
public class MainActivity extends AppCompatActivity {

    AsyncTaskGitHubAPI asyncTaskGitHubAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asyncTaskGitHubAPI = new AsyncTaskGitHubAPI("https://api.github.com/users/bernardinhio/repos");
    }

    public void showAccountRepositories(View view) {
        asyncTaskGitHubAPI.execute();
    }

    public void cancelAsyncTask(View view){
        asyncTaskGitHubAPI.cancel(true);
    }

    public void showPublicRepositories(View view) {
    }


    private static class AsyncTaskGitHubAPI extends AsyncTask<String, Void, String>{

        String urlString = "";

        AsyncTaskGitHubAPI(String urlString) {
            this.urlString = urlString;
        }

        @Override
        protected void onPreExecute() {
            // format her the url to make it an API call ex:
            // https://api.github.com/users/bernardinhio/repos
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient okHttpClient = new OkHttpClient();

            Request.Builder requestBuilder = new Request.Builder();

            requestBuilder.url(urlString);

            Request request = requestBuilder.build();

            Call call = okHttpClient.newCall(request);

            Response response = null;

            String stringResponseBody = "";

            try {

                response = call.execute();
                ResponseBody responseBody = response.body();
                stringResponseBody = responseBody != null ? responseBody.string() : null;
                Log.d("stringResponseBody", stringResponseBody);

            } catch (IOException exception) {
                Log.d("stringResponseBody", exception.getMessage());
                exception.printStackTrace();
            }

            return stringResponseBody;
        }


        @Override
        protected void onPostExecute(String stringResponseBody) {
            super.onPostExecute(stringResponseBody);

            Log.d("stringResponseBody", stringResponseBody);

            /**
            ArrayList<String> repositories = new ArrayList<String>();
            String lastComit = "";

            try {

                JSONObject jsonObject1 = new JSONObject(stringResponseBody);

                JSONArray firstArray = jsonObject1.getJSONArray("repos");

                JSONObject jsonObject2 = firstArray.getJSONObject(2);

                // fill here the data
                lastComit = jsonObject2.getString("ecgecveghvxgh");

            } catch (JSONException exception){

                Log.d("JSONException", exception.getMessage());
                // prints the stack trace of the Exception to System.err.
                exception.printStackTrace();
            }


            // set here textCView or recyclerview or any with the data collected
             **/

        }
    }
}
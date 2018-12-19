package bernardo.bernardinhio.accessgithubapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bernardo.bernardinhio.accessgithubapi.model.Account;
import bernardo.bernardinhio.accessgithubapi.model.Commit;
import bernardo.bernardinhio.accessgithubapi.model.Repository;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RepositoriesActivity extends AppCompatActivity {

    private String userId;
    private String username;
    private String createdAt;
    private String htmlUrl;
    private String apiUrl;
    private String avatarUrl;
    private String repositoriesApiUrl;

    static AdapterRV adapterRV;
    private static RecyclerView recyclerView;

    private ArrayList<Repository> arrayListRepositories = new ArrayList<Repository>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        getIntentValues();

        if (repositoriesApiUrl != null && !repositoriesApiUrl.isEmpty()){
            startAsyncTaskReposApi(repositoriesApiUrl);
        }

        setupRecyclerView();
        setAdapter();
    }


    private void getIntentValues(){
        Intent intent = this.getIntent();
        if (intent != null){
            userId = intent.getStringExtra("userId");
            username = intent.getStringExtra("username");
            createdAt = intent.getStringExtra("createdAt");
            htmlUrl = intent.getStringExtra("htmlUrl");
            apiUrl = intent.getStringExtra("apiUrl");
            avatarUrl = intent.getStringExtra("avatarUrl");
            repositoriesApiUrl = intent.getStringExtra("repositoriesApiUrl");

            this.setTitle(username + "'s repos");
        }
    }


    private void startAsyncTaskReposApi(String repositoriesApiUrl) {
        AsyncTaskReposApi asyncTaskReposApi = new AsyncTaskReposApi(repositoriesApiUrl);
        asyncTaskReposApi.execute();
    }

    private class AsyncTaskReposApi extends AsyncTask<String, Void, String> {

        private String reposApi = "";

        public AsyncTaskReposApi(String reposApi) {
            this.reposApi = reposApi;
        }

        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(reposApi);
            Request request = requestBuilder.build();
            Call call = okHttpClient.newCall(request);
            Response response = null;
            String stringResponseBody = "";

            try {
                response = call.execute();
                ResponseBody responseBody = response.body();
                stringResponseBody = responseBody != null ? responseBody.string() : null;
                Log.d("AT_repos_api", stringResponseBody);
            } catch (IOException exception) {
                Log.d("AT_repos_api", exception.getMessage());
                exception.printStackTrace();
            }
            return stringResponseBody;
        }

        @Override
        protected void onPostExecute(String stringResponseBody) {
            super.onPostExecute(stringResponseBody);

            Log.d("AT_repos_api", stringResponseBody);

            try {

                JSONArray jsonArray = new JSONArray(stringResponseBody);
                JSONObject jsonObject;
                String repositoryId;
                String repositoryName;
                String repositoryCreatedAt;
                String repositoryUpdatedAt;
                String repositoryHtmlUrl;
                String repositoryApiUrl;
                String commitsApiUrl = "";
                String repositoryProgrammingLanguage;

                Account ownerAccount = new Account(
                        userId,
                        username,
                        createdAt,
                        htmlUrl,
                        apiUrl,
                        avatarUrl,
                        repositoriesApiUrl,
                        "", // to be completed later after last commit call
                        new ArrayList<Repository>() // to be completed later after last commit call
                );

                Commit lastCommit = new Commit(
                        "",
                        ownerAccount,
                        "",
                        "",
                        "",
                        ""
                );

                Repository newRepository;

                for (int i = 0; i< jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);

                    repositoryId = String.valueOf(jsonObject.getLong("id"));
                    repositoryName = jsonObject.getString("name");
                    repositoryCreatedAt = jsonObject.getString("created_at");
                    repositoryUpdatedAt = jsonObject.getString("updated_at");
                    repositoryHtmlUrl = jsonObject.getString("html_url");
                    repositoryApiUrl = jsonObject.getString("url");
                    commitsApiUrl = "https://api.github.com/repos/" + username + "/" + repositoryName + "/commits";
                    // https://api.github.com/repos/bernardinhio/ScorePinsGame/commits
                    //commitsApiUrl = jsonObject.getString("commits_url");
                    repositoryProgrammingLanguage = jsonObject.getString("language");

                    newRepository = new Repository(
                            repositoryId,
                            repositoryName,
                            repositoryCreatedAt,
                            repositoryUpdatedAt,
                            repositoryHtmlUrl,
                            repositoryApiUrl,
                            commitsApiUrl,
                            repositoryProgrammingLanguage,
                            ownerAccount,
                            lastCommit
                    );

                    arrayListRepositories.add(newRepository);

                    // send the ownerAccount and lastCommit to be modified after the last commit is found
                    setLastCommit(i, commitsApiUrl, newRepository);
                }

                // now the list of repositories is completed, then update the account
                ownerAccount.setArrayListRepositories(arrayListRepositories);

            } catch (JSONException exception){
                Log.d("AT_repos_api", exception.getMessage());
                exception.printStackTrace();
            }

            adapterRV.notifyDataSetChanged();
        }
    }

    private void setLastCommit(final int currentPosition, final String commitsApiUrl, final Repository repository) {
        if (commitsApiUrl != null && !commitsApiUrl.isEmpty()){

            findLastCommentAndUpdateAccountAndRepository2(currentPosition, commitsApiUrl, repository);
            //findLastCommentAndUpdateAccountAndRepository(currentPosition, commitsApiUrl, repository);
        } else Toast.makeText(this, "Doesn't have commits!", Toast.LENGTH_SHORT).show();
    }



    private void findLastCommentAndUpdateAccountAndRepository2 (final int currentPosition, String commitsApiUrl, final Repository repository) {
        AsyncTaskCommitsApi asyncTaskCommitsApi = new AsyncTaskCommitsApi(currentPosition, commitsApiUrl, repository);
        asyncTaskCommitsApi.execute();
    }

    private class AsyncTaskCommitsApi extends AsyncTask<String, Void, String>{

        int currentPosition;
        String commitsApiUrl;
        Repository repository;

        public AsyncTaskCommitsApi(int currentPosition, String commitsApiUrl, Repository repository) {
            this.currentPosition = currentPosition;
            this.commitsApiUrl = commitsApiUrl;
            this.repository = repository;
        }

        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(commitsApiUrl);
            Request request = requestBuilder.build();
            Call call = okHttpClient.newCall(request);
            Response response = null;
            String stringResponseBody = "";

            try {
                response = call.execute();
                ResponseBody responseBody = response.body();
                stringResponseBody = responseBody != null ? responseBody.string() : null;
                Log.d("AT_commits_api", stringResponseBody);
            } catch (IOException exception) {
                Log.d("AT_commits_api", exception.getMessage());
                exception.printStackTrace();
            }
            return stringResponseBody;
        }

        @Override
        protected void onPostExecute(String stringResponseBody) {
            super.onPostExecute(stringResponseBody);

            // define var
            String commitshaID;
            String authorEmail; // to update Account
            String commitDate;
            String commitMessage;
            String commitHtmlUrl;
            String commitApiUrl;

            try {

                JSONArray jsonArrayAllCommits = new JSONArray(stringResponseBody);

                JSONObject jsonObjectCommit;
                JSONObject jsonObjectCommitInfo;
                JSONObject jsonObjectAuthor;

                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                JSONObject jsonObjectEarliestCommit = jsonArrayAllCommits.getJSONObject(0);
                long earliestDate = 0;

                for (int i = 0; i< jsonArrayAllCommits.length(); i++){
                    jsonObjectCommit = jsonArrayAllCommits.getJSONObject(i);
                    jsonObjectCommitInfo = jsonObjectCommit.getJSONObject("commit");
                    jsonObjectAuthor = jsonObjectCommitInfo.getJSONObject("author");
                    commitDate = jsonObjectAuthor.getString("date"); // 2018-11-17T05:11:40Z

                    try {
                        String[] parts1 = commitDate.split("T");
                        String part1 = parts1[0];
                        String part2 = parts1[1];
                        String[] parts2 =  part2.split("Z");
                        String part3 = parts2[0];
                        String result = part1 + " " + part3;

                        Date date = formatDate.parse(result); // 2018-11-17T05:11:40Z
                        String str = formatDate.format(date);
                        if(date.getTime() > earliestDate){
                            earliestDate = date.getTime();
                            jsonObjectEarliestCommit = jsonObjectCommit;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                jsonObjectCommitInfo = jsonObjectEarliestCommit.getJSONObject("commit");
                jsonObjectAuthor = jsonObjectCommitInfo.getJSONObject("author");
                commitDate = jsonObjectAuthor.getString("date");
                commitshaID = jsonObjectEarliestCommit.getString("sha");

                authorEmail = jsonObjectAuthor.getString("email");

                commitMessage = jsonObjectCommitInfo.getString("message");

                commitHtmlUrl = jsonObjectEarliestCommit.getString("html_url");
                commitApiUrl = jsonObjectEarliestCommit.getString("url");

                // update the email of Account
                repository.getAuthor().setEmail(authorEmail);

                // update the last commit
                Commit lastCommit = repository.getLastCommit();
                lastCommit.setShaID(commitshaID);
                lastCommit.setCommitter(repository.getAuthor());
                lastCommit.setDate(commitDate);
                lastCommit.setMessage(commitMessage);
                lastCommit.setHtmlUrl(commitHtmlUrl);
                lastCommit.setApiUrl(commitApiUrl);

                adapterRV.notifyDataSetChanged();
                recyclerView.getAdapter().notifyItemChanged(currentPosition);

            } catch (JSONException exception){

                Log.d("AT_commits", exception.getMessage());
                exception.printStackTrace();
            }

            recyclerView.getAdapter().notifyItemChanged(currentPosition);
        }

    }

    private void setupRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setAdapter(){
        adapterRV = new AdapterRV(arrayListRepositories);
        recyclerView.setAdapter(adapterRV);
        adapterRV.notifyDataSetChanged();
    }
}
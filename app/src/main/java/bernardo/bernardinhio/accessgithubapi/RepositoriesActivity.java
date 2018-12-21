package bernardo.bernardinhio.accessgithubapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RepositoriesActivity extends AppCompatActivity {

    private Account ownerAccount;
    private String ownerId;
    private String ownerUsername;
    private String ownerCreatedAt;
    private String ownerHtmlUrl;
    private String ownerApiUrl;
    private String ownerAvatarUrl;
    private String ownerRepositoriesApiUrl;

    static AdapterRV adapterRV;
    private static RecyclerView recyclerView;

    private ArrayList<Repository> arrayListRepositories = new ArrayList<Repository>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        getIntentAndSetOwnerFields();
        setOwnerAccount();

        if (ownerRepositoriesApiUrl != null && !ownerRepositoriesApiUrl.isEmpty()){
            startAsyncTaskReposApi(ownerRepositoriesApiUrl);
        }
    }

    private void getIntentAndSetOwnerFields(){
        Intent intent = this.getIntent();
        if (intent != null){
            ownerId = intent.getStringExtra("ownerId");
            ownerUsername = intent.getStringExtra("ownerUsername");
            ownerCreatedAt = intent.getStringExtra("ownerCreatedAt");
            ownerHtmlUrl = intent.getStringExtra("ownerHtmlUrl");
            ownerApiUrl = intent.getStringExtra("ownerApiUrl");
            ownerAvatarUrl = intent.getStringExtra("ownerAvatarUrl");
            ownerRepositoriesApiUrl = intent.getStringExtra("ownerRepositoriesApiUrl");

            this.setTitle(ownerUsername + "'s repos");
        }
    }

    private void setOwnerAccount(){
        ownerAccount = new Account(
                ownerId,
                ownerUsername,
                ownerCreatedAt,
                ownerHtmlUrl,
                ownerApiUrl,
                ownerAvatarUrl,
                ownerRepositoriesApiUrl,
                "",
                new ArrayList<Repository>() // to be completed later after last commit call
        );
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

                JSONArray jsonArrayRepositories = new JSONArray(stringResponseBody);
                JSONObject jsonObject;

                String repositoryId;
                String repositoryName;
                String repositoryCreatedAt;
                String repositoryUpdatedAt;
                String repositoryHtmlUrl;
                String repositoryApiUrl;
                String commitsApiUrl = "";
                String repositoryProgrammingLanguage;

                for (int i = 0; i< jsonArrayRepositories.length(); i++){
                    jsonObject = jsonArrayRepositories.getJSONObject(i);

                    repositoryId = String.valueOf(jsonObject.getLong("id"));
                    repositoryName = jsonObject.getString("name");
                    repositoryCreatedAt = jsonObject.getString("created_at");
                    repositoryUpdatedAt = jsonObject.getString("updated_at");
                    repositoryHtmlUrl = jsonObject.getString("html_url");
                    repositoryApiUrl = jsonObject.getString("url");
                    commitsApiUrl = "https://api.github.com/repos/" + ownerUsername + "/" + repositoryName + "/commits";
                    // ex: https://api.github.com/repos/bernardinhio/ScorePinsGame/commits
                    repositoryProgrammingLanguage = jsonObject.getString("language");

                    Repository newRepository = new Repository(
                            repositoryId,
                            repositoryName,
                            repositoryCreatedAt,
                            repositoryUpdatedAt,
                            repositoryHtmlUrl,
                            repositoryApiUrl,
                            commitsApiUrl,
                            repositoryProgrammingLanguage,
                            ownerAccount,
                            new Commit()
                    );

                    arrayListRepositories.add(newRepository);
                }

                // now the list of repositories is completed, then update the account
                ownerAccount.setArrayListRepositories(arrayListRepositories);

            } catch (JSONException exception){
                Log.d("AT_repos_api", exception.getMessage());
                exception.printStackTrace();
            }

            setupRecyclerView();
            setAdapter();
        }
    }

    public void showLastCommitInfo(View view) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findContainingViewHolder(view);
        if (viewHolder != null){
            int currentPosition = viewHolder.getAdapterPosition();

            // update ui for commit using adapter values
            arrayListRepositories.get(currentPosition).getLastCommit().setLastCommitLoading(true);
            arrayListRepositories.get(currentPosition).getLastCommit().setLastCommitDataAvailable(false);
            recyclerView.getAdapter().notifyItemChanged(currentPosition);
            
            String commitsApiUrl = arrayListRepositories.get(currentPosition).getCommitsApiUrl();

            if (!commitsApiUrl.isEmpty()){
                findLastCommit(currentPosition, commitsApiUrl, arrayListRepositories.get(currentPosition));
            }
        }
    }

    private void findLastCommit(final int currentPosition, final String commitsApiUrl, final Repository repository) {
        if (commitsApiUrl != null && !commitsApiUrl.isEmpty()){

             callCommitsApiAndFindLastCommit(currentPosition, commitsApiUrl, repository);
        } else Toast.makeText(this, "Doesn't have commits!", Toast.LENGTH_SHORT).show();
    }

    private void callCommitsApiAndFindLastCommit(final int currentPosition, String commitsApiUrl, final Repository repository) {
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
            String commitShaID;
            String committerName;
            String committerEmail;
            String commitDate;
            String commitMessage;
            String commitHtmlUrl;
            String commitApiUrl;

            try {

                JSONArray jsonArrayAllCommits = new JSONArray(stringResponseBody);

                JSONObject jsonObjectCommit;
                JSONObject jsonObjectCommitInfo;
                JSONObject jsonObjectCommitter;

                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                JSONObject jsonObjectLastCommit = new JSONObject();
                long lastCommitDate = 0;

                for (int i = 0; i< jsonArrayAllCommits.length(); i++){
                    jsonObjectCommit = jsonArrayAllCommits.getJSONObject(i);
                    jsonObjectCommitInfo = jsonObjectCommit.getJSONObject("commit");
                    jsonObjectCommitter = jsonObjectCommitInfo.getJSONObject("author");
                    commitDate = jsonObjectCommitter.getString("date"); // 2018-11-17T05:11:40Z

                    try {
                        String[] parts1 = commitDate.split("T");
                        String part1 = parts1[0];
                        String part2 = parts1[1];
                        String[] parts2 =  part2.split("Z");
                        String part3 = parts2[0];
                        String result = part1 + " " + part3;

                        Date date = formatDate.parse(result); // 2018-11-17T05:11:40Z
                        String str = formatDate.format(date);
                        if(date.getTime() > lastCommitDate){
                            lastCommitDate = date.getTime();
                            jsonObjectLastCommit = jsonObjectCommit;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                commitShaID = jsonObjectLastCommit.getString("sha");
                commitHtmlUrl = jsonObjectLastCommit.getString("html_url");
                commitApiUrl = jsonObjectLastCommit.getString("url");

                jsonObjectCommitInfo = jsonObjectLastCommit.getJSONObject("commit");
                commitMessage = jsonObjectCommitInfo.getString("message");

                jsonObjectCommitter = jsonObjectCommitInfo.getJSONObject("author");
                commitDate = jsonObjectCommitter.getString("date");
                committerName = jsonObjectCommitter.getString("name");
                committerEmail = jsonObjectCommitter.getString("email");

                // update the committer account name & email
                repository.getLastCommit().getCommitter().setUsername(committerName);
                repository.getLastCommit().getCommitter().setEmail(committerEmail);

                // update the last commit fields
                Commit lastCommit = repository.getLastCommit();
                lastCommit.setShaID(commitShaID);
                lastCommit.setDate(commitDate);
                lastCommit.setMessage(commitMessage);
                lastCommit.setHtmlUrl(commitHtmlUrl);
                lastCommit.setApiUrl(commitApiUrl);

                recyclerView.getAdapter().notifyItemChanged(currentPosition);

            } catch (JSONException exception){

                Log.d("AT_commits", exception.getMessage());
                exception.printStackTrace();
            }

            arrayListRepositories.get(currentPosition).getLastCommit().setLastCommitLoading(false);
            arrayListRepositories.get(currentPosition).getLastCommit().setLastCommitDataAvailable(true);
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
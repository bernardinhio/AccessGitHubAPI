package bernardo.bernardinhio.accessgithubapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private EditText etInputAccountUrl;
    private ProgressBar progressBar;
    private CircleImageView circleImageView;
    private TextView tvUsername, tvCreatedAt;
    private ConstraintLayout containerAccountInfo;
    private Button btnShowAccountRepositories;

    private String userId;
    private String username;
    private String createdAt;
    private String htmlUrl;
    private String apiUrl;
    private String avatarUrl;
    private String repositoriesApiUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        etInputAccountUrl = findViewById(R.id.input_account_url);
        progressBar = findViewById(R.id.progress_bar_account_page);
        circleImageView = this.findViewById(R.id.avatar);
        tvUsername = findViewById(R.id.tv_username);
        tvCreatedAt = findViewById(R.id.created_at);
        containerAccountInfo = findViewById(R.id.container_account_info);
        btnShowAccountRepositories = findViewById(R.id.show_account_repositories);
    }

    public void showAccountInfo(View view) {
        String accountHtmlUrl = String.valueOf(etInputAccountUrl.getText());
        if (!accountHtmlUrl.isEmpty()){

            String accountApiUrl = generateAccountApiUrlFromAccountJtmlUrl(accountHtmlUrl);

            AsyncTaskAccountProfile asyncTaskAccountProfile = new AsyncTaskAccountProfile(accountApiUrl);
            asyncTaskAccountProfile.execute();

        } else Toast.makeText(this, "Empty url", Toast.LENGTH_SHORT).show();
    }

    private String generateAccountApiUrlFromAccountJtmlUrl(String accountHtmlUrl){
        if (accountHtmlUrl.matches("^(http|https)://.*$")){
            String[] parts = accountHtmlUrl.split("https://github.com/");
            String part1 = parts[0]; // ex: "https://github.com/"
            String part2 = parts[1]; // ex: "bernardinhio"
            String accountApiUrl = "https://api.github.com/users/" + part2;
            etInputAccountUrl.setTextColor(Color.BLUE);
            return accountApiUrl;
        } else {
            Toast.makeText(this, "Account URL wrong", Toast.LENGTH_SHORT).show();
            etInputAccountUrl.setTextColor(Color.RED);
            return "";
        }
    }

    private class AsyncTaskAccountProfile extends AsyncTask<String, Void, String>{

        String accountApiUrl = "";
        // constructor
        AsyncTaskAccountProfile(String accountApiUrl) {
            this.accountApiUrl = accountApiUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);  // start showing progress bar
            containerAccountInfo.setVisibility(View.GONE);
            btnShowAccountRepositories.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(accountApiUrl);
            Request request = requestBuilder.build();
            Call call = okHttpClient.newCall(request);
            Response response = null;
            String stringResponseBody = "";

            try {
                response = call.execute();
                ResponseBody responseBody = response.body();
                stringResponseBody = responseBody != null ? responseBody.string() : null;
                Log.d("AT_profile_info", stringResponseBody);
            } catch (IOException exception) {
                Log.d("AT_profile_info", exception.getMessage());
                exception.printStackTrace();
            }
            return stringResponseBody;
        }

        @Override
        protected void onPostExecute(String stringResponseBody) {
            super.onPostExecute(stringResponseBody);
            Log.d("AT_profile_info", stringResponseBody);

            try {
                JSONObject jsonObject1 = new JSONObject(stringResponseBody);

                userId = String.valueOf(jsonObject1.getLong("id"));
                username = jsonObject1.getString("login");
                createdAt = jsonObject1.getString("created_at");
                htmlUrl = jsonObject1.getString("html_url");
                apiUrl = jsonObject1.getString("url");
                avatarUrl = jsonObject1.getString("avatar_url");
                repositoriesApiUrl = jsonObject1.getString("repos_url");

                callAvatarApiUrl(avatarUrl);

            } catch (JSONException exception){
                Log.d("JSONException", exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    private void callAvatarApiUrl(final String avatarUrl) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(avatarUrl);
        Request request = requestBuilder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException exception) {
                Log.d("AT_avatar_img", exception.getMessage());
                exception.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                InputStream inputStream = responseBody.byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        updateAccountPageUi(bitmap);
                    }
                };
                handler.post(runnable);
            }
        });
    }

    private void updateAccountPageUi(Bitmap bitmap){
        // modify the UI when the image is created
        circleImageView.setImageBitmap(bitmap);
        tvUsername.setText(username);
        tvCreatedAt.setText("Created: " + createdAt.substring(0,10));
        containerAccountInfo.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        btnShowAccountRepositories.setEnabled(true);
        // set on button repositories click listener
        setShowRepositoriesOnClickListener(repositoriesApiUrl);
    }

    private void setShowRepositoriesOnClickListener(final String repositoriesApiUrl){
        btnShowAccountRepositories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShowRepositoriesActivity();
            }
        });
    }

    private void startShowRepositoriesActivity(){

        Intent intent = new Intent(btnShowAccountRepositories.getContext(), RepositoriesActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("username", username);
        intent.putExtra("createdAt", createdAt);
        intent.putExtra("htmlUrl", htmlUrl);
        intent.putExtra("apiUrl", apiUrl);
        intent.putExtra("avatarUrl", avatarUrl);
        intent.putExtra("repositoriesApiUrl", repositoriesApiUrl);

        this.startActivities(new Intent[]{intent});
    }

}
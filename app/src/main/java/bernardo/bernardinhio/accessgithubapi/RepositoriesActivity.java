package bernardo.bernardinhio.accessgithubapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class RepositoriesActivity extends AppCompatActivity {

    private String userId;
    private String username;
    private String createdAt;
    private String htmlUrl;
    private String apiUrl;
    private String avatarUrl;
    private String repositoriesApiUrl;

    private RecyclerView recyclerView;
    private ArrayList<String> textArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        getIntentValues();
        setupRecyclerView();
        addElementsToDataProvider();
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

    private void setupRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void addElementsToDataProvider(){
        textArrayList.add(userId);
        textArrayList.add(username);
        textArrayList.add(createdAt);
        textArrayList.add(htmlUrl);
        textArrayList.add(apiUrl);
        textArrayList.add(avatarUrl);
        textArrayList.add(repositoriesApiUrl);
    }

    private void setAdapter(){
        AdapterRV adapterRV = new AdapterRV(textArrayList);
        recyclerView.setAdapter(adapterRV);
        adapterRV.notifyDataSetChanged();
    }
}

package bernardo.bernardinhio.accessgithubapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class RepositoriesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> textArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        textArrayList.add("eferfgerrgyju");
        textArrayList.add("zzzzzzzzzzzzzzzz");
        textArrayList.add("xxxxxxxxxxxxxxxxxxxxx");
        textArrayList.add("iiiiiiiiiiiiiiiiiii");


        AdapterRV adapterRV = new AdapterRV(textArrayList);

        recyclerView.setAdapter(adapterRV);

        adapterRV.notifyDataSetChanged();

    }
}

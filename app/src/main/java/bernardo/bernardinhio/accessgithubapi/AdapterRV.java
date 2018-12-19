package bernardo.bernardinhio.accessgithubapi;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bernardo.bernardinhio.accessgithubapi.model.Repository;

public class AdapterRV extends RecyclerView.Adapter <AdapterRV.ViewHolderRV>{

    private ArrayList<Repository> arrayListRepositories;

    AdapterRV(ArrayList<Repository> arrayListRepositories) {
        this.arrayListRepositories = arrayListRepositories;
    }

    @Override
    public ViewHolderRV onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View viewInflated = layoutInflater.inflate(R.layout.fragment_repository_details, parent, false);

        return new ViewHolderRV(viewInflated);
    }

    @Override
    public void onBindViewHolder(final ViewHolderRV holder, int position) {

        Repository repository = arrayListRepositories.get(position);

        holder.tvRepositoryName.setText(repository.getName());
        holder.tvHtmlUrl.setText(repository.getHtmlUrl());
        holder.tvProgramingLanguage.setText(repository.getProgramingLanguage());
        holder.tvCreatedAt.setText("Created:" + repository.getCreatedAt().substring(0,10));
        holder.tvUpdatedAt.setText("Updated:" + repository.getUpdatedAt().substring(0,10));
        holder.tvDateCommit.setText("Committed: " + repository.getUpdatedAt().substring(0,10));
        holder.tvUsernameCommitter.setText(repository.getAuthor().getUsername());
        holder.tvEmailCommitter.setText("By:" + repository.getAuthor().getEmail());
        holder.tvMessageCommit.setText(repository.getLastCommit().getMessage());
        holder.tvCommitUrl.setText(repository.getLastCommit().getHtmlUrl());

        holder.btnShowLastCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(holder.btnShowLastCommit.getContext(), "clicked", Toast.LENGTH_SHORT).show();
                //holder.containerLastCommit.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListRepositories.size();
    }


    class ViewHolderRV extends RecyclerView.ViewHolder{

        private ProgressBar progressBar;
        private TextView tvRepositoryName;
        private TextView tvHtmlUrl;
        private TextView tvProgramingLanguage;
        private TextView tvCreatedAt;
        private TextView btnShowLastCommit;
        private TextView tvUpdatedAt;
        private ConstraintLayout containerLastCommit;
        private TextView tvDateCommit;
        private TextView tvUsernameCommitter;
        private TextView tvEmailCommitter;
        private TextView tvMessageCommit;
        private TextView tvCommitUrl;

        ViewHolderRV(View itemView) {
            super(itemView);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar_account_page);
            this.tvRepositoryName = (TextView) itemView.findViewById(R.id.repo_name);
            this.tvHtmlUrl = (TextView) itemView.findViewById(R.id.html_url);
            this.tvProgramingLanguage = (TextView) itemView.findViewById(R.id.programing_language);
            this.tvCreatedAt = (TextView) itemView.findViewById(R.id.created_at);
            this.btnShowLastCommit = (TextView) itemView.findViewById(R.id.tv_show_last_commit);
            this.tvUpdatedAt = (TextView) itemView.findViewById(R.id.updated_at);
            this.containerLastCommit = (ConstraintLayout) itemView.findViewById(R.id.container_account_info);
            this.tvDateCommit = (TextView) itemView.findViewById(R.id.date_commit);
            this.tvUsernameCommitter = (TextView) itemView.findViewById(R.id.username_committer);
            this.tvEmailCommitter = (TextView) itemView.findViewById(R.id.email_committer);
            this.tvMessageCommit = (TextView) itemView.findViewById(R.id.message_commit);
            this.tvCommitUrl = (TextView) itemView.findViewById(R.id.commit_url);
        }
    }
}
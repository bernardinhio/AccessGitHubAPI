package bernardo.bernardinhio.accessgithubapi;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import bernardo.bernardinhio.accessgithubapi.model.Commit;
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
        holder.tvRepoName.setText(repository.getName());
        holder.tvRepoHtmlUrl.setText(repository.getHtmlUrl());
        holder.tvRepoProgramingLanguage.setText(repository.getProgramingLanguage());
        holder.tvRepoCreatedAt.setText("Created:" + repository.getCreatedAt().substring(0,10));
        holder.tvRepoUpdatedAt.setText("Updated:" + repository.getUpdatedAt().substring(0,10));

        Commit lastCommit = repository.getLastCommit();
        if (!lastCommit.getMessage().isEmpty()){
            holder.tvCommitterUsername.setText("By: " + lastCommit.getCommitter().getUsername());
            holder.tvCommitterEmail.setText(lastCommit.getCommitter().getEmail());
            holder.tvCommitDate.setText( (lastCommit.getDate().isEmpty()) ? "Committed: " : "Committed: " + lastCommit.getDate().substring(0,10) );
            holder.tvCommitMessage.setText(lastCommit.getMessage());
            holder.tvCommitUrl.setText(lastCommit.getHtmlUrl());
        }

    }

    @Override
    public int getItemCount() {
        return arrayListRepositories.size();
    }

    class ViewHolderRV extends RecyclerView.ViewHolder{

        private ProgressBar progressBar;
        private TextView tvRepoName;
        private TextView tvRepoHtmlUrl;
        private TextView tvRepoProgramingLanguage;
        private TextView tvRepoCreatedAt;
        private TextView btnShowLastCommit;
        private TextView tvRepoUpdatedAt;
        private ConstraintLayout containerLastCommit;
        private TextView tvCommitDate;
        private TextView tvCommitterUsername;
        private TextView tvCommitterEmail;
        private TextView tvCommitMessage;
        private TextView tvCommitUrl;

        ViewHolderRV(View itemView) {
            super(itemView);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar_account_page);
            this.tvRepoName = (TextView) itemView.findViewById(R.id.repo_name);
            this.tvRepoHtmlUrl = (TextView) itemView.findViewById(R.id.html_url);
            this.tvRepoProgramingLanguage = (TextView) itemView.findViewById(R.id.programing_language);
            this.tvRepoCreatedAt = (TextView) itemView.findViewById(R.id.created_at);
            this.btnShowLastCommit = (TextView) itemView.findViewById(R.id.tv_show_last_commit);
            this.tvRepoUpdatedAt = (TextView) itemView.findViewById(R.id.updated_at);
            this.containerLastCommit = (ConstraintLayout) itemView.findViewById(R.id.container_account_info);
            this.tvCommitDate = (TextView) itemView.findViewById(R.id.date_commit);
            this.tvCommitterUsername = (TextView) itemView.findViewById(R.id.username_committer);
            this.tvCommitterEmail = (TextView) itemView.findViewById(R.id.email_committer);
            this.tvCommitMessage = (TextView) itemView.findViewById(R.id.message_commit);
            this.tvCommitUrl = (TextView) itemView.findViewById(R.id.commit_url);
        }
    }
}
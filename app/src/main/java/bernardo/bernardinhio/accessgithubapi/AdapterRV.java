package bernardo.bernardinhio.accessgithubapi;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRV extends RecyclerView.Adapter <AdapterRV.ViewHolderRV>{

    private ArrayList<String> textArrayList;

    AdapterRV(ArrayList<String> textArrayList) {
        this.textArrayList = textArrayList;
    }

    private ArrayList<String> getTextArrayList() {
        return this.textArrayList;
    }

    @Override
    public ViewHolderRV onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View viewInflated = layoutInflater.inflate(R.layout.fragment_text, parent, false);

        return new ViewHolderRV(viewInflated);
    }

    @Override
    public void onBindViewHolder(ViewHolderRV holder, int position) {
        holder.getTvRepositoryName().setText(this.getTextArrayList().get(position));
    }

    @Override
    public int getItemCount() {
        return this.getTextArrayList().size();
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
        }

        TextView getTvRepositoryName() {
            return this.tvRepositoryName;
        }
    }
}
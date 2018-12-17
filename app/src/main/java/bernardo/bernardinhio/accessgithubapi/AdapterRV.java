package bernardo.bernardinhio.accessgithubapi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.getTextView().setText(this.getTextArrayList().get(position));
    }

    @Override
    public int getItemCount() {
        return this.getTextArrayList().size();
    }

    class ViewHolderRV extends RecyclerView.ViewHolder{

        private TextView textView;

        ViewHolderRV(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.text_view);
        }

        TextView getTextView() {
            return this.textView;
        }
    }
}

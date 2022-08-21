package ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.leaveAppDialogues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.ShowShiftAdapter;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.ShowShiftList;

public class ForwardHistoryAdapter extends RecyclerView.Adapter<ForwardHistoryAdapter.FHVHolder> {

    public ArrayList<ForwardHistoryList> forwardHistoryLists;

    public Context myContext;


    public ForwardHistoryAdapter(ArrayList<ForwardHistoryList> forwardHistoryLists, Context myContext) {
        this.forwardHistoryLists = forwardHistoryLists;
        this.myContext = myContext;
    }


    @NonNull
    @Override
    public FHVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.forward_history_list_item, parent, false);
        FHVHolder ammvh = new FHVHolder(v);
        return ammvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FHVHolder holder, int position) {

        ForwardHistoryList jobdddd = forwardHistoryLists.get(position);

        holder.foby.setText(jobdddd.getForby());
        holder.foc.setText(jobdddd.getForComm());
        holder.fot.setText(jobdddd.getForTo());

    }

    @Override
    public int getItemCount() {
        return forwardHistoryLists.size();
    }

    public class FHVHolder extends RecyclerView.ViewHolder {

        private TextView foby;
        private TextView foc;
        private TextView fot;



        public FHVHolder(@NonNull View itemView) {
            super(itemView);

            foby = itemView.findViewById(R.id.forwarded_by_leave_approve);
            foc = itemView.findViewById(R.id.forward_comment_leave_approve);
            fot = itemView.findViewById(R.id.forward_to_leave_approve);



        }
    }

}

package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;

public class LeaveBalanceFroAPPAdapter extends RecyclerView.Adapter<LeaveBalanceFroAPPAdapter.BalanceHolder> {

    public ArrayList<LeaveBalanceForAPPList> leaveBalanceForAPPLists;

    public Context myContext;


    public LeaveBalanceFroAPPAdapter(ArrayList<LeaveBalanceForAPPList> leaveBalanceForAPPLists, Context myContext) {
        this.leaveBalanceForAPPLists = leaveBalanceForAPPLists;
        this.myContext = myContext;
    }


    @NonNull
    @Override
    public BalanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.leave_balance_list_two_items, parent, false);
        BalanceHolder ammvh = new BalanceHolder(v);
        return ammvh;
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceHolder holder, int position) {

        LeaveBalanceForAPPList jobdddd = leaveBalanceForAPPLists.get(position);

        holder.name.setText(jobdddd.getName());
        holder.balance.setText(jobdddd.getQty());


    }

    @Override
    public int getItemCount() {
        return leaveBalanceForAPPLists.size();
    }

    public class BalanceHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView balance;



        public BalanceHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.leave_name_category);
            balance = itemView.findViewById(R.id.leave_balance_total);


        }
    }


}

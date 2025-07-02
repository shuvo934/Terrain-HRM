package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication.leaveApplicatinStatus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.attendance.status.StatusList;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication.leaveApplicatinStatus.details.LeaveAppStatusDetails;

public class LeaveAppStatusAdapter extends RecyclerView.Adapter<LeaveAppStatusAdapter.LeaAppStatusHolder> {

    public ArrayList<StatusList> statusLists;

    public Context myContext;

    String leaveCode = "";
    String leave_app_Status = "";
    String lea_type = "";
    String from_date = "";
    String to_date = "";
    String totalDays = "";
    String approverrrrrrrr = "";

    public LeaveAppStatusAdapter(ArrayList<StatusList> statusLists, Context myContext) {
        this.statusLists = statusLists;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public LeaAppStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.leave_app_status_item_lists, parent, false);
        return new LeaAppStatusHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaAppStatusHolder holder, int position) {

        StatusList jobdddd = statusLists.get(position);

        holder.code_no.setText(jobdddd.getApp_code());
        String approve = jobdddd.getApprover();
        String stat = jobdddd.getApproved();

        switch (stat) {
            case "0":
                stat = "PENDING";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#636e72"));
                holder.appLay.setVisibility(View.GONE);
                holder.approver.setText("");
                break;
            case "1":
                stat = "APPROVED";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#1abc9c"));
                holder.approver.setText(approve);
                holder.appLay.setVisibility(View.VISIBLE);
                String at = "Approved By:";
                holder.appcan.setText(at);
                break;
            case "2":
                stat = "REJECTED";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#d63031"));
                holder.approver.setText(approve);
                holder.appLay.setVisibility(View.VISIBLE);
                String rt = "Rejected By:";
                holder.appcan.setText(rt);
                break;
            case "3":
                stat = "CANCEL APPROVED LEAVE";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#ff7675"));
                String cancel = jobdddd.getCanceler();
                if (cancel != null) {
                    holder.approver.setText(cancel);
                    holder.appLay.setVisibility(View.VISIBLE);
                    String ct = "Cancelled By:";
                    holder.appcan.setText(ct);
                } else {
                    holder.appLay.setVisibility(View.GONE);
                    holder.approver.setText("");
                }

                break;
        }

        holder.status.setText(stat);
        holder.appDate.setText(jobdddd.getReq_date());
        holder.leaTyp.setText(jobdddd.getReq_type());
        holder.fromDate.setText(jobdddd.getUp_date());
        holder.toDate.setText(jobdddd.getArr_time());
        holder.total.setText(jobdddd.getDep_time());

//        if (approve != null) {
//            holder.approver.setText(approve);
//            holder.appLay.setVisibility(View.VISIBLE);
//        } else {
//            holder.appLay.setVisibility(View.GONE);
//            holder.approver.setText("");
//        }


    }

    @Override
    public int getItemCount() {
        return statusLists.size();
    }

    public class LeaAppStatusHolder extends RecyclerView.ViewHolder {

        TextView code_no;
        TextView status;
        TextView appDate;
        TextView leaTyp;
        TextView fromDate;
        TextView toDate;
        TextView total;
        TextView approver;
        TextView appcan;

        LinearLayout appLay;

        CardView cardView;

        MaterialButton details;


        public LeaAppStatusHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.leave_app_status_card);
            code_no = itemView.findViewById(R.id.leave_code_status);
            status = itemView.findViewById(R.id.leave_app_status_status);
            appDate = itemView.findViewById(R.id.application_date_status);
            leaTyp = itemView.findViewById(R.id.leave_type_status);
            fromDate = itemView.findViewById(R.id.application_from_date_status);
            toDate = itemView.findViewById(R.id.application_to_date_status);
            total = itemView.findViewById(R.id.total_leave_status);
            approver = itemView.findViewById(R.id.application_status_approver_name_by);
            appcan = itemView.findViewById(R.id.name_of_approver_canceler);

            appLay = itemView.findViewById(R.id.applicatin_status_approver_layout);
            details = itemView.findViewById(R.id.button_application_status_all);

            details.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                leaveCode = code_no.getText().toString();
                leave_app_Status = status.getText().toString();
                lea_type = leaTyp.getText().toString();
                from_date = fromDate.getText().toString();
                to_date = toDate.getText().toString();
                totalDays = total.getText().toString();

                approverrrrrrrr = approver.getText().toString();

                Intent intent = new Intent(myContext, LeaveAppStatusDetails.class);
                intent.putExtra("LEAVE", leaveCode);
                intent.putExtra("STATUS", leave_app_Status);
                intent.putExtra("LEAVE_TYPE", lea_type);
                intent.putExtra("FROM_DATE", from_date);
                intent.putExtra("TO_DATE", to_date);
                intent.putExtra("TOTAL", totalDays);
                intent.putExtra("APPROVER",approverrrrrrrr);
                activity.startActivity(intent);
            });

        }
    }

}

package ttit.com.shuvo.ikglhrm.attendance.status;

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
import ttit.com.shuvo.ikglhrm.attendance.status.statusDetail.AttendanceStatusDetails;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusView> {

    public ArrayList<StatusList> statusLists;

    public Context myContext;




    public StatusAdapter(ArrayList<StatusList> statusLists, Context myContext) {
        this.statusLists = statusLists;
        this.myContext = myContext;
    }


    @NonNull
    @Override
    public StatusView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.status_item_list, parent, false);
        return new StatusView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusView holder, int position) {

        StatusList jobdddd = statusLists.get(position);

        holder.req_no.setText(jobdddd.getApp_code());
        String approve = jobdddd.getApprover();
        String stat = jobdddd.getApproved();

        switch (stat) {
            case "0":
                stat = "PENDING";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#636e72"));
                holder.appLay.setVisibility(View.GONE);
                holder.approver.setText("");
                break;
            case "1": {
                stat = "APPROVED";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#1abc9c"));
                holder.approver.setText(approve);
                holder.appLay.setVisibility(View.VISIBLE);
                String tt = "Approved By:";
                holder.appRej.setText(tt);
                break;
            }
            case "2": {
                stat = "REJECTED";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#c0392b"));
                holder.approver.setText(approve);
                holder.appLay.setVisibility(View.VISIBLE);
                String tt = "Rejected By:";
                holder.appRej.setText(tt);
                break;
            }
        }
        holder.status.setText(stat);
        holder.reqDate.setText(jobdddd.getReq_date());
        holder.reqType.setText(jobdddd.getReq_type());
        holder.upDate.setText(jobdddd.getUp_date());
        holder.arr.setText(jobdddd.getArr_time());
        holder.dept.setText(jobdddd.getDep_time());

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

    public class StatusView extends RecyclerView.ViewHolder {

        TextView req_no;
        TextView status;
        TextView reqDate;
        TextView reqType;
        TextView upDate;
        TextView arr;
        TextView dept;
        TextView approver;
        TextView appRej;

        LinearLayout appLay;

        CardView cardView;

        MaterialButton details;


        public StatusView(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.status_card);
            req_no = itemView.findViewById(R.id.request_code_status);
            status = itemView.findViewById(R.id.status_status);
            reqDate = itemView.findViewById(R.id.request_date_status);
            reqType = itemView.findViewById(R.id.request_type_status);
            upDate = itemView.findViewById(R.id.request_update_date_status);
            arr = itemView.findViewById(R.id.arrival_time_status);
            dept = itemView.findViewById(R.id.departure_time_status);
            approver = itemView.findViewById(R.id.approver_name_by);
            appRej = itemView.findViewById(R.id.text_app_rej);

            appLay = itemView.findViewById(R.id.approver_layout);
            details = itemView.findViewById(R.id.button_status_all);

            details.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                String reqNo;
                String att_Status;
                String app_Date;
                String update_Date;
                String arrTime;
                String depTime;
                String approverrrrr;
                reqNo = req_no.getText().toString();
                att_Status = status.getText().toString();
                app_Date = reqDate.getText().toString();
                update_Date = upDate.getText().toString();
                arrTime = arr.getText().toString();
                depTime = dept.getText().toString();

                approverrrrr = approver.getText().toString();

                Intent intent = new Intent(myContext, AttendanceStatusDetails.class);
                intent.putExtra("Request", reqNo);
                intent.putExtra("Status", att_Status);
                intent.putExtra("APP_DATE", app_Date);
                intent.putExtra("UPDATE_DATE", update_Date);
                intent.putExtra("ARRIVAL", arrTime);
                intent.putExtra("DEPARTURE", depTime);
                intent.putExtra("APPROVER",approverrrrr);
                activity.startActivity(intent);
            });
        }
    }
}

package ttit.com.shuvo.ikglhrm.attendance.update.dialogue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobAdapter;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobDescDetails;
import ttit.com.shuvo.ikglhrm.R;

public class ShowShiftAdapter extends RecyclerView.Adapter<ShowShiftAdapter.ShowShiftHolder> {

    public ArrayList<ShowShiftList> showShiftLists;

    public Context myContext;


    public ShowShiftAdapter(ArrayList<ShowShiftList> showShiftLists, Context myContext) {
        this.showShiftLists = showShiftLists;
        this.myContext = myContext;
    }


    @NonNull
    @Override
    public ShowShiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.shift_in_details_view, parent, false);
        ShowShiftHolder ammvh = new ShowShiftHolder(v);
        return ammvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowShiftHolder holder, int position) {

        ShowShiftList jobdddd = showShiftLists.get(position);

        holder.shift.setText(jobdddd.getShift());
        holder.inTime.setText(jobdddd.getInTime());
        holder.lateArr.setText(jobdddd.getLateART());
        holder.early.setText(jobdddd.getEarlyBT());
        holder.ouutt.setText(jobdddd.getOutTime());
        holder.extend.setText(jobdddd.getExtendedOT());

    }

    @Override
    public int getItemCount() {
        return showShiftLists.size();
    }

    public class ShowShiftHolder extends RecyclerView.ViewHolder {

        private TextView shift;
        private TextView inTime;
        private TextView lateArr;
        private TextView early;
        private TextView ouutt;
        private TextView extend;


        public ShowShiftHolder(@NonNull View itemView) {
            super(itemView);

            shift = itemView.findViewById(R.id.shift_name_from_list);
            inTime = itemView.findViewById(R.id.in_time_from_list);
            lateArr = itemView.findViewById(R.id.late_arr_time_from_list);
            early = itemView.findViewById(R.id.early_bef_time_from_list);
            ouutt = itemView.findViewById(R.id.out_time_from_list);
            extend = itemView.findViewById(R.id.extended_out_time_from_list);


        }
    }


}

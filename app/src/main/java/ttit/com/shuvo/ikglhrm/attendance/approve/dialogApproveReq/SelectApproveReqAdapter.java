package ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;


public class SelectApproveReqAdapter extends RecyclerView.Adapter<SelectApproveReqAdapter.ApproveHolder> {

    private ArrayList<SelectApproveReqList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;


    public SelectApproveReqAdapter(ArrayList<SelectApproveReqList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class ApproveHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ftext;
        public TextView stext;
        public TextView ttext;
        public TextView fotext;
        public TextView fitext;

        ClickedItem mClickedItem;

        public ApproveHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            ftext = itemView.findViewById(R.id.app_code_approve);
            stext = itemView.findViewById(R.id.emp_name_approve);
            ttext = itemView.findViewById(R.id.emp_id_approve);
            fotext = itemView.findViewById(R.id.app_date_approve);
            fitext = itemView.findViewById(R.id.update_date_approve);


            this.mClickedItem = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getReqCode());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public ApproveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.approval_request_view, parent, false);
        ApproveHolder categoryViewHolder = new ApproveHolder(view, myClickedItem);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveHolder holder, int position) {

        SelectApproveReqList categoryItem = mCategoryItem.get(position);

        holder.ftext.setText(categoryItem.getReqCode());
        holder.stext.setText(categoryItem.getName());
        holder.ttext.setText(categoryItem.getId());
        holder.fotext.setText(categoryItem.getAppdate());
        holder.fitext.setText(categoryItem.getUpDate());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<SelectApproveReqList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

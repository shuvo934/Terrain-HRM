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

public class ForwardAdapter extends RecyclerView.Adapter<ForwardAdapter.FHolder> {

    private final ArrayList<ForwardEMPList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public ForwardAdapter(ArrayList<ForwardEMPList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class FHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ftext;
        public TextView stext;
        public TextView ttext;
        public TextView fotext;

        ClickedItem mClickedItem;

        public FHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            ftext = itemView.findViewById(R.id.emp_name_forward);
            stext = itemView.findViewById(R.id.title_emp_forward);
            ttext = itemView.findViewById(R.id.designation_forward);
            fotext = itemView.findViewById(R.id.division_forward);



            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getEmpID());
        }

    }
    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public FHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.forward_req_list_item, parent, false);
        return new FHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FHolder holder, int position) {

        ForwardEMPList categoryItem = mCategoryItem.get(position);

        holder.ftext.setText(categoryItem.getEmpName());
        holder.stext.setText(categoryItem.getEmpTitle());
        holder.ttext.setText(categoryItem.getDesignation());
        holder.fotext.setText(categoryItem.getDivision());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

}

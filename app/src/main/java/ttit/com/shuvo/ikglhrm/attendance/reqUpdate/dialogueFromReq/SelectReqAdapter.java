package ttit.com.shuvo.ikglhrm.attendance.reqUpdate.dialogueFromReq;

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

public class SelectReqAdapter extends RecyclerView.Adapter<SelectReqAdapter.ReqViewHolder> {

    private ArrayList<SelectReqList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;


    public SelectReqAdapter(ArrayList<SelectReqList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class ReqViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ftext;
        public TextView stext;
        public TextView ttext;
        public TextView fotext;
        public TextView fitext;

        ClickedItem mClickedItem;

        public ReqViewHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            ftext = itemView.findViewById(R.id.req_first_item);
            stext = itemView.findViewById(R.id.req_second_item);
            ttext = itemView.findViewById(R.id.req_third_item);
            fotext = itemView.findViewById(R.id.req_fourth_item);
            fitext = itemView.findViewById(R.id.req_fifth_item);


            this.mClickedItem = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getDarm_id());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public ReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.request_itrem_list_view, parent, false);
        ReqViewHolder categoryViewHolder = new ReqViewHolder(view, myClickedItem);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReqViewHolder holder, int position) {

        SelectReqList categoryItem = mCategoryItem.get(position);

        holder.ftext.setText(categoryItem.getDarm_app_code());
        holder.stext.setText(categoryItem.getDarm_date());
        holder.ttext.setText(categoryItem.getUpdate_time());
        holder.fotext.setText(categoryItem.getArrival());
        holder.fitext.setText(categoryItem.getDeparture());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<SelectReqList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

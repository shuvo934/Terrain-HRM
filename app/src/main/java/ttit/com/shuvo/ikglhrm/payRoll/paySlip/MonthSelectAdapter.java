package ttit.com.shuvo.ikglhrm.payRoll.paySlip;

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


public class MonthSelectAdapter extends RecyclerView.Adapter<MonthSelectAdapter.MonthHolder> {

    private ArrayList<MonthSelectList> monthSelectLists;
    private final ClickedItem myClickedItem;
    private final Context myContext;


    public MonthSelectAdapter(ArrayList<MonthSelectList> monthSelectLists, Context context, ClickedItem cli) {
        this.monthSelectLists = monthSelectLists;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class MonthHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView monthName;
        public TextView monStart;
        public TextView monEnd;


        ClickedItem mClickedItem;

        public MonthHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            monthName = itemView.findViewById(R.id.month_name_mmm);
            monStart = itemView.findViewById(R.id.month_start);
            monEnd = itemView.findViewById(R.id.month_end);



            this.mClickedItem = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", monthSelectLists.get(getAdapterPosition()).getMonthName());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public MonthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.month_select_list, parent, false);
        MonthHolder categoryViewHolder = new MonthHolder(view, myClickedItem);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MonthHolder holder, int position) {

        MonthSelectList categoryItem = monthSelectLists.get(position);

        holder.monthName.setText(categoryItem.getMonthName());
        holder.monStart.setText(categoryItem.getMonthstart());
        holder.monEnd.setText(categoryItem.getMonthEnd());


    }

    @Override
    public int getItemCount() {
        return monthSelectLists.size();
    }

    public void filterList(ArrayList<MonthSelectList> filteredList) {
        monthSelectLists = filteredList;
        notifyDataSetChanged();
    }
}

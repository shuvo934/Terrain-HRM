package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.errorReason;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.isOtherReason;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.leaveType;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.leaveTypeLay;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.otherReasonLay;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.reason;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.selected_leave_type_id;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.selected_leave_type_name;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.selected_reason;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.selectingIndivdual;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.showD;

public class SelectLeaveType extends AppCompatDialogFragment implements LeaveTypeAdapter.ClickedItem {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LeaveTypeAdapter selectAllAdapter;

    TextInputEditText search;
    AlertDialog dialog;
    TextView ftext;

    Boolean isfiltered = false;
    ArrayList<LeaveTypeList> filteredList = new ArrayList<>();
    private ArrayList<LeaveTypeList> lists = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.leave_type_list, null);

        recyclerView = view.findViewById(R.id.all_leave_type_list);

        ftext = view.findViewById(R.id.name_of_type_reason);

        search = view.findViewById(R.id.leave_type_search);

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        lists = new ArrayList<>();

        if (showD == 1) {
            String ft = "Leave Type";
            ftext.setText(ft);
        } else if (showD == 2) {
            String ft = "Leave Reason";
            ftext.setText(ft);
        }
        lists = selectingIndivdual;

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        selectAllAdapter = new LeaveTypeAdapter(lists, getContext(),this);
        recyclerView.setAdapter(selectAllAdapter);

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> {

            showD = 0;
            dialog.dismiss();
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });

        return dialog;


    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (LeaveTypeList item : lists) {
            if (item.getTypeName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        selectAllAdapter.filterList(filteredList);
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        String name;
        String id;
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getTypeName();
            id = filteredList.get(CategoryPosition).getId();
        } else {
            name = lists.get(CategoryPosition).getTypeName();
            id = lists.get(CategoryPosition).getId();
        }

        System.out.println(name);
        System.out.println(id);

        if (showD == 1) {
            leaveType.setText(name);
            leaveType.setTextColor(Color.BLACK);

            selected_leave_type_id = id;
            selected_leave_type_name = name;
            leaveTypeLay.setHelperText("");

        } else if (showD == 2) {
            reason.setText(name);
            reason.setTextColor(Color.BLACK);

            selected_reason = name;
            if (selected_reason.equals("Others")) {
                isOtherReason = true;
                otherReasonLay.setVisibility(View.VISIBLE);
            } else {
                isOtherReason = false;
                otherReasonLay.setVisibility(View.GONE);
            }
            errorReason.setVisibility(View.GONE);
        }
        showD = 0;
        dialog.dismiss();
    }
}

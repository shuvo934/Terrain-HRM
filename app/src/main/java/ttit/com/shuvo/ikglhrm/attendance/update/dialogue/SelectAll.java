package ttit.com.shuvo.ikglhrm.attendance.update.dialogue;

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
import ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication;

import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.approverTestEdit;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.dialogText;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.errorApprover;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.errorShift;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.selectAllLists;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.selected_approver_id;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.selected_approver_name;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.selected_shift_id;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.selected_shift_name;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.shiftTestEdit;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.showShoftTime;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.allWorkBackup;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.errorBackup;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.selected_worker;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.selected_worker_id;
import static ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication.workBackup;

public class SelectAll extends AppCompatDialogFragment implements SelectAllAdapter.ClickedItem{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SelectAllAdapter selectAllAdapter;

    TextView first;
    TextView second;
    TextView third;
    TextView fourth;

    TextInputEditText search;
    AlertDialog dialog;

    Boolean isfiltered = false;
    ArrayList<SelectAllList> filteredList = new ArrayList<>();
    private ArrayList<SelectAllList> lists = new ArrayList<>();


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.all_list_of, null);

        recyclerView = view.findViewById(R.id.all_list_of_item);
        first = view.findViewById(R.id.first_text);
        second = view.findViewById(R.id.second_text);
        third = view.findViewById(R.id.third_text);
        fourth = view.findViewById(R.id.fourth_text);

        search = view.findViewById(R.id.search_text);

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        lists = new ArrayList<>();

        String ft;
        String st;
        String tt;
        String fot;
        if (dialogText == 1) {
            ft = "Shift";
            st = "Start Time";
            tt = "Late After";
            fot = "End Time";
            first.setText(ft);
            second.setText(st);
            third.setText(tt);
            fourth.setText(fot);
            lists = selectAllLists;
        } else if (dialogText == 2) {
            ft = "Employee";
            st = "Functional Designation";
            tt = "Structure Designation";
            fot = "Division";
            first.setText(ft);
            second.setText(st);
            third.setText(tt);
            fourth.setText(fot);
            lists = selectAllLists;
        }
//        else if (AttendanceReqUpdate.dialogText_update == 1) {
//            first.setText("Shift");
//            second.setText("Start Time");
//            third.setText("Late After");
//            fourth.setText("End Time");
//            lists = selectAllListsUpdate;
//        }
//        else if (AttendanceReqUpdate.dialogText_update == 2) {
//            first.setText("Employee");
//            second.setText("Functional Designation");
//            third.setText("Structure Designation");
//            fourth.setText("Division");
//            lists = selectAllListsUpdate;
//        }
        else if (LeaveApplication.dialogText_leave == 1) {
            ft = "Name";
            st = "Calling Title";
            tt = "Job Title";
            fot = "Division";
            first.setText(ft);
            second.setText(st);
            third.setText(tt);
            fourth.setText(fot);
            lists = allWorkBackup;
        }
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        selectAllAdapter = new SelectAllAdapter(lists, getContext(),this);
        recyclerView.setAdapter(selectAllAdapter);



        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> {

            dialogText = 0;
//                AttendanceReqUpdate.dialogText_update = 0;
            LeaveApplication.dialogText_leave = 0;
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
        for (SelectAllList item : lists) {
            if (item.getFirst().toLowerCase().contains(text.toLowerCase())) {
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
             name = filteredList.get(CategoryPosition).getFirst();
             id = filteredList.get(CategoryPosition).getId();
        } else {
             name = lists.get(CategoryPosition).getFirst();
             id = lists.get(CategoryPosition).getId();
        }


        System.out.println(name);
        System.out.println(id);
        if (dialogText == 1) {
            shiftTestEdit.setText(name);
            shiftTestEdit.setTextColor(Color.BLACK);
            errorShift.setVisibility(View.GONE);
            selected_shift_id = id;
            selected_shift_name = name;
            showShoftTime.setVisibility(View.VISIBLE);
        } else if (dialogText == 2) {
            approverTestEdit.setText(name);
            approverTestEdit.setTextColor(Color.BLACK);
            errorApprover.setVisibility(View.GONE);
            selected_approver_id = id;
            selected_approver_name = name;
        }
//        else if (AttendanceReqUpdate.dialogText_update == 1) {
//            shiftTestEditUpdate.setText(name);
//            shiftTestEditUpdate.setTextColor(Color.BLACK);
//            errorShiftUpdate.setVisibility(View.GONE);
//            selected_shift_id_update = id;
//            selected_shift_name_update = name;
//            showShoftTimeUpdate.setVisibility(View.VISIBLE);
//        }
//        else if (AttendanceReqUpdate.dialogText_update == 2) {
//            approverTestEditUpdate.setText(name);
//            approverTestEditUpdate.setTextColor(Color.BLACK);
//            errorApproverUpdate.setVisibility(View.GONE);
//            selected_approver_id_update = id;
//            selected_approver_name_update = name;
//        }
        else if (LeaveApplication.dialogText_leave == 1) {
            workBackup.setText(name);
            workBackup.setTextColor(Color.BLACK);
            errorBackup.setVisibility(View.GONE);
            selected_worker = name;
            selected_worker_id = id;
        }


        dialogText = 0;
//        AttendanceReqUpdate.dialogText_update = 0;
        LeaveApplication.dialogText_leave = 0;
        dialog.dismiss();

    }
}

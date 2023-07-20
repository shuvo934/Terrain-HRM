package ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;


import static ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove.darm_emp_id;
import static ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove.darm_id;
import static ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove.fromAttApp;
import static ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove.req_code;
import static ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove.requestCode;
import static ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove.selectApproveReqLists;
import static ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove.fromLApp;
import static ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove.la_emp_id;
import static ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove.la_id;
import static ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove.leaveReqList;
import static ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove.requestCodeLeave;
import static ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove.req_code_leave;

public class SelectApproveReq extends AppCompatDialogFragment implements SelectApproveReqAdapter.ClickedItem{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SelectApproveReqAdapter selectReqAdapter;


    TextInputEditText search;
    AlertDialog dialog;

    TextView dateOrDays;

    Boolean isfiltered = false;
    ArrayList<SelectApproveReqList> filteredList = new ArrayList<>();

    ArrayList<SelectApproveReqList> selectedReqList;

    AppCompatActivity activity;
    View view;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.approval_request_list, null);

        activity = (AppCompatActivity) view.getContext();

        recyclerView = view.findViewById(R.id.request_list_for_approve);
//        first = view.findViewById(R.id.first_text);
//        second = view.findViewById(R.id.second_text);
//        third = view.findViewById(R.id.third_text);
//        fourth = view.findViewById(R.id.fourth_text);

        search = view.findViewById(R.id.search_by_emp_name);
        dateOrDays = view.findViewById(R.id.for_att_leave_approve);

        selectedReqList = new ArrayList<>();

        search.setImeOptions(EditorInfo.IME_ACTION_DONE);


        if (fromAttApp == 1) {
            selectedReqList = selectApproveReqLists;
            dateOrDays.setText("Update Date");
        } else if(fromLApp == 1) {
            selectedReqList = leaveReqList;
            dateOrDays.setText("Leave Days");
        }

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        selectReqAdapter = new SelectApproveReqAdapter(selectedReqList, getContext(),this);
        recyclerView.setAdapter(selectReqAdapter);



        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                fromAttApp = 0;
                fromLApp = 0;
                dialog.dismiss();
            }
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

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        search.clearFocus();
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        return dialog;

    }

    private void closeKeyBoard() {

        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (SelectApproveReqList item : selectedReqList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        selectReqAdapter.filterList(filteredList);
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {


        String name = "";
        String id = "";
        String darmID = "";
        String darmEmp = "";
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getName();
            id = filteredList.get(CategoryPosition).getReqCode();
            darmID = filteredList.get(CategoryPosition).getDarmID();
            darmEmp = filteredList.get(CategoryPosition).getDarmEmpId();
        } else {
            name = selectedReqList.get(CategoryPosition).getName();
            id = selectedReqList.get(CategoryPosition).getReqCode();
            darmID = selectedReqList.get(CategoryPosition).getDarmID();
            darmEmp = selectedReqList.get(CategoryPosition).getDarmEmpId();
        }

        if (fromAttApp == 1) {
            System.out.println(name);
            System.out.println(id);
            System.out.println(darmID);
            System.out.println(darmEmp);
            req_code = id;
            darm_id = darmID;
            darm_emp_id = darmEmp;
            requestCode.setText(id);
            requestCode.setTextColor(Color.BLACK);
            fromAttApp = 0;
        } else if (fromLApp == 1) {
            System.out.println(name);
            System.out.println(id);
            System.out.println(darmID);
            System.out.println(darmEmp);
            req_code_leave = id;
            la_id = darmID;
            la_emp_id = darmEmp;
            requestCodeLeave.setText(id);
            requestCodeLeave.setTextColor(Color.BLACK);
            fromLApp = 0;
        }



        dialog.dismiss();

    }
}

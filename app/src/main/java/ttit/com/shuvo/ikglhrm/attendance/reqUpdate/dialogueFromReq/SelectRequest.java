package ttit.com.shuvo.ikglhrm.attendance.reqUpdate.dialogueFromReq;

import android.app.Dialog;
import android.content.DialogInterface;
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
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAllAdapter;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAllList;

import static ttit.com.shuvo.ikglhrm.attendance.reqUpdate.AttendanceReqUpdate.darm_id;
import static ttit.com.shuvo.ikglhrm.attendance.reqUpdate.AttendanceReqUpdate.requestCode;
import static ttit.com.shuvo.ikglhrm.attendance.reqUpdate.AttendanceReqUpdate.selectReqLists;
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

public class SelectRequest extends AppCompatDialogFragment implements SelectReqAdapter.ClickedItem{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SelectReqAdapter selectReqAdapter;

    TextView first;
    TextView second;
    TextView third;
    TextView fourth;

    TextInputEditText search;
    AlertDialog dialog;

    Boolean isfiltered = false;
    ArrayList<SelectReqList> filteredList = new ArrayList<>();


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.request_list_of_attendance, null);

        recyclerView = view.findViewById(R.id.request_list_item);
//        first = view.findViewById(R.id.first_text);
//        second = view.findViewById(R.id.second_text);
//        third = view.findViewById(R.id.third_text);
//        fourth = view.findViewById(R.id.fourth_text);

        search = view.findViewById(R.id.search_by_date);

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        selectReqAdapter = new SelectReqAdapter(selectReqLists, getContext(),this);
        recyclerView.setAdapter(selectReqAdapter);



        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

        return dialog;

    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (SelectReqList item : selectReqLists) {
            if (item.getDarm_date().toLowerCase().contains(text.toLowerCase())) {
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
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getDarm_app_code();
            id = filteredList.get(CategoryPosition).getDarm_id();
        } else {
            name = selectReqLists.get(CategoryPosition).getDarm_app_code();
            id = selectReqLists.get(CategoryPosition).getDarm_id();
        }


        System.out.println(name);
        System.out.println(id);
        requestCode.setText(name);
        darm_id = id;
        requestCode.setTextColor(Color.BLACK);
//        if (dialogText == 1) {
//            shiftTestEdit.setText(name);
//            shiftTestEdit.setTextColor(Color.BLACK);
//            errorShift.setVisibility(View.GONE);
//            selected_shift_id = id;
//            selected_shift_name = name;
//            showShoftTime.setVisibility(View.VISIBLE);
//        } else if (dialogText == 2) {
//            approverTestEdit.setText(name);
//            approverTestEdit.setTextColor(Color.BLACK);
//            errorApprover.setVisibility(View.GONE);
//            selected_approver_id = id;
//            selected_approver_name = name;
//        }

        dialog.dismiss();

    }
}

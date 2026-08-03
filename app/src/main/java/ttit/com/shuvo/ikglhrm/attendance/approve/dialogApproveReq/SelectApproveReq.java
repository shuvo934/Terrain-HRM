package ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq;

import android.app.Dialog;
import android.content.Context;
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
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.utilities.ReqSelectionListener;

import static ttit.com.shuvo.ikglhrm.utilities.Constants.ATT_REQ_SELECT_TYPE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LVE_REQ_SELECT_TYPE;

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
    String approveReqType = "";

    public SelectApproveReq() {}

    public static SelectApproveReq newInstance(String type, ArrayList<SelectApproveReqList> selectedReqList) {
        SelectApproveReq fragment = new SelectApproveReq();
        Bundle args = new Bundle();
        args.putSerializable("selectedReqList", selectedReqList);
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    private ReqSelectionListener reqSelectionListener;

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        if (getActivity() instanceof ReqSelectionListener)
            reqSelectionListener = (ReqSelectionListener) getActivity();

        View view = inflater.inflate(R.layout.approval_request_list, null);

        recyclerView = view.findViewById(R.id.request_list_for_approve);
//        first = view.findViewById(R.id.first_text);
//        second = view.findViewById(R.id.second_text);
//        third = view.findViewById(R.id.third_text);
//        fourth = view.findViewById(R.id.fourth_text);

        search = view.findViewById(R.id.search_by_emp_name);
        dateOrDays = view.findViewById(R.id.for_att_leave_approve);

        selectedReqList = new ArrayList<>();

        search.setImeOptions(EditorInfo.IME_ACTION_DONE);

        if (getArguments() != null) {
            selectedReqList = (ArrayList<SelectApproveReqList>) getArguments().getSerializable("selectedReqList");
            approveReqType = getArguments().getString("type", "");
        }

        if (selectedReqList == null) selectedReqList = new ArrayList<>();


        if (approveReqType.equals(ATT_REQ_SELECT_TYPE)) {
            String tt = "Update Date";
            dateOrDays.setText(tt);
        } else if(approveReqType.equals(LVE_REQ_SELECT_TYPE)) {
            String tt = "Leave Days";
            dateOrDays.setText(tt);
        }

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        selectReqAdapter = new SelectApproveReqAdapter(selectedReqList, getContext(),this);
        recyclerView.setAdapter(selectReqAdapter);

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> dialog.dismiss());

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

        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    search.clearFocus();
                    InputMethodManager mgr = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return false; // consume.
                }
            }
            return false;
        });

        return dialog;

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
        String name;
        String id;
        String darmID;
        String darmEmp;
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

        System.out.println(name);
        System.out.println(id);
        System.out.println(darmID);
        System.out.println(darmEmp);

        if (reqSelectionListener != null)
            reqSelectionListener.onReqSelection(approveReqType, id, darmID, darmEmp);

        dialog.dismiss();

    }
}

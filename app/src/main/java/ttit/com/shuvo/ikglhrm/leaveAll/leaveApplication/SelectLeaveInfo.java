package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication;

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
import ttit.com.shuvo.ikglhrm.utilities.LeaveInfoListener;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LVE_REASON_SELECT_INFO;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LVE_TYPE_SELECT_INFO;

public class SelectLeaveInfo extends AppCompatDialogFragment implements LeaveTypeAdapter.ClickedItem {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LeaveTypeAdapter selectAllAdapter;

    TextInputEditText search;
    AlertDialog dialog;
    TextView ftext;

    Boolean isfiltered = false;
    ArrayList<LeaveTypeList> filteredList = new ArrayList<>();
    private ArrayList<LeaveTypeList> lists = new ArrayList<>();
    String leaveInfoType = "";

    public SelectLeaveInfo() {}

    public static SelectLeaveInfo newInstance(String type, ArrayList<LeaveTypeList> leaveTypeLists) {
        SelectLeaveInfo dial = new SelectLeaveInfo();
        Bundle bundle = new Bundle();
        bundle.putSerializable("leaveTypeLists", leaveTypeLists);
        bundle.putString("type", type);
        dial.setArguments(bundle);
        return dial;
    }

    private LeaveInfoListener listener;

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        if (getActivity() instanceof LeaveInfoListener)
            listener = (LeaveInfoListener) getActivity();

        View view = inflater.inflate(R.layout.leave_type_list, null);

        recyclerView = view.findViewById(R.id.all_leave_type_list);

        ftext = view.findViewById(R.id.name_of_type_reason);

        search = view.findViewById(R.id.leave_type_search);

        lists = new ArrayList<>();

        if (getArguments() != null) {
            lists = (ArrayList<LeaveTypeList>) getArguments().getSerializable("leaveTypeLists");
            leaveInfoType = getArguments().getString("type", "");
        }

        if (lists == null) lists = new ArrayList<>();

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);

        if (leaveInfoType.equals(LVE_TYPE_SELECT_INFO)) {
            String ft = "Leave Type";
            ftext.setText(ft);
        } else if (leaveInfoType.equals(LVE_REASON_SELECT_INFO)) {
            String ft = "Leave Reason";
            ftext.setText(ft);
        }

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        selectAllAdapter = new LeaveTypeAdapter(lists, getContext(),this);
        recyclerView.setAdapter(selectAllAdapter);

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

        if (listener != null)
            listener.onLeaveInfoSelected(leaveInfoType, name, id);

        dialog.dismiss();
    }
}

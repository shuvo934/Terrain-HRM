package ttit.com.shuvo.ikglhrm.attendance.update.dialogue;

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
import ttit.com.shuvo.ikglhrm.utilities.SelectAllListener;

import static ttit.com.shuvo.ikglhrm.utilities.Constants.ATT_APPROVER_SELECT_TYPE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.ATT_SHIFT_SELECT_TYPE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LVE_BACKUP_SELECT_TYPE;

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

    String dialogueType = "";

    public SelectAll() {}

    public static SelectAll newInstance(String type, ArrayList<SelectAllList> selectAllLists) {
        SelectAll fragment = new SelectAll();
        Bundle args = new Bundle();
        args.putSerializable("selectAllLists", selectAllLists);
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    private SelectAllListener allListener;

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        if (getActivity() instanceof SelectAllListener)
            allListener = (SelectAllListener) getActivity();

        View view = inflater.inflate(R.layout.all_list_of, null);

        recyclerView = view.findViewById(R.id.all_list_of_item);
        first = view.findViewById(R.id.first_text);
        second = view.findViewById(R.id.second_text);
        third = view.findViewById(R.id.third_text);
        fourth = view.findViewById(R.id.fourth_text);

        search = view.findViewById(R.id.search_text);

        lists = new ArrayList<>();

        if (getArguments() != null) {
            lists = (ArrayList<SelectAllList>) getArguments().getSerializable("selectAllLists");
            dialogueType = getArguments().getString("type", "");
        }

        if (lists == null) lists = new ArrayList<>();

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        String ft;
        String st;
        String tt;
        String fot;
        switch (dialogueType) {
            case ATT_SHIFT_SELECT_TYPE:
                ft = "Shift";
                st = "Start Time";
                tt = "Late After";
                fot = "End Time";
                first.setText(ft);
                second.setText(st);
                third.setText(tt);
                fourth.setText(fot);
                break;
            case ATT_APPROVER_SELECT_TYPE:
                ft = "Employee";
                st = "Functional Designation";
                tt = "Structure Designation";
                fot = "Division";
                first.setText(ft);
                second.setText(st);
                third.setText(tt);
                fourth.setText(fot);
                break;
            case LVE_BACKUP_SELECT_TYPE:
                ft = "Name";
                st = "Calling Title";
                tt = "Job Title";
                fot = "Division";
                first.setText(ft);
                second.setText(st);
                third.setText(tt);
                fourth.setText(fot);
                break;
        }

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        selectAllAdapter = new SelectAllAdapter(lists, getContext(),this);
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
        if (allListener != null)
            allListener.onItemSelected(dialogueType,name,id);

        dialog.dismiss();

    }
}

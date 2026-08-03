package ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.leaveAppDialogues;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ttit.com.shuvo.ikglhrm.R;

import java.util.ArrayList;


public class ForwardHistoryDial extends AppCompatDialogFragment {

    RecyclerView apptRecyclerView;
    ForwardHistoryAdapter forwardHistoryAdapter;
    RecyclerView.LayoutManager apptLayout;

    ArrayList<ForwardHistoryList> forwardHistoryLists;

    public ForwardHistoryDial() {}

    public static ForwardHistoryDial newInstance(ArrayList<ForwardHistoryList> forwardHistoryLists) {
        ForwardHistoryDial dial = new ForwardHistoryDial();
        Bundle bundle = new Bundle();
        bundle.putSerializable("forwardHistoryLists", forwardHistoryLists);
        dial.setArguments(bundle);
        return dial;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.forward_history_list_view, null);

        apptRecyclerView = view.findViewById(R.id.forward_list_for_leave);

        forwardHistoryLists = new ArrayList<>();

        if (getArguments() != null) {
            forwardHistoryLists = (ArrayList<ForwardHistoryList>) getArguments().getSerializable("forwardHistoryLists");
        }

        if (forwardHistoryLists == null) forwardHistoryLists = new ArrayList<>();

        apptLayout = new LinearLayoutManager(getContext());
        apptRecyclerView.setLayoutManager(apptLayout);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(apptRecyclerView.getContext(),DividerItemDecoration.VERTICAL);
        apptRecyclerView.addItemDecoration(dividerItemDecoration);
        forwardHistoryAdapter = new ForwardHistoryAdapter(forwardHistoryLists,getContext());
        apptRecyclerView.setAdapter(forwardHistoryAdapter);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", (dialog1, which) -> dialog1.dismiss());

        return dialog;
    }
}

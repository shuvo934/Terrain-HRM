package ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.leaveAppDialogues;

import android.app.Dialog;
import android.content.DialogInterface;
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
import static ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove.forwardHistoryLists;


public class ForwardHistoryDial extends AppCompatDialogFragment {

    private RecyclerView apptRecyclerView;
    private ForwardHistoryAdapter forwardHistoryAdapter;
    private RecyclerView.LayoutManager apptLayout;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.forward_history_list_view, null);

        apptRecyclerView = view.findViewById(R.id.forward_list_for_leave);
        apptRecyclerView.setHasFixedSize(true);
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

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return dialog;
    }
}

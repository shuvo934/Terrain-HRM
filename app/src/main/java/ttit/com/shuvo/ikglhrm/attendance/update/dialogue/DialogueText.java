package ttit.com.shuvo.ikglhrm.attendance.update.dialogue;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove;
import ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate;
import ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove;

public class DialogueText extends AppCompatDialogFragment {

    private TextInputEditText editText;
    TextInputLayout textLay;
    Button getAdd;
    AppCompatActivity activity;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.textdialogue, null);

        editText = view.findViewById(R.id.dialogue_text_edit);
        getAdd = view.findViewById(R.id.get_address);
        textLay = view.findViewById(R.id.dialogue_text_edit_lay);
        activity = (AppCompatActivity) view.getContext();

        builder.setView(view);
        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if (AttendanceUpdate.number == 1) {
            String h = "Write Reason Description:";
            textLay.setHint(h);
            editText.setText(AttendanceUpdate.text);
        } else if (AttendanceUpdate.number == 2) {
            String h = "Write Address During Outside of Station:";
            textLay.setHint(h);
            editText.setText(AttendanceUpdate.text);
        } else if (LeaveApplication.leaveNumber == 1) {
            String h = "Write Your Other Reason:";
            textLay.setHint(h);
            editText.setText(LeaveApplication.text);
        } else if (LeaveApplication.leaveNumber == 2) {
            String h = "Write Address During Leave:";
            textLay.setHint(h);
            editText.setText(LeaveApplication.text);
        }
//        else if (AttendanceReqUpdate.number_update == 1) {
//            textLay.setHint(AttendanceReqUpdate.hint_update);
//            editText.setText(AttendanceReqUpdate.text_update);
//        }
//        else if (AttendanceReqUpdate.number_update == 2) {
//            textLay.setHint(AttendanceReqUpdate.hint_update);
//            editText.setText(AttendanceReqUpdate.text_update);
//        }
        else if (AttendanceApprove.number == 1) {
            textLay.setHint(AttendanceApprove.hint);
            editText.setText(AttendanceApprove.text);
        } else if (LeaveApprove.number == 1) {
            textLay.setHint(LeaveApprove.hintLA);
            editText.setText(LeaveApprove.textLA);
        }


        dialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {

            String text = Objects.requireNonNull(editText.getText()).toString();
            if (AttendanceUpdate.number == 1) {
                AttendanceUpdate.reasonDesc.setText(text);
                if (text.isEmpty()) {
                    AttendanceUpdate.reasonLay.setHint("Write Reason Description:");
                } else {
                    AttendanceUpdate.reasonLay.setHint("Reason Description:");
                }

                AttendanceUpdate.errorReasonDesc.setVisibility(View.GONE);
            } else if (AttendanceUpdate.number == 2) {
                if (text.isEmpty()) {
                    AttendanceUpdate.addStaLay.setHint("Write Address During Outside of Station:");
                } else {
                    AttendanceUpdate.addStaLay.setHint("Address During Outside of Station:");
                }

                AttendanceUpdate.addressStation.setText(text);
            } else if (LeaveApplication.leaveNumber == 1) {
                LeaveApplication.otherReason.setText(text);
                if (text.isEmpty()) {
                    LeaveApplication.otherReasonInputLay.setHint("Write Your Other Reason:");
                } else {
                    LeaveApplication.otherReasonInputLay.setHint("Other Reason:");
                }

                LeaveApplication.errorReason.setVisibility(View.GONE);

            } else if (LeaveApplication.leaveNumber == 2) {
                if (text.isEmpty()) {
                    LeaveApplication.leaveAddressLay.setHint("Write Address During Leave:");
                } else {
                    LeaveApplication.leaveAddressLay.setHint("Address During Leave:");
                }
                LeaveApplication.leaveAddress.setText(text);

            }
//                else if (AttendanceReqUpdate.number_update == 1) {
//                    AttendanceReqUpdate.reasonDescUpdate.setText(text);
//                    AttendanceReqUpdate.errorReasonDescUpdate.setVisibility(View.GONE);
//                }
//                else if (AttendanceReqUpdate.number_update == 2) {
//                    AttendanceReqUpdate.addressStationUpdate.setText(text);
//                }
            else if (AttendanceApprove.number == 1) {
                AttendanceApprove.comments.setText(text);
                if (text.isEmpty()) {
                    AttendanceApprove.commentsLay.setHint("Write Approve/Reject Comments:");
                } else {
                    AttendanceApprove.commentsLay.setHint("Comments:");
                }
            } else if (LeaveApprove.number == 1) {
                LeaveApprove.comments.setText(text);
                if (text.isEmpty()) {
                    LeaveApprove.commentsLay.setHint("Write Approve/Reject Comments:");
                } else {
                    LeaveApprove.commentsLay.setHint("Comments:");
                }
            }
            AttendanceUpdate.number = 0;
//                AttendanceReqUpdate.number_update = 0;
            LeaveApplication.leaveNumber = 0;
            AttendanceApprove.number = 0;
            LeaveApprove.number = 0;

            dialog1.dismiss();
        });

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", (dialog12, which) -> {
            AttendanceUpdate.number = 0;
//                AttendanceReqUpdate.number_update = 0;
            LeaveApplication.leaveNumber = 0;
            AttendanceApprove.number = 0;
            LeaveApprove.number = 0;
            dialog12.dismiss();
        });

        return dialog;

    }
}

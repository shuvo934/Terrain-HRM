package ttit.com.shuvo.ikglhrm.utilities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ttit.com.shuvo.ikglhrm.R;

public class DialogueText extends AppCompatDialogFragment {

    private static final String KEY_HINT = "hint";
    private static final String KEY_TEXT = "text";
    private static final String KEY_CODE = "r_code";

    private TextInputEditText editText;
    TextInputLayout textLay;
    AppCompatActivity activity;

    private TextSubmitListener listener;

    public DialogueText() {
    }

    public static DialogueText newInstance(int reqCode, String hint, String defaultText) {
        DialogueText dialog = new DialogueText();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_HINT, hint);
        bundle.putString(KEY_TEXT, defaultText);
        bundle.putInt(KEY_CODE, reqCode);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        if (getActivity() instanceof TextSubmitListener)
            listener = (TextSubmitListener) getActivity();

        View view = inflater.inflate(R.layout.textdialogue, null);

        editText = view.findViewById(R.id.dialogue_text_edit);
        textLay = view.findViewById(R.id.dialogue_text_edit_lay);
        activity = (AppCompatActivity) view.getContext();

        String argHint = "";
        String argText = "";
        int argType;

        if (getArguments() != null) {
            argHint = getArguments().getString(KEY_HINT, "");
            argText = getArguments().getString(KEY_TEXT, "");
            argType = getArguments().getInt(KEY_CODE,0);
        } else {
            argType = 0;
        }

        builder.setView(view);
        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        editText.setText(argText);
        textLay.setHint(argHint);

        dialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {

            String text = Objects.requireNonNull(editText.getText()).toString();

            if (listener != null) {
                listener.onTextSubmitted(argType,text);
            }
            dialog1.dismiss();
        });

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", (dialog12, which) -> dialog12.dismiss());

        return dialog;
    }
}
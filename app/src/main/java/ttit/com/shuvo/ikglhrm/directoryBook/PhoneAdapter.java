package ttit.com.shuvo.ikglhrm.directoryBook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneHolder> {

    public ArrayList<PhoneList> phoneLists;
    public Context myContext;


    public PhoneAdapter(Context context, ArrayList<PhoneList> phoneLists) {
        this.phoneLists = phoneLists;
        this.myContext = context;
    }

    @NonNull
    @Override
    public PhoneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.phone_item, parent, false);
        return new PhoneHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneHolder holder, int position) {

        PhoneList foodOptionCategoryList = phoneLists.get(position);


        String phone = foodOptionCategoryList.getPhone();

        if (phone == null) {
            String nnft = "No Number Found";
            holder.phoneNumber.setText(nnft);
            holder.phoneClick.setVisibility(View.GONE);
        } else {
            char[] chars = phone.toCharArray();

            boolean found = false;
            for(char c : chars){
                if(Character.isDigit(c)){
                    found = true;
                }
            }

            if (!found) {
                String nnft = "No Number Found";
                holder.phoneNumber.setText(nnft);
                holder.phoneClick.setVisibility(View.GONE);
            } else {
                holder.phoneClick.setVisibility(View.VISIBLE);
                holder.phoneNumber.setText(foodOptionCategoryList.getPhone());
            }
        }


    }

    @Override
    public int getItemCount() {
        return phoneLists.size();
    }

    public static class PhoneHolder extends RecyclerView.ViewHolder {

        TextView phoneNumber;
        ImageView phoneClick;


        public PhoneHolder(@NonNull View itemView) {
            super(itemView);

            phoneNumber = itemView.findViewById(R.id.directory_phone);
            phoneClick = itemView.findViewById(R.id.phone_click);


            phoneClick.setOnClickListener(v -> {
                Log.i("Name", phoneNumber.getText().toString());
                String number = phoneNumber.getText().toString();
                //  Log.i("ID", foodOptionCategoryLists.get(getAdapterPosition()).getFoodCategoryID());
//                    fID = foodOptionCategoryLists.get(getAdapterPosition()).getFoodCategoryID();
//                    fname = foodCategoryName.getText().toString();
//                    fOpID = foodOptionCategoryLists.get(getAdapterPosition()).getOptionID();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                    if (fname.equals("FLAVOR")) {
//                        OptionsFlavorDialogue optionsFlavorDialogue = new OptionsFlavorDialogue();
//                        optionsFlavorDialogue.show(activity.getSupportFragmentManager(),"FlavorOption");
//                    } else {
//                        OptionsDialogoue optionsDialogoue = new OptionsDialogoue();
//
////                    //Activity activity = (Activity) ();
////
////                    OptionsMenu optionsMenu = (OptionsMenu) context;
////                    FragmentManager fm = ((FragmentActivity)myContext).getSupportFragmentManager();
//                        optionsDialogoue.show(activity.getSupportFragmentManager(),"sww");
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setMessage("Do you want to make a call to "+number+" ?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v1 -> {

                    dialog.dismiss();
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+number));
                    activity.startActivity(callIntent);

                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v12 -> dialog.dismiss());


            });

        }
    }

}

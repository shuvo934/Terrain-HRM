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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAllList;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryHolder> {

    public ArrayList<DirectoryList> directoryLists;
    public Context myContext;
    AppCompatActivity activity;
    public static PhoneAdapter phoneAdapter;

    public DirectoryAdapter(Context context, ArrayList<DirectoryList> directoryLists) {
        this.myContext = context;
        this.directoryLists = directoryLists;
    }

    @NonNull
    @Override
    public DirectoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.directory_item, parent, false);
        DirectoryHolder ammvh = new DirectoryHolder(v);
        return ammvh;
    }


    @Override
    public void onBindViewHolder(@NonNull DirectoryHolder holder, int position) {

        DirectoryList itemList = directoryLists.get(position);

        holder.empName.setText(itemList.getEmp_name());
        holder.div.setText(itemList.getDiv_name());
        holder.dep.setText(itemList.getDep_name());
        holder.des.setText(itemList.getDes_name());
        String mail = itemList.getEmail_name();
        if (mail == null) {
            holder.mailName.setText("No Email Found");
            holder.mailClick.setVisibility(View.GONE);
        } else {
            holder.mailName.setText(itemList.getEmail_name());
            holder.mailClick.setVisibility(View.VISIBLE);
        }

        String id = itemList.getEmp_id();

        ArrayList<PhoneList> newArray = new ArrayList<>();

        String no = itemList.getNo();

        if (no.equals("1")) {
            for (int i = 0; i < Directory.allPhoneLists.size(); i++) {
                if (id.equals(Directory.allPhoneLists.get(i).getP_emp_id())) {
                    newArray.add(new PhoneList(Directory.allPhoneLists.get(i).getP_emp_id(),Directory.allPhoneLists.get(i).getPhone()));
                }
            }
        }
        else if (no.equals("2")) {
            if(DirectoryWithDivision.allPhoneLists.size() != 0) {
                for (int i = 0; i < DirectoryWithDivision.allPhoneLists.size(); i++) {
                    if (id.equals(DirectoryWithDivision.allPhoneLists.get(i).getP_emp_id())) {
                        newArray.add(new PhoneList(DirectoryWithDivision.allPhoneLists.get(i).getP_emp_id(), DirectoryWithDivision.allPhoneLists.get(i).getPhone()));
                    }
                }
            }
        }

        if (newArray.size() == 0) {
            if (itemList.getContact_no() != null) {
                if (!itemList.getContact_no().isEmpty()) {
                    newArray.add(new PhoneList(id, itemList.getContact_no()));
                }
            }
        }





        if (newArray.size() == 0) {
            System.out.println("FAKA ARRAY");
            holder.numberNot.setVisibility(View.VISIBLE);
            holder.phoneView.setVisibility(View.GONE);
        } else {
            System.out.println("KISU PAISE");
            holder.numberNot.setVisibility(View.GONE);
            holder.phoneView.setVisibility(View.VISIBLE);
            holder.phoneView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
            holder.phoneView.setLayoutManager(layoutManager);
//            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.phoneView.getContext(),DividerItemDecoration.VERTICAL);
//            holder.phoneView.addItemDecoration(dividerItemDecoration);
            phoneAdapter = new PhoneAdapter(myContext, newArray);
            holder.phoneView.setAdapter(phoneAdapter);
        }



    }

    @Override
    public int getItemCount() {
        return directoryLists.size();
    }

    public class DirectoryHolder extends RecyclerView.ViewHolder {

         TextView empName;
         TextView div;
         TextView dep;
         TextView des;
         TextView mailName;
         TextView numberNot;
         RecyclerView phoneView;
         ImageView mailClick;

        public DirectoryHolder(@NonNull View itemView) {
            super(itemView);

            empName = itemView.findViewById(R.id.directory_emp_name);
            div = itemView.findViewById(R.id.directory_division);
            dep = itemView.findViewById(R.id.directory_department);
            des = itemView.findViewById(R.id.directory_designation);
            mailName = itemView.findViewById(R.id.directory_mail);
            phoneView = itemView.findViewById(R.id.phone_number_list_view);
            mailClick = itemView.findViewById(R.id.mail_click);
            numberNot = itemView.findViewById(R.id.phone_number_not_found);

            mailClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    String mmm = mailName.getText().toString();
//                    Intent email = new Intent(Intent.ACTION_SEND);
//                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{mmm});
//                    //email.setType("message/rfc822");
//                    email.setType("vnd.android.cursor.dir/email");
//                    activity.startActivity(Intent.createChooser(email, "Choose an Email client :"));


//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    Uri data = Uri.parse("mailto:recipient@example.com?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body));
//                    intent.setData(data);
//                    activity.startActivity(intent);




                    AlertDialog dialog = new AlertDialog.Builder(activity)
                            .setMessage("Do you want to send an email to "+mmm+" ?")
                            .setPositiveButton("Yes", null)
                            .setNegativeButton("No",null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri data = Uri.parse("mailto:"+mmm);
                            intent.setData(data);
                            try {
                                activity.startActivity(intent);

                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(activity, "There is no email app found.", Toast.LENGTH_SHORT).show();
                            }



                        }
                    });
                    Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });

        }
    }

    public void filterList(ArrayList<DirectoryList> filteredList) {
        directoryLists = filteredList;
        notifyDataSetChanged();
    }
}

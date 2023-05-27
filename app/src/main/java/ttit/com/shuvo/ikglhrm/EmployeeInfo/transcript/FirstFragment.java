package ttit.com.shuvo.ikglhrm.EmployeeInfo.transcript;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import ttit.com.shuvo.ikglhrm.EmployeeInfo.personal.EMPInformation;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.personal.PersonalData;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link FirstFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    EditText revision;
    EditText revisionDate;
    EditText effectedDate;
    EditText publish;
    EditText appointDate;
    EditText band;
    EditText jobNo;
    EditText struc_des;
    EditText func_des;
    EditText func_des_bn;
    EditText job_obj;
    EditText div;
    EditText depa;
    EditText section;
    EditText add_ch;
    EditText prim_station;
    EditText sec_station;
    EditText proj;
    EditText workTime;
    EditText earlyArrival;
    EditText ext_dep;
    EditText temp_ch;
    EditText e_mail;
    EditText soft_acc;
    EditText rem_acc;
    EditText comp;
    EditText dorm;
    EditText uniform;
    EditText apart;

//    Spinner st_pos;
//    Spinner late_accp;
//    Spinner att_bon;
    EditText st_pos;
    EditText late_accp;
    EditText att_bon;

    ArrayList<String> strrPos;
    ArrayList<String> late;
    ArrayList<String> attBon;

    ArrayAdapter<String> strrPosAdapter;
    ArrayAdapter<String> lateAdapter;
    ArrayAdapter<String> attAdapter;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Connection connection;

    String emp_id = "";
    String emp_name = "";

    Context mContext;

    public static ArrayList<FirstPageData> firstPageData;
    public static ArrayList<FirstPageAnotherData> firstPageAnotherData;


    public FirstFragment(Context context) {
        // Required empty public constructor
        this.mContext = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static FirstFragment newInstance(String param1, String param2) {
//        FirstFragment fragment = new FirstFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        emp_id = userInfoLists.get(0).getEmp_id();

        emp_name = userInfoLists.get(0).getUserName();

        revision = view.findViewById(R.id.revision_no_TR);
        revisionDate = view.findViewById(R.id.revision_dateee);
        effectedDate = view.findViewById(R.id.effectedd_dateee);
        publish = view.findViewById(R.id.publish);
        appointDate = view.findViewById(R.id.appointment_receive);
        band = view.findViewById(R.id.band);
        jobNo = view.findViewById(R.id.job_nong);
        struc_des = view.findViewById(R.id.structure_designation);
        func_des = view.findViewById(R.id.functional_designation);
        func_des_bn = view.findViewById(R.id.functional_designation_bn);
        job_obj = view.findViewById(R.id.job_objective);
        div = view.findViewById(R.id.division);
        depa = view.findViewById(R.id.department);
        section = view.findViewById(R.id.section);
        add_ch = view.findViewById(R.id.additional_charge);
        prim_station = view.findViewById(R.id.primary_station);
        sec_station = view.findViewById(R.id.secondary_station);
        proj = view.findViewById(R.id.project);
        workTime = view.findViewById(R.id.work_time);
        earlyArrival = view.findViewById(R.id.early_arrival);
        ext_dep = view.findViewById(R.id.extended_dep);
        temp_ch = view.findViewById(R.id.temp_charge);
        e_mail = view.findViewById(R.id.emall_E_MM);
        soft_acc = view.findViewById(R.id.software_access);
        rem_acc = view.findViewById(R.id.remote_access);
        comp = view.findViewById(R.id.computer);
        dorm = view.findViewById(R.id.dormitory);
        uniform = view.findViewById(R.id.uniform);
        apart = view.findViewById(R.id.apartment);

        st_pos = view.findViewById(R.id.spinner_struc_pos);
        late_accp = view.findViewById(R.id.spinner_late_acceptable);
        att_bon = view.findViewById(R.id.spinner_att_bonus);

        strrPos = new ArrayList<>();
        late = new ArrayList<>();
        attBon = new ArrayList<>();


        strrPos.add("Select Position");
        strrPos.add("CEO");
        strrPos.add("Division Chief");
        strrPos.add("Department Chief");
        strrPos.add("General Employee");
        strrPos.add("Others");

        late.add("No");
        late.add("Yes");

        attBon.add("No");
        attBon.add("Yes");

//        st_pos.setEnabled(false);
//        late_accp.setEnabled(false);
//        att_bon.setEnabled(false);

        firstPageData = new ArrayList<>();
        firstPageAnotherData = new ArrayList<>();

        // STRUCTURAL POSITION
//        strrPosAdapter = new ArrayAdapter<String>(
//                getContext(),R.layout.item_country,strrPos){
//            @Override
//            public boolean isEnabled(int position){
//                if(position == 0)
//                {
//                    // Disable the first item from Spinner
//                    // First item will be use for hint
//                    return false;
//                }
//                else
//                {
//                    return true;
//                }
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//        st_pos.setGravity(Gravity.END);
//        strrPosAdapter.setDropDownViewResource(R.layout.item_country);
//        st_pos.setAdapter(strrPosAdapter);
//
//        // LATE ACCEPTABLE
//        lateAdapter = new ArrayAdapter<String>(
//                getContext(),R.layout.item_country,late){
//            @Override
//            public boolean isEnabled(int position){
////                if(position == 0)
////                {
////                    // Disable the first item from Spinner
////                    // First item will be use for hint
////                    return false;
////                }
////                else
////                {
////                    return true;
////                }
//                return true;
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
////                if(position == 0){
////                    // Set the hint text color gray
////                    tv.setTextColor(Color.GRAY);
////                }
////                else {
////                    tv.setTextColor(Color.BLACK);
////                }
//                tv.setTextColor(Color.BLACK);
//                return view;
//            }
//        };
//        late_accp.setGravity(Gravity.END);
//        lateAdapter.setDropDownViewResource(R.layout.item_country);
//        late_accp.setAdapter(lateAdapter);
//
//        // ATTENDANCE BONUS
//        attAdapter = new ArrayAdapter<String>(
//                getContext(),R.layout.item_country,attBon){
//            @Override
//            public boolean isEnabled(int position){
////                if(position == 0)
////                {
////                    // Disable the first item from Spinner
////                    // First item will be use for hint
////                    return false;
////                }
////                else
////                {
////                    return true;
////                }
//                return true;
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
////                if(position == 0){
////                    // Set the hint text color gray
////                    tv.setTextColor(Color.GRAY);
////                }
////                else {
////                    tv.setTextColor(Color.BLACK);
////                }
//                tv.setTextColor(Color.BLACK);
//                return view;
//            }
//        };
//        att_bon.setGravity(Gravity.END);
//        attAdapter.setDropDownViewResource(R.layout.item_country);
//        att_bon.setAdapter(attAdapter);

        new Check().execute();

        return view;
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return false;
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                DataQuery();
                if (connected) {
                    conn = true;
                    message= "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {

                if (firstPageData.size() != 0) {
                    for (int i = 0 ; i < firstPageData.size(); i++) {

//                        final String OLD_FORMAT = "yyyy-MM-dd";
//                        final String NEW_FORMAT = "dd-MMM-yyyy";
//                        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.getDefault());
//                        String oldDateString;
//                        String newDateString;
//                        Date d = null;
                        String dateC = firstPageData.get(i).getRevisionDate();
                        if (dateC != null) {
//                            dateC = dateC.substring(0, 10);
//                            oldDateString = dateC;
//                            try {
//                                d = sdf.parse(oldDateString);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            sdf.applyPattern(NEW_FORMAT);
//                            //sdf.applyPattern(NEW_FORMAT);
//                            newDateString = sdf.format(d);
                            revisionDate.setText(dateC);
                        } else {
                            revisionDate.setText("");
                        }



                        String datt = firstPageData.get(i).getEffectedDate();
                        if (datt != null) {
//                            datt = datt.substring(0, 10);
//                            oldDateString = datt;
//                            sdf.applyPattern(OLD_FORMAT);
//
//                            try {
//                                d = sdf.parse(oldDateString);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            sdf.applyPattern(NEW_FORMAT);
//                            newDateString = sdf.format(d);

                            effectedDate.setText(datt);
                        } else {
                            effectedDate.setText("");
                        }


                        String aptdat = firstPageData.get(i).getAppointmentDate();

                        if (aptdat != null) {
//                            aptdat = aptdat.substring(0, 10);
//                            oldDateString = aptdat;
//                            sdf.applyPattern(OLD_FORMAT);
//
//
//                            try {
//                                d = sdf.parse(oldDateString);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            sdf.applyPattern(NEW_FORMAT);
//                            newDateString = sdf.format(d);

                            appointDate.setText(aptdat);
                        } else {
                            appointDate.setText("");
                        }

                        if (firstPageData.get(i).getRevision() != null) {
                            revision.setText(firstPageData.get(i).getRevision());
                        }
                        if (firstPageData.get(i).getPublish() != null) {
                            publish.setText(firstPageData.get(i).getPublish());
                        }
                        if (firstPageData.get(i).getBand() != null) {
                            band.setText(firstPageData.get(i).getBand());
                        }
                        if (firstPageData.get(i).getFunc_desg() != null) {
                            func_des.setText(firstPageData.get(i).getFunc_desg());
                        }
                        if (firstPageData.get(i).getFunc_desg_bn() != null) {
                            func_des_bn.setText(firstPageData.get(i).getFunc_desg_bn());
                        }
                        if (firstPageData.get(i).getSection() != null) {
                            section.setText(firstPageData.get(i).getSection());
                        }
                        if (firstPageData.get(i).getAdd_charge() != null) {
                            add_ch.setText(firstPageData.get(i).getAdd_charge());
                        }
                        if (firstPageData.get(i).getPrim_st() != null) {
                            prim_station.setText(firstPageData.get(i).getPrim_st());
                        }
                        if (firstPageData.get(i).getSec_st() != null) {
                            sec_station.setText(firstPageData.get(i).getSec_st());
                        }
                        if (firstPageData.get(i).getProj() != null) {
                            proj.setText(firstPageData.get(i).getProj());
                        }
                        if (firstPageData.get(i).getWork_time() != null) {
                            workTime.setText(firstPageData.get(i).getWork_time());
                        }
                        if (firstPageData.get(i).getEarly_arr() != null) {
                            earlyArrival.setText(firstPageData.get(i).getEarly_arr());
                        }
                        if (firstPageData.get(i).getExted_dep() != null) {
                            ext_dep.setText(firstPageData.get(i).getExted_dep());
                        }
                        if (firstPageData.get(i).getTemp_ch() != null) {
                            temp_ch.setText(firstPageData.get(i).getTemp_ch());
                        }
                        if (firstPageData.get(i).getEmmmall() != null) {
                            e_mail.setText(firstPageData.get(i).getEmmmall());
                        }
                        if (firstPageData.get(i).getSoft_acc() != null) {
                            soft_acc.setText(firstPageData.get(i).getSoft_acc());
                        }
                        if (firstPageData.get(i).getRem_acc() != null) {
                            rem_acc.setText(firstPageData.get(i).getRem_acc());
                        }
                        if (firstPageData.get(i).getComp() != null) {
                            comp.setText(firstPageData.get(i).getComp());
                        }
                        if (firstPageData.get(i).getDorm() != null) {
                            dorm.setText(firstPageData.get(i).getDorm());
                        }
                        if (firstPageData.get(i).getSt_pos() != null) {
//                            st_pos.setSelection(Integer.parseInt(firstPageData.get(i).getSt_pos()));
                            st_pos.setText(strrPos.get(Integer.parseInt(firstPageData.get(i).getSt_pos())));
                        }
                        if (firstPageData.get(i).getUniff() != null) {
                            uniform.setText(firstPageData.get(i).getUniff());
                        }
                        if (firstPageData.get(i).getApart() != null) {
                            apart.setText(firstPageData.get(i).getApart());
                        }
                        if (firstPageData.get(i).getLat_acc() != null) {
//                            late_accp.setSelection(Integer.parseInt(firstPageData.get(i).getLat_acc()));
                            late_accp.setText(late.get(Integer.parseInt(firstPageData.get(i).getLat_acc())));
                        }
                        if (firstPageData.get(i).getAtt_bonus() != null) {
//                            att_bon.setSelection(Integer.parseInt(firstPageData.get(i).getAtt_bonus()));
                            att_bon.setText(attBon.get(Integer.parseInt(firstPageData.get(i).getAtt_bonus())));
                        }

                    }
                }


                if (firstPageAnotherData.size() != 0) {
                    for (int i= 0; i < firstPageAnotherData.size(); i++) {

                        if (firstPageAnotherData.get(i).getJob_no() != null) {
                            jobNo.setText(firstPageAnotherData.get(i).getJob_no());
                        }
                        if (firstPageAnotherData.get(i).getStruc_desg() != null) {
                            struc_des.setText(firstPageAnotherData.get(i).getStruc_desg());
                        }
                        if (firstPageAnotherData.get(i).getJob_objective() != null) {
                            job_obj.setText(firstPageAnotherData.get(i).getJob_objective());
                        }
                        if (firstPageAnotherData.get(i).getDivisoin() != null) {
                            div.setText(firstPageAnotherData.get(i).getDivisoin());
                        }
                        if (firstPageAnotherData.get(i).getDepartment() != null) {
                            depa.setText(firstPageAnotherData.get(i).getDepartment());
                        }

                    }
                }

                conn = false;
                connected = false;



            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Check().execute();
                        dialog.dismiss();
                    }
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Activity)mContext).finish();
                    }
                });
            }
        }
    }

    public void DataQuery() {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            firstPageData = new ArrayList<>();
            firstPageAnotherData = new ArrayList<>();
            Statement stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("SELECT JOB_TRANS_CODE,TO_CHAR(TO_DATE(JOB_REVISION_DATE),'DD-MON-YYYY') JOB_REVISION_DATE, TO_CHAR(TO_DATE(JOB_EFF_DATE),'DD-MON-YYYY') JOB_EFF_DATE, JOB_REVISION_STATUS, TO_CHAR(TO_DATE(JOB_APPOINT_RCV_DATE),'DD-MON-YYYY') JOB_APPOINT_RCV_DATE,\n" +
                    "(Select EMP_PACKAGE.GET_BAND_BY_EMP_CODE('"+emp_name+"') from Dual ) as BAND, JOB_CALLING_TITLE, JOB_CALLING_TITLE_BN,\n" +
                    "(SELECT SECT_NAME FROM SECTION_MST WHERE SECT_ID = JOB_SECT_ID) as SECTION, JOB_AD_CHARGE,\n" +
                    "(SELECT COMPANY_OFFICE_ADDRESS.COA_NAME FROM COMPANY_OFFICE_ADDRESS WHERE COA_ID = JOB_PRI_COA_ID) as PRIMARY_STATION,\n" +
                    "(SELECT  COMPANY_OFFICE_ADDRESS.COA_NAME FROM COMPANY_OFFICE_ADDRESS WHERE COA_ID = JOB_SEC_COA_ID) as SEC_STATION,\n" +
                    "( SELECT PROJECT_MST.PM_NAME FROM PROJECT_MST WHERE PM_ID = JOB_PM_ID) as Project,\n" +
                    "JOB_WORK_TIME, JOB_EARLY_ARIVAL, \n" +
                    "JOB_EXTENDED_DEPARTURE, JOB_TEMP_CHARGE, JOB_EMAIL, JOB_SOFT_ACCESS, JOB_REM_ACCESS, JOB_COMPUTER, JOB_DORMITORY, \n" +
                    "JOB_STRUCTURAL_POSITION, JOB_UNIFORM, JOB_APARTMENT_STATUS, JOB_LATE_ACCEPT, JOB_ATTD_BONUS_FLAG\n" +
                    "FROM EMP_JOB_HISTORY\n" +
                    "WHERE JOB_ID = (SELECT MAX(JOB_ID) FROM EMP_JOB_HISTORY WHERE JOB_EMP_ID = "+emp_id+")");


            while(rs.next()) {

                firstPageData.add(new FirstPageData(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4), rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
                        rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),
                        rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),
                        rs.getString(20),rs.getString(21),rs.getString(22),rs.getString(23),
                        rs.getString(24),rs.getString(25),rs.getString(26),rs.getString(27)));

            }

            ResultSet resultSet = stmt.executeQuery("SELECT DISTINCT JOB_SETUP_MST.JSM_CODE, JOB_SETUP_MST.JSM_NAME, \n" +
                    "JOB_SETUP_DTL.JSD_OBJECTIVE, DIVISION_MST.DIVM_NAME, DEPT_MST.DEPT_NAME\n" +
                    "FROM JOB_SETUP_MST, JOB_SETUP_DTL, DEPT_MST, DIVISION_MST, DESIG_MST\n" +
                    "WHERE ((JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
                    " AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
                    " AND (DEPT_MST.DEPT_ID = JOB_SETUP_MST.JSM_DEPT_ID)\n" +
                    " AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID))\n" +
                    " AND JOB_SETUP_DTL.JSD_ID = (SELECT JOB_JSD_ID\n" +
                    "                            FROM EMP_JOB_HISTORY\n" +
                    "                            WHERE JOB_ID = (SELECT MAX(JOB_ID) FROM EMP_JOB_HISTORY WHERE JOB_EMP_ID = "+emp_id+"))");

            while (resultSet.next()) {

                firstPageAnotherData.add(new FirstPageAnotherData(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
                        resultSet.getString(4),resultSet.getString(5)));
            }

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}
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

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link SecondFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText expecJoin;
    EditText actuJoin;
    EditText calcJoin;
    EditText appoinType;
    EditText revisoinType;
    EditText serviceStatus;
    EditText description;
    EditText probaPeriod;
    EditText probaComp;
    EditText workPeEdDate;
    EditText workPePeriod;
    EditText workPeComp;
    EditText quitDate;
    EditText shiftAlloc;
    EditText salaryPac;
    EditText increAmnt;
    EditText decreAmnt;
    EditText grossSalary;
    EditText cashSalary;
    EditText basicSalary;
    EditText houseRent;
    EditText medicalAllow;
    EditText conveyaAllow;
    EditText habitation;
    EditText utilty;
    EditText houseAllow;
    EditText entertain;
    EditText factoryAllow;
    EditText committedSalary;
    EditText mobileBill;
    EditText additinAllow;
    EditText fixedOt;
    EditText foodSubsidy;
    EditText otherAlloDetl;
    EditText overtimeAllowApp;
    EditText overtimeRate;
    EditText providentFundType;
    EditText providentFund;
    EditText otCalPeriod;
    EditText taxDeduc;
    EditText sitting;
    EditText lunchStatus;
    EditText transport;
    EditText pabx_office;
    EditText pabx_factory;

//    Spinner fesTypeBonus;
//    Spinner foodSubsidyType;
//    Spinner taxBearer;
    EditText fesTypeBonus;
    EditText foodSubsidyType;
    EditText taxBearer;

    ArrayList<String> fesBonus;
    ArrayList<String> foodsubsidy;
    ArrayList<String> taxBear;

    ArrayAdapter<String> fesAdapter;
    ArrayAdapter<String> foodAdapter;
    ArrayAdapter<String> taxAdapter;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Connection connection;

    String emp_id = "";

    public static ArrayList<SecondPageData> secondPageData;

    Context mContext;
    public SecondFragment(Context context) {
        // Required empty public constructor
        this.mContext = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static SecondFragment newInstance(String param1, String param2) {
//        SecondFragment fragment = new SecondFragment();
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
        View view =  inflater.inflate(R.layout.fragment_second, container, false);

        emp_id = userInfoLists.get(0).getEmp_id();

        secondPageData = new ArrayList<>();

        expecJoin = view.findViewById(R.id.expected_join);
        actuJoin = view.findViewById(R.id.actual_join);
        calcJoin = view.findViewById(R.id.calculation_join);
        appoinType = view.findViewById(R.id.appointment_type);
        revisoinType = view.findViewById(R.id.revision_type);
        serviceStatus = view.findViewById(R.id.service_status);
        description = view.findViewById(R.id.description);
        probaPeriod = view.findViewById(R.id.probation_period);
        probaComp = view.findViewById(R.id.probation_comp);
        workPeEdDate = view.findViewById(R.id.work_pe_eff_dat);
        workPePeriod = view.findViewById(R.id.work_per_period);
        workPeComp = view.findViewById(R.id.work_pe_completion);
        quitDate = view.findViewById(R.id.quit_dat);
        shiftAlloc = view.findViewById(R.id.shift_allocation);
        salaryPac = view.findViewById(R.id.salary_package);
        increAmnt = view.findViewById(R.id.increment_amnt);
        decreAmnt = view.findViewById(R.id.decrement_amnt);
        grossSalary = view.findViewById(R.id.gross_salary);
        cashSalary = view.findViewById(R.id.cash_salary);
        basicSalary = view.findViewById(R.id.basic_salary);
        houseRent = view.findViewById(R.id.house_rent);
        medicalAllow = view.findViewById(R.id.medical_allow);
        conveyaAllow = view.findViewById(R.id.conveyance_allow);
        habitation = view.findViewById(R.id.habitation);
        utilty = view.findViewById(R.id.utilities);
        houseAllow = view.findViewById(R.id.house_allow);
        entertain = view.findViewById(R.id.entertainment);
        factoryAllow = view.findViewById(R.id.factory_allow);
        committedSalary = view.findViewById(R.id.committed_salary);
        mobileBill = view.findViewById(R.id.mobile_bill_coverage);
        additinAllow = view.findViewById(R.id.additional_allow);
        fixedOt = view.findViewById(R.id.fixed_ot);
        foodSubsidy = view.findViewById(R.id.food_subsidy);
        otherAlloDetl = view.findViewById(R.id.others_allow_detl);
        overtimeAllowApp = view.findViewById(R.id.overtime_allow_applic);
        overtimeRate = view.findViewById(R.id.overtime_rate);
        providentFundType = view.findViewById(R.id.provident_fund_type);
        providentFund = view.findViewById(R.id.provident_fund);
        otCalPeriod = view.findViewById(R.id.ot_cal_period);
        taxDeduc = view.findViewById(R.id.tax_deduction);
        sitting = view.findViewById(R.id.sitting);
        lunchStatus = view.findViewById(R.id.lunch_status);
        transport = view.findViewById(R.id.transport);
        pabx_office = view.findViewById(R.id.pabx_corporate_offc);
        pabx_factory = view.findViewById(R.id.pabx_factory);

        fesTypeBonus = view.findViewById(R.id.spinner_fes_bon_type);
        foodSubsidyType = view.findViewById(R.id.spinner_food_sub_type);
        taxBearer = view.findViewById(R.id.spinner_tax_bearer);

        fesBonus = new ArrayList<>();
        foodsubsidy = new ArrayList<>();
        taxBear = new ArrayList<>();


        fesBonus.add("Not Applicable");
        fesBonus.add("Regular");
        fesBonus.add("100 % of Basic");
        fesBonus.add("75 % of Basic");
        fesBonus.add("50 % of Basic");
        fesBonus.add("25% of Basic");


        foodsubsidy.add("N/A");
        foodsubsidy.add("Fixed");
        foodsubsidy.add("Subject to attendance");

        taxBear.add("Self");
        taxBear.add("Company");

//        fesTypeBonus.setEnabled(false);
//        foodSubsidyType.setEnabled(false);
//        taxBearer.setEnabled(false);

        // Festival Bonus Type
//        fesAdapter = new ArrayAdapter<String>(
//                getContext(),R.layout.item_country,fesBonus){
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
//        fesTypeBonus.setGravity(Gravity.END);
//        fesAdapter.setDropDownViewResource(R.layout.item_country);
//        fesTypeBonus.setAdapter(fesAdapter);
//
//
//        // Food Subsidy Type
//        foodAdapter = new ArrayAdapter<String>(
//                getContext(),R.layout.item_country,foodsubsidy){
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
//        foodSubsidyType.setGravity(Gravity.END);
//        foodAdapter.setDropDownViewResource(R.layout.item_country);
//        foodSubsidyType.setAdapter(foodAdapter);
//
//        // TAX Bearer
//        taxAdapter = new ArrayAdapter<String>(
//                getContext(),R.layout.item_country,taxBear){
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
//        taxBearer.setGravity(Gravity.END);
//        taxAdapter.setDropDownViewResource(R.layout.item_country);
//        taxBearer.setAdapter(taxAdapter);

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

                if (secondPageData.size() != 0) {
                    for (int i = 0 ; i < secondPageData.size(); i++) {

//                    final String OLD_FORMAT = "yyyy-MM-dd";
//                    final String NEW_FORMAT = "dd-MMM-yyyy";
//                    SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.getDefault());
//                    String oldDateString;
//                    String newDateString;
//                    Date d = null;
                        String dateC = secondPageData.get(i).getExpectedDate();
                        if (dateC != null) {
//                        dateC = dateC.substring(0, 10);
//                        oldDateString = dateC;
//                        try {
//                            d = sdf.parse(oldDateString);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        sdf.applyPattern(NEW_FORMAT);
//                        //sdf.applyPattern(NEW_FORMAT);
//                        newDateString = sdf.format(d);
                            expecJoin.setText(dateC);
                        } else {
                            expecJoin.setText("");
                        }



                        String datt = secondPageData.get(i).getActualDate();
                        if (datt != null) {
//                        datt = datt.substring(0, 10);
//                        oldDateString = datt;
//                        sdf.applyPattern(OLD_FORMAT);
//
//                        try {
//                            d = sdf.parse(oldDateString);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        sdf.applyPattern(NEW_FORMAT);
//                        newDateString = sdf.format(d);

                            actuJoin.setText(datt);
                        } else {
                            actuJoin.setText("");
                        }


                        String aptdat = secondPageData.get(i).getCalculationDate();

                        if (aptdat != null) {
//                        aptdat = aptdat.substring(0, 10);
//                        oldDateString = aptdat;
//                        sdf.applyPattern(OLD_FORMAT);
//
//
//                        try {
//                            d = sdf.parse(oldDateString);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        sdf.applyPattern(NEW_FORMAT);
//                        newDateString = sdf.format(d);

                            calcJoin.setText(aptdat);
                        } else {
                            calcJoin.setText("");
                        }

                        String procompdat = secondPageData.get(i).getProba_comp();

                        if (procompdat != null) {
//                        procompdat = procompdat.substring(0, 10);
//                        oldDateString = procompdat;
//                        sdf.applyPattern(OLD_FORMAT);
//
//
//                        try {
//                            d = sdf.parse(oldDateString);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        sdf.applyPattern(NEW_FORMAT);
//                        newDateString = sdf.format(d);

                            probaComp.setText(procompdat);
                        } else {
                            probaComp.setText("");
                        }


                        String woreffedat = secondPageData.get(i).getWork_per_date();

                        if (woreffedat != null) {
//                        woreffedat = woreffedat.substring(0, 10);
//                        oldDateString = woreffedat;
//                        sdf.applyPattern(OLD_FORMAT);
//
//
//                        try {
//                            d = sdf.parse(oldDateString);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        sdf.applyPattern(NEW_FORMAT);
//                        newDateString = sdf.format(d);

                            workPeEdDate.setText(woreffedat);
                        } else {
                            workPeEdDate.setText("");
                        }

                        String workPEEEComDa = secondPageData.get(i).getWork_per_comp();

                        if (workPEEEComDa != null) {
//                        workPEEEComDa = workPEEEComDa.substring(0, 10);
//                        oldDateString = workPEEEComDa;
//                        sdf.applyPattern(OLD_FORMAT);
//
//
//                        try {
//                            d = sdf.parse(oldDateString);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        sdf.applyPattern(NEW_FORMAT);
//                        newDateString = sdf.format(d);

                            workPeComp.setText(workPEEEComDa);
                        } else {
                            workPeComp.setText("");
                        }

                        String quitdAt = secondPageData.get(i).getQuit();

                        if (quitdAt != null) {
//                        quitdAt = quitdAt.substring(0, 10);
//                        oldDateString = quitdAt;
//                        sdf.applyPattern(OLD_FORMAT);
//
//
//                        try {
//                            d = sdf.parse(oldDateString);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        sdf.applyPattern(NEW_FORMAT);
//                        newDateString = sdf.format(d);

                            quitDate.setText(quitdAt);
                        } else {
                            quitDate.setText("");
                        }

                        if (secondPageData.get(i).getAppointmentType() != null) {
                            appoinType.setText(secondPageData.get(i).getAppointmentType());
                        }
                        if (secondPageData.get(i).getRevisionType() != null) {
                            revisoinType.setText(secondPageData.get(i).getRevisionType());
                        }
                        if (secondPageData.get(i).getSer_status() != null) {
                            serviceStatus.setText(secondPageData.get(i).getSer_status());
                        }
                        if (secondPageData.get(i).getDesc() != null) {
                            description.setText(secondPageData.get(i).getDesc());
                        }
                        if (secondPageData.get(i).getProba_per() != null) {
                            probaPeriod.setText(secondPageData.get(i).getProba_per());
                        }
                        if (secondPageData.get(i).getWork_per_period() != null) {
                            workPePeriod.setText(secondPageData.get(i).getWork_per_period());
                        }
                        if (secondPageData.get(i).getShift() != null) {
                            shiftAlloc.setText(secondPageData.get(i).getShift());
                        }


                        if (secondPageData.get(i).getSalary_pack() != null) {
                            salaryPac.setText(secondPageData.get(i).getSalary_pack());
                        }
                        if (secondPageData.get(i).getIncre_amnt() != null) {
                            increAmnt.setText(secondPageData.get(i).getIncre_amnt());
                        }
                        if (secondPageData.get(i).getDecr_amnt() != null) {
                            decreAmnt.setText(secondPageData.get(i).getDecr_amnt());
                        }
                        if (secondPageData.get(i).getGross_sal() != null) {
                            grossSalary.setText(secondPageData.get(i).getGross_sal());
                        }
                        if (secondPageData.get(i).getCash_sal() != null) {
                            cashSalary.setText(secondPageData.get(i).getCash_sal());
                        }
                        if (secondPageData.get(i).getBasic_sal() != null) {
                            basicSalary.setText(secondPageData.get(i).getBasic_sal());
                        }
                        if (secondPageData.get(i).getHouse_rent() != null) {
                            houseRent.setText(secondPageData.get(i).getHouse_rent());
                        }
                        if (secondPageData.get(i).getMed_allow() != null) {
                            medicalAllow.setText(secondPageData.get(i).getMed_allow());
                        }
                        if (secondPageData.get(i).getConv_allow() != null) {
                            conveyaAllow.setText(secondPageData.get(i).getConv_allow());
                        }
                        if (secondPageData.get(i).getHabitation() != null) {
                            habitation.setText(secondPageData.get(i).getHabitation());
                        }
                        if (secondPageData.get(i).getUtilti() != null) {
                            utilty.setText(secondPageData.get(i).getUtilti());
                        }
                        if (secondPageData.get(i).getHoiuse_allow() != null) {
                            houseAllow.setText(secondPageData.get(i).getHoiuse_allow());
                        }
                        if (secondPageData.get(i).getEntertain() != null) {
                            entertain.setText(secondPageData.get(i).getEntertain());
                        }
                        if (secondPageData.get(i).getFact_allow() != null) {
                            factoryAllow.setText(secondPageData.get(i).getFact_allow());
                        }


                        if (secondPageData.get(i).getComm_sal() != null) {
                            committedSalary.setText(secondPageData.get(i).getComm_sal());
                        }
                        if (secondPageData.get(i).getFes_bon_type() != null) {
//                            fesTypeBonus.setSelection(Integer.parseInt(secondPageData.get(i).getFes_bon_type()));
                            fesTypeBonus.setText(fesBonus.get(Integer.parseInt(secondPageData.get(i).getFes_bon_type())));
                        }
                        if (secondPageData.get(i).getMobile_bill() != null) {
                            mobileBill.setText(secondPageData.get(i).getMobile_bill());
                        }
                        if (secondPageData.get(i).getAdd_allow() != null) {
                            additinAllow.setText(secondPageData.get(i).getAdd_allow());
                        }
                        if (secondPageData.get(i).getFixed_ot() != null) {
                            fixedOt.setText(secondPageData.get(i).getFixed_ot());
                        }
                        if (secondPageData.get(i).getFood_sub_type() != null) {
//                            foodSubsidyType.setSelection(Integer.parseInt(secondPageData.get(i).getFood_sub_type()));
                            foodSubsidyType.setText(foodsubsidy.get(Integer.parseInt(secondPageData.get(i).getFood_sub_type())));
                        }
                        if (secondPageData.get(i).getFood_sub() != null) {
                            foodSubsidy.setText(secondPageData.get(i).getFood_sub());
                        }
                        if (secondPageData.get(i).getOthers_allow_detl() != null) {
                            otherAlloDetl.setText(secondPageData.get(i).getOthers_allow_detl());
                        }
                        if (secondPageData.get(i).getOver_allow_app() != null) {
                            overtimeAllowApp.setText(secondPageData.get(i).getOver_allow_app());
                        }
                        if (secondPageData.get(i).getOver_rate() != null) {
                            overtimeRate.setText(secondPageData.get(i).getOver_rate());
                        }
                        if (secondPageData.get(i).getPro_fund_type() != null) {
                            providentFundType.setText(secondPageData.get(i).getPro_fund_type());
                        }
                        if (secondPageData.get(i).getPro_fund() != null) {
                            providentFund.setText(secondPageData.get(i).getPro_fund());
                        }
                        if (secondPageData.get(i).getOt_cal_per() != null) {
                            otCalPeriod.setText(secondPageData.get(i).getOt_cal_per());
                        }



                        if (secondPageData.get(i).getTax_bearer() != null) {
//                            taxBearer.setSelection(Integer.parseInt(secondPageData.get(i).getTax_bearer()));
                            taxBearer.setText(taxBear.get(Integer.parseInt(secondPageData.get(i).getTax_bearer())));
                        }
                        if (secondPageData.get(i).getTax_deduc() != null) {
                            taxDeduc.setText(secondPageData.get(i).getTax_deduc());
                        }
                        if (secondPageData.get(i).getSitting() != null) {
                            sitting.setText(secondPageData.get(i).getSitting());
                        }
                        if (secondPageData.get(i).getLunch() != null) {
                            lunchStatus.setText(secondPageData.get(i).getLunch());
                        }
                        if (secondPageData.get(i).getTransport() != null) {
                            transport.setText(secondPageData.get(i).getTransport());
                        }
                        if (secondPageData.get(i).getPabx_off() != null) {
                            pabx_office.setText(secondPageData.get(i).getPabx_off());
                        }
                        if (secondPageData.get(i).getPabx_fact() != null) {
                            pabx_factory.setText(secondPageData.get(i).getPabx_fact());
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

            secondPageData = new ArrayList<>();

            Statement stmt = connection.createStatement();

            ResultSet rs=stmt.executeQuery("SELECT TO_CHAR(TO_DATE(JOB_EXP_DATE),'DD-MON-YYYY') JOB_EXP_DATE, TO_CHAR(TO_DATE(JOB_ACTUAL_DATE),'DD-MON-YYYY') JOB_ACTUAL_DATE, TO_CHAR(TO_DATE(JOB_CALCULATION_DATE),'DD-MON-YYYY') JOB_CALCULATION_DATE, JOB_RECRUIT_TYPE, JOB_INCREMENT_FLAG, JOB_STATUS,\n" +
                    "JOB_STATUS_DETAILS, JOB_PROBATION_PERIOD, TO_CHAR(TO_DATE(JOB_PROBATION_DATE),'DD-MON-YYYY') JOB_PROBATION_DATE, TO_CHAR(TO_DATE(JOB_WORK_PE_DATE),'DD-MON-YYYY') JOB_WORK_PE_DATE, JOB_WORK_P_PERIOD,\n" +
                    "TO_CHAR(TO_DATE(JOB_WORK_P_COM_DATE),'DD-MON-YYYY') JOB_WORK_P_COM_DATE, TO_CHAR(TO_DATE(JOB_QUIT_DATE),'DD-MON-YYYY') JOB_QUIT_DATE, JOB_SHIFT, \n" +
                    "(SELECT SBSM_NAME FROM SALARY_BREAKDOWN_SETUP_MST WHERE SBSM_ID = JOB_SBSM_ID) as Package,\n" +
                    "JOB_INCREMENT_AMT, JOB_DECREMENT_AMT, JOB_GROSS_SAL, JOB_CASH_AMT, JOB_BASIC, JOB_HR,\n" +
                    "JOB_MD, JOB_CONVEYANCE, JOB_HABITATION, JOB_UTILITIES, JOB_HOUSE_ALLOWANCE, JOB_ENTERTAINMENT, JOB_FACTORY_ALLOWANCE_AMT,\n" +
                    "JOB_COMMITTED_SALARY, JOB_FESTIVAL_BONUS_FLAG, JOB_MOBILE_BILL, JOB_OTH_ALL, JOB_FIXED_OT, JOB_FOOD_SUBSIDY_FLAG,\n" +
                    "JOB_FOOD_SUBSIDY, JOB_OTH_ALL_DTL, JOB_OVERTIME_AVAIL_FLAG, JOB_OT_AMT, JOB_PF_TYPE, JOB_PF, JOB_OT_CAL_PERIOD,\n" +
                    "JOB_TAX_FLAG, JOB_TAX, JOB_SITTING, JOB_LUNCH_STATUS, JOB_TRANSPORT, JOB_PABX_CORPORATE, JOB_PABX_FACTORY\n" +
                    "FROM EMP_JOB_HISTORY\n" +
                    "WHERE JOB_ID = (SELECT MAX(JOB_ID) FROM EMP_JOB_HISTORY WHERE JOB_EMP_ID = "+emp_id+")");


            while(rs.next()) {

                secondPageData.add(new SecondPageData(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4), rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
                        rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),
                        rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),
                        rs.getString(20),rs.getString(21),rs.getString(22),rs.getString(23),
                        rs.getString(24),rs.getString(25),rs.getString(26),rs.getString(27),
                        rs.getString(28),rs.getString(29),rs.getString(30),rs.getString(31),
                        rs.getString(32),rs.getString(33),rs.getString(34),rs.getString(35),
                        rs.getString(36),rs.getString(37),rs.getString(38),rs.getString(39),
                        rs.getString(40),rs.getString(41),rs.getString(42),rs.getString(43),
                        rs.getString(44),rs.getString(45),rs.getString(46),rs.getString(47),
                        rs.getString(48)));

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
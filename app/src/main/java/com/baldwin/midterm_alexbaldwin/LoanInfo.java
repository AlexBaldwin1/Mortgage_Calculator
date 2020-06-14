package com.baldwin.midterm_alexbaldwin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class LoanInfo extends Fragment {

    EditText etLoanAmount;
    EditText etMonthlyPayment;
    TextView tvLoanYears;
    Button btnChangeAmountOfYears;
    int numberOfYears;
    View root;
    long loanAmount;
    int monthlyPayment;


    ChangeLoanYearsListener mCallBack;

    DecimalFormat formatter;

    //InterestRate will be in thousandths
    int interestRate;

    public void setLoanYears(int newYears) {
        numberOfYears = newYears;
        tvLoanYears.setText(Integer.valueOf(numberOfYears) + " Year repayment");

        calculatePayment();
    }

    public void reset() {
        setLoanYears(30);
        loanAmount = 300000;
        etLoanAmount.setText("300,000");
        calculatePayment();

    }


    public interface ChangeLoanYearsListener {
        void ChangeLoanYears();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ChangeLoanYearsListener) {
            mCallBack = (ChangeLoanYearsListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement LoanInfo.ChangeLoanYearsListner");
        }
    }

    public LoanInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*if(numberOfYears == 0){
            numberOfYears = 30;
        }
        if(loanAmount == 0){
            loanAmount = 300000;
        }

        //TODO change this
        interestRate = 3400;*/

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");

        // Inflate the layout for this fragment
        root =inflater.inflate(R.layout.fragment_loan_info, container, false);
        return root;

    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        //spEdit.putLong("LoanAmount", Long.parseLong(etLoanAmount.getText().toString().replaceAll(",", "")));
        spEdit.putLong("LoanAmount", loanAmount);
        spEdit.putInt("PaymentAmount", Integer.parseInt(etMonthlyPayment.getText().toString().replaceAll(",", "")));
        spEdit.putInt("InterestRate1", interestRate);
        spEdit.putInt("NumberOfYears", numberOfYears);
        spEdit.commit();
    }

    @Override
    public void onResume() {
        etLoanAmount = root.findViewById(R.id.etLoanAmount);
        etMonthlyPayment = root.findViewById(R.id.etMonthlyPayment);
        tvLoanYears = root.findViewById(R.id.tvLoanYears);
        btnChangeAmountOfYears = root.findViewById(R.id.btnChangeYears);

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        loanAmount = sp.getLong("LoanAmount",300000);
        // etLoanAmount.setText(String.valueOf(sp.getInt("LoanAmount",0)));
        monthlyPayment = 0;//sp.getInt("PaymentAmount",0);
        //etMonthlyPayment.setText(String.valueOf(sp.getInt("PaymentAmount",0)));
        interestRate = sp.getInt("InterestRate1", 3500);
        numberOfYears = sp.getInt("NumberOfYears", 30);
        tvLoanYears.setText(Integer.valueOf(numberOfYears) + " Year repayment");



        btnChangeAmountOfYears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Dialog for selecting amount of years
            mCallBack.ChangeLoanYears();

            }
        });



       final TextWatcher txtWatcherLoanAmount = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //loanAmount = Integer.parseInt(etLoanAmount.getText().toString());
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(etLoanAmount.getText().length() == 0){
                    etLoanAmount.removeTextChangedListener(this);
                    etLoanAmount.setText("");
                    etLoanAmount.addTextChangedListener(this);
                    return;
                }

                loanAmount =  Integer.parseInt(etLoanAmount.getText().toString().replaceAll(",",""));
                if(etLoanAmount.isFocused()){

                    float interestRateFloat = (float) (interestRate/100000.0/12);
                    int numberOfMonths = numberOfYears *12 *-1;
                    monthlyPayment =(int) ((interestRateFloat *loanAmount) / (1- ((Math.pow(interestRateFloat +1,numberOfMonths)))));
                    etMonthlyPayment.setText(String.valueOf(monthlyPayment));

                }

                etLoanAmount.removeTextChangedListener(this);
                etLoanAmount.setText(formatter.format(loanAmount));
                etLoanAmount.setSelection(etLoanAmount.getText().length());
                etLoanAmount.addTextChangedListener(this);

/*                // Get the new loan amount
                int newLoanAmount = Integer.parseInt(etLoanAmount.getText().toString().replaceAll(",",""));

                //There is a difference between old and new amount
                if(etLoanAmount.getText().length() >0 && loanAmount !=  newLoanAmount){
                    // It is zero return
                    if(newLoanAmount == 0) {
                        loanAmount = newLoanAmount;
                        return;
                    }

                    //loanAmountChanged();

                    loanAmount = newLoanAmount;
                    monthlyPayment = 0;
                    Log.d("LoanInfoFrag", "LoanAmountChanged");


                    etLoanAmount.removeTextChangedListener(this);
                    etLoanAmount.setText(formatter.format(loanAmount));
                    etLoanAmount.setSelection(etLoanAmount.getText().length());
                    etLoanAmount.addTextChangedListener(this);
                    //setTextWithoutTriggeringListener(etLoanAmount, loanAmount, this);
                    //setTextWithoutTriggeringListener();

                }*/
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };

       //Monthly watcher
        final TextWatcher txtWatcherMonthlyAmount = new TextWatcher() {
            int newMonthlyPayment;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //monthlyPayment = Integer.parseInt(etMonthlyPayment.getText().toString());
                if(etMonthlyPayment.isSelected()){
                    Log.d("monthly", "Before TextChanged");
                }

            }
            //Monthly
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etMonthlyPayment.getText().length() == 0){
                    etMonthlyPayment.removeTextChangedListener(this);
                    etMonthlyPayment.setText("");
                    etMonthlyPayment.addTextChangedListener(this);
                    return;
                }

                if( etMonthlyPayment.isFocused()){

                    monthlyPayment = Integer.parseInt(etMonthlyPayment.getText().toString().replaceAll(",",""));
                    // Calculate loan amount
                    float interestRateFloat = (float) (interestRate/100000.0/12);
                    int numberOfMonths = numberOfYears *12 *-1;
                    loanAmount = (int) (monthlyPayment * (1- ((Math.pow(interestRateFloat +1,numberOfMonths))))/interestRateFloat);
                    etLoanAmount.setText(String.valueOf(loanAmount));

                }

                /*mewMonthlyPayment = Integer.parseInt(etMonthlyPayment.getText().toString().replaceAll(",",""));
                if(etMonthlyPayment.getText().length() > 0 &&  monthlyPayment !=newMonthlyPayment){
                    if(newMonthlyPayment == 0) {
                        monthlyPayment = newMonthlyPayment;
                        return;
                    }
                }*/
                etMonthlyPayment.removeTextChangedListener(this);
                etMonthlyPayment.setText(formatter.format(monthlyPayment));
                etMonthlyPayment.setSelection(etMonthlyPayment.getText().length());
                etMonthlyPayment.addTextChangedListener(this);

            }
            //Monthly
            @Override
            public void afterTextChanged(Editable s) {}
        };


        etLoanAmount.addTextChangedListener(txtWatcherLoanAmount);
        etMonthlyPayment.addTextChangedListener(txtWatcherMonthlyAmount);

        calculatePayment();
        super.onResume();
    }
    public void setTextWithoutTriggeringListener(EditText et, long value, TextWatcher tw){
        //Find a way to get the text watcher or make it gloabal
        et.removeTextChangedListener(tw);
        et.setText(formatter.format(value));
        et.setSelection(et.getText().length());
        et.addTextChangedListener(tw);

    }

/*    private void monthlyAmountChanged() {
        loanAmount = 0;
        monthlyPayment = Integer.parseInt(etMonthlyPayment.getText().toString().replaceAll(",",""));;
        Log.d("LoanInfoFrag", "MonthlyAmountChanged");
        calculatePayment();
    }

    private void loanAmountChanged() {
        loanAmount = Integer.parseInt(etLoanAmount.getText().toString().replaceAll(",",""));
        monthlyPayment = 0;
        Log.d("LoanInfoFrag", "LoanAmountChanged");
        calculatePayment();

    }*/

    //This will set to ints of the global.
    private void calculatePayment() {
        float interestRateFloat = (float) (interestRate/100000.0/12);
        int numberOfMonths = numberOfYears *12 *-1;


        if(monthlyPayment == 0){
            monthlyPayment =(int) ((interestRateFloat *loanAmount) / (1- ((Math.pow(interestRateFloat +1,numberOfMonths)))));
            etMonthlyPayment.setText(String.valueOf(monthlyPayment));
        }else if(loanAmount == 0 ){
            loanAmount = (int) (monthlyPayment * (1- ((Math.pow(interestRateFloat +1,numberOfMonths))))/interestRateFloat);
            etLoanAmount.setText(String.valueOf(loanAmount));
        }
        else{
            // assume to recalculate monthly payment this is the same as the first
            monthlyPayment =(int) ((interestRateFloat *loanAmount) / (1- ((Math.pow(interestRateFloat +1,numberOfMonths)))));
            etMonthlyPayment.setText(String.valueOf(monthlyPayment));
        }

    }

    public void setInterest(int interest) {
        this.interestRate = interest;
        calculatePayment();
    }
}

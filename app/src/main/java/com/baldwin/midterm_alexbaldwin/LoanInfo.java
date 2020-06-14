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

import static java.lang.StrictMath.round;


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
        spEdit.putLong("LoanAmount", loanAmount);
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
        monthlyPayment = 0;
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
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etLoanAmount.getText().length() == 0 ||
                        Long.parseLong(etLoanAmount.getText().toString().replaceAll(",","")) == 0){return;}
                //There is a new loan amount
                if(loanAmount != Long.parseLong(etLoanAmount.getText().toString().replaceAll(",",""))){

                    loanAmount = Long.parseLong(etLoanAmount.getText().toString().replaceAll(",",""));

                    calculateMonthlyPayment();
                }

                etLoanAmount.removeTextChangedListener(this);
                etLoanAmount.setText(formatter.format(loanAmount));
                etLoanAmount.setSelection(etLoanAmount.getText().length());
                etLoanAmount.addTextChangedListener(this);

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };

       //Monthly watcher
        final TextWatcher txtWatcherMonthlyAmount = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            //Monthly
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etMonthlyPayment.getText().length() == 0 ||
                        Integer.parseInt(etMonthlyPayment.getText().toString().replaceAll(",","")) == 0){return;}
                //There is a new loan amount
                if(monthlyPayment != Integer.parseInt(etMonthlyPayment.getText().toString().replaceAll(",",""))){

                    monthlyPayment =  Integer.parseInt(etMonthlyPayment.getText().toString().replaceAll(",",""));
                    calculateLoanAmount();
                }

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
        etLoanAmount.setText(String.valueOf(loanAmount));
        super.onResume();
    }

    private void calculateLoanAmount() {
        float interestRateFloat = (float) (interestRate/100000.0/12);
        int numberOfMonths = numberOfYears *12 *-1;
        loanAmount = (int) (monthlyPayment * (1- ((Math.pow(interestRateFloat +1,numberOfMonths))))/interestRateFloat);
        etLoanAmount.setText(String.valueOf(loanAmount));
    }

    private void calculateMonthlyPayment() {
        float interestRateFloat = (float) (interestRate/100000.0/12);
        int numberOfMonths = numberOfYears *12 *-1;
        monthlyPayment =(int) round((interestRateFloat *loanAmount) / (1- ((Math.pow(interestRateFloat +1,numberOfMonths)))));
        etMonthlyPayment.setText(String.valueOf(monthlyPayment));
    }


    //This will set to ints of the global.
    private void calculatePayment() {

        if(loanAmount == 0 ){
            calculateLoanAmount();
        }else{
            // Assume recalculate monthly payment.
            calculateMonthlyPayment();
        }

    }

    public void setInterest(int interest) {
        this.interestRate = interest;
        calculatePayment();
    }
}

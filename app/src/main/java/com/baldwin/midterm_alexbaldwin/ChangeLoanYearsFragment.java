package com.baldwin.midterm_alexbaldwin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeLoanYearsFragment extends DialogFragment implements View.OnClickListener {

    ChangeLoanYearsCallBack mCallBack;
    View root;

    public ChangeLoanYearsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate t
        // he layout for this fragment
        root = inflater.inflate(R.layout.fragment_change_loan_years, container, false);;
        return root;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn1Years:
                mCallBack.ChangeLoanYears(1);
                break;
            case R.id.btn2Years:
                mCallBack.ChangeLoanYears(2);
                break;
            case R.id.btn3Years:
                mCallBack.ChangeLoanYears(3);
                break;
            case R.id.btn5Years:
                mCallBack.ChangeLoanYears(5);
                break;
            case R.id.btn7Years:
                mCallBack.ChangeLoanYears(7);
                break;
            case R.id.btn10Years:
                mCallBack.ChangeLoanYears(10);
                break;
            case R.id.btn15Years:
                mCallBack.ChangeLoanYears(15);
                break;
            case R.id.btn20Years:
                mCallBack.ChangeLoanYears(20);
                break;
            case R.id.btn25Years:
                mCallBack.ChangeLoanYears(25);
                break;
            case R.id.btn30Years:
                mCallBack.ChangeLoanYears(30);
                break;
        }
        this.dismiss();
    }

    public interface ChangeLoanYearsCallBack {
        void ChangeLoanYears(int newYears);
    }

    @Override
    public void onResume() {
        super.onResume();

        root.findViewById(R.id.btn1Years).setOnClickListener( this);
        root.findViewById(R.id.btn2Years).setOnClickListener( this);
        root.findViewById(R.id.btn3Years).setOnClickListener( this);
        root.findViewById(R.id.btn5Years).setOnClickListener( this);
        root.findViewById(R.id.btn7Years).setOnClickListener( this);
        root.findViewById(R.id.btn10Years).setOnClickListener( this);
        root.findViewById(R.id.btn15Years).setOnClickListener( this);
        root.findViewById(R.id.btn20Years).setOnClickListener( this);
        root.findViewById(R.id.btn25Years).setOnClickListener( this);
        root.findViewById(R.id.btn30Years).setOnClickListener( this);






    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ChangeLoanYearsFragment.ChangeLoanYearsCallBack) {
            mCallBack = (ChangeLoanYearsFragment.ChangeLoanYearsCallBack) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement ChangeResults.SendDialog");
        }
    }
}

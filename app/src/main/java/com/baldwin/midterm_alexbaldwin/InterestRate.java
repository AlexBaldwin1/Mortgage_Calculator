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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class InterestRate extends Fragment {


    SeekBar sbInterestRate;
    EditText etInterestRate;
    View root;
    InterestRateChange mCallBack;
    int interestRate;
    DecimalFormat formatter;

    SeekBar.OnSeekBarChangeListener sbChangeListener;

    public InterestRate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_interest_rate, container, false);
        // Inflate the layout for this fragment
        return root;
    }

    public void reset() {
        interestRate = 3500;
        updateTaxRateEditText();
        updateProgressBar();

    }


    interface InterestRateChange{
        void InterestRateChanged(int interest);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof InterestRateChange) {
            mCallBack = (InterestRateChange) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement InterestRateChange.InterestRateChanged");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor spEdit = sp.edit();
        //spEdit.putInt("InterestRate", interestRate);

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        interestRate = (int)sp.getInt("InterestRate1", 3500);

       formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
       formatter.applyPattern("##.###");

        sbInterestRate = root.findViewById(R.id.sbInterestRate);
        etInterestRate = root.findViewById(R.id.etInterestRate);

       updateTaxRateEditText();
        sbInterestRate.setMax(25000);

        //TODO change this to do it programatically
       //sbInterestRate.setMin(1);
        sbInterestRate.setProgress(interestRate);

        sbChangeListener = new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               etInterestRate.requestFocus();
               interestRate = sbInterestRate.getProgress();
               updateTaxRateEditText();
           }
           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {}
           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {}
       };

        sbInterestRate.setOnSeekBarChangeListener(sbChangeListener);


        etInterestRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(! etInterestRate.getText().toString().equals(".")){
                    if(etInterestRate.getText().length()>0){
                        BigDecimal bd = new BigDecimal(etInterestRate.getText().toString());
                        if(bd.compareTo(new BigDecimal(25))>0){
                            Log.d("InterestRate", "too large");
                            Toast toast = new Toast(getContext());
                            toast.makeText(getContext(),"Interest rate is too big find a better bank", Toast.LENGTH_SHORT ).show();

                            etInterestRate.removeTextChangedListener(this);
                            etInterestRate.setText("25");
                            etInterestRate.setSelection(etInterestRate.getText().length());
                            etInterestRate.addTextChangedListener(this);
                        }

                        if(bd.compareTo(new BigDecimal(0))<0){
                            Log.d("InterestRate", "too large");
                            Toast toast = new Toast(getContext());
                            toast.makeText(getContext(),"Zero percent interest rates do not exist!", Toast.LENGTH_SHORT ).show();

                            etInterestRate.removeTextChangedListener(this);
                            etInterestRate.setText("0");
                            etInterestRate.setSelection(etInterestRate.getText().length());
                            etInterestRate.addTextChangedListener(this);
                        }
                        updateProgressBar();
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void updateProgressBar() {
        interestRate = Integer.parseInt((new BigDecimal(etInterestRate.getText().toString())).multiply(new BigDecimal(1000)).toString().split("\\.")[0]);
        sbInterestRate.setOnSeekBarChangeListener(null);
        sbInterestRate.setProgress(interestRate);
        sbInterestRate.setOnSeekBarChangeListener(sbChangeListener);
        if (interestRate == 0) interestRate = 1;
        mCallBack.InterestRateChanged((interestRate));
    }

    private void updateTaxRateEditText() {
        etInterestRate.setText(formatter.format(interestRate/1000.0));
        mCallBack.InterestRateChanged(interestRate);
    }
}

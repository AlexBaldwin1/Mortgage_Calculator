package com.baldwin.midterm_alexbaldwin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity
    implements InterestRate.InterestRateChange
    ,ChangeLoanYearsFragment.ChangeLoanYearsCallBack
    , LoanInfo.ChangeLoanYearsListener {

    FragmentManager fm;
    InterestRate fmInterestRate = new InterestRate();
    LoanInfo fmLoanInfo = new LoanInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.topFrame, fmLoanInfo, "LoanInfo")
                .replace(R.id.bottomFrame, fmInterestRate, "InterestRate")
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*fmLoanInfo =(LoanInfo) fm.findFragmentByTag("LoanInfo");
        fmInterestRate = (InterestRate)fm.findFragmentByTag("InterestRate");*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        fmInterestRate.reset();
        fmLoanInfo.reset();
        super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void InterestRateChanged(int interest) {
        fmLoanInfo.setInterest(interest);
    }

    @Override
    public void ChangeLoanYears(int newYears) {
        fmLoanInfo.setLoanYears(newYears);
    }

    @Override
    public void ChangeLoanYears() {
        ChangeLoanYearsFragment changeLoanYearsFragment = new ChangeLoanYearsFragment();
        changeLoanYearsFragment.show(fm, "ChangeLoanYears");
    }
}

package com.x.wallet.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.x.wallet.R;
import com.x.wallet.ui.activity.CurrencyActivity;
import com.x.wallet.ui.activity.ServicePolicyActivity;
import com.x.wallet.ui.view.ChangeCurrencyPrefView;
import com.x.wallet.ui.view.PrivacyPolicyView;

/**
 * Created by wuliang on 18-3-13.
 */

public class SettingsFragment extends Fragment{

    private static final int CHOOSE_CURRENCY_REQUEST_CODE = 1;
    private ChangeCurrencyPrefView mChangeCurrencyPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        View manageAccountItem = view.findViewById(R.id.manage_account_rl);
        manageAccountItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.x.wallet.action.MANAGE_ALL_ACCOUNT_ACTION");
                startActivity(intent);
            }
        });

        View servicePolicyItem = view.findViewById(R.id.mark_rl);
        PrivacyPolicyView.setOnClickListener(getContext(), servicePolicyItem, ServicePolicyActivity.TYPE_SERVICE);

        View privacyPolicyItem = view.findViewById(R.id.about_us_rl);
        PrivacyPolicyView.setOnClickListener(getContext(), privacyPolicyItem, ServicePolicyActivity.TYPE_PRIVACY);

        mChangeCurrencyPref = view.findViewById(R.id.change_currency_pref);
        mChangeCurrencyPref.updateCurrentCurrencyText();
        mChangeCurrencyPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.x.wallet.action.CHOOSE_CURRENCY_ACTION");
                SettingsFragment.this.startActivityForResult(intent, CHOOSE_CURRENCY_REQUEST_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(CHOOSE_CURRENCY_REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK){
            mChangeCurrencyPref.updateCurrentCurrencyText(data.getStringExtra(CurrencyActivity.CHOOSE_CURRENCY));
        }
    }
}

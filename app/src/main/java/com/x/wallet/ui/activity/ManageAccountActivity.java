package com.x.wallet.ui.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.x.wallet.AppUtils;
import com.x.wallet.R;
import com.x.wallet.db.XWalletProvider;
import com.x.wallet.transaction.DeleteAccountAsycTask;
import com.x.wallet.transaction.key.DecryptKeyAsycTask;
import com.x.wallet.transaction.keystore.DecryptKeyStoreAsycTask;
import com.x.wallet.ui.data.AccountItem;
import com.x.wallet.ui.dialog.ContentShowDialogHelper;
import com.x.wallet.ui.dialog.PasswordCheckDialogHelper;

/**
 * Created by wuliang on 18-3-16.
 */

public class ManageAccountActivity extends WithBackAppCompatActivity {
    private AccountItem mAccountItem;
    private TextView mAccountNameTv;
    private TextView mAddressTv;

    private View mMnemonicView;
    private View mKeyView;
    private View mKeyStoreView;

    private View mDeleteView;
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_account_activity);
        mActivity = this;
        mAccountItem = (AccountItem) getIntent().getSerializableExtra(AppUtils.ACCOUNT_DATA);

        initViews();
    }

    private void initViews(){

        mAccountNameTv = findViewById(R.id.account_name_tv);
        mAccountNameTv.setText(mAccountItem.getAccountName());

        mAddressTv = findViewById(R.id.address_tv);
        mAddressTv.setText(mAccountItem.getAddress());

        mMnemonicView = findViewById(R.id.mnemonic_tv);
        mMnemonicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = ContentUris.withAppendedId(XWalletProvider.CONTENT_URI, mAccountItem.getId());
                Intent intent = new Intent("com.x.wallet.action.BACKUP_MNEMONIC_ACTION");
                intent.putExtra(AppUtils.ADDRESS_URI, uri);
                startActivity(intent);
            }
        });
        mKeyView = findViewById(R.id.key_tv);
        mKeyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasswordCheckDialogForKey();
            }
        });

        mKeyStoreView = findViewById(R.id.keystore_tv);
        mKeyStoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasswordCheckDialogForKeyStore();
            }
        });

        mDeleteView = findViewById(R.id.delete_account_tv);
        mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasswordCheckDialogForDelete();
            }
        });
    }

    /**
     * BtcCreateAddressHelper.readMnemonic(mAccountItem.getEncryMnemonic(), passwordEt.getText().toString()):
     BtcCreateAddressHelper.readPrivateKey(mAccountItem.getEncrySeed(), passwordEt.getText().toString());
     */

    private void showPasswordCheckDialogForKey(){
        final PasswordCheckDialogHelper mPasswordCheckDialogHelper = new PasswordCheckDialogHelper();

        mPasswordCheckDialogHelper.showPasswordDialog(this, new PasswordCheckDialogHelper.ConfirmBtnClickListener() {
            @Override
            public boolean onConfirmBtnClick(String password, Context context) {
                new DecryptKeyAsycTask(context, mAccountItem.getCoinType(), "", mAccountItem.getKeyStore(),
                        password, new DecryptKeyAsycTask.OnDecryptKeyFinishedListener() {
                    @Override
                    public void onDecryptKeyFinished(String key) {
                        if(!TextUtils.isEmpty(key)){
                            mPasswordCheckDialogHelper.dismissDialog();
                            ContentShowDialogHelper.showContentDialog(mActivity, R.string.backup_key, R.string.copy_key, key);
                        } else {
                            mPasswordCheckDialogHelper.updatePasswordCheckError();
                        }
                    }
                }).execute();
                return true;
            }
        }, R.string.confirm_password);
    }

    private void showPasswordCheckDialogForKeyStore(){
        final PasswordCheckDialogHelper mPasswordCheckDialogHelper = new PasswordCheckDialogHelper();

        mPasswordCheckDialogHelper.showPasswordDialog(this, new PasswordCheckDialogHelper.ConfirmBtnClickListener() {
            @Override
            public boolean onConfirmBtnClick(String password, Context context) {
                new DecryptKeyStoreAsycTask(context, mAccountItem.getCoinType(), mAccountItem.getKeyStore(),
                        password, new DecryptKeyStoreAsycTask.OnDecryptKeyStoreFinishedListener() {
                    @Override
                    public void onDecryptKeyStoreFinished(String keyStore) {
                        if(!TextUtils.isEmpty(keyStore)){
                            mPasswordCheckDialogHelper.dismissDialog();
                            ContentShowDialogHelper.showContentDialog(mActivity, R.string.backup_keystore, R.string.copy_keystore, keyStore);
                        } else {
                            mPasswordCheckDialogHelper.updatePasswordCheckError();
                        }
                    }
                }).execute();
                return true;
            }
        }, R.string.confirm_password);
    }

    private void showPasswordCheckDialogForDelete(){
        final PasswordCheckDialogHelper mPasswordCheckDialogHelper = new PasswordCheckDialogHelper();

        mPasswordCheckDialogHelper.showPasswordDialog(this, new PasswordCheckDialogHelper.ConfirmBtnClickListener() {
            @Override
            public boolean onConfirmBtnClick(String password, Context context) {
                new DecryptKeyStoreAsycTask(context, mAccountItem.getCoinType(), mAccountItem.getKeyStore(),
                        password, new DecryptKeyStoreAsycTask.OnDecryptKeyStoreFinishedListener() {
                    @Override
                    public void onDecryptKeyStoreFinished(String keyStore) {
                        if(!TextUtils.isEmpty(keyStore)){
                            mPasswordCheckDialogHelper.dismissDialog();
                            ContentShowDialogHelper.showConfirmDialog(mActivity, R.string.delete_account
                                    , mActivity.getResources().getString(R.string.confirm_delete_account, mAccountItem.getAccountName())
                                    , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    new DeleteAccountAsycTask(mActivity, mAccountItem.getId(), new DeleteAccountAsycTask.OnDeleteFinishedListener() {
                                        @Override
                                        public void onDeleteFinished(int count) {
                                            if(count > 0){
                                                Toast.makeText(mActivity, R.string.delete_account_success, Toast.LENGTH_LONG).show();
                                                mActivity.finish();
                                            } else {
                                                Toast.makeText(mActivity, R.string.delete_account_failed, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }).execute();
                                }
                            });
                        } else {
                            mPasswordCheckDialogHelper.updatePasswordCheckError();
                        }
                    }
                }).execute();
                return true;
            }
        }, R.string.confirm_password);
    }
}

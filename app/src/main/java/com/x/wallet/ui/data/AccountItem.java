package com.x.wallet.ui.data;

import android.content.ContentUris;
import android.database.Cursor;
import android.text.TextUtils;

import com.x.wallet.XWalletApplication;
import com.x.wallet.db.XWalletProvider;
import com.x.wallet.transaction.balance.ItemLoadedCallback;
import com.x.wallet.transaction.balance.ItemLoadedFuture;
import com.x.wallet.transaction.token.TokenLoaderManager;

import java.util.List;

/**
 * Created by wuliang on 18-3-16.
 */

public class AccountItem{
    private long mId;
    private String mAddress;
    private String mAccountName;
    private String mCoinName;
    private int mCoinType;
    private String mEncrySeed;
    private String mEncryMnemonic;
    private String mKeyStore;
    private String mBalance;
    private int mHasToken;
    private TokenLoadedCallback mTokenLoadedCallback;
    private ItemLoadedFuture mItemLoadedFuture;
    private List<TokenItem> mTokenItemList;

    public AccountItem(long id, String address, String accountName, String coinName,
                       int coinType, String encrySeed, String encryMnemonic, String keyStore,
                       String balance, int hasToken) {
        mId = id;
        mAddress = address;
        mAccountName = accountName;
        mCoinName = coinName;
        mCoinType = coinType;
        mEncrySeed = encrySeed;
        mEncryMnemonic = encryMnemonic;
        mKeyStore = keyStore;
        mBalance = balance;
        mHasToken = hasToken;

        if(isHasToken()){
            mItemLoadedFuture = XWalletApplication.getApplication().getTokenLoaderManager()
                    .getTokenList(ContentUris.withAppendedId(XWalletProvider.CONTENT_URI, mId),
                            new TokenLoadedAccountItemCallback());
        }
    }

    public long getId() {
        return mId;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public void setAccountName(String accountName) {
        mAccountName = accountName;
    }

    public String getCoinName() {
        return mCoinName;
    }

    public void setCoinName(String coinName) {
        mCoinName = coinName;
    }

    public int getCoinType() {
        return mCoinType;
    }

    public void setCoinType(int coinType) {
        mCoinType = coinType;
    }

    public String getEncryMnemonic() {
        return mEncryMnemonic;
    }

    public void setEncryMnemonic(String encryMnemonic) {
        mEncryMnemonic = encryMnemonic;
    }

    public String getEncrySeed() {
        return mEncrySeed;
    }

    public void setEncrySeed(String encrySeed) {
        mEncrySeed = encrySeed;
    }

    public String getKeyStore() {
        return mKeyStore;
    }

    public void setKeyStore(String keyStore) {
        mKeyStore = keyStore;
    }

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(String balance) {
        mBalance = balance;
    }

    public boolean isHasToken() {
        return mHasToken == 1;
    }

    public List<TokenItem> getTokenItemList() {
        return mTokenItemList;
    }

    public boolean hasMnemonic(){
        return !TextUtils.isEmpty(mEncryMnemonic);
    }

    public boolean hasKey(){
        return false;
    }

    public boolean hasKeyStore(){
        return !TextUtils.isEmpty(mKeyStore);
    }

    public void setTokenLoadedCallback(TokenLoadedCallback tokenLoadedCallback) {
        mTokenLoadedCallback = tokenLoadedCallback;
    }

    public static SerializableAccountItem translateToSerializable(AccountItem accountItem) {
        return new SerializableAccountItem(accountItem.getId(),
                                        accountItem.getAddress(),
                                        accountItem.getAccountName(),
                                        accountItem.getCoinName(),
                                        accountItem.getCoinType(),
                                        accountItem.getEncrySeed(),
                                        accountItem.getEncryMnemonic(),
                                        accountItem.getKeyStore(),
                                        accountItem.getBalance());
    }

    public interface TokenLoadedCallback {
        void onTokenLoaded(AccountItem accountItem);
    }

    public class TokenLoadedAccountItemCallback implements ItemLoadedCallback {
        public void onItemLoaded(Object result, Throwable exception) {
            mTokenItemList = ((TokenLoaderManager.TokenLoaded)result).mTokenList;
            if (mTokenLoadedCallback != null) {
                mTokenLoadedCallback.onTokenLoaded(AccountItem.this);
            }
        }
    }

    public static AccountItem createFromCursor(Cursor cursor){
        return new AccountItem(cursor.getLong(COLUMN_ID),
                cursor.getString(COLUMN_ACCOUNT_ADDRESS),
                cursor.getString(COLUMN_ACCOUNT_NAME),
                cursor.getString(COLUMN_COIN_NAME),
                cursor.getInt(COLUMN_COIN_TYPE),
                cursor.getString(COLUMN_COIN_SEED),
                cursor.getString(COLUMN_COIN_MNEMONIC),
                cursor.getString(COLUMN_KEYSTORE),
                cursor.getString(COLUMN_BALANCE),
                cursor.getInt(COLUMN_HAS_TOKEN));
    }

    @Override
    public String toString() {
        return "AccountItem{" +
                "mAddress='" + mAddress + '\'' +
                ", mAccountName='" + mAccountName + '\'' +
                ", mCoinName='" + mCoinName + '\'' +
                ", mCoinType=" + mCoinType +
                ", mEncryMnemonic='" + mEncryMnemonic + '\'' +
                ", mEncrySeed='" + mEncrySeed + '\'' +
                ", mBalance='" + mBalance + '\'' +
                '}';
    }

    static final int COLUMN_ID                      = 0;
    static final int COLUMN_ACCOUNT_ADDRESS         = 1;
    static final int COLUMN_ACCOUNT_NAME            = 2;
    static final int COLUMN_COIN_NAME               = 3;
    static final int COLUMN_COIN_TYPE               = 4;
    static final int COLUMN_COIN_SEED               = 5;
    static final int COLUMN_COIN_MNEMONIC           = 6;
    static final int COLUMN_RPIV_KEY                = 7;
    static final int COLUMN_KEYSTORE                = 8;
    static final int COLUMN_BALANCE                 = 9;
    static final int COLUMN_HAS_TOKEN               = 10;
}

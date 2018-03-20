package com.xwallet.ethwallet.api;

import com.xwallet.ethwallet.data.EthAccountData;

import net.bither.bitherj.crypto.mnemonic.MnemonicException;

import java.security.SecureRandom;

/**
 * Created by zhangxing on 18-3-16.
 */

public class EthAccountHelper {
    public static EthAccountData create(String password){
        try {
            return EthAccountCreateHelper.createAccount(new SecureRandom(), password);

        }catch (MnemonicException.MnemonicLengthException e){

        }
        return null;
    }
}

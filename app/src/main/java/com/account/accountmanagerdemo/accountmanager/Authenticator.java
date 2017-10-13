package com.account.accountmanagerdemo.accountmanager;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.account.accountmanagerdemo.ConstantsGlobal;

import java.util.Random;

/**
 * date：2017/9/18 on 15:20
 * description: AbstractAccountAuthenticator的实现类用户账户添加，登陆接口认证操作
 */

public class Authenticator extends AbstractAccountAuthenticator {
    private Context ctx;

    public Authenticator(Context context) {
        super(context);
        ctx = context;
    }

    /**
     * 返回一个Bundle，其中包含可用于编辑属性的活动的Intent,一般返回null
     */
    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        return null;
    }

    /**
     * 添加指定的accountType的帐户。
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String s, String s1,
                             String[] strings, Bundle bundle) throws NetworkErrorException {
        /**
         *  这里是跳转到新的页面让用户添加账户,还可以直接添加账户
         */
        Intent intent = new Intent(ctx, RegisterActivty.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);
        Bundle b = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return b;
    }

    /**
     * 检查用户是否知道帐户的凭据。连接服务器进行身份校验
     */
    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    /**
     * 获得一个账户authtoken.
     */
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle options) throws NetworkErrorException {
        //可以请求服务器获取token,这里为了简单直接返回
        Bundle bundle;
        if (!s.equals(ConstantsGlobal.AUTH_TOKEN_TYPE)) {
            // 通过blockingGetAuthToken方法传来的Constants.AUTHTOKEN_TYPE进行比较
            bundle = new Bundle();
            bundle.putInt(AccountManager.KEY_ERROR_CODE, 1);
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authToken");
            return bundle;
        }

        AccountManager am = AccountManager.get(ctx);
        String psw = am.getPassword(account);
        if (!TextUtils.isEmpty(psw)) {
            //根据服务器接口根据账户密码获取authToken
            // String authToken = NetworkUtilities.authenticate(account.name, psw);

            //这里为了测试使用随机数
            Random random = new Random();
            bundle = new Bundle();
            String authToken = random.nextLong() + "";

            //如果已经到服务器验证过账号并保存到AccountManager中，并且返回
            if (!TextUtils.isEmpty(authToken)) {
                Bundle result = new Bundle();
                //result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                //不返回name和type会报错“the type and name should not be empty”
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                return bundle;
            }
        }

            //如果没有到服务器验证过账号并保存到AccountManager中，则重新倒添加账号页面中验证。
            bundle = new Bundle();
            Intent intent = new Intent(ctx, AuthenticatorActivity.class);
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);
            return bundle;
    }

        /**
         * 向认证者询问给定的authTokenType的本地化标签。
         */
        @Override
        public String getAuthTokenLabel (String s){
            return null;
        }

        /**
         * 更新帐户的本地存储的凭据。
         */
        @Override
        public Bundle updateCredentials (AccountAuthenticatorResponse
        accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws
        NetworkErrorException {
            return null;
        }

        /**
         * 检查帐户是否支持所有指定的验证器特定功能。
         */
        @Override
        public Bundle hasFeatures (AccountAuthenticatorResponse
        accountAuthenticatorResponse, Account account, String[]strings) throws NetworkErrorException {
            return null;
        }
    }

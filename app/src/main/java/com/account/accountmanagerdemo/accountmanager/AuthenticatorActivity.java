package com.account.accountmanagerdemo.accountmanager;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.account.accountmanagerdemo.ConstantsGlobal;
import com.account.accountmanagerdemo.R;

import static com.account.accountmanagerdemo.R.id.login;

/**
 * date：2017/9/18 on 13:37
 * 本demo实现账户注册，账户登陆验证，退出登陆，自动更新，手动更新，批量更新用户名，获取指定账号，获取所有账号信息
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity implements View.OnClickListener {

    private AccountManager accountManager;
    private TextView content;
    private EditText account;
    private EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myauthenticator);
        account = (EditText) findViewById(R.id.account);
        pwd = (EditText) findViewById(R.id.pwd);
        Button btnLogin = (Button) findViewById(R.id.login);
        Button btnRegist = (Button) findViewById(R.id.register);
        Button getAccountAll = (Button) findViewById(R.id.getAccountAll);
        Button refreshAccount = (Button) findViewById(R.id.refresh);
        content = (TextView) findViewById(R.id.content);
        btnLogin.setOnClickListener(this);
        btnRegist.setOnClickListener(this);
        getAccountAll.setOnClickListener(this);
        refreshAccount.setOnClickListener(this);

        //初始化AccountManager
        accountManager = AccountManager.get(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case login:
                login();
                break;
            case R.id.register:
                Intent intent = new Intent(this, RegisterActivty.class);
                startActivity(intent);
                break;
            case R.id.getAccountAll:
                getAccountAll();
                break;
            case R.id.refresh:
                refreshAccount();
                break;
            default:
                break;
        }
    }

    /**
     * 手动刷新，强烈建议使用自动刷新账号以及SyncAdapter中其他刷新方式，这里举例手动刷新
     */
    private void refreshAccount() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(CreateSyncAccount(), ConstantsGlobal.AUTHORITY, bundle);
    }

    /**
     * 登陆
     */
    private void login() {
        String mAccount = account.getText().toString().trim();
        String mPwd = pwd.getText().toString().trim();
        if (TextUtils.isEmpty(mAccount) || TextUtils.isEmpty(mPwd)) {
            Toast.makeText(AuthenticatorActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //请求接口，从服务器获取数据token
        Account account = new Account(mAccount, ConstantsGlobal.ACCOUNT_TYPE);
        AccountManagerCallback<Bundle> callback = new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    //获取token
                    String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                    Intent intent = new Intent(AuthenticatorActivity.this, MainActivity.class);
                    Account account1 = new Account(future.getResult().getString(AccountManager.KEY_ACCOUNT_NAME),
                            future.getResult().getString(AccountManager.KEY_ACCOUNT_TYPE));
                    intent.putExtra(ConstantsGlobal.KEY_ACCOUNT, account1);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        accountManager.getAuthToken(account, ConstantsGlobal.AUTH_TOKEN_TYPE, null, AuthenticatorActivity.this, callback, null);
    }

    /**
     * 获取所有的账户信息:注意添加权限
     */
    private void getAccountAll() {
        Account[] accounts = accountManager.getAccounts();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < accounts.length; i++) {
            String acc = accounts[i].toString();
            builder.append(i + ". " + acc + "\n");
        }
        content.setText("获取的的账户信息：" + "\n" + builder.toString());
    }

    /**
     * 设置虚拟账户，用于自动更新时候调用
     *
     * @param context
     * @return
     */
    private static final String oldAccountName = "中国中央电视台";//此数据一般是账号变更或者服务器返回新账号

    public Account CreateSyncAccount() {
        // 创建账户类型和默认账户名称
        Account newAccount = new Account(oldAccountName, ConstantsGlobal.ACCOUNT_TYPE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
           Log.e("AuthenticatorActivity", "账户更新成功");
            Toast.makeText(this,"更新成功，请重新点击查看账户列表",Toast.LENGTH_SHORT).show();
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            Log.e("AuthenticatorActivity", "账户更新失败，或者此账户已经存在");
            Toast.makeText(this,"更新成功，数据没有变化，请查看列表",Toast.LENGTH_SHORT).show();
        }
        return newAccount;
    }
}

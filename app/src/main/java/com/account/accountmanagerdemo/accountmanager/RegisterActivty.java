package com.account.accountmanagerdemo.accountmanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.account.accountmanagerdemo.ConstantsGlobal;
import com.account.accountmanagerdemo.R;

/**
 * date：2017/9/18 on 16:13
 * 注册界面
 */
public class RegisterActivty extends AppCompatActivity implements View.OnClickListener {

    private EditText account;
    private EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activty);
        account = (EditText) findViewById(R.id.account);
        pwd = (EditText) findViewById(R.id.pwd);
        Button btnOK = (Button) findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                //在这里调用接口访问服务器进行注册。这里为了方便，不进行网络操作
                String mAccount = account.getText().toString().trim();
                String mPwd = pwd.getText().toString().trim();
                if (TextUtils.isEmpty(mAccount) || TextUtils.isEmpty(mPwd)) {
                    Toast.makeText(RegisterActivty.this, "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果注册成功，则执行以下代码，否则重新注册，这里默认注册成功
                Account account = new Account(mAccount, ConstantsGlobal.ACCOUNT_TYPE);
                AccountManager am = AccountManager.get(RegisterActivty.this);
                am.addAccountExplicitly(account, mPwd, null);
                finish();
                break;
        }
    }
}

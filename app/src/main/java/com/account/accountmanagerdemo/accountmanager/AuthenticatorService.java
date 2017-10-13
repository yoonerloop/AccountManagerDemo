package com.account.accountmanagerdemo.accountmanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * date：2017/9/18 on 15:37
 * description: 用户账户信息的添加服务，.为Authenticator创建Service
 * 注意：必须指定清单文件配置信息，
 */

public class AuthenticatorService extends Service {

    private static Authenticator myAuthenticator = null;

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (AuthenticatorService.class) {
            if (myAuthenticator == null) {
                myAuthenticator = new Authenticator(getApplicationContext());
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Authenticator(getApplicationContext()).getIBinder();
    }
}

package com.account.accountmanagerdemo.accountrefresh;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * date：2017/9/19 on 11:24
 * description: 提供更新的服务,需要在清单文件中配置一下信息：
 * action android:name="android.content.SyncAdapter"
 * 通过这个binder，系统底层就可以调用到SyncAdapter里面的onPerformSync()方法。
 */

public class SyncService extends Service {

    private static SyncAdapter syncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //实例化的过程要保证线程安全，以免同步框架会将多次同步响应添加到队列中。
        synchronized (SyncService.class) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}

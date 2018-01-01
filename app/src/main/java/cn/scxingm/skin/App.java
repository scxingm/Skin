package cn.scxingm.skin;

import android.app.Application;

import cn.scxingm.skinutils.SkinManager;

/**
 * Created by scxingm on 2018/1/1.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this, "cn.scxingm.skin_plugin");
    }
}

package cn.scxingm.skinutils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.scxingm.skinutils.config.Constant;

/**
 * Created by scxingm on 2018/1/1.
 */

public class ConfigUtils {

    private Context context;

    public ConfigUtils(Context context) {
        this.context = context;
    }

    public void saveSkinPath(String path){
        SharedPreferences sp = context.getSharedPreferences(Constant.CONFIG_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(Constant.KEY_SKIN_PATH, path).apply();
    }

    public void saveSkinPkg(String pkg){
        SharedPreferences sp = context.getSharedPreferences(Constant.CONFIG_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(Constant.KEY_SKIN_PKG, pkg).apply();
    }

    public void saveSuffix(String suffix){
        SharedPreferences sp = context.getSharedPreferences(Constant.CONFIG_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(Constant.KEY_SKIN_SUFFIX, suffix).apply();
    }

    public String getSuffix(){
        SharedPreferences sp = context.getSharedPreferences(Constant.CONFIG_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.KEY_SKIN_SUFFIX, "");
    }

    public String getSkinFile(){
        SharedPreferences sp = context.getSharedPreferences(Constant.CONFIG_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.KEY_SKIN_PATH, "");
    }

    public String getSkinPkg(){
        SharedPreferences sp = context.getSharedPreferences(Constant.CONFIG_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.KEY_SKIN_PKG, "");
    }

    // 清理配置
    public void clear() {
        SharedPreferences sp = context.getSharedPreferences(Constant.CONFIG_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}

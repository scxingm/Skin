package cn.scxingm.skinutils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.scxingm.skinutils.attr.SkinAttr;
import cn.scxingm.skinutils.attr.SkinAttrSupport;
import cn.scxingm.skinutils.attr.SkinView;
import cn.scxingm.skinutils.callback.SkinChangeCallback;
import cn.scxingm.skinutils.callback.SkinChangedListener;

/**
 * Created by scxingm on 2018/1/1.
 */

public class SkinManager {

    private Context context;
    private static SkinManager sInstance;
    private String pkgName;
    private String skinDir;

    private List<SkinChangedListener> listeners = new ArrayList<>();

    private Map<SkinChangedListener, List<SkinView>> skinViewMaps = new HashMap<>();

    private ResourcesManager resourcesManager;

    private ConfigUtils configUtils;

    private String currentFile;
    private String currentPkg;
    private String suffix;

    private SkinManager() {

    }

    public static SkinManager getInstance(){
        if (sInstance == null){
            synchronized (SkinManager.class){
                if (sInstance == null){
                    sInstance = new SkinManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context, String pkgName){
        this.context = context.getApplicationContext();
        this.pkgName = pkgName;
        this.skinDir = context.getObbDir().getAbsolutePath()+File.separator;

        configUtils = new ConfigUtils(this.context);

        // TODO ctrl + alt + T
        try {
            String skinFile = configUtils.getSkinFile();
            String skinPkg = configUtils.getSkinPkg();
            suffix = configUtils.getSuffix();

            File file = new File(skinDir+skinFile);
            if (file.exists()){
                loadSkin(skinFile, skinPkg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            configUtils.clear();
        }
    }

    public ResourcesManager getResourcesManager() {
        if (!useSkin()){
            return new ResourcesManager(context.getResources(), context.getPackageName(), suffix);
        }
        return resourcesManager;
    }

    private void loadSkin(String skinFile, String skinPkg) {
        try {
            if (skinFile.equals(currentFile) && skinPkg.equals(currentPkg)) return;

            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, skinDir+skinFile);//当前对象, 路径
            Resources superRes = context.getResources();
            Resources resources = new Resources(
                    assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());

            resourcesManager = new ResourcesManager(resources, skinPkg, null);

            currentFile = skinFile;
            currentPkg = skinPkg;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SkinView> getSkinViews(SkinChangedListener listener){
        return skinViewMaps.get(listener);
    }

    public void addSkinView(SkinChangedListener listener, List<SkinView> skinViews){
        skinViewMaps.put(listener, skinViews);
    }

    public void register(AppCompatActivity activity){
        listeners.add((SkinChangedListener) activity);

        initCreate(activity);
    }

    private static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    private static final Map<String, Constructor<? extends View>> sConstructorMap
            = new android.support.v4.util.ArrayMap<>();
    private final Object[] mConstructorArgs = new Object[2];

    private Method createViewMethod = null;
    static final Class<?>[] createViewSignature = new Class[]{
            View.class, String.class, Context.class, AttributeSet.class
    };
    private final Object[] createViewArgs = new Object[4];
    private void initCreate(final AppCompatActivity activity) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // xxxxxCompat 类是为解决版本问题存在
//        LayoutInflaterCompat.setFactory(inflater, new LayoutInflaterFactory() {
//            @Override
//            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
//                Log.i(TAG, "onCreateView: name: "+name);
//                for (int i = 0, n = attrs.getAttributeCount(); i < n; i ++){
//                    String attrName = attrs.getAttributeName(i);
//                    String attrVals = attrs.getAttributeValue(i);
//                    Log.i(TAG, "onCreateView: attrName: "+attrName+", attrVals: "+attrVals);
//                }
//                if ("TextView".equals(name)){
//                    return new EditText(context, attrs);
//                }
//                return null;
//            }
//        });
        LayoutInflaterCompat.setFactory(inflater, new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                //
                AppCompatDelegate delegate = activity.getDelegate();
                View view = null;
                List<SkinAttr> skinAttrs = null;
                try {
                    if (createViewMethod == null){
                        createViewMethod = delegate.getClass()
                                .getMethod("createView", createViewSignature);
                    }
                    createViewArgs[0] = parent;
                    createViewArgs[1] = name;
                    createViewArgs[2] = context;
                    createViewArgs[3] = attrs;
                    view = (View) createViewMethod.invoke(delegate, createViewArgs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                skinAttrs = SkinAttrSupport.getSkinAttrs(attrs, context);
                if (skinAttrs.isEmpty()) return null;// 没有view需要换肤

                if(view == null){
                    view = createViewFromTag(context, name, attrs);
                }
                if (view != null){
                    injectSkin(view, skinAttrs, activity);
                }

                return view;
            }
        });
    }

    private void injectSkin(View view, List<SkinAttr> skinAttrs, AppCompatActivity activity) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews((SkinChangedListener) activity);
        if (skinViews == null){
            skinViews = new ArrayList<>();
            SkinManager.getInstance().addSkinView((SkinChangedListener) activity, skinViews);
        }
        skinViews.add(new SkinView(view, skinAttrs));

        if (SkinManager.getInstance().needChangeSkin()){
            SkinManager.getInstance().skinChanged((SkinChangedListener) activity);
        }
    }

    private View createViewFromTag(Context context, String name, AttributeSet attrs){
        if (name.equals("view")){
            name = attrs.getAttributeValue(null, "class");
        }
        try{
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (name.indexOf(".") == -1){
                return createView(context, name, "android.widget.");
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            return null;
        } finally {
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createView(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }

    public void unregister(SkinChangedListener listener){
        listeners.remove(listener);
        skinViewMaps.remove(listener);
    }

    // 后缀
    public void changeSkin(String suffix){
        clearSkinInfo();

        this.suffix = suffix;
        configUtils.saveSuffix(this.suffix);

        notifyChangedListener();
    }

    // 清除配置
    private void clearSkinInfo() {
        currentFile = null;
        currentPkg = null;
        suffix = null;

        configUtils.clear();
    }

    public void changeSkin(final String fileName, SkinChangeCallback callback) {
        if (callback == null) {
            callback = SkinChangeCallback.DEFAULT_CALLBACK ;
        }
        final SkinChangeCallback finalCallback = callback;

        finalCallback.onStart();
        new AsyncTask<Void, Void, Integer>(){
            @Override
            protected Integer doInBackground(Void... params) {
                try{
                    loadSkin(fileName, pkgName);
                }catch (Exception e){
                    e.printStackTrace();
                    return -1;
                }
                return 0;
            }
            @Override
            protected void onPostExecute(Integer code) {
                if (code == -1){
                    finalCallback.onError(null);
                    return ;
                }
                try{
                    notifyChangedListener();
                    finalCallback.onSucces();

                    updateSkinInfo(fileName, pkgName);
                }catch (Exception e){
                    e.printStackTrace();
                    finalCallback.onError(e);
                }
            }
        }.execute();
    }

    // 存储信息
    private void updateSkinInfo(String skinPath, String skinPkg) {
        configUtils.saveSkinPath(skinPath);
        configUtils.saveSkinPkg(skinPkg);
    }

    private void notifyChangedListener() {
        for (SkinChangedListener listener : listeners){
            skinChanged(listener);
            listener.onSkinChanged();
        }
    }

    public void skinChanged(SkinChangedListener listener) {
        List<SkinView> skinViews = skinViewMaps.get(listener);
        for (SkinView skinView : skinViews){
            skinView.apply();
        }
    }

    public boolean needChangeSkin() {
        return useSkin() || useSuffix();
    }

    public boolean useSkin(){
        return currentFile != null && !currentFile.trim().equals("");
    }

    public boolean useSuffix(){
        return suffix != null && !suffix.trim().equals("");
    }
}

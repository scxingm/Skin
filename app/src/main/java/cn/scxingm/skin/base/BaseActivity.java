package cn.scxingm.skin.base;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.scxingm.skinutils.SkinManager;
import cn.scxingm.skinutils.attr.SkinAttr;
import cn.scxingm.skinutils.attr.SkinAttrSupport;
import cn.scxingm.skinutils.attr.SkinView;
import cn.scxingm.skinutils.callback.SkinChangedListener;

public class BaseActivity extends AppCompatActivity implements SkinChangedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SkinManager.getInstance().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        SkinManager.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onSkinChanged() {
        Toast.makeText(this, "皮肤切换成功了!", Toast.LENGTH_SHORT).show();
    }

    //    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
//        if (name.equals("view")) {
//            name = attrs.getAttributeValue(null, "class");
//        }
//
//        try {
//            mConstructorArgs[0] = context;
//            mConstructorArgs[1] = attrs;
//
//            if (-1 == name.indexOf('.')) {
//                for (int i = 0; i < sClassPrefixList.length; i++) {
//                    final View view = createView(context, name, sClassPrefixList[i]);
//                    if (view != null) {
//                        return view;
//                    }
//                }
//                return null;
//            } else {
//                return createView(context, name, null);
//            }
//        } catch (Exception e) {
//            // We do not want to catch these, lets return null and let the actual LayoutInflater
//            // try
//            return null;
//        } finally {
//            // Don't retain references on context.
//            mConstructorArgs[0] = null;
//            mConstructorArgs[1] = null;
//        }
//    }
}

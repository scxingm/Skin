package cn.scxingm.skin;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;

import cn.scxingm.skinutils.SkinManager;
import cn.scxingm.skinutils.callback.SkinChangeCallback;

/**
 * Created by scxingm on 2018/1/1.
 */

public class MenuFrament extends Fragment implements View.OnClickListener {

    private View redView;
    private View greenView;
    private View appSkin;
    private View defaultSkin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        redView = view.findViewById(R.id.menu_item_01);
        greenView = view.findViewById(R.id.menu_item_02);
        appSkin = view.findViewById(R.id.menu_item_03);
        defaultSkin = view.findViewById(R.id.menu_item_04);

        redView.setOnClickListener(this);
        greenView.setOnClickListener(this);
        appSkin.setOnClickListener(this);
        defaultSkin.setOnClickListener(this);
    }

    private String skinFile = "skin_plugin.apk";
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_item_01:
                SkinManager.getInstance().changeSkin("red");
                break;
            case R.id.menu_item_02:
                SkinManager.getInstance().changeSkin("green");
                break;
            case R.id.menu_item_03:
                SkinManager.getInstance().changeSkin(skinFile, null);
                break;
            case R.id.menu_item_04:
                SkinManager.getInstance().changeSkin("");
                break;
        }
    }
}

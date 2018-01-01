package cn.scxingm.skin;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import java.io.File;

import cn.scxingm.skin.base.BaseActivity;
import cn.scxingm.skinutils.SkinManager;
import cn.scxingm.skinutils.callback.SkinChangeCallback;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        initEvent();
    }

    private DrawerLayout drawerLayout;
    private ListView listView;
    FragmentManager fragmentManager;
    Fragment fragment;
    private void initView(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.frameLayout);
        if (fragment == null){
            fragmentManager.beginTransaction().add(R.id.frameLayout, new MenuFrament()).commit();
        }

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this, -1, datas){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null){
                    convertView = LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.item_list, parent, false);
                }

                TextView textView = (TextView) convertView.findViewById(R.id.item_list_text);
                textView.setText(getItem(position));
                return convertView;
            }
        });
    }

    private void initEvent() {
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View view = drawerLayout.getChildAt(0);
                View menu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT")){
                    float leftScale = 1 - 0.3f * scale;

//                    menu.setScaleX();

                    ViewHelper.setScaleX(menu, leftScale);
                    ViewHelper.setScaleY(menu, leftScale);
                    ViewHelper.setAlpha(menu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(
                            view,
                            menu.getMeasuredWidth() * (1 - scale));
                    view.invalidate();
                    ViewHelper.setScaleX(view, rightScale);
                    ViewHelper.setScaleY(view, rightScale);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private String[] datas = null;
    private void initData(){
        datas = new String[]{
                "我是第一", "我是第二", "我是第三", "我是第四", "我是第五", "我是第六", "我是第七",
                "我是第一", "我是第二", "我是第三", "我是第四", "我是第五", "我是第六", "我是第七"
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
        }
        return super.onOptionsItemSelected(item);
    }


}

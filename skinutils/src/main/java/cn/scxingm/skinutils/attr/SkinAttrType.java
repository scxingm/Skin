package cn.scxingm.skinutils.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.scxingm.skinutils.ResourcesManager;
import cn.scxingm.skinutils.SkinManager;

/**
 * Created by scxingm on 2018/1/1.
 */

public enum SkinAttrType {

    BACKGROUND("background"){
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourcesManager().getDrawableByResName(resName);
            if (drawable != null){
                view.setBackground(drawable);
            }
        }
    },SRC("src") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourcesManager().getDrawableByResName(resName);
            if (view instanceof ImageView){
                ImageView imageView = (ImageView) view;
                if (drawable != null){
                    imageView.setImageDrawable(drawable);
                }
            }
        }
    },TEXT_COLOR("textColor") {
        @Override
        public void apply(View view, String resName) {

            ColorStateList colorList = getResourcesManager().getColorByResName(resName);
            if (view instanceof TextView){
                TextView textView = (TextView) view;
                if (colorList != null){
                    textView.setTextColor(colorList);
                }
            }
        }
    };

    String resType;

    public String getResType() {
        return resType;
    }

    SkinAttrType(String resType) {
        this.resType = resType;
    }

    public abstract void apply(View view, String resName);

    public ResourcesManager getResourcesManager(){
        return SkinManager.getInstance().getResourcesManager();
    }
}

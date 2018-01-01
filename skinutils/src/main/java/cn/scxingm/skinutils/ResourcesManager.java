package cn.scxingm.skinutils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * Created by scxingm on 2018/1/1.
 */

public class ResourcesManager {

    private Resources resources;
    private String pkgName;
    private String suffix;

    public ResourcesManager(Resources resources, String pkgName, String suffix) {
        this.resources = resources;
        this.pkgName = pkgName;
        if (suffix == null){
            suffix = "";
        }
        this.suffix = suffix;
    }

    public Drawable getDrawableByResName(String pkgName){
        pkgName = appendSuffix(pkgName);
        try{
            return resources.getDrawable(resources.getIdentifier(pkgName, "drawable", this.pkgName));
        }catch (Exception e){
//            e.printStackTrace();
            return null;
        }
    }

    public ColorStateList getColorByResName(String colorName){
        colorName = appendSuffix(colorName);
        try{
            return resources.getColorStateList(resources.getIdentifier(colorName, "color", pkgName));
        }catch (Exception e){
//            e.printStackTrace();
            return null;
        }
    }

    private String appendSuffix(String pkgName) {
        if (!TextUtils.isEmpty(this.suffix)){
            pkgName += "_" + this.suffix;
        }
        return pkgName;
    }
}

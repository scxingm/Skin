package cn.scxingm.skinutils.attr;

import android.view.View;

/**
 * Created by scxingm on 2018/1/1.
 */

public class SkinAttr {

    private String resName;
    private SkinAttrType type;

    public SkinAttr(String resName, SkinAttrType type) {
        this.resName = resName;
        this.type = type;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public SkinAttrType getType() {
        return type;
    }

    public void setType(SkinAttrType type) {
        this.type = type;
    }

    public void apply(View view){
        type.apply(view, resName);
    }

}

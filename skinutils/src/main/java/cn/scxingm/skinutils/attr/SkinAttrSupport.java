package cn.scxingm.skinutils.attr;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import cn.scxingm.skinutils.config.Constant;

/**
 * 辅助类
 * Created by scxingm on 2018/1/1.
 */

public class SkinAttrSupport {

    public static List<SkinAttr> getSkinAttrs(AttributeSet attrs, Context context){
        SkinAttrType type = null;
        SkinAttr skinAttr = null;
        List<SkinAttr> skinAttrs = new ArrayList<>();
        for (int i = 0, n = attrs.getAttributeCount(); i < n; i ++){
            String attrName = attrs.getAttributeName(i);
            String attrVal = attrs.getAttributeValue(i);

            if (attrVal.startsWith("@")){ // 引用资源
                int id = -1;
                try{
                    id = Integer.parseInt(attrVal.substring(1)); // 拿到id
                }catch (Exception e){}
                if (id == -1) continue;

                String resName = context.getResources().getResourceEntryName(id); //

                if(resName.startsWith(Constant.SKIN_PREFIX)){ // 是skin_开头
                    // TODO 需要换肤
                    type = getSupportAttrType(attrName);
                    if (type == null) continue;

                    skinAttr = new SkinAttr(resName, type);
                    skinAttrs.add(skinAttr);
                }
            }
        }
        return skinAttrs;
    }

    private static SkinAttrType getSupportAttrType(String attrName) {
        for (SkinAttrType type : SkinAttrType.values()){
            if (type.getResType().equals(attrName)){
                return type;
            }
        }
        return null;
    }

}

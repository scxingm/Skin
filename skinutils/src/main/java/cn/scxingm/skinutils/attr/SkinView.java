package cn.scxingm.skinutils.attr;

import android.view.View;

import java.util.List;

/**
 * Created by scxingm on 2018/1/1.
 */

public class SkinView {

    private View view;
    private List<SkinAttr> attrs;

    public SkinView() {
    }

    public SkinView(View view, List<SkinAttr> attrs) {
        // TODO check参数是否合法
        // TODO initArgemntException
        this.view = view;
        this.attrs = attrs;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<SkinAttr> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<SkinAttr> attrs) {
        this.attrs = attrs;
    }

    public void apply(){
        for (SkinAttr attr : attrs){
            attr.apply(view);
        }
    }
}

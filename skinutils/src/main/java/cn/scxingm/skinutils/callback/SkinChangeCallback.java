package cn.scxingm.skinutils.callback;

/**
 * Created by scxingm on 2018/1/1.
 */

public interface SkinChangeCallback {
    void onStart();
    void onError(Exception e);
    void onSucces();

    public static DefaultCallback DEFAULT_CALLBACK = new DefaultCallback();

    public class DefaultCallback implements SkinChangeCallback {

        @Override
        public void onStart() {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSucces() {

        }
    }
}

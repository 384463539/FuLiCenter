package ucai.cn.day_filicenter;

import android.app.Application;

import ucai.cn.day_filicenter.bean.UserAvatar;
import ucai.cn.day_filicenter.utils.L;


/**
 * Created by Administrator on 2016/10/17.
 */
public class FuLiCenterApplication extends Application {
    public static FuLiCenterApplication application;
    private static FuLiCenterApplication instance;
    private static UserAvatar userAvatar;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        application = this;
    }

    public static FuLiCenterApplication getInstance() {
        if (instance == null) {
            instance = new FuLiCenterApplication();
        }
        return instance;
    }

    public static void setUser(UserAvatar user) {
        userAvatar = user;
        L.i("lication"+ user);
    }

    public static UserAvatar getUser() {
        return userAvatar;
    }
}

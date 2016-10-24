package ucai.cn.day_filicenter.dao;

import android.content.Context;

import ucai.cn.day_filicenter.bean.UserAvatar;

/**
 * Created by Administrator on 2016/10/24.
 */
public class UserDao {
    public static final String USER_TABLE_NAME = "t_superwechat_user";
    public static final String USER_COLUMN_NAME = "m_user_name";
    public static final String USER_COLUMN_NICK = "m_user_nick";
    public static final String USER_COLUMN_AVATAR_ID = "m_user_avatar_id";
    public static final String USER_COLUMN_AVATAR_PATH = "m_user_avatar_path";
    public static final String USER_COLUMN_AVATAR_SUFFIX = "m_user_avatar_suffix";
    public static final String USER_COLUMN_AVATAR_TYPE = "m_user_avatar_type";
    public static final String USER_COLUMN_AVATAR_LASTUPDATE_TIME = "m_user_avatar_LASTUPDATE_TIME";

    public UserDao(Context context) {
        DBManager.getInstance().onInit(context);
    }

    public boolean savaUser(UserAvatar user) {
        return DBManager.getInstance().savaUser(user);
    }

    public boolean updataUser(UserAvatar user) {
        return DBManager.getInstance().updataUser(user);
    }

    public UserAvatar getUser(String username) {
        return DBManager.getInstance().getUser(username);
    }
}

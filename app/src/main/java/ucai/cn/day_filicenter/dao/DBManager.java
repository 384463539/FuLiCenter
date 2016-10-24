package ucai.cn.day_filicenter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ucai.cn.day_filicenter.bean.UserAvatar;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBManager {
    public static DBManager dbManager = new DBManager();
    DBOpenHelper dbOpenHelper;

    void onInit(Context context) {
        dbOpenHelper = DBOpenHelper.getOpenHelper(context);
    }

    public static synchronized DBManager getInstance() {
        return dbManager;
    }

    public synchronized boolean savaUser(UserAvatar userAvatar) {
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME, userAvatar.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK, userAvatar.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID, userAvatar.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE, userAvatar.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH, userAvatar.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX, userAvatar.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME, userAvatar.getMavatarLastUpdateTime());
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long insert = db.insert(UserDao.USER_TABLE_NAME, null, values);
        return insert > 0;
    }

    public synchronized boolean updataUser(UserAvatar userAvatar) {
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME, userAvatar.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK, userAvatar.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID, userAvatar.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE, userAvatar.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH, userAvatar.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX, userAvatar.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME, userAvatar.getMavatarLastUpdateTime());
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int update = db.update(UserDao.USER_TABLE_NAME, values, UserDao.USER_COLUMN_NAME + " =?", new String[]{userAvatar.getMuserName()});
        return update > 0;
    }

    public synchronized UserAvatar getUser(String username) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        UserAvatar userAvatar = new UserAvatar();
        Cursor query = db.query(UserDao.USER_TABLE_NAME, null, UserDao.USER_COLUMN_NAME + " =?", new String[]{username}, null, null, null);
        while (query.moveToNext()) {
            int id = query.getInt(query.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID));
            String name = query.getString(query.getColumnIndex(UserDao.USER_COLUMN_NAME));
            String nick = query.getString(query.getColumnIndex(UserDao.USER_COLUMN_NICK));
            int type = query.getInt(query.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE));
            String path = query.getString(query.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH));
            String suffix = query.getString(query.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX));
            String time = query.getString(query.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME));
            userAvatar.setMavatarId(id);
            userAvatar.setMavatarLastUpdateTime(time);
            userAvatar.setMavatarPath(path);
            userAvatar.setMavatarSuffix(suffix);
            userAvatar.setMavatarType(type);
            userAvatar.setMuserName(name);
            userAvatar.setMuserNick(nick);
        }
        return userAvatar;
    }

    public synchronized void closeDB() {
        if (dbOpenHelper != null) {
            dbOpenHelper.closeDB();
        }
    }
}

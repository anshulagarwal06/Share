package com.sharesmile.share.v2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.sharesmile.share.v2.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property EmailId = new Property(2, String.class, "emailId", false, "EMAIL_ID");
        public final static Property Birthday = new Property(3, String.class, "birthday", false, "BIRTHDAY");
        public final static Property MobileNO = new Property(4, String.class, "mobileNO", false, "MOBILE_NO");
        public final static Property Gender = new Property(5, String.class, "gender", false, "GENDER");
        public final static Property ProfileImageUrl = new Property(6, String.class, "profileImageUrl", false, "PROFILE_IMAGE_URL");
    };


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"EMAIL_ID\" TEXT NOT NULL ," + // 2: emailId
                "\"BIRTHDAY\" TEXT," + // 3: birthday
                "\"MOBILE_NO\" TEXT," + // 4: mobileNO
                "\"GENDER\" TEXT," + // 5: gender
                "\"PROFILE_IMAGE_URL\" TEXT);"); // 6: profileImageUrl
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        stmt.bindString(3, entity.getEmailId());
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(4, birthday);
        }
 
        String mobileNO = entity.getMobileNO();
        if (mobileNO != null) {
            stmt.bindString(5, mobileNO);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(6, gender);
        }
 
        String profileImageUrl = entity.getProfileImageUrl();
        if (profileImageUrl != null) {
            stmt.bindString(7, profileImageUrl);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.getString(offset + 2), // emailId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // birthday
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // mobileNO
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // gender
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // profileImageUrl
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setEmailId(cursor.getString(offset + 2));
        entity.setBirthday(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMobileNO(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setGender(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setProfileImageUrl(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}

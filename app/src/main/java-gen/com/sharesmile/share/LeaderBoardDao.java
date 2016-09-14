package com.sharesmile.share;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.sharesmile.share.LeaderBoard;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LEADER_BOARD".
*/
public class LeaderBoardDao extends AbstractDao<LeaderBoard, Long> {

    public static final String TABLENAME = "LEADER_BOARD";

    /**
     * Properties of entity LeaderBoard.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property First_name = new Property(1, String.class, "first_name", false, "FIRST_NAME");
        public final static Property Last_name = new Property(2, String.class, "last_name", false, "LAST_NAME");
        public final static Property Social_thumb = new Property(3, String.class, "social_thumb", false, "SOCIAL_THUMB");
        public final static Property Last_week_distance = new Property(4, Float.class, "last_week_distance", false, "LAST_WEEK_DISTANCE");
    };


    public LeaderBoardDao(DaoConfig config) {
        super(config);
    }
    
    public LeaderBoardDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LEADER_BOARD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"FIRST_NAME\" TEXT," + // 1: first_name
                "\"LAST_NAME\" TEXT," + // 2: last_name
                "\"SOCIAL_THUMB\" TEXT," + // 3: social_thumb
                "\"LAST_WEEK_DISTANCE\" REAL);"); // 4: last_week_distance
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LEADER_BOARD\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, LeaderBoard entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String first_name = entity.getFirst_name();
        if (first_name != null) {
            stmt.bindString(2, first_name);
        }
 
        String last_name = entity.getLast_name();
        if (last_name != null) {
            stmt.bindString(3, last_name);
        }
 
        String social_thumb = entity.getSocial_thumb();
        if (social_thumb != null) {
            stmt.bindString(4, social_thumb);
        }
 
        Float last_week_distance = entity.getLast_week_distance();
        if (last_week_distance != null) {
            stmt.bindDouble(5, last_week_distance);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public LeaderBoard readEntity(Cursor cursor, int offset) {
        LeaderBoard entity = new LeaderBoard( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // first_name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // last_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // social_thumb
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4) // last_week_distance
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, LeaderBoard entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFirst_name(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLast_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSocial_thumb(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLast_week_distance(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(LeaderBoard entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(LeaderBoard entity) {
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

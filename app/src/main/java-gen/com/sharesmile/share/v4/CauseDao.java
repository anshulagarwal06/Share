package com.sharesmile.share.v4;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.sharesmile.share.v4.Cause;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CAUSE".
*/
public class CauseDao extends AbstractDao<Cause, Long> {

    public static final String TABLENAME = "CAUSE";

    /**
     * Properties of entity Cause.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CauseTitle = new Property(1, String.class, "causeTitle", false, "CAUSE_TITLE");
        public final static Property CauseDescription = new Property(2, String.class, "causeDescription", false, "CAUSE_DESCRIPTION");
        public final static Property ConversionRate = new Property(3, Float.class, "conversionRate", false, "CONVERSION_RATE");
        public final static Property MinDistance = new Property(4, Integer.class, "minDistance", false, "MIN_DISTANCE");
        public final static Property CauseCategory = new Property(5, String.class, "causeCategory", false, "CAUSE_CATEGORY");
        public final static Property CauseBrief = new Property(6, String.class, "causeBrief", false, "CAUSE_BRIEF");
        public final static Property CauseImage = new Property(7, String.class, "causeImage", false, "CAUSE_IMAGE");
        public final static Property CauseThankyouImage = new Property(8, String.class, "causeThankyouImage", false, "CAUSE_THANKYOU_IMAGE");
        public final static Property Share_template = new Property(9, String.class, "share_template", false, "SHARE_TEMPLATE");
        public final static Property IsActive = new Property(10, Boolean.class, "isActive", false, "IS_ACTIVE");
        public final static Property SponsorId = new Property(11, Integer.class, "sponsorId", false, "SPONSOR_ID");
        public final static Property SponsorCompany = new Property(12, String.class, "sponsorCompany", false, "SPONSOR_COMPANY");
        public final static Property SponsorNgo = new Property(13, String.class, "sponsorNgo", false, "SPONSOR_NGO");
        public final static Property SponsorLogo = new Property(14, String.class, "sponsorLogo", false, "SPONSOR_LOGO");
        public final static Property PartnerId = new Property(15, Integer.class, "partnerId", false, "PARTNER_ID");
        public final static Property PartnerCompany = new Property(16, String.class, "partnerCompany", false, "PARTNER_COMPANY");
        public final static Property PartnerNgo = new Property(17, String.class, "partnerNgo", false, "PARTNER_NGO");
        public final static Property PartnerType = new Property(18, String.class, "partnerType", false, "PARTNER_TYPE");
        public final static Property Order_priority = new Property(19, Integer.class, "order_priority", false, "ORDER_PRIORITY");
    };


    public CauseDao(DaoConfig config) {
        super(config);
    }
    
    public CauseDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CAUSE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"CAUSE_TITLE\" TEXT," + // 1: causeTitle
                "\"CAUSE_DESCRIPTION\" TEXT," + // 2: causeDescription
                "\"CONVERSION_RATE\" REAL," + // 3: conversionRate
                "\"MIN_DISTANCE\" INTEGER," + // 4: minDistance
                "\"CAUSE_CATEGORY\" TEXT," + // 5: causeCategory
                "\"CAUSE_BRIEF\" TEXT," + // 6: causeBrief
                "\"CAUSE_IMAGE\" TEXT," + // 7: causeImage
                "\"CAUSE_THANKYOU_IMAGE\" TEXT," + // 8: causeThankyouImage
                "\"SHARE_TEMPLATE\" TEXT," + // 9: share_template
                "\"IS_ACTIVE\" INTEGER," + // 10: isActive
                "\"SPONSOR_ID\" INTEGER," + // 11: sponsorId
                "\"SPONSOR_COMPANY\" TEXT," + // 12: sponsorCompany
                "\"SPONSOR_NGO\" TEXT," + // 13: sponsorNgo
                "\"SPONSOR_LOGO\" TEXT," + // 14: sponsorLogo
                "\"PARTNER_ID\" INTEGER," + // 15: partnerId
                "\"PARTNER_COMPANY\" TEXT," + // 16: partnerCompany
                "\"PARTNER_NGO\" TEXT," + // 17: partnerNgo
                "\"PARTNER_TYPE\" TEXT," + // 18: partnerType
                "\"ORDER_PRIORITY\" INTEGER);"); // 19: order_priority
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CAUSE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Cause entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String causeTitle = entity.getCauseTitle();
        if (causeTitle != null) {
            stmt.bindString(2, causeTitle);
        }
 
        String causeDescription = entity.getCauseDescription();
        if (causeDescription != null) {
            stmt.bindString(3, causeDescription);
        }
 
        Float conversionRate = entity.getConversionRate();
        if (conversionRate != null) {
            stmt.bindDouble(4, conversionRate);
        }
 
        Integer minDistance = entity.getMinDistance();
        if (minDistance != null) {
            stmt.bindLong(5, minDistance);
        }
 
        String causeCategory = entity.getCauseCategory();
        if (causeCategory != null) {
            stmt.bindString(6, causeCategory);
        }
 
        String causeBrief = entity.getCauseBrief();
        if (causeBrief != null) {
            stmt.bindString(7, causeBrief);
        }
 
        String causeImage = entity.getCauseImage();
        if (causeImage != null) {
            stmt.bindString(8, causeImage);
        }
 
        String causeThankyouImage = entity.getCauseThankyouImage();
        if (causeThankyouImage != null) {
            stmt.bindString(9, causeThankyouImage);
        }
 
        String share_template = entity.getShare_template();
        if (share_template != null) {
            stmt.bindString(10, share_template);
        }
 
        Boolean isActive = entity.getIsActive();
        if (isActive != null) {
            stmt.bindLong(11, isActive ? 1L: 0L);
        }
 
        Integer sponsorId = entity.getSponsorId();
        if (sponsorId != null) {
            stmt.bindLong(12, sponsorId);
        }
 
        String sponsorCompany = entity.getSponsorCompany();
        if (sponsorCompany != null) {
            stmt.bindString(13, sponsorCompany);
        }
 
        String sponsorNgo = entity.getSponsorNgo();
        if (sponsorNgo != null) {
            stmt.bindString(14, sponsorNgo);
        }
 
        String sponsorLogo = entity.getSponsorLogo();
        if (sponsorLogo != null) {
            stmt.bindString(15, sponsorLogo);
        }
 
        Integer partnerId = entity.getPartnerId();
        if (partnerId != null) {
            stmt.bindLong(16, partnerId);
        }
 
        String partnerCompany = entity.getPartnerCompany();
        if (partnerCompany != null) {
            stmt.bindString(17, partnerCompany);
        }
 
        String partnerNgo = entity.getPartnerNgo();
        if (partnerNgo != null) {
            stmt.bindString(18, partnerNgo);
        }
 
        String partnerType = entity.getPartnerType();
        if (partnerType != null) {
            stmt.bindString(19, partnerType);
        }
 
        Integer order_priority = entity.getOrder_priority();
        if (order_priority != null) {
            stmt.bindLong(20, order_priority);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Cause readEntity(Cursor cursor, int offset) {
        Cause entity = new Cause( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // causeTitle
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // causeDescription
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // conversionRate
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // minDistance
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // causeCategory
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // causeBrief
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // causeImage
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // causeThankyouImage
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // share_template
            cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0, // isActive
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // sponsorId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // sponsorCompany
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // sponsorNgo
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // sponsorLogo
            cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15), // partnerId
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // partnerCompany
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // partnerNgo
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // partnerType
            cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19) // order_priority
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Cause entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCauseTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCauseDescription(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setConversionRate(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setMinDistance(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setCauseCategory(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCauseBrief(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCauseImage(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCauseThankyouImage(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setShare_template(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setIsActive(cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0);
        entity.setSponsorId(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setSponsorCompany(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setSponsorNgo(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setSponsorLogo(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setPartnerId(cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15));
        entity.setPartnerCompany(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setPartnerNgo(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setPartnerType(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setOrder_priority(cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Cause entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Cause entity) {
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

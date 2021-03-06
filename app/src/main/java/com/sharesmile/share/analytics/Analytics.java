package com.sharesmile.share.analytics;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.sharesmile.share.MainApplication;
import com.sharesmile.share.User;
import com.sharesmile.share.UserDao;
import com.sharesmile.share.analytics.events.AnalyticsEvent;
import com.sharesmile.share.core.Constants;
import com.sharesmile.share.utils.Logger;
import com.sharesmile.share.utils.SharedPrefsManager;

import java.util.List;

/**
 * Created by ankitm on 11/04/16.
 */
public class Analytics {

    private static final String TAG = "Analytics";

    private static Analytics instance;
    private ClevertapManager clevertapManager;
    private Context context;

    private Analytics(Context ctx){
        clevertapManager = new ClevertapManager(ctx);
        context = ctx;
    }

    public static synchronized void initialize(Context appContext) {
        if (null == instance) {
            synchronized (Analytics.class) {
                if (null == instance) {
                    instance = new Analytics(appContext);
                }
            }
        }
    }

    public static Analytics getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "Analytics is not initialized, call initialize(applicationContext) " +
                            "static method first");
        }
        return instance;
    }

    /**
     Triggered when a client post an analytics event on EventBus
     */
    public void handleAnalyticsEvent(AnalyticsEvent event) {
        if (event == null) {
            //Someone is trolling, return silently
            return;
        }
        try{
            Logger.d(TAG, "handleAnalyticsEvent " + event.getEventName() + ":\n"
                    + event.toJsonString());
            clevertapManager.sendEvent(event);
        }catch (Exception e){
            String message = "Exception while handling " + event.getEventName();
            Logger.e(TAG, message, e);
            Crashlytics.log(message);
            Crashlytics.logException(e);
        }
    }

    public void setUserProperties(){
        UserDao mUserDao = MainApplication.getInstance().getDbWrapper().getDaoSession().getUserDao();
        int user_id = SharedPrefsManager.getInstance().getInt(Constants.PREF_USER_ID);
        List<User> userList = mUserDao.queryBuilder().where(UserDao.Properties.Id.eq(user_id)).list();
        if (userList != null && !userList.isEmpty()) {
            User mUser = userList.get(0);
            setUserName(mUser.getName());
            setUserId(mUser.getId().intValue());
            setUserEmail(mUser.getEmailId());
            setUserPhone(mUser.getMobileNO());
            setUserGender(mUser.getGender());
            setUserPhoto(mUser.getProfileImageUrl());
            setUserImpactLeagueTeamCode(SharedPrefsManager.getInstance().getInt(Constants.PREF_LEAGUE_TEAM_ID));
        }
    }

    public void setUserProperty(String propertyName, Object value){
        clevertapManager.setUserProperty(propertyName, value);
    }

    public void setUserName(String name){
        clevertapManager.setUserProperty("Name", name);
    }

    public void setUserEmail(String email){
        clevertapManager.setUserProperty("Email", email);
    }

    public void setUserId(int userId){
        clevertapManager.setUserProperty("Identity", userId);
    }

    /**
     * Sets 10 digit phone number without country code
     * @param phone
     */
    public void setUserPhone(String phone){
        clevertapManager.setUserProperty("Phone", phone);
    }

    /**
     * Sets gender, "M" or "F"
     * @param gender
     */
    public void setUserGender(String gender){
        clevertapManager.setUserProperty("Gender", gender);
    }

    public void setUserImpactLeagueTeamCode(int teamCode){
        clevertapManager.setUserProperty("team_code", teamCode);
    }

    public void setUserAge(int age){
        clevertapManager.setUserProperty("Age", age);
    }

    public void setUserPhoto(String pictureUrl){
        clevertapManager.setUserProperty("Photo", pictureUrl);
    }

}

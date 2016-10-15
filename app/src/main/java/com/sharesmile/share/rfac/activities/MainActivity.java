package com.sharesmile.share.rfac.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.sharesmile.share.BuildConfig;
import com.sharesmile.share.Events.DBEvent;
import com.sharesmile.share.MainApplication;
import com.sharesmile.share.R;
import com.sharesmile.share.core.BaseActivity;
import com.sharesmile.share.core.Constants;
import com.sharesmile.share.core.PermissionCallback;
import com.sharesmile.share.rfac.fragments.FeedbackFragment;
import com.sharesmile.share.rfac.fragments.LeaderBoardFragment;
import com.sharesmile.share.rfac.fragments.OnScreenFragment;
import com.sharesmile.share.rfac.fragments.ProfileFragment;
import com.sharesmile.share.rfac.fragments.SettingsFragment;
import com.sharesmile.share.rfac.fragments.WebViewFragment;
import com.sharesmile.share.sync.SyncHelper;
import com.sharesmile.share.utils.CustomTypefaceSpan;
import com.sharesmile.share.utils.Logger;
import com.sharesmile.share.utils.SharedPrefsManager;
import com.sharesmile.share.utils.Utils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import Models.CampaignList;
import butterknife.BindView;
import butterknife.ButterKnife;
import fragments.FaqFragment;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.FragmentInterface {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_LOGIN = 1001;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Toolbar toolbar;
    MixpanelAPI mixpanel;

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    private InputMethodManager inputMethodManager;
    private boolean isAppUpdateDialogShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);

        mixpanel = MixpanelAPI.getInstance(this, getString(R.string.mixpanel_project_token));

        try {
            JSONObject props = new JSONObject();
            props.put("Logged in", false);
            mixpanel.track("MainActivity - onCreate called", props);
        } catch (JSONException e) {
            Logger.e(TAG, "Unable to add properties to JSONObject", e);
        }

        ButterKnife.bind(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        if (savedInstanceState == null) {
            loadInitialFragment();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        updateNavigationMenu();
        checkAppVersion();
        SyncHelper.syncCampaignData(getApplicationContext());
    }

    private void checkAppVersion() {

        int latestAppVersion = SharedPrefsManager.getInstance().getInt(Constants.PREF_LATEST_APP_VERSION, 0);
        boolean forceUpdate = SharedPrefsManager.getInstance().getBoolean(Constants.PREF_FORCE_UPDATE, false);

        boolean showAppUpdateDialog = SharedPrefsManager.getInstance().getBoolean(Constants.PREF_SHOW_APP_UPDATE_DIALOG, false);
        String message = SharedPrefsManager.getInstance().getString(Constants.PREF_APP_UPDATE_MESSAGE);

        if (!showAppUpdateDialog || latestAppVersion <= BuildConfig.VERSION_CODE) {
            return;
        }

        isAppUpdateDialogShown = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(getString(R.string.title_app_update))
                .setMessage(message).
                        setPositiveButton(getString(R.string.update), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.redirectToPlayStore(MainActivity.this);
                            }
                        });

        if (forceUpdate) {
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            builder.setCancelable(false);
        } else {
            SharedPrefsManager.getInstance().setBoolean(Constants.PREF_SHOW_APP_UPDATE_DIALOG, false);
            builder.setNegativeButton(getString(R.string.later), null);
        }
        builder.show();
    }

    public void updateNavigationMenu() {
        Menu menu = mNavigationView.getMenu();
        MenuItem loginMenu = menu.findItem(R.id.nav_item_login);
        MenuItem profileMenu = menu.findItem(R.id.nav_item_profile);
        MenuItem leaderboardMenu = menu.findItem(R.id.nav_item_leaderboard);
        if (SharedPrefsManager.getInstance().getBoolean(Constants.PREF_IS_LOGIN)) {
            loginMenu.setVisible(false);
            profileMenu.setVisible(true);
            leaderboardMenu.setVisible(true);
        } else {
            loginMenu.setVisible(true);
            profileMenu.setVisible(false);
            leaderboardMenu.setVisible(false);
        }

        Menu m = mNavigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void loadInitialFragment() {
        addFragment(new OnScreenFragment(), false);
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.containerView;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public void performOperation(int operationId, Object input) {
        super.performOperation(operationId, input);
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void requestPermission(int requestCode, PermissionCallback permissionsCallback) {

    }

    @Override
    public void unregisterForPermissionRequest(int requestCode) {

    }

    @Override
    public void updateToolBar(String title, boolean showAsUpEnable) {
        mToolbarTitle.setText(title);
        showHomeAsUpEnable(showAsUpEnable);
    }

    public void showHomeAsUpEnable(boolean showUp) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (showUp) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            } else {
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                mDrawerLayout.addDrawerListener(mDrawerToggle);
            }
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }

        hideKeyboard(null);
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            ActivityCompat.finishAffinity(this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        Logger.d(TAG, "onNavigationItemSelected");

        if (menuItem.getItemId() == R.id.nav_item_profile) {
            replaceFragment(new ProfileFragment(), true);
        }

        if (menuItem.getItemId() == R.id.nav_item_aboutUs) {
            replaceFragment(WebViewFragment.getInstance(WebViewFragment.DISPLAY_ABOUT_US), true);
        }

        if (menuItem.getItemId() == R.id.nav_item_feedback) {
            Logger.d(TAG, "feedback clicked");
            replaceFragment(new FeedbackFragment(), true);
        }

        if (menuItem.getItemId() == R.id.nav_item_settings) {
            Logger.d(TAG, "settings clicked");
            replaceFragment(new SettingsFragment(), true);
        } else if (menuItem.getItemId() == R.id.nav_item_home) {
            showHome();
        } else if (menuItem.getItemId() == R.id.nav_item_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.BUNDLE_FROM_MAINACTIVITY, true);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
        } else if (menuItem.getItemId() == R.id.nav_item_faq) {
            replaceFragment(new FaqFragment(), true);
        } else if (menuItem.getItemId() == R.id.nav_item_share) {
            share();
        } else if (menuItem.getItemId() == R.id.nav_item_leaderboard) {
            replaceFragment(new LeaderBoardFragment(), true);
        }


        mDrawerLayout.closeDrawers();

        return false;
    }

    private void share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_msg));
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    public void showHome() {
        replaceFragment(new OnScreenFragment(), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN) {
            updateNavigationMenu();
        }
    }

    protected void hideKeyboard(View view) {
        if (inputMethodManager == null) {
            inputMethodManager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
        }


        if (view == null) {
            view = this.getCurrentFocus();
        }
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public void onDestroy() {
        mixpanel.flush();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DBEvent.CampaignDataUpdated campaignDataUpdated) {
        showPromoModal(campaignDataUpdated.getCampaign());
    }

    public void showPromoModal(final CampaignList.Campaign campaign) {

        if (campaign == null) {
            return;
        }
        //check for eligibility of campaign;
        boolean needToShowCampaign = true;

        Boolean isCampaignAlreadyShown = SharedPrefsManager.getInstance().getBoolean(Constants.PREF_CAMPAIGN_SHOWN_ONCE, false);
        if (!campaign.isAlways()) {
            needToShowCampaign = needToShowCampaign && !isCampaignAlreadyShown;
        }

        if (campaign.getShowOnSignUp()) {
            Boolean signUpUser = SharedPrefsManager.getInstance().getBoolean(Constants.PREF_IS_SIGN_UP_USER, false);
            needToShowCampaign = needToShowCampaign && signUpUser;
        }

        needToShowCampaign = needToShowCampaign && !isAppUpdateDialogShown;


        boolean isModelAlreadyShown = MainApplication.getInstance().isModelShown();
        needToShowCampaign = needToShowCampaign && !isModelAlreadyShown;
        if (!needToShowCampaign) {
            return;
        }
        MainApplication.getInstance().setModelShown();

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_promotion);
        Button share = (Button) dialog.findViewById(R.id.share);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView sponsors = (TextView) dialog.findViewById(R.id.sponser);
        TextView message = (TextView) dialog.findViewById(R.id.description);
        ImageView image = (ImageView) dialog.findViewById(R.id.image_run);

        Picasso.with(this).load(campaign.getImageUrl()).into(image);
        share.setText(campaign.getButtonText());
        title.setText(campaign.getTitle());
        message.setText(campaign.getDescritption());
        sponsors.setText("By " + campaign.getSponsor());
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.share(v.getContext(), null, campaign.getShareTemplate());
            }
        });

        SharedPrefsManager.getInstance().setBoolean(Constants.PREF_CAMPAIGN_SHOWN_ONCE, true);
        dialog.show();

    }

}

package com.sharesmile.share.core;

/**
 * Controller for fragments, it contains methods which invoked by fragments and implemented by Activity holding those fragments
 * Created by ankitmaheshwari1 on 11/01/16.
 */
public interface IFragmentController {

    String TAG = "IFragmentController";

    /*
        Operation codes
        Each operation code denotes a unique operation which is requested by fragment and performed by activity
     */

    int END_RUN_START_COUNTDOWN = 100;

    int START_RUN = 101;

    int START_RUN_TEST = 102;

    int SAY_THANK_YOU = 103;

    int START_MAIN_ACTIVITY = 104;

    int SHOW_MESSAGE_CENTER = 105;
    int SHOW_LEAGUE_ACTIVITY = 106;
    int SHOW_FAQ_FRAGMENT = 107;
    int SHOW_FEEDBACK_FRAGMENT = 108;


    /*
        Common methods which can be implemented either by abstract BaseActivity or by solid child activities
     */

    void addFragment(BaseFragment fragment, boolean addToBackStack);

    void replaceFragment(BaseFragment fragment, boolean addToBackStack);

    void loadInitialFragment();

    int getFrameLayoutId();

    String getName();

    void performOperation(int operationId, Object input);

    void exit();

    void requestPermission(int requestCode, PermissionCallback permissionsCallback);

    void unregisterForPermissionRequest(int requestCode);

    void updateToolBar(String title,boolean showAsUpEnable);


}

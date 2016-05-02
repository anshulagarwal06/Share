package com.sharesmile.share.core;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.sharesmile.share.TrackerActivity;
import com.sharesmile.share.rfac.RealRunFragment;
import com.sharesmile.share.rfac.activities.ThankYouActivity;
import com.sharesmile.share.utils.Logger;

/**
 * Created by ankitmaheshwari1 on 29/01/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements IFragmentController{

    private static final String TAG = "BaseActivity";

    @Override
    public void addFragment(BaseFragment fragmentToBeLoaded, boolean addToBackStack) {
        boolean allowStateLoss = true;

        if (!getSupportFragmentManager().isDestroyed()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction
                    .add(getFrameLayoutId(), fragmentToBeLoaded, fragmentToBeLoaded.getName());
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragmentToBeLoaded.getName());
            }
            if (allowStateLoss) {
                fragmentTransaction.commitAllowingStateLoss();
            } else {
                fragmentTransaction.commit();
            }
        } else {
            Logger.e(getName(), "addFragmen: Actvity Destroyed, won't perform FT to load" +
                    " Fragment " + fragmentToBeLoaded.getName());
        }
    }

    @Override
    public void replaceFragment(BaseFragment fragmentToBeLoaded, boolean addToBackStack) {
        boolean allowStateLoss = true;

        if (!getSupportFragmentManager().isDestroyed()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(getFrameLayoutId(), fragmentToBeLoaded,
                    fragmentToBeLoaded.getName());
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragmentToBeLoaded.getName());
            }
            if (allowStateLoss) {
                fragmentTransaction.commitAllowingStateLoss();
            } else {
                fragmentTransaction.commit();
            }
        } else {
            Logger.e(getName(), "replaceFragment: Actvity Destroyed, won't perform FT to load" +
                    " Fragment " + fragmentToBeLoaded.getName());
        }
    }

    @Override
    public void performOperation(int operationId, Object input) {
        switch (operationId){
            case START_RUN:
                if (input instanceof Boolean){
                    Intent intent = new Intent(this, TrackerActivity.class);
                    intent.putExtra(TrackerActivity.RUN_IN_TEST_MODE, (Boolean) input);
                    startActivity(intent);
                }else{
                    throw new IllegalArgumentException();
                }
                break;
        }
    }
    @Override
    public void setActionBarTitle(String title) {
       ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(title);
        }
    }
}

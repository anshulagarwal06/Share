package com.sharesmile.share.rfac;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharesmile.share.Events.UpdateUiOnMockLocation;
import com.sharesmile.share.Events.UpdateUiOnWorkoutPauseEvent;
import com.sharesmile.share.Events.UpdateUiOnWorkoutResumeEvent;
import com.sharesmile.share.Events.UsainBoltForceExit;
import com.sharesmile.share.TrackerActivity;
import com.sharesmile.share.analytics.events.AnalyticsEvent;
import com.sharesmile.share.analytics.events.Event;
import com.sharesmile.share.analytics.events.Properties;
import com.sharesmile.share.core.BaseFragment;
import com.sharesmile.share.core.Constants;
import com.sharesmile.share.gps.WorkoutSingleton;
import com.sharesmile.share.gps.models.WorkoutData;
import com.sharesmile.share.utils.Logger;
import com.sharesmile.share.utils.SharedPrefsManager;
import com.sharesmile.share.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

;


public abstract class RunFragment extends BaseFragment implements View.OnClickListener {

    private static final String PARAM_TITLE = "param_title";

    private static final String TAG = "RunFragment";
    public static final long TIMER_TICK = 1000; // in millis
    public static final long NOTIFICATION_TIMER_TICK = 60000; // in millis


    TrackerActivity myActivity;
    private View baseView;

    Handler handler = new Handler();

    private boolean isTimerRunning = false;

    public RunFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (TrackerActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
        baseView = inflater.inflate(getLayoutResId(), container, false);
        populateViews(baseView);
        EventBus.getDefault().register(this);
        return baseView;
    }

    protected abstract void populateViews(View baseView);

    protected abstract int getLayoutResId();

    public abstract void updateTimeView(String newTime);

    public abstract void onWorkoutResult(WorkoutData data);

    public void showUpdate(float speed, float distanceCovered, int elapsedTimeInSecs){
        Logger.d(TAG, "showUpdate: distanceCovered = " + distanceCovered
                + ", elapsedTimeInSecs " + elapsedTimeInSecs);
        if (!isTimerRunning){
            startTimer(elapsedTimeInSecs);
        }
    }

    public void showSteps(int stepsSoFar, int elapsedTimeInSecs ){
        if (!isTimerRunning){
            startTimer(elapsedTimeInSecs);
        }
    }

    private void startTimer(int initialSecs){
        Logger.d(TAG, "startTimer");
        handler.removeCallbacks(timer);
        handler.postDelayed(timer, TIMER_TICK);
        isTimerRunning = true;
        updateTimeView(Utils.secondsToHHMMSS(initialSecs));
    }

    protected abstract void onEndRun();

    protected abstract void onPauseRun();

    protected abstract void onResumeRun();

    protected abstract void onBeginRun();

    protected abstract void onContinuedRun(boolean isPaused);

    public abstract void showErrorMessage(String text);


    public void endRun(boolean userEnded) {
        Logger.d(TAG, "endRun, userEnded = " + userEnded);
        if (userEnded) {
            myActivity.endRun();
        }
        SharedPrefsManager.getInstance().removeKey(Constants.KEY_WORKOUT_TEST_MODE_ON);
        stopTimer();
        onEndRun();
    }


    public void pauseRun(boolean userPaused) {
        Logger.d(TAG, "pauseRun");
        stopTimer();
        if (userPaused) {
            myActivity.pauseWorkout();
        }
        onPauseRun();
    }

    public void resumeRun() {
        Logger.d(TAG, "resumeRun");
        if (myActivity.resumeWorkout()){
            startTimer((int) myActivity.getElapsedTimeInSecs());
            onResumeRun();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateUiOnWorkoutPauseEvent updateUiOnWorkoutPauseEvent) {
        Logger.d(TAG, "onEvent: UpdateUiOnWorkoutPauseEvent");

        onPauseRun();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateUiOnWorkoutResumeEvent updateUiOnWorkoutResumeEvent) {
        Logger.d(TAG, "onEvent: UpdateUiOnWorkoutResumeEvent");
        startTimer((int) myActivity.getElapsedTimeInSecs());
        onResumeRun();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateUiOnMockLocation updateUiOnMockLocation) {
        Logger.d(TAG, "onEvent: UpdateUiOnMockLocation");
        showForceExitDialogAfterStopRun("Mock location detected!", "Please disable mock location to proceed");
        AnalyticsEvent.create(Event.ON_LOAD_DISBALE_MOCK_LOCATION)
                .addBundle(getWorkoutBundle())
                .buildAndDispatch();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UsainBoltForceExit usainBoltForceExit) {
        Logger.d(TAG, "onEvent: UsainBoltForceExit");
        showForceExitDialogAfterStopRun("You are in a vehicle", "Stopping your run. Please start tracking while running/walking");
        AnalyticsEvent.create(Event.ON_LOAD_USAIN_BOLT_FORCE_EXIT)
                .addBundle(getWorkoutBundle())
                .buildAndDispatch();
    }

    public void stopTimer(){
        Logger.d(TAG, "stopTimer");
        handler.removeCallbacks(timer);
        isTimerRunning = false;
    }

    private void showForceExitDialogAfterStopRun(String title, String content) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(title);
        alertDialog.setMessage(content);
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isAttachedToActivity()) {
                    myActivity.exit();
                }
            }
        });
        alertDialog.show();
    }

    public boolean isRunActive() {
        synchronized (RunFragment.class) {
            return WorkoutSingleton.getInstance().isWorkoutActive();
        }
    }

    public void refreshWorkoutData(){
        if (isAttachedToActivity()){
            showUpdate(myActivity.getCurrentSpeed(), myActivity.getTotalDistanceInMeters(),
                    (int) myActivity.getElapsedTimeInSecs());
        }
    }

    public Properties getWorkoutBundle(){
        if (myActivity != null){
            return myActivity.getWorkoutBundle();
        }
        return null;
    }

    protected void continuedRun(){
        Logger.d(TAG, "continuedRun");
        int elapsedTImeInSecs = (int) WorkoutSingleton.getInstance().getDataStore().getElapsedTime();
        myActivity.continuedRun();
        if (!isRunning()){
            // If the run is in paused state then don't start timer
            updateTimeView(Utils.secondsToHHMMSS(elapsedTImeInSecs));
        }else {
            startTimer(elapsedTImeInSecs);
        }
        onContinuedRun(!isRunning());
    }


    protected void beginRun() {
        Logger.d(TAG, "beginRun");
        myActivity.beginRun();
        startTimer(0);
        onBeginRun();
    }

    public boolean isRunning() {
        return WorkoutSingleton.getInstance().isRunning();
    }

    @Override
    public void onDestroyView() {
        Logger.d(TAG, "onDestroyView");
        super.onDestroyView();
        stopTimer();
        EventBus.getDefault().unregister(this);
    }

    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            if (isAttachedToActivity()){
                int elapsedTimeInSecs = (int) myActivity.getElapsedTimeInSecs();
                updateTimeView(Utils.secondsToHHMMSS(elapsedTimeInSecs));
            }
            handler.postDelayed(this, TIMER_TICK);
        }
    };

    public abstract void showStopDialog();
}

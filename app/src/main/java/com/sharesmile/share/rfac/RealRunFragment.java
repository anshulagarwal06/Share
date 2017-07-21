package com.sharesmile.share.rfac;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.sharesmile.share.MainApplication;
import com.sharesmile.share.R;
import com.sharesmile.share.analytics.events.AnalyticsEvent;
import com.sharesmile.share.analytics.events.Event;
import com.sharesmile.share.gps.WorkoutSingleton;
import com.sharesmile.share.gps.models.Calorie;
import com.sharesmile.share.gps.models.WorkoutData;
import com.sharesmile.share.rfac.fragments.ShareFragment;
import com.sharesmile.share.rfac.models.CauseData;
import com.sharesmile.share.utils.Logger;
import com.sharesmile.share.utils.ShareImageLoader;
import com.sharesmile.share.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ankitm on 22/04/16.
 */
public class RealRunFragment extends RunFragment {

    private static final String TAG = "RealRunFragment";
    public static final String BUNDLE_CAUSE_DATA = "bundle_cause_data";

    TextView time;
    TextView distanceTextView;
    TextView impact;
    ProgressBar runProgressBar;
    Button pauseButton;
    Button stopButton;

    @BindView(R.id.img_sponsor_logo)
    ImageView mSponsorLogo;

    @BindView(R.id.timer_indicator)
    TextView mTimerIndicator;

    @BindView(R.id.tv_calories_progress)
    TextView tvCalorieMets;

    @BindView(R.id.live_calories_container)
    RelativeLayout caloriesContainer;

    @BindView(R.id.live_distance_container)
    RelativeLayout distanceContainer;

    @BindView(R.id.live_timer_container)
    View timerContainer;

    private CauseData mCauseData;


    public static RealRunFragment newInstance(CauseData causeData) {
        RealRunFragment fragment = new RealRunFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_CAUSE_DATA, causeData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        mCauseData = (CauseData) arg.getSerializable(BUNDLE_CAUSE_DATA);
    }

    @Override
    protected void populateViews(View baseView) {
        ButterKnife.bind(this, baseView);
        time = (TextView) baseView.findViewById(R.id.tv_run_progress_timer);
        distanceTextView = (TextView) baseView.findViewById(R.id.tv_run_progress_distance);
        impact = (TextView) baseView.findViewById(R.id.tv_run_progress_impact);
        runProgressBar = (ProgressBar) baseView.findViewById(R.id.run_progress_bar);
        pauseButton = (Button) baseView.findViewById(R.id.btn_pause);
        stopButton = (Button) baseView.findViewById(R.id.btn_stop);
        pauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        if (MainApplication.getInstance().getBodyWeight() <= 0){
            // Need to hide caloriesContainer and reset distanceContainer LayoutParams
            caloriesContainer.setVisibility(View.GONE);
            distanceContainer.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) distanceContainer.getLayoutParams();
            params.weight = 4;
            distanceContainer.setLayoutParams(params);
        }else {
            // Need to show caloriesContainer and set distanceContainer LayoutParams
            caloriesContainer.setVisibility(View.VISIBLE);
            distanceContainer.setGravity(Gravity.LEFT);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) distanceContainer.getLayoutParams();
            params.weight = 3;
            distanceContainer.setLayoutParams(params);
        }

        ShareImageLoader.getInstance().loadImage(mCauseData.getSponsor().getLogoUrl(), mSponsorLogo);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Will begin workout if not already active
        if (!myActivity.isWorkoutActive()) {
            beginRun();
        } else {
            continuedRun();
        }
        AnalyticsEvent.create(Event.ON_LOAD_TRACKER_SCREEN)
                .buildAndDispatch();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.run_progress;
    }

    @Override
    public void updateTimeView(String newTime) {
        time.setText(newTime);
        if (newTime.length() > 5) {
            mTimerIndicator.setText("HR:MIN:SEC");
        }
    }

    @Override
    public void onWorkoutResult(WorkoutData data) {
        //Workout completed and results obtained, time to show the next Fragment
        Logger.d(TAG, "onWorkoutResult");
        if (isAttachedToActivity()) {
            if (data.isMockLocationDetected()){
                // Do nothing, DisableMock blocking popup is on display on the screen
                return;
            }

            if (data.getUsainBoltCount() >= 3){
                // Do nothing, ConsecutiveUsainBoltsForceExit blocking popup is on display
                return;
            }

            if (WorkoutSingleton.getInstance().toShowFeedbackDialog()){
                // Need to show post run feedback dialog before
                showPostRunFeedbackDialog(data);
                // ShareScreen will be launched after taking feedback from user
                return;
            }
            exitFeedback(data);
        }
    }

    @Override
    protected void exitFeedback(WorkoutData data){
        Logger.d(TAG, "exit");
        String distanceString = Utils.formatToKmsWithTwoDecimal(data.getDistance());
        Float fDistance = Float.parseFloat(distanceString);
        if (mCauseData.getMinDistance() > (fDistance * 1000)
                && !data.isMockLocationDetected()) {
            myActivity.exit();
            stopTimer();
            return;
        }
        getFragmentController().replaceFragment(ShareFragment.newInstance(data, mCauseData), false);
    }

    @Override
    public void showUpdate(float speed, float distanceCoveredMeters, int elapsedTimeInSecs) {
        super.showUpdate(speed, distanceCoveredMeters, elapsedTimeInSecs);
        float distanceOnDisplay = 0f;
        try {
            distanceOnDisplay = 1000*Float.parseFloat(distanceTextView.getText().toString());
        }catch (NumberFormatException nfe){
            String message = "NumberFormatException while parsing distanceTextView on display: " + nfe.getMessage();
            Logger.e(TAG, message);
            Crashlytics.log(message);
            nfe.printStackTrace();
        }
        if (distanceCoveredMeters < distanceOnDisplay || distanceCoveredMeters - distanceOnDisplay >= 40){
            // Only when the delta is greater than 0.04 km we show the update
            String distanceString = Utils.formatToKmsWithTwoDecimal(distanceCoveredMeters);
            distanceTextView.setText(distanceString);
            int rupees = (int) Math.floor(getConversionFactor() * Float.parseFloat(distanceString));
            impact.setText(String.valueOf(rupees));
            if (WorkoutSingleton.getInstance().getDataStore() != null){
                Calorie calorie = WorkoutSingleton.getInstance().getDataStore().getCalories();
                if (calorie != null){
                    String caloriesString = "";
                    if (calorie.getCalories() > 100){
                        caloriesString = String.valueOf(Math.round(calorie.getCalories()));
                    }else {
                        caloriesString = Utils.formatWithOneDecimal(calorie.getCalories());
                    }
                    tvCalorieMets.setText(caloriesString);
                }
            }
        }
    }


    private String getImpactInRupees(float distanceCovered){
        String distanceString = Utils.formatToKmsWithTwoDecimal(distanceCovered);
        int rupees = (int) Math.floor(getConversionFactor() * Float.parseFloat(distanceString));
        return String.valueOf(rupees);
    }

    @Override
    public void showSteps(int stepsSoFar, int elapsedTimeInSecs) {
        super.showSteps(stepsSoFar, elapsedTimeInSecs);
    }

    @Override
    protected void onEndRun() {
        // Will wait for workout result broadcast
        Logger.d(TAG, "onEndRun");
    }

    @Override
    protected void onPauseRun() {
        pauseButton.setText(R.string.resume);
        runProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResumeRun() {
        pauseButton.setText(R.string.pause);
        runProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onBeginRun() {
        impact.setText("0");
        distanceTextView.setText("0.00");
        tvCalorieMets.setText("0.0");
    }

    @Override
    protected void onContinuedRun(boolean isPaused) {
        if (!isRunning()) {
            pauseButton.setText(R.string.resume);
            runProgressBar.setVisibility(View.INVISIBLE);
        } else {
            pauseButton.setText(R.string.pause);
            runProgressBar.setVisibility(View.VISIBLE);
        }

        float distanceCovered = WorkoutSingleton.getInstance().getDataStore().getDistanceCoveredSinceLastResume(); // in meters
        impact.setText(getImpactInRupees(distanceCovered));
        distanceTextView.setText(Utils.formatToKmsWithTwoDecimal(distanceCovered));
        if (WorkoutSingleton.getInstance().getDataStore() != null){
            Calorie calorie = WorkoutSingleton.getInstance().getDataStore().getCalories();
            if (calorie != null){
                String caloriesString = "";
                if (calorie.getCalories() > 100){
                    caloriesString = String.valueOf(Math.round(calorie.getCalories()));
                }else {
                    caloriesString = Utils.formatWithOneDecimal(calorie.getCalories());
                }
                tvCalorieMets.setText(caloriesString);
            }
        }

        if (WorkoutSingleton.getInstance().toShowEndRunDialog()){
            showRunEndDialog();
            WorkoutSingleton.getInstance().setToShowEndRunDialog(false);
        }
    }

    @Override
    public void showErrorMessage(String msg) {
        if (isAttachedToActivity()){
            showErrorDialog(msg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pause:
                if (isRunActive()) {
                    if (isRunning()) {
                        pauseRun(true);
                        AnalyticsEvent.create(Event.ON_CLICK_PAUSE_RUN)
                                .addBundle(getWorkoutBundle())
                                .buildAndDispatch();
                    } else {
                        resumeRun();
                        AnalyticsEvent.create(Event.ON_CLICK_RESUME_RUN)
                                .addBundle(getWorkoutBundle())
                                .buildAndDispatch();
                    }
                }
                break;

            case R.id.btn_stop:
                showStopDialog();
                AnalyticsEvent.create(Event.ON_CLICK_STOP_RUN)
                        .addBundle(getWorkoutBundle())
                        .buildAndDispatch();
                break;
        }
    }

    @Override
    public void showStopDialog() {
        String rDistance = distanceTextView.getText().toString();
        Float fDistance = Float.parseFloat(rDistance);
        if (mCauseData.getMinDistance() > (fDistance * 1000)) {
            showMinDistanceDialog();
        } else {
            showRunEndDialog();
        }
    }

    private void showRunEndDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Finish Run");
        alertDialog.setMessage("Are you sure you want to end the run?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (isAttachedToActivity()){
                    endRun(true);
                }
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        AnalyticsEvent.create(Event.ON_LOAD_FINISH_RUN_POPUP)
                .addBundle(getWorkoutBundle())
                .buildAndDispatch();
    }

    private void showMinDistanceDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.dialog_title_min_distance));
        alertDialog.setMessage(getString(R.string.dialog_msg_min_distance, mCauseData.getMinDistance()));
        alertDialog.setPositiveButton(getString(R.string.dialog_positive_button_min_distance), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.setNegativeButton(getString(R.string.dialog_negative_button_min_distance), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (isAttachedToActivity()) {
                    endRun(true);
                }
            }
        });
        alertDialog.show();
        AnalyticsEvent.create(Event.ON_LOAD_TOO_SHORT_POPUP)
                .addBundle(getWorkoutBundle())
                .buildAndDispatch();
    }

    /*  Rs per km*/
    public float getConversionFactor() {
        return mCauseData.getConversionRate();
    }

    private void showErrorDialog(String msg) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater(null).inflate(R.layout.alert_dialog_title, null);
        view.setBackgroundColor(getResources().getColor(R.color.neon_red));
        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(getString(R.string.error));
        alertDialog.setCustomTitle(view);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.resume), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (isAttachedToActivity()){
                    resumeRun();
                }

            }
        });
        alertDialog.setNegativeButton(getString(R.string.stop), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (isAttachedToActivity()){
                    endRun(true);
                }
            }
        });

        alertDialog.show();
    }

}

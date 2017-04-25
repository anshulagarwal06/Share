package com.sharesmile.share.rfac.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.RelativeLayout;

import com.sharesmile.share.R;
import com.sharesmile.share.core.Constants;
import com.sharesmile.share.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by apurvgandhwani on 4/9/2016.
 */
public class ThankYouActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String BUNDLE_THANKYOU_IMAGE_URL = "bundle_thanks_image_url";

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.thank_you_layout)
    RelativeLayout mThankYouLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thank_you);
        ButterKnife.bind(this);
        mThankYouLayout.setOnClickListener(this);
        loadThankYouImage();
    }

    private void loadThankYouImage() {

        Picasso.with(this).load(getIntent().getStringExtra(BUNDLE_THANKYOU_IMAGE_URL)).placeholder(R.drawable.placeholder_thankyou_image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mThankYouLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                mThankYouLayout.setBackgroundDrawable(placeHolderDrawable);
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ThankYouActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        boolean showFirstRunFeedBack = SharedPrefsManager.getInstance().getBoolean(Constants.PREF_FIRST_RUN_FEEDBACK, false);
        intent.putExtra(Constants.BUNDLE_FIRST_RUN_FEEDBACK, showFirstRunFeedBack);
        intent.putExtra(Constants.BUNDLE_SHOW_RUN_STATS, true);
        startActivity(intent);
    }
}

package com.sharesmile.share.rfac.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sharesmile.share.R;
import com.sharesmile.share.analytics.events.AnalyticsEvent;
import com.sharesmile.share.analytics.events.Event;
import com.sharesmile.share.core.BaseFragment;
import com.sharesmile.share.rfac.models.CauseData;
import com.sharesmile.share.views.MLTextView;
import com.sharesmile.share.views.MRTextView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by apurvgandhwani on 3/22/2016.
 */
public class CauseSwipeFragment extends BaseFragment implements View.OnClickListener {
    public static final String ARG_OBJECT = "object";

    private CauseData cause;

    @BindView(R.id.run_screen_sponsor)
    MLTextView mSponsor;

    @BindView(R.id.run_screen_title)
    MRTextView mTitle;
    @BindView(R.id.category)
    MRTextView mCategory;


    @BindView(R.id.run_screen_description)
    MLTextView mDescription;

    @BindView(R.id.img_run)
    ImageView mCauseImage;

    @BindView(R.id.card_view)
    CardView mCardView;

    public static Fragment getInstance(CauseData causeData) {

        Fragment fragment = new CauseSwipeFragment();
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_OBJECT, causeData);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        cause = (CauseData) arg.getSerializable(ARG_OBJECT);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.swipe_layout, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mDescription.setText(cause.getDetailText());
        mTitle.setText(cause.getTitle());
        mCategory.setText(cause.getCategory());
        if (cause.getExecutor() != null) {
            if(cause.getExecutor().getType().equalsIgnoreCase("ngo")){
                mSponsor.setText("with " + cause.getExecutor().getPartnerNgo());
            }else {
                mSponsor.setText("with " + cause.getExecutor().getPartnerCompany());
            }
        }

        mCardView.setOnClickListener(this);

        //load image
        Picasso.with(getContext()).load(cause.getImageUrl()).placeholder(R.drawable.cause_image_placeholder).into(mCauseImage);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_view:
                showCauseInfoFragment();
                AnalyticsEvent.create(Event.ON_CLICK_CAUSE_CARD)
                        .put("cause_title", cause.getTitle())
                        .put("cause_id", cause.getId())
                        .buildAndDispatch();
                break;
            default:
        }
    }

    private void showCauseInfoFragment() {
        getFragmentController().replaceFragment(CauseInfoFragment.getInstance(cause), true);
    }
}

package com.sharesmile.share.rfac.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sharesmile.share.Message;
import com.sharesmile.share.R;
import com.sharesmile.share.core.BaseFragment;
import com.sharesmile.share.utils.Utils;
import com.sharesmile.share.views.MLTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shine on 8/27/2016.
 */
public class MessageInfoFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "MessageInfoFragment";

    public static final String BUNDLE_MESSAGE_OBJECT = "bundle_cause_object";

    @BindView(R.id.run_screen_description)
    MLTextView mDescription;

    @BindView(R.id.image_run)
    ImageView mMessageImage;

    @BindView(R.id.share)
    Button mShareBtn;

    @BindView(R.id.run_screen_title)
    TextView mTitle;

    private Message message;


    public static MessageInfoFragment getInstance(Message message) {

        MessageInfoFragment fragment = new MessageInfoFragment();
        Bundle arg = new Bundle();
        arg.putString(BUNDLE_MESSAGE_OBJECT, new Gson().toJson(message));
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        message = new Gson().fromJson(arg.getString(BUNDLE_MESSAGE_OBJECT), Message.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_info, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }


    private void init() {
        mDescription.setText(message.getMessage_description());
        mTitle.setText(message.getMessage_title());
        mShareBtn.setOnClickListener(this);
        //load image
        Picasso.with(getContext()).load(message.getMessage_image()).placeholder(R.drawable.cause_image_placeholder).into(mMessageImage);
        updateActionbar();
    }

    private void updateActionbar() {
        getFragmentController().updateToolBar(getString(R.string.messages), true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                onShareMessageClick(message);
                break;
        }
    }

    private void onShareMessageClick(final Message message) {
        Picasso.with(getContext()).load(message.getMessage_image()).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Utils.share(getContext(), Utils.getLocalBitmapUri(bitmap, getContext()), message.getShareTemplate());
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Utils.share(getContext(), null, message.getShareTemplate());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }

        });
    }

}


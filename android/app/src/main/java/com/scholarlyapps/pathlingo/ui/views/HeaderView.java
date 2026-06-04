package com.scholarlyapps.pathlingo.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.lifecycle.ViewTreeViewModelStoreOwner;

import coil.Coil;
import coil.request.ImageRequest;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.ProgressViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HeaderView extends LinearLayout {

    private ImageView imgAvatar;
    private TextView txtDate;
    private TextView txtCoins;

    public HeaderView(Context context) {
        super(context);
        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        inflate(context, R.layout.layout_home_header, this);
        imgAvatar = findViewById(R.id.imgAvatar);
        txtDate = findViewById(R.id.txtDate);
        txtCoins = findViewById(R.id.txtCoins);
        setCurrentDate();
        if (isInEditMode()) {
            txtCoins.setText("2145");
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCurrentDate() {
        txtDate.setText(new SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(new Date()));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) return;
        ViewModelStoreOwner vmOwner = ViewTreeViewModelStoreOwner.get(this);
        LifecycleOwner lcOwner = ViewTreeLifecycleOwner.get(this);
        if (vmOwner == null || lcOwner == null) return;

        ProgressViewModel viewModel = new ViewModelProvider(vmOwner, new AppViewModelFactory()).get(ProgressViewModel.class);

        viewModel.getUser().observe(lcOwner, user -> {
            if (user != null) {
                txtCoins.setText(String.valueOf(user.xp));
            }
        });

        viewModel.getAvatarUrl().observe(lcOwner, url -> {
            if (url == null || url.isEmpty()) return;
            ImageViewCompat.setImageTintList(imgAvatar, null);
            Coil.imageLoader(getContext()).enqueue(
                    new ImageRequest.Builder(getContext())
                            .data(url)
                            .target(imgAvatar)
                            .build()
            );
        });

        viewModel.refresh();
    }
}

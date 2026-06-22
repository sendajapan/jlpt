package com.scholarlyapps.pathlingo.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.lifecycle.ViewTreeViewModelStoreOwner;

import java.util.Locale;

import coil.Coil;
import coil.request.ImageRequest;

import com.google.android.material.appbar.MaterialToolbar;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.models.User;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.ProgressViewModel;

public class HeaderToolbar extends MaterialToolbar {

    private View btnBack;
    private View btnBell;
    private ImageView imgAvatar;
    private TextView txtName;
    private TextView txtCoins;

    private boolean observersAttached = false;

    public HeaderToolbar(Context context) {
        super(context);
        init(context, null);
    }

    public HeaderToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HeaderToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_home_header, this);

        btnBack = findViewById(R.id.btnBack);
        btnBell = findViewById(R.id.btnBell);
        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtName);
        txtCoins = findViewById(R.id.txtCoins);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderToolbar);
            setBackButtonVisible(a.getBoolean(R.styleable.HeaderToolbar_showBackButton, false));
            setAvatarVisible(a.getBoolean(R.styleable.HeaderToolbar_showAvatar, true));
            a.recycle();
        }
    }

    public void setBackButtonVisible(boolean visible) {
        btnBack.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setAvatarVisible(boolean visible) {
        imgAvatar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setOnBackClickListener(View.OnClickListener listener) {
        btnBack.setOnClickListener(listener);
    }

    public void setOnBellClickListener(View.OnClickListener listener) {
        btnBell.setOnClickListener(listener);
    }

    public void setOnAvatarClickListener(View.OnClickListener listener) {
        imgAvatar.setOnClickListener(listener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) return;

        ViewModelStoreOwner vmOwner = ViewTreeViewModelStoreOwner.get(this);
        LifecycleOwner lcOwner = ViewTreeLifecycleOwner.get(this);
        if (vmOwner == null || lcOwner == null) return;

        ProgressViewModel vm = new ViewModelProvider(vmOwner, new AppViewModelFactory())
                .get(ProgressViewModel.class);

        if (!observersAttached) {
            observersAttached = true;
            vm.getUser().observe(lcOwner, this::onUserChanged);
            vm.getAvatarUrl().observe(lcOwner, this::loadAvatar);
        }

        vm.refresh();
    }

    private void onUserChanged(User user) {
        if (user == null) return;
        txtName.setText(user.name);
        txtCoins.setText(formatCoins(user.coins));
    }

    private void loadAvatar(String url) {
        if (url == null || url.isEmpty()) return;
        ImageViewCompat.setImageTintList(imgAvatar, null);
        Coil.imageLoader(getContext()).enqueue(
                new ImageRequest.Builder(getContext())
                        .data(url)
                        .placeholder(imgAvatar.getDrawable())
                        .error(R.drawable.bg_step_bar_inactive)
                        .target(imgAvatar)
                        .build()
        );
    }

    private static String formatCoins(int coins) {
        if (coins >= 1_000_000) return String.format(Locale.US, "%.1fM", coins / 1_000_000f);
        if (coins >= 1_000) return String.format(Locale.US, "%.1fK", coins / 1_000f);
        return String.valueOf(coins);
    }
}

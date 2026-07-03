package com.scholarlyapps.pathlingo.ui.utils;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;

import coil.Coil;
import coil.request.ErrorResult;
import coil.request.ImageRequest;
import coil.request.SuccessResult;

public class ShimmerImage {

    public static void load(ShimmerFrameLayout shimmer, ImageView target, String url) {
        if (url == null || url.isEmpty()) {
            hide(shimmer);
            return;
        }
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmer();
        Coil.imageLoader(target.getContext()).enqueue(
            new ImageRequest.Builder(target.getContext())
                .data(url)
                .crossfade(true)
                .target(target)
                .listener(new ImageRequest.Listener() {
                    @Override
                    public void onSuccess(@NonNull ImageRequest request, @NonNull SuccessResult result) {
                        hide(shimmer);
                    }

                    @Override
                    public void onError(@NonNull ImageRequest request, @NonNull ErrorResult result) {
                        hide(shimmer);
                    }
                })
                .build()
        );
    }

    private static void hide(ShimmerFrameLayout shimmer) {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
    }
}

package com.scholarlyapps.pathlingo;

import android.app.Application;

import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;

import java.io.File;

import coil.Coil;
import coil.ImageLoader;
import coil.disk.DiskCache;
import coil.memory.MemoryCache;

public class PathlingoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceLocator.init(this);
        Coil.setImageLoader(
            new ImageLoader.Builder(this)
                .memoryCache(
                    new MemoryCache.Builder(this)
                        .maxSizePercent(0.20)
                        .build()
                )
                .diskCache(
                    new DiskCache.Builder()
                        .directory(new File(getFilesDir(), "image_cache"))
                        .maxSizeBytes(200L * 1024 * 1024)
                        .build()
                )
                .respectCacheHeaders(false)
                .crossfade(true)
                .build()
        );
    }
}

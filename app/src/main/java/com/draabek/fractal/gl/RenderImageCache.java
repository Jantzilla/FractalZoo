package com.draabek.fractal.gl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple bitmap disk cache. Used when the view has nothing to show yet
 * Created by Vojtech Drabek on 2018-02-13.
 */
public class RenderImageCache {

    private Map<String, String> cacheFileNames;

    RenderImageCache(File cacheDir) {
        cacheFileNames = new HashMap<>();
    }

    public void add(Bitmap bitmap, String fractalName) {
        File cacheFile;
        try {
            cacheFile = File.createTempFile(fractalName, "cache");
            if (cacheFile.exists()) {
                if (!cacheFile.delete()) {
                    Log.w(this.getClass().getName(), String.format("Could not delete cache for %s", fractalName));
                }
            }
            cacheFileNames.put(fractalName, cacheFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(cacheFile));
        } catch (FileNotFoundException e) {
            Log.e(this.getClass().getName(), String.format(
                    "Could not save cache to %s: %s", cacheFile.getAbsolutePath(), "" + e));
        }
        cacheFileNames.put(fractalName, cacheFile.getAbsolutePath());
    }

    public Bitmap get(String fractalName) {
        String absolutePath = cacheFileNames.get(fractalName);
        if (absolutePath == null) return null;
        return BitmapFactory.decodeFile(absolutePath);
    }


}

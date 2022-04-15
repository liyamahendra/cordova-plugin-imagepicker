package com.devapps.imagepicker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ImagePicker extends CordovaPlugin {

    private Context mContext;
    private Activity mCurrentActivity;
    private CallbackContext mCallbackContext;

    private final int PERMISSION_REQUEST_CODE = 100;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mCurrentActivity = this.cordova.getActivity();
        mContext = this.cordova.getActivity().getApplicationContext();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;

        if (action.equals("hasReadPermission")) {
            mCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, hasReadPermission()));
            return true;
        } else if (action.equals("requestReadPermission")) {
            requestReadPermission();
            return true;
        } else if (action.equals("getPictures")) {

            final JSONObject params = args.getJSONObject(0);

            int desiredWidth = 320;
            int desiredHeight = 480;
            int outputType = 0;


            if (params.has("width")) {
                desiredWidth = params.getInt("width");
            }
            if (params.has("height")) {
                desiredHeight = params.getInt("height");
            }

            if (params.has("outputType")) {
                outputType = params.getInt("outputType");
            }

            if (hasReadPermission()) {
                cordova.setActivityResultCallback(this);
                com.github.dhaval2404.imagepicker.ImagePicker.with(mCurrentActivity)
                        .galleryOnly()
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .maxResultSize(desiredWidth, desiredHeight)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }

            return true;
        }

        return false;
    }

    @SuppressLint("InlinedApi")
    private boolean hasReadPermission() {
        return Build.VERSION.SDK_INT < 23 ||
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(mCurrentActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @SuppressLint("InlinedApi")
    private void requestReadPermission() {
        if (!hasReadPermission()) {
            ActivityCompat.requestPermissions(
                    this.cordova.getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }

        mCallbackContext.success();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == Activity.RESULT_OK) {
            Uri uri = intent.getData();
            mCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, uri.toString()));
        } else if(requestCode == com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR) {
            Toast.makeText(mContext, com.github.dhaval2404.imagepicker.ImagePicker.getError(intent), Toast.LENGTH_SHORT).show();
            mCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, com.github.dhaval2404.imagepicker.ImagePicker.getError(intent)));
        } else {
            Toast.makeText(mContext, "Cancelled", Toast.LENGTH_SHORT).show();
            mCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Cancelled"));
        }
    }
}

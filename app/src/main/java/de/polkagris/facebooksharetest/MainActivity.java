package de.polkagris.facebooksharetest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOGTAG = "LOGTAG";
    private CallbackManager callbackManager;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Collections.singletonList("publish_actions");

        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this, permissionNeeds);

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                publishImage();
            }

            @Override
            public void onCancel() {
                Log.d(LOGTAG, "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("onError");
                Log.d(LOGTAG, error.getCause().toString());
            }
        });
    }

    private void publishImage() {
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("This is the caption")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);

    }
}

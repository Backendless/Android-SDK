package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.backendless.Backendless;

public class ChooseSocialNetworkActivity extends Activity {
	private Button loginWithFacebookSDK;
	private Button loginWithGooglePlusSDK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_social_network);

		initUI();
		initUIBehavior();

		Backendless.initApp(getApplicationContext(), getString(R.string.backendless_AppId), getString(R.string.backendless_ApiKey));
		Backendless.setUrl(getString(R.string.backendless_ApiHost));
	}

	private void initUI() {
		loginWithFacebookSDK = findViewById(R.id.button_loginWithFacebookSDK);
		loginWithGooglePlusSDK =  findViewById(R.id.button_loginWithGooglePlusSDK);

	}

	private void initUIBehavior() {
		loginWithFacebookSDK.setOnClickListener( v -> {
			Intent intent = new Intent(ChooseSocialNetworkActivity.this, LoginWithFacebookSDKActivity.class);
			startActivity(intent);
		} );

		loginWithGooglePlusSDK.setOnClickListener( v -> {
			Intent intent = new Intent(ChooseSocialNetworkActivity.this, LoginWithGooglePlusSDKActivity.class);
			startActivity(intent);
		} );
	}
}

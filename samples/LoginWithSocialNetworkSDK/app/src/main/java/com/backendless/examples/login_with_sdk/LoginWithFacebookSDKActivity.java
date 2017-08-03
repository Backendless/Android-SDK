package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Map;

public class LoginWithFacebookSDKActivity extends Activity {

	private LoginButton loginFacebookButton;
	private Button logoutFacebookButton;
	private Button loginInBackendless;
	private EditText socialAccountInfo;
	private EditText backendlessUserInfo;

	private CallbackManager callbackManager;
	private String fbAccessToken = null;

	private boolean isLoginnedInFacebook = false;
	private boolean isLoginnedInBackendless = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_with_facebook_sdk);
		initUI();
		initUIBehaviour();
	}

	private void initUI() {
		loginFacebookButton = (LoginButton) findViewById(R.id.button_FacebookLogin);
		logoutFacebookButton = (Button) findViewById(R.id.button_FacebookLogout);
		loginInBackendless = (Button) findViewById(R.id.button_fbLoginInBackendless);
		socialAccountInfo = (EditText) findViewById(R.id.editText_fbSocialAccountInfo);
		backendlessUserInfo = (EditText) findViewById(R.id.editText_fbBackendlessUserInfo);
	}

	private void initUIBehaviour() {
		callbackManager = configureFacebookSDKLogin();

		logoutFacebookButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				logoutFromFacebook();
			}
		});

		if (AccessToken.getCurrentAccessToken() != null)
		{
			isLoginnedInFacebook = true;
			fbAccessToken = null;
			loginFacebookButton.setVisibility(View.INVISIBLE);
			logoutFacebookButton.setVisibility(View.VISIBLE);
		}

		loginInBackendless.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isLoginnedInBackendless)
					logoutFromBackendless();
				else
					loginInBackendless();
			}
		});

		BackendlessUser user = Backendless.UserService.CurrentUser();
		if (user != null)
		{
			isLoginnedInBackendless = true;
			backendlessUserInfo.setTextColor(getColor(android.R.color.black));
			backendlessUserInfo.setText("Current user: " + user.getEmail());
			loginInBackendless.setText(getString(R.string.btn_LogoutBackendless));
		}
	}

	private void loginInBackendless()
	{
		Backendless.UserService.loginWithFacebookSdk(fbAccessToken, new AsyncCallback<BackendlessUser>() {
			@Override
			public void handleResponse(BackendlessUser response) {
				isLoginnedInBackendless = true;

				String msg = "ObjectId: " + response.getObjectId() + "\n"
						+ "UserId: " + response.getUserId() + "\n"
						+ "Email: " + response.getEmail() + "\n"
						+ "Properties: " + "\n";

				for (Map.Entry<String, Object> entry : response.getProperties().entrySet())
					msg += entry.getKey() + " : " + entry.getValue() + "\n";

				backendlessUserInfo.setTextColor(getColor(android.R.color.black));
				backendlessUserInfo.setText(msg);

				loginInBackendless.setText(getString(R.string.btn_LogoutBackendless));
			}

			@Override
			public void handleFault(BackendlessFault fault) {
				backendlessUserInfo.setTextColor(getColor(android.R.color.holo_red_dark));
				backendlessUserInfo.setText(fault.toString());
			}
		});
	}

	private void logoutFromBackendless(){
		Backendless.UserService.logout(new AsyncCallback<Void>() {
			@Override
			public void handleResponse(Void response) {
				isLoginnedInBackendless = false;
				backendlessUserInfo.setTextColor(getColor(android.R.color.black));
				backendlessUserInfo.setText("");
				loginInBackendless.setText(getString(R.string.btn_LoginBackendless));
				if (!isLoginnedInFacebook)
					loginInBackendless.setEnabled(false);
			}

			@Override
			public void handleFault(BackendlessFault fault) {
				backendlessUserInfo.setTextColor(getColor(android.R.color.holo_red_dark));
				backendlessUserInfo.setText(fault.toString());
			}
		});
	}

	private CallbackManager configureFacebookSDKLogin() {
		loginFacebookButton.setReadPermissions("email");
		// If using in a fragment
		//loginFacebookButton.setFragment(this);

		CallbackManager callbackManager = CallbackManager.Factory.create();

		// Callback registration
		loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				isLoginnedInFacebook = true;

				fbAccessToken = loginResult.getAccessToken().getToken();
				String msg = "ApplicationId: " + loginResult.getAccessToken().getApplicationId() + "\n"
						+ "UserId: " + loginResult.getAccessToken().getUserId() + "\n"
						+ "Token: " + loginResult.getAccessToken().getToken() + "\n"
						+ "LastRefresh: " + loginResult.getAccessToken().getLastRefresh().toString() + "\n"
						+ "Expires: " + loginResult.getAccessToken().getExpires().toString();
				socialAccountInfo.setTextColor(getColor(android.R.color.black));
				socialAccountInfo.setText(msg);
				loginInBackendless.setEnabled(true);

				loginFacebookButton.setVisibility(View.INVISIBLE);
				logoutFacebookButton.setVisibility(View.VISIBLE);
			}

			@Override
			public void onCancel() {
				// App code
				Log.i("LoginProcess", "loginFacebookButton::onCancel");
			}

			@Override
			public void onError(FacebookException exception) {
				fbAccessToken = null;
				String msg = exception.getMessage() + "\nCause:\n" + (exception.getCause() != null ? exception.getCause().getMessage() : "none");
				socialAccountInfo.setTextColor(getColor(android.R.color.holo_red_dark));
				socialAccountInfo.setText(msg);
				isLoginnedInFacebook = false;
				loginInBackendless.setEnabled(false);
			}
		});

		return callbackManager;
	}

	private void logoutFromFacebook()
	{
		if (!isLoginnedInFacebook)
			return;

		if (AccessToken.getCurrentAccessToken() != null)
			LoginManager.getInstance().logOut();

		isLoginnedInFacebook = false;
		fbAccessToken = null;
		socialAccountInfo.setTextColor(getColor(android.R.color.black));
		socialAccountInfo.setText("");
		if (!isLoginnedInBackendless)
			loginInBackendless.setEnabled(false);

		logoutFacebookButton.setVisibility(View.INVISIBLE);
		loginFacebookButton.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}


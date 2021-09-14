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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.HashMap;
import java.util.Map;

public class LoginWithGooglePlusSDKActivity extends Activity {

	private SignInButton loginGooglePlusButton;
	private Button gpLogoutBackendlessButton;
	private EditText socialAccountInfo;
	private EditText backendlessUserInfo;

	private final int RC_SIGN_IN = 112233; // arbitrary number
	private GoogleSignInClient googleSignInClient;
	private String gpAccessToken = null;

	private boolean isLoggedInGoogle = false;
	private boolean isLoggedInBackendless = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_with_google_plus_sdk);
		initUI();
		initUIBehaviour();
	}

	private void initUI() {
		loginGooglePlusButton = findViewById(R.id.button_googlePlusLogin);
		gpLogoutBackendlessButton = findViewById(R.id.button_gpBackendlessLogout);

		socialAccountInfo = findViewById(R.id.editText_gpSocialAccountInfo);
		backendlessUserInfo = findViewById(R.id.editText_gpBackendlessUserInfo);
	}

	private void initUIBehaviour() {
		configureGooglePlusSDK();

		googleSignInClient.silentSignIn()
				.addOnFailureListener(exception -> handleSignInFailure(exception.getMessage()))
				.onSuccessTask( signInAccount -> {
					handleSignInSuccess(signInAccount);
					return null;
				} );

		gpLogoutBackendlessButton.setOnClickListener(v -> {
			if (isLoggedInBackendless)
				logoutFromBackendless();

			if (isLoggedInGoogle)
				logoutFromGoogle();
		});

		BackendlessUser user = Backendless.UserService.CurrentUser();
		if (user != null)
		{
			isLoggedInBackendless = true;
			backendlessUserInfo.setTextColor(getColor(android.R.color.black));
			backendlessUserInfo.setText("Current user: " + user.getEmail());

			loginGooglePlusButton.setVisibility(View.INVISIBLE);
			gpLogoutBackendlessButton.setVisibility(View.VISIBLE);
		}
	}

	private void loginToBackendless()
	{
		Backendless.UserService.loginWithOAuth2(
				"googleplus",
				gpAccessToken,
				new HashMap<String, String>(),
				new AsyncCallback<BackendlessUser>() {
					@Override
					public void handleResponse(BackendlessUser response) {
						isLoggedInBackendless = true;

						String msg = "ObjectId: " + response.getObjectId() + "\n"
								+ "UserId: " + response.getUserId() + "\n"
								+ "Email: " + response.getEmail() + "\n"
								+ "Properties: " + "\n";

						for (Map.Entry<String, Object> entry : response.getProperties().entrySet())
							msg += entry.getKey() + " : " + entry.getValue() + "\n";

						backendlessUserInfo.setTextColor(getColor(android.R.color.black));
						backendlessUserInfo.setText(msg);

						loginGooglePlusButton.setVisibility(View.INVISIBLE);
						gpLogoutBackendlessButton.setVisibility(View.VISIBLE);
					}

					@Override
					public void handleFault(BackendlessFault fault) {
						backendlessUserInfo.setTextColor(getColor(android.R.color.holo_red_dark));
						backendlessUserInfo.setText(fault.toString());
					}
				}, true );
	}

	private void logoutFromBackendless(){
		Backendless.UserService.logout(new AsyncCallback<Void>() {
			@Override
			public void handleResponse(Void response) {
				isLoggedInBackendless = false;
				backendlessUserInfo.setTextColor(getColor(android.R.color.black));
				backendlessUserInfo.setText("");

				gpLogoutBackendlessButton.setVisibility(View.INVISIBLE);
				loginGooglePlusButton.setVisibility(View.VISIBLE);
			}

			@Override
			public void handleFault(BackendlessFault fault) {
				backendlessUserInfo.setTextColor(getColor(android.R.color.holo_red_dark));
				backendlessUserInfo.setText(fault.toString());
			}
		});
	}

	private void configureGooglePlusSDK()
	{
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestScopes(new Scope(Scopes.PROFILE), new Scope(Scopes.PLUS_ME), new Scope(Scopes.EMAIL))
				.requestId()
				.requestIdToken(getString(R.string.gp_WebApp_ClientId))
				.requestServerAuthCode(getString(R.string.gp_WebApp_ClientId), false)
				.requestEmail()
				.build();

		googleSignInClient = GoogleSignIn.getClient(this, gso );

		loginGooglePlusButton.setOnClickListener(v -> {
			Intent signInIntent = googleSignInClient.getSignInIntent();
			startActivityForResult(signInIntent, RC_SIGN_IN);
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess())
			{
				handleSignInSuccess(result.getSignInAccount());
			}
			else
			{
				handleSignInFailure(result.getStatus().getStatusMessage());
			}
		}
	}

	private void handleSignInSuccess(GoogleSignInAccount signInAccount) {
		isLoggedInGoogle = true;
		loginGooglePlusButton.setVisibility(View.INVISIBLE);
		gpLogoutBackendlessButton.setVisibility(View.VISIBLE);

		final String gpAuthToken = signInAccount.getServerAuthCode();

		Thread t = new Thread(() -> {
			exchangeAuthTokenOnAccessToken(gpAuthToken);

			/*
			 ***********************************************************************
			 * Now that the login to Google has completed successfully, the code
			 * will login to Backendless by "exchanging" Google's access token
			 * for BackendlessUser. This is done in the loginToBackendless() method.
			 ***********************************************************************
			 */
			loginToBackendless();
		});
		t.setDaemon(true);
		t.start();

		String msg = "Id: " + signInAccount.getId() + "\n"
				+ "IdToken: " + signInAccount.getIdToken() + "\n"
				+ "DisplayName: " + signInAccount.getDisplayName() + "\n"
				+ "Email: " + signInAccount.getEmail() + "\n"
				+ "ServerAuthCode: " + signInAccount.getServerAuthCode();
		socialAccountInfo.setTextColor(getColor(android.R.color.black));
		socialAccountInfo.setText(msg);
	}

	private void handleSignInFailure( String statusMessage ) {
		// Signed out, show unauthenticated UI.
		gpAccessToken = null;
		String msg = "Unsuccessful login.\nStatus message:\n" + statusMessage;
		socialAccountInfo.setTextColor(getColor(android.R.color.holo_red_dark));
		socialAccountInfo.setText(msg);
		isLoggedInGoogle = false;
	}

	private void exchangeAuthTokenOnAccessToken(String gpAuthToken ) {
		GoogleTokenResponse tokenResponse;
		try {
			tokenResponse = new GoogleAuthorizationCodeTokenRequest(
				new NetHttpTransport(),
				JacksonFactory.getDefaultInstance(),
				"https://www.googleapis.com/oauth2/v4/token",
				getString(R.string.gp_WebApp_ClientId),
				getString(R.string.gp_WebApp_ClientSecret),
				gpAuthToken,
				"")  // Specify the same redirect URI that you use with your web
				// app. If you don't have a web version of your app, you can
				// specify an empty string.
				.execute();
		} catch (Exception e) {
			Log.e("LoginWithGooglePlus", e.getMessage(), e);
			String msg = socialAccountInfo.getText() + "\n"
					+ e.getMessage();
			socialAccountInfo.setText(msg);
			socialAccountInfo.setTextColor(getColor(android.R.color.holo_red_dark));
			return;
		}

		gpAccessToken = tokenResponse.getAccessToken();
		final String msg = socialAccountInfo.getText() + "\n"
				+ "AccessToken: " + gpAccessToken;

		this.runOnUiThread(() -> socialAccountInfo.setText(msg));
	}

	private void logoutFromGoogle()
	{
		googleSignInClient.signOut().onSuccessTask(
				result -> {
					isLoggedInGoogle = false;
					gpLogoutBackendlessButton.setVisibility(View.INVISIBLE);
					loginGooglePlusButton.setVisibility(View.VISIBLE);

					gpAccessToken = null;
					socialAccountInfo.setTextColor(getColor(android.R.color.black));
					socialAccountInfo.setText("");

					return null;
				});
	}
}
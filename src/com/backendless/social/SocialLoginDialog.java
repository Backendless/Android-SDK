package com.backendless.social;

import android.app.Activity;

public class SocialLoginDialog extends android.app.Dialog
{
  private AbstractSocialLoginStrategy loginStrategy;

  public SocialLoginDialog( Activity context, AbstractSocialLoginStrategy loginStrategy )
  {
    super( context, android.R.style.Theme_Translucent_NoTitleBar );
    this.loginStrategy = loginStrategy;
  }

  @Override
  protected void onCreate( android.os.Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );

    requestWindowFeature( android.view.Window.FEATURE_NO_TITLE );
    requestWindowFeature( android.view.Window.FEATURE_PROGRESS );
  }

  @Override
  public void dismiss()
  {
    loginStrategy.dismiss();
    super.dismiss();
  }
}
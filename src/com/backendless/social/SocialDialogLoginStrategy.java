package com.backendless.social;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.backendless.async.callback.AsyncCallback;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class SocialDialogLoginStrategy extends AbstractSocialLoginStrategy
{
  private Dialog dialog;

  public SocialDialogLoginStrategy( Activity context, SocialType socialType, Map<String, String> fieldsMappings,
                                    List<String> permissions, AsyncCallback<JSONObject> responder )
  {
    super( context, null, socialType, fieldsMappings, permissions, responder );
    this.dialog = new SocialLoginDialog( context, this );
    this.dialog.show();
  }

  @Override
  public void createSpinner()
  {
    super.createSpinner();

    getSpinner().setOnCancelListener( new android.content.DialogInterface.OnCancelListener()
    {
      @Override
      public void onCancel( android.content.DialogInterface dialogInterface )
      {
        if( dialog.isShowing() )
          dialog.dismiss();
      }
    } );
  }

  @Override
  public BackendlessSocialJSInterface getJSInterface( AsyncCallback<JSONObject> responder )
  {
    try
    {
      return new BackendlessSocialJSInterfaceAnnotated( getContext(), dialog, responder );
    }
    catch( Exception e )
    {
      return new BackendlessSocialJSInterface( getContext(), dialog, responder );
    }
  }

  @Override
  public void createLayout()
  {
    RelativeLayout mainContainer = new RelativeLayout( getContext() );
    android.widget.ImageView closeImage = createCloseImage();
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
    layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
    layoutParams.addRule( RelativeLayout.ALIGN_PARENT_TOP );
    int closeImageWidth = closeImage.getDrawable().getIntrinsicWidth() / 2;
    mainContainer.setPadding( closeImageWidth, closeImageWidth, closeImageWidth, closeImageWidth );

    dialog.addContentView( mainContainer, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
    mainContainer.addView( getWebView() );
    mainContainer.addView( closeImage, layoutParams );
  }

  private android.widget.ImageView createCloseImage()
  {
    android.widget.ImageView closeImageView = new android.widget.ImageView( getContext() );
    closeImageView.setOnClickListener( new android.view.View.OnClickListener()
    {
      @Override
      public void onClick( android.view.View v )
      {
        if( dialog.isShowing() )
          dialog.dismiss();
      }
    } );
    android.graphics.drawable.Drawable closeDrawable = getContext().getResources().getDrawable( R.drawable.ic_menu_close_clear_cancel );
    closeImageView.setImageDrawable( closeDrawable );

    return closeImageView;
  }
}

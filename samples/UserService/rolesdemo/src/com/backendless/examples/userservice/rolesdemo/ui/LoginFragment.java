package com.backendless.examples.userservice.rolesdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.backendless.examples.userservice.rolesdemo.Defaults;
import com.backendless.examples.userservice.rolesdemo.R;
import com.backendless.examples.userservice.rolesdemo.utils.BackendlessUtils;

public class LoginFragment extends Fragment
{
  @Override
  public View onCreateView( final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState )
  {
    final View view = inflater.inflate( R.layout.fragment_login, container, false );

    final EditText name = (EditText) view.findViewById( R.id.name );
    final EditText password = (EditText) view.findViewById( R.id.password );

    view.findViewById( R.id.signIn ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        boolean cancel = false;
        View focusView = null;

        if( TextUtils.isEmpty( name.getText() ) )
        {
          name.setError( "Name cannot be empty" );
          focusView = name;
          cancel = true;
        }

        if( TextUtils.isEmpty( password.getText() ) )
        {
          password.setError( "Password cannot be empty" );
          focusView = password;
          cancel = true;
        }

        if( cancel )
        {
          focusView.requestFocus();

          return;
        }

        Intent intent = new Intent();
        intent.setClass( inflater.getContext(), TasksListActivity.class );

        BackendlessUtils.signIn( name.getText().toString(), password.getText().toString(), getArguments().getString( Defaults.ROLE_TAG ), view, intent );
      }
    } );

    return view;
  }
}
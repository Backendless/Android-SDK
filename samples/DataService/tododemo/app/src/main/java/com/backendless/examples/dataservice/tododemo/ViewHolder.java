package com.backendless.examples.dataservice.tododemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.*;

import com.backendless.example.dataservice.tododemo.R;

public class ViewHolder
{
  protected TextView textField;
  protected CheckBox isDoneCheckbox;
  protected ImageView trashIcon;

  static class Builder
  {
    public static ViewHolder create( Activity context, View view, TasksAdapter adapter )
    {
      ViewHolder viewHolder = new ViewHolder();
      viewHolder.textField = view.findViewById( R.id.textField );
      viewHolder.isDoneCheckbox = view.findViewById( R.id.isDoneCheckbox );
      viewHolder.trashIcon = view.findViewById( R.id.trashIcon );
      viewHolder.bindTextFieldListener( context, adapter );
      viewHolder.bindCheckBoxListener( adapter );
      viewHolder.bindTrashIconListener( adapter );

      return viewHolder;
    }
  }

  private void bindTextFieldListener( final Context context, final TasksAdapter adapter )
  {
    textField.setOnClickListener( view -> {
      AlertDialog.Builder alertDialog = new AlertDialog.Builder( context );
      alertDialog.setTitle( "Edit ToDo record" );
      final EditText input = new EditText( context );
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.FILL_PARENT );
      input.setLayoutParams( lp );
      input.setText( textField.getText() );
      input.setSingleLine();
      alertDialog.setView( input );
      alertDialog.setPositiveButton( "Save", ( dialog, whichButton ) -> {
        Task entity = (Task) isDoneCheckbox.getTag();
        entity.setTitle( input.getText().toString() );
        adapter.saveEntity( entity );
        textField.setText( input.getText().toString() );
      } );
      alertDialog.show();
    } );
  }

  private void bindCheckBoxListener( final TasksAdapter adapter )
  {
    isDoneCheckbox.setOnClickListener( view -> {
      final Task entity = (Task) isDoneCheckbox.getTag();
      entity.setCompleted( !entity.isCompleted() );
      adapter.updateEntity( entity );
      setChecked( entity.isCompleted() );
    } );
  }

  private void bindTrashIconListener( final TasksAdapter adapter )
  {
    trashIcon.setOnClickListener( view -> adapter.remove( (Task) isDoneCheckbox.getTag() ) );
  }

  public void setChecked( boolean isChecked )
  {
    trashIcon.setVisibility( isChecked ? View.VISIBLE : View.INVISIBLE );
    isDoneCheckbox.setChecked( isChecked );
  }
}

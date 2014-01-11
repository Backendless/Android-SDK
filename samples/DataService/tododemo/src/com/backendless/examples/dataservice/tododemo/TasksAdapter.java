package com.backendless.examples.dataservice.tododemo;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class TasksAdapter extends ArrayAdapter<Task>
{
  private Activity context;
  private TasksList taskList;

  public TasksAdapter( Activity context, List<Task> list, TasksList taskList )
  {
    super( context, R.layout.listitem, list );
    this.context = context;
    this.taskList = taskList;
  }

  @Override
  public void add( Task entity )
  {
    super.add( entity );
    taskList.addEntity( entity );
  }

  @Override
  public void remove( final Task entity )
  {
    super.remove( entity );
    TasksManager.remove( entity, context, new InnerCallback<Long>()
    {
      @Override
      public void handleResponse( Long response )
      {
        taskList.removeEntity( entity );
      }
    } );
  }

  public void addAll( List<Task> currentPage )
  {
    for( Task task : currentPage )
      add( task );
  }

  public void removeAll( List<Task> currentPage )
  {
    for( Task task : currentPage )
      remove( task );
  }

  public void saveEntity( final Task entity )
  {
    TasksManager.saveEntity( entity, context, null );
  }

  public void updateEntity( Task entity )
  {
    saveEntity( entity );
    taskList.changeState( entity );
  }

  @Override
  public View getView( int position, View convertView, ViewGroup parent )
  {
    ViewHolder viewHolder;

    if( convertView == null )
    {
      convertView = context.getLayoutInflater().inflate( R.layout.listitem, null );
      viewHolder = ViewHolder.Builder.create( context, convertView, this );
      convertView.setTag( viewHolder );
    }
    else
      viewHolder = ((ViewHolder) convertView.getTag());

    Task task = getItem( position );
    viewHolder.isDoneCheckbox.setTag( task );
    viewHolder.textField.setText( task.getTitle() );
    viewHolder.setChecked( task.isCompleted() );

    return convertView;
  }
}
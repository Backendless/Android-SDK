package com.backendless.examples.dataservice.tododemo;

import java.util.concurrent.CopyOnWriteArrayList;

public class TasksList
{
  private NotifyList leftEntities;
  private NotifyList doneEntities;

  public TasksList( final Listener toDoListListener )
  {
    leftEntities = new NotifyList( toDoListListener::onLeftEntitiesChanged );
    doneEntities = new NotifyList( toDoListListener::onDoneEntitiesChanged );
  }

  private void completeToDo( Task entity )
  {
    doneEntities.add( entity );
    leftEntities.remove( entity );
  }

  private void unCompleteToDo( Task entity )
  {
    doneEntities.remove( entity );
    leftEntities.add( entity );
  }

  public void changeState( Task entity )
  {
    if( entity.isCompleted() )
      completeToDo( entity );
    else
      unCompleteToDo( entity );
  }

  public void addEntity( Task entity )
  {
    if( entity.isCompleted() )
      doneEntities.add( entity );
    else
      leftEntities.add( entity );
  }

  public void removeEntity( Task entity )
  {
    if( entity.isCompleted() )
      doneEntities.remove( entity );
    else
      leftEntities.remove( entity );
  }

  public NotifyList getDoneEntities()
  {
    return doneEntities;
  }

  public interface Listener
  {
    void onLeftEntitiesChanged( int entitiesCount );

    void onDoneEntitiesChanged( int entitiesCount );
  }

  private interface ListSizeListener
  {
    void onListSizeChanged( int entitiesCount );
  }

  private static class NotifyList extends CopyOnWriteArrayList<Task>
  {
    private static final long serialVersionUID = -4865952093095667412L;

    private ListSizeListener listener;

    private NotifyList( ListSizeListener listener )
    {
      this.listener = listener;
    }

    @Override
    public boolean add( Task object )
    {
      boolean result = super.add( object );
      listener.onListSizeChanged( size() );

      return result;
    }

    @Override
    public boolean remove( Object object )
    {
      boolean result = super.remove( object );
      listener.onListSizeChanged( size() );

      return result;
    }
  }
}

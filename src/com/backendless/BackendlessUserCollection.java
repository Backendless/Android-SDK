package com.backendless;

import com.backendless.exceptions.BackendlessException;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chupov
 * Date: 20.01.14
 * Time: 14:36
 */
public class BackendlessUserCollection<E extends BackendlessUser> extends BackendlessCollection<E>
{
  @Override
  public BackendlessCollection<E> getPage(int pageSize, int offset) throws BackendlessException
  {
    IBackendlessQuery tempQuery = new BackendlessDataQuery();
    tempQuery.setOffset( offset );
    tempQuery.setPageSize( pageSize );

    BackendlessCollection<HashMap> response = Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "find", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), "Users", tempQuery } );
    BackendlessUserCollection<E> result = create( response, this.getType() );
    result.setQuery( tempQuery );
    return result;
  }

  static <E extends BackendlessUser> BackendlessUserCollection<E> create(
          BackendlessCollection<HashMap> backendlessCollection, Class<E> userClass ) throws BackendlessException
  {
    List<E> data = new ArrayList<E>();
    for( HashMap properties : backendlessCollection.getCurrentPage() )
    {
      try
      {
        E user = userClass.newInstance();
        user.setProperties( properties );
        data.add( user );
      }
      catch( Throwable t )
      {
        throw new BackendlessException( t );
      }
    }

    BackendlessUserCollection<E> backendlessUserCollection = new BackendlessUserCollection<E>();
    backendlessUserCollection.setData( data );
    backendlessUserCollection.setQuery( backendlessCollection.getQuery() );
    backendlessUserCollection.setTotalObjects( backendlessCollection.getTotalObjects() );
    backendlessUserCollection.setType( userClass );

    return backendlessUserCollection;
  }
}

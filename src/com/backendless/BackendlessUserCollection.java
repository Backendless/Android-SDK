package com.backendless;

import com.backendless.exceptions.BackendlessException;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.HashMap;

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
    BackendlessUserCollection<E> result = Backendless.UserService.convertResponse( response, this.getType() );
    result.setQuery( tempQuery );
    return result;
  }
}

package com.backendless.persistence.offline;

import com.backendless.exceptions.BackendlessFault;

public interface OfflineAwareCallback<T>
{
    void handleLocalResponse( T response );
    void handleLocalFault( BackendlessFault fault );
    void handleRemoteResponse( T response );
    void handleRemoteFault( BackendlessFault fault );
}

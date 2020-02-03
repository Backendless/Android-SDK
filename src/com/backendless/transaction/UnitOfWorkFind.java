package com.backendless.transaction;

import com.backendless.persistence.DataQueryBuilder;

public interface UnitOfWorkFind
{
  OpResult find( String tableName, DataQueryBuilder queryBuilder );
}

package com.backendless.transaction;

import com.backendless.persistence.DataQueryBuilder;

interface UnitOfWorkFind
{
  OpResult find( String tableName, DataQueryBuilder queryBuilder );
}

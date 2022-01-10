package com.backendless.transaction;

interface IUnitOfWork extends UnitOfWorkCreate, UnitOfWorkUpsert, UnitOfWorkDelete, UnitOfWorkUpdate, UnitOfWorkFind, UnitOfWorkAddRelation, UnitOfWorkSetRelation, UnitOfWorkDeleteRelation, UnitOfWorkExecutor
{
}

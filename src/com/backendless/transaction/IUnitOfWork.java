package com.backendless.transaction;

public interface IUnitOfWork extends UnitOfWorkCreate, UnitOfWorkDelete, UnitOfWorkUpdate, UnitOfWorkFind, UnitOfWorkAddRelation, UnitOfWorkSetRelation, UnitOfWorkDeleteRelation, UnitOfWorkExecutor
{
}

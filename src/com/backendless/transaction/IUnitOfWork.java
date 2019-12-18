package com.backendless.transaction;

public interface IUnitOfWork extends UnitOfWorkCreate, UnitOfWorkDelete, UnitOfWorkUpdate, UnitOfWorkAddRelation, UnitOfWorkSetRelation, UnitOfWorkDeleteRelation, UnitOfWorkExecutor
{
}

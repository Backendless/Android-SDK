package com.backendless.transaction;

interface IUnitOfWork extends UnitOfWorkCreate, UnitOfWorkDelete, UnitOfWorkUpdate, UnitOfWorkFind, UnitOfWorkAddRelation, UnitOfWorkSetRelation, UnitOfWorkDeleteRelation, UnitOfWorkExecutor
{
}

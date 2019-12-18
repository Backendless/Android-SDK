package com.backendless.transaction;

public interface IUnitOfWork extends UnitOfWorkCreate, UnitOfWorkDelete, UnitOfWorkUpdate, UnitOfWorkAddRelation, UnitOfWorkSetRelation
{
  UnitOfWorkStatus execute();
}

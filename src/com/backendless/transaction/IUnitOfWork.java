package com.backendless.transaction;

public interface IUnitOfWork extends UnitOfWorkCreate, UnitOfWorkDelete, UnitOfWorkUpdate, UnitOfWorkAddRelation
{
  UnitOfWorkStatus execute();
}

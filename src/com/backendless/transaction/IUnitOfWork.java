package com.backendless.transaction;

public interface IUnitOfWork extends UnitOfWorkCreate, UnitOfWorkDelete, UnitOfWorkUpdate
{
  UnitOfWorkStatus execute();
}

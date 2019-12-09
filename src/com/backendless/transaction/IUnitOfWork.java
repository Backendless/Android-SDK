package com.backendless.transaction;

public interface IUnitOfWork extends UnitOfWorkCreate, UnitOfWorkDelete, UnitOFWorkUpdate
{
  UnitOfWorkStatus execute();
}

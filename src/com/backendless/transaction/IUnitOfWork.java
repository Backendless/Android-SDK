package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface IUnitOfWork extends UnitOfWorkCreate, UnitOfWorkDelete, UnitOFWorkUpdate
{
  UnitOfWorkStatus execute();
}

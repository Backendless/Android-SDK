package com.backendless.rt.data;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RelationStatus
{
  /**
   * this is the objectId of the object which is the parent in the relation
   */
  private String parentObjectId;

  /**
   * this is set to true if the children where assigned through a where clause in the child table
   */
  private boolean isConditional;

  /**
   * if isConditional is true, this has the value which was used to identify the children
   */
  private String whereClause;

  /**
   * if isConditional is false, this contains a collection of child objectId values
   */
  private List<String> children;
}

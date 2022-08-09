package com.backendless.persistence;


import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;


public final class JSONUpdateBuilder
{
	public static final String OPERATION_FIELD_NAME = "___operation";
	public static final String ARGS_FIELD_NAME = "args";
	
	public enum Operation
	{
		SET("JSON_SET"),
		INSERT("JSON_INSERT"),
		REPLACE("JSON_REPLACE"),
		REMOVE("JSON_REMOVE"),
		ARRAY_APPEND("JSON_ARRAY_APPEND"),
		ARRAY_INSERT("JSON_ARRAY_INSERT");

    @Getter
		private final String operationName;

		Operation(String operationName) {
			this.operationName = operationName;
		}
	}
	
	
	private final HashMap<String, Object> jsonUpdate = new HashMap<>();
	
	private JSONUpdateBuilder(Operation op)
	{
		jsonUpdate.put(OPERATION_FIELD_NAME, op.getOperationName());
	}
	
	
	public static GeneralArgHolder SET()
	{
		return new JSONUpdateBuilder(Operation.SET).new GeneralArgHolder();
	}
	
	public static GeneralArgHolder INSERT()
	{
		return new JSONUpdateBuilder(Operation.INSERT).new GeneralArgHolder();
	}
	
	public static GeneralArgHolder REPLACE()
	{
		return new JSONUpdateBuilder(Operation.REPLACE).new GeneralArgHolder();
	}
	
	public static GeneralArgHolder ARRAY_APPEND()
	{
		return new JSONUpdateBuilder(Operation.ARRAY_APPEND).new GeneralArgHolder();
	}
	
	public static GeneralArgHolder ARRAY_INSERT()
	{
		return new JSONUpdateBuilder(Operation.ARRAY_INSERT).new GeneralArgHolder();
	}
	
	public static RemoveArgHolder REMOVE()
	{
		return new JSONUpdateBuilder(Operation.REMOVE).new RemoveArgHolder();
	}
	
	
	public final class GeneralArgHolder extends ArgHolder
	{
		private final LinkedHashMap<String, Object> jsonUpdateArgs = new LinkedHashMap<>();
		
		private GeneralArgHolder()
		{
			JSONUpdateBuilder.this.jsonUpdate.put(ARGS_FIELD_NAME, jsonUpdateArgs);
		}
		
		public GeneralArgHolder addArgument(String jsonPath, Object value)
		{
			jsonUpdateArgs.put(jsonPath, value);
			return this;
		}
	}
	
	public final class RemoveArgHolder extends ArgHolder
	{
		private RemoveArgHolder()
		{
			JSONUpdateBuilder.this.jsonUpdate.put(ARGS_FIELD_NAME, jsonUpdateArgs);
		}
		
		private final LinkedHashSet<String> jsonUpdateArgs = new LinkedHashSet<>();
		
		public RemoveArgHolder addArgument(String jsonPath)
		{
			jsonUpdateArgs.add(jsonPath);
			return this;
		}
	}
	
	public abstract class ArgHolder
	{
		private ArgHolder()
		{ }
		
		public HashMap<String, Object> create()
		{
			return JSONUpdateBuilder.this.jsonUpdate;
		}
	}
}

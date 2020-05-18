package com.backendless.geo;

import com.backendless.persistence.JsonDTO;
import com.backendless.util.JSONUtil;
import weborb.reader.AnonymousObject;
import weborb.reader.NamedObject;
import weborb.reader.NullType;
import weborb.reader.ReferenceCache;
import weborb.types.IAdaptingType;
import weborb.util.IArgumentObjectFactory;

import java.lang.reflect.Type;
import java.util.Map;


public class BackendlessJsonFactory implements IArgumentObjectFactory
{
	@Override
	public Object createObject(IAdaptingType iAdaptingType)
	{
		if( iAdaptingType instanceof NamedObject)
			iAdaptingType = ((NamedObject) iAdaptingType).getTypedObject();
		
		if( iAdaptingType.getClass() == NullType.class )
			return null;
		
		ReferenceCache refCache = ReferenceCache.getInstance();
		
		if( refCache.hasObject( iAdaptingType, JsonDTO.class ) )
		{
			return refCache.getObject( iAdaptingType, JsonDTO.class );
		}
		else
		{
			if( iAdaptingType instanceof AnonymousObject)
			{
				@SuppressWarnings( "unchecked" )
				Map<String, Object> properties = (Map<String, Object>) iAdaptingType.defaultAdapt();
				String rawJsonString = (String) properties.get( "rawJsonString" );
				
				if (rawJsonString == null)
					return null;
				
				Class<?> expectedType = ((AnonymousObject) iAdaptingType).getExpectedType();
				Object result = JSONUtil.getJsonConverter().readObject(rawJsonString, expectedType);
				
				refCache.addObject( iAdaptingType, JsonDTO.class, result );
				return result;
			}
			else
				throw new RuntimeException( "unknown type" );
		}
	}
	
	@Override
	public boolean canAdapt(IAdaptingType iAdaptingType, Type type)
	{
		return false;
	}
}

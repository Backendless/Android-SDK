package com.backendless.rt.rso;

import weborb.exceptions.AdaptingException;
import weborb.reader.ArrayType;
import weborb.types.IAdaptingType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <tt>Invocation</tt>
 *
 * @author <a href="http://www.themidnightcoders.com">Midnight Coders, LLC</a>
 */

public class InvocationHelper
{
  private static final Logger Log = Logger.getLogger( "InvocationHelper" );

  public static Object invoke( Object object, String function,
                               ArrayType arguments ) throws NoSuchMethodException, IllegalAccessException, AdaptingException, InvocationTargetException
  {
    final Object[] argumentsArray = (Object[]) arguments.getArray();
    IAdaptingType[] adaptingTypeArray = Arrays.copyOf( argumentsArray, argumentsArray.length, IAdaptingType[].class);

    return invoke( object, function, adaptingTypeArray );
  }

  public static Object invoke( Object object, String function,
                               IAdaptingType[] adaptingTypes ) throws NoSuchMethodException, IllegalAccessException, AdaptingException, InvocationTargetException
  {
    Method method = MethodLookup.findMethod( object.getClass(), function, adaptingTypes );

    if( Log.isLoggable( Level.INFO ) )
      Log.log( Level.INFO, "Resolved java object and method, proceeding to invocation" );

    return InvocationHelper.invoke( object, method, adaptingTypes );
  }

  private static Object invoke( Object object, Method method,
                                IAdaptingType[] adaptingTypes ) throws IllegalAccessException, AdaptingException, InvocationTargetException
  {
    return InvocationHelper.invoke( object, method.getGenericParameterTypes(), method, adaptingTypes );
  }

  private static Object invoke( Object object, Type[] formalArgs, Method method,
                                IAdaptingType[] adaptingTypes ) throws AdaptingException, InvocationTargetException, IllegalAccessException
  {

    Object[] arguments = new Object[ adaptingTypes.length ];

    for( int i = 0; i < formalArgs.length; i++ )
    {
      Object adaptedObject = adaptingTypes[ i ].adapt( formalArgs[ i ] );
      arguments[ i ] = adaptedObject;
    }

    return method.invoke( object, arguments );
  }
}

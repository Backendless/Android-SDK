package com.backendless.rt.rso;

import com.backendless.exceptions.BackendlessException;
import weborb.config.ORBConfig;
import weborb.config.SerializationConfigHandler;
import weborb.types.IAdaptingType;
import weborb.util.ClassUtils;
import weborb.util.IArgumentObjectFactory;
import weborb.util.ObjectFactories;
import weborb.util.reflect.MethodUtils;
import weborb.util.reflect.TypeUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

class MethodLookup
{

  static Method findMethod( Class clazz, String methodName, IAdaptingType[] arguments ) throws NoSuchMethodException
  {
    if( arguments == null )
      throw new BackendlessException( "Invocation arguments can not be null" );

    Method[] methods = clazz.getMethods();
    ArrayList<Method> selectedMethods = new ArrayList<>();
    int argCount = arguments.length;

    nextMethod:
    for( Method method : methods )
    {
      Type[] formalArgs = method.getGenericParameterTypes();

      if( method.getName().equals( methodName ) && formalArgs.length == argCount )
      {
        for( int j = 0; j < formalArgs.length; j++ )
          if( !matches( arguments[ j ], formalArgs[ j ] ) )
            continue nextMethod;

        selectedMethods.add( method );
      }
    }

    methods = new Method[ selectedMethods.size() ];
    selectedMethods.toArray( methods );
    methods = MethodUtils.chooseNotSyntheticMethod( clazz, methods );

    if( methods.length == 0 )
    {
      SerializationConfigHandler serializationConfig = ORBConfig.getORBConfig().getSerializationConfig();
      String modifiedMethodName = serializationConfig.getWithoutPrefix( methodName );

      if( !modifiedMethodName.equals( methodName ) )
        return findMethod( clazz, modifiedMethodName, arguments );

      String error = "Unable to find method with name " + methodName + " in class " + clazz.getName() + ". Argument count - " + arguments.length;

      throw new NoSuchMethodException( error );
    }
    else if( methods.length > 1 )
    {
      return resolveAmbiguity( methodName, methods.length );
    }

    return methods[ 0 ];
  }

  private static Method resolveAmbiguity( String methodName, int count )
  {
    throw new BackendlessException( "There is " + count + " methods found with name '" + methodName + "'" );
  }

  private static boolean matches( IAdaptingType argument, Type formalArg )
  {
    if( !(formalArg instanceof Class && argument.getClass().isAssignableFrom( (Class) formalArg )) )
    {
      String typeName = ClassUtils.getTypeName( formalArg );
      IArgumentObjectFactory factory = ObjectFactories.getArgumentObjectFactory( typeName );

      if( factory != null )
      {
        return factory.canAdapt( argument, formalArg );
      }

      if( formalArg instanceof TypeVariable )
      {
        List<Class> boundClasses = TypeUtils.getBoundClasses( (TypeVariable) formalArg );

        for( Class boundClass : boundClasses )
          if( argument.canAdaptTo( boundClass ) )
            return true;
      }
      else
      {
        return argument.canAdaptTo( formalArg );
      }
    }

    return false;
  }
}

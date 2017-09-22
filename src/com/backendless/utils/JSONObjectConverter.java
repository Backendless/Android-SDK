package com.backendless.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weborb.protocols.jsonrpc.JsonTextReader;
import weborb.types.IAdaptingType;
import weborb.util.io.ISerializer;
import weborb.util.io.Serializer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * When using easyLogin (for Facebook, Google+ or Twitter) the returned BackendlessUser can contain not correct fields:
 * JSONObject.NULL instead of null
 * JSONArray instead of List
 * JSONObject instead of Map
 *
 * This class is used to convert JSONObject.. fields into simple java.lang types
 */

public class JSONObjectConverter {


    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    public static List toList(JSONArray array) throws JSONException {
        List list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    public static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }

    public static String toJSONString( Object object )
    {
        try
        {
            return new String( Serializer.toBytes( object, ISerializer.JSON ) );
        }
        catch( Exception e )
        {
            return null;
        }
    }

    public static HashMap getJSONObject( String jsonString )
    {
      InputStream stream = new ByteArrayInputStream( jsonString.getBytes() );
      InputStreamReader streamReader = null;
      JsonTextReader reader = null;

      try
      {
        streamReader = new InputStreamReader( stream, "UTF8" );
        reader = new JsonTextReader( streamReader );
        reader.read();
        IAdaptingType jsonType = weborb.protocols.jsonrpc.JsonRequestParser.readJSON( reader );
        return (HashMap) jsonType.adapt( HashMap.class );
      }
      catch( Throwable t )
      {
        //e.printStackTrace();
        return null;
      }
      finally
      {
        try
        {
          if( reader != null )
            reader.close();

          if( streamReader != null )
            streamReader.close();

          if( stream != null )
            stream.close();
        }
        catch( Throwable t )
        {

        }
      }
    }
}
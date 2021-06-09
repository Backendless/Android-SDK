package com.backendless;

import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscoverApiEndpointService
{
  private final static String DEFAULT_API_ENDPOINT = "https://api.backendless.com";

  static String discoverApiEndpoint( String applicationId )
  {
    String discoverUrl = DEFAULT_API_ENDPOINT + "/discover-api-endpoint?appId=" + applicationId;

    try
    {
      URL connectionUrl = new URL( discoverUrl );

      HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
      connection.setRequestMethod( "GET" );

      BufferedReader in = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
      String inputLine;
      StringBuilder response = new StringBuilder();

      while( ( inputLine = in.readLine() ) != null )
      {
        response.append( inputLine );
      }
      in.close();

      return new JSONObject( response.toString() ).getString( "url" );
    }
    catch( IOException | JSONException e )
    {
      throw new BackendlessException( ExceptionMessage.DISCOVERY_ENDPOINT_EXCEPTION, e.getMessage() );
    }
  }
}

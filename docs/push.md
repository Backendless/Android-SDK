# Push

### Setting up the pushes
About setting up GCM services read at https://backendless.com/documentation/messaging/android/messaging_push_notification_setup_androi.htm

### Receiving pushes

You must implement two classes to receive and handle push notifications within your android application. First, create a new class which extends `BackendlessPushSerice`. This class provides 4 methods, one for each of the possible GCM actions. By overriding these methods, you can run any code when a message is received (for example, showing a notification or just writing to the logs). 

```java
import com.backendless.push.BackendlessPushService;

public class MyPushService extends BackendlessPushService
{

  @Override
  public void onRegistered( Context context, String registrationId )
  {
    Toast.makeText( context, "registered" + registrationId, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onUnregistered( Context context, Boolean unregistered )
  {
    Toast.makeText( context, "unregistered", Toast.LENGTH_SHORT).show();
  }

  @Override
  public boolean onMessage( Context context, Intent intent )
  {
    String message = intent.getStringExtra( "message" );
    Toast.makeText( context, "Push message received. Message: " + message, Toast.LENGTH_LONG ).show();
    return true;
  }

  @Override
  public void onError( Context context, String message )
  {
    Toast.makeText( context, message, Toast.LENGTH_SHORT).show();
  }
}
```

After defining the class which will handle receiving messages, you have to register this class with GCM. Create a new class in the same package as the above, and extend `BackendlessBroadcastReceiver`. You need to explicitly tell Backendless which class extends `BackendlessPushService` by overriding method `getServiceClass()`.
```java
import com.backendless.push.BackendlessBroadcastReceiver;
import com.backendless.push.BackendlessPushService;

public class MyPushReceiver extends BackendlessBroadcastReceiver
{
  @Override
  public Class<? extends BackendlessPushService> getServiceClass()
  {
    return MyPushService.class;
  }
}
```

### Manifest changes

To use GCM for push notifications, add the following permissions to your project's AndroidManifest.xml file within the `<manifest>` tags:
```xml
<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WAKE_LOCK" />
```
Also add the following lines within the `<manifest>` tags, however replacing com.myPackage with your app's package name. 
```xml
<permission android:name="com.myPackage.permission.C2D_MESSAGE"
    android:protectionLevel="signature" />
<uses-permission android:name="com.myPackage.permission.C2D_MESSAGE" />
```
Next, within your `<application>` tag, register the receiver and service you created earlier. Replace com.myPackage with your application's package, and ensure that the receiver name is set to match the custom MyPushReceiver and the service name is set to match the MyPushService.
```xml
<receiver
    android:name=".MyPushReceiver"
    android:permission="com.google.android.c2dm.permission.SEND" >
    <intent-filter>
        <action android:name="com.google.android.c2dm.intent.RECEIVE" />
        <category android:name="com.myPackage" />
    </intent-filter>
</receiver>
<service android:name=".MyPushService" />
```

### Registering the Device
Now that you have configured your manifest and added the two necessary classes, it is necessary to register a device to receive Push notifications. To register the active user to receive push notifications on the current device, use the following: 

```java
Backendless.Messaging.registerDevice( Defaults.GCMSenderId, channel, registrationCallback );
```
After registration has completed, your application will be notified via the onRegistered(…) method of the `MyPushService`. 
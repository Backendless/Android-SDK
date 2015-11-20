/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless.push.adm;

import android.content.Context;

import com.amazon.device.messaging.ADM;
import com.amazon.device.messaging.development.ADMManifest;
import com.backendless.push.AbstractRegistrar;
import com.backendless.push.BackendlessPushBroadcastReceiver;
import com.backendless.push.Constants;

import java.util.Date;
import java.util.List;

public class ADMRegistrar extends AbstractRegistrar
{
  private static ADM adm;

  @Override
  public void subscribe( Context context, String senderId, String channelName )
  {
    throw new UnsupportedOperationException( Constants.SUBSCRIBE_METHOD_IS_NOT_SUITABLE_FOR_ADM_REGISTRAR );
  }

  @Override
  public void checkPossibility( Context context )
  {
    ADMManifest.checkManifestAuthoredProperly( context );
  }

  @Override
  public void register( Context context, String senderId, List<String> channels, Date expiration )
  {
    ADM adm = getAdm( context );
    if ( adm.getRegistrationId() != null )
    {
      BackendlessPushBroadcastReceiver.setInternalUnregistered( true );
      unregister( context );
    }

    if( expiration != null )
      BackendlessPushBroadcastReceiver.setRegistrationExpiration( expiration.getTime() );

    if( channels != null )
      BackendlessPushBroadcastReceiver.setChannels( channels );

    adm.startRegister();
  }

  @Override
  public void unregister( Context context )
  {
    ADM adm = getAdm( context );
    if ( adm.getRegistrationId() != null )
      adm.startUnregister();
  }

  public static ADM getAdm(Context context)
  {
    if (adm == null)
      adm = new ADM( context );
    return adm;
  }
}

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

package com.backendless.media.lib;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 * An interface that represents a Stream. 
 */
public interface Stream {

	/**
	 * Configures the stream. You need to call this before calling {@link #getSessionDescription()} 
	 * to apply your configuration of the stream.
	 */
	public void configure() throws IllegalStateException, IOException;
	
	/**
	 * Starts the stream.
	 * This method can only be called after {@link Stream#configure()}.
	 */
	public void start() throws IllegalStateException, IOException;

	/**
	 * Stops the stream.
	 */
	public void stop();

	/**
	 * Sets the Time To Live of packets sent over the network.
	 * @param ttl The time to live
	 * @throws java.io.IOException
	 */
	public void setTimeToLive( int ttl ) throws IOException;

	/**
	 * Sets the destination ip address of the stream.
	 * @param dest The destination address of the stream
	 */
	public void setDestinationAddress( InetAddress dest );

	/**
	 * Sets the destination ports of the stream.
	 * If an odd number is supplied for the destination port then the next
	 * lower even number will be used for RTP and it will be used for RTCP.
	 * If an even number is supplied, it will be used for RTP and the next odd
	 * number will be used for RTCP.
	 * @param dport The destination port
	 */
	public void setDestinationPorts( int dport );

	/**
	 * Sets the destination ports of the stream.
	 * @param rtpPort Destination port that will be used for RTP
	 * @param rtcpPort Destination port that will be used for RTCP
	 */
	public void setDestinationPorts( int rtpPort, int rtcpPort );

	/**
	 * If a TCP is used as the transport protocol for the RTP session,
	 * the output stream to which RTP packets will be written to must
	 * be specified with this method.
	 */
	public void setOutputStream( OutputStream stream, byte channelIdentifier );

	/**
	 * Returns a pair of source ports, the first one is the
	 * one used for RTP and the second one is used for RTCP.
	 **/
	public int[] getLocalPorts();

	/**
	 * Returns a pair of destination ports, the first one is the
	 * one used for RTP and the second one is used for RTCP.
	 **/
	public int[] getDestinationPorts();


	/**
	 * Returns the SSRC of the underlying {@link com.backendless.media.lib.rtp.RtpSocket}.
	 * @return the SSRC of the stream.
	 */
	public int getSSRC();

	/**
	 * Returns an approximation of the bit rate consumed by the stream in bit per seconde.
	 */
	public long getBitrate();

	/**
	 * Returns a description of the stream using SDP.
	 * This method can only be called after {@link Stream#configure()}.
	 * @throws IllegalStateException Thrown when {@link Stream#configure()} wa not called.
	 */
	public String getSessionDescription() throws IllegalStateException;

	public boolean isStreaming();

}

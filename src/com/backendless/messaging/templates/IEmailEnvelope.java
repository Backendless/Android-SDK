package com.backendless.messaging.templates;

import java.util.List;

public interface IEmailEnvelope
{

  IEmailEnvelope addCc( List<String> ccAddresses );

  IEmailEnvelope setCc( List<String> ccAddresses );

  List<String> getCc();

  IEmailEnvelope addBcc( List<String> bccAddresses );

  IEmailEnvelope setBcc( List<String> bccAddresses );

  List<String> getBcc();
}

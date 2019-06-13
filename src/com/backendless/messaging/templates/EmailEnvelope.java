package com.backendless.messaging.templates;

public class EmailEnvelope
{

  public static EmailEnvelopeWithRecipients withRecipients()
  {
    return new EmailEnvelopeWithRecipients();
  }

  public static EmailEnvelopeWithQuery withQuery()
  {
    return new EmailEnvelopeWithQuery();
  }
}

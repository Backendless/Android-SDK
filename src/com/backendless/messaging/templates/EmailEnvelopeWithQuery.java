package com.backendless.messaging.templates;

import java.util.ArrayList;
import java.util.List;

public class EmailEnvelopeWithQuery implements IEmailEnvelope
{
    private List<String> ccAddresses;
    private List<String> bccAddresses;
    private String query;

    public EmailEnvelopeWithQuery()
    {
        ccAddresses = new ArrayList<>();
        bccAddresses = new ArrayList<>();
    }

    public EmailEnvelopeWithQuery addCc(List<String> ccAddresses)
    {
        this.ccAddresses.addAll(ccAddresses);
        return this;
    }

    public EmailEnvelopeWithQuery setCc(List<String> ccAddresses)
    {
        this.ccAddresses = ccAddresses;
        return this;
    }

    public List<String> getCc()
    {
        return ccAddresses;
    }

    public EmailEnvelopeWithQuery addBcc(List<String> bccAddresses)
    {
        this.bccAddresses.addAll(bccAddresses);
        return this;
    }

    public EmailEnvelopeWithQuery setBcc(List<String> bccAddresses)
    {
        this.bccAddresses = bccAddresses;
        return this;
    }

    public List<String> getBcc()
    {
        return bccAddresses;
    }

    public String getRecipientsQuery()
    {
        return query;
    }

    public EmailEnvelopeWithQuery setRecipientsQuery(String query)
    {
        this.query = query;
        return this;
    }
}

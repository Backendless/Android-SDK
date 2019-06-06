package com.backendless.messaging.templates;

import java.util.ArrayList;
import java.util.List;

public class EmailEnvelopeWithRecipients implements IEmailEnvelope
{
    private List<String> ccAddresses;
    private List<String> bccAddresses;
    private List<String> toAddresses;

    public EmailEnvelopeWithRecipients()
    {
        ccAddresses = new ArrayList<>();
        bccAddresses = new ArrayList<>();
        toAddresses = new ArrayList<>();
    }

    public EmailEnvelopeWithRecipients addCc(List<String> ccAddresses)
    {
        this.ccAddresses.addAll(ccAddresses);
        return this;
    }

    public EmailEnvelopeWithRecipients setCc(List<String> ccAddresses)
    {
        this.ccAddresses = ccAddresses;
        return this;
    }

    public List<String> getCc()
    {
        return ccAddresses;
    }

    public EmailEnvelopeWithRecipients addBcc(List<String> bccAddresses)
    {
        this.bccAddresses.addAll(bccAddresses);
        return this;
    }

    public EmailEnvelopeWithRecipients setBcc(List<String> bccAddresses)
    {
        this.bccAddresses = bccAddresses;
        return this;
    }

    public List<String> getBcc()
    {
        return bccAddresses;
    }

    public EmailEnvelopeWithRecipients addTo(List<String> toAddresses)
    {
        this.toAddresses.addAll(toAddresses);
        return this;
    }

    public EmailEnvelopeWithRecipients setTo(List<String> toAddresses)
    {
        this.toAddresses = toAddresses;
        return this;
    }

    public List<String> getTo()
    {
        return toAddresses;
    }
}

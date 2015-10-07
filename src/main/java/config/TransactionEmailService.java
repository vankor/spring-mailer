package config;

import com.cribbstechnologies.clients.mandrill.model.MandrillRecipient;
import com.cribbstechnologies.clients.mandrill.model.MergeVar;
import model.MailParams;
import model.MailSubscriber;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface TransactionEmailService {

    /**
     * sends an email with an activationLink to the user
     */
    Boolean sendSingleTemplatedMessage(String mandrillTemplate, MailParams params, MailSubscriber subscriber);

    Boolean sendMultiTemplatedMessage(String mandrillTemplate, MailParams params, Map<String, String> mailToName);

}
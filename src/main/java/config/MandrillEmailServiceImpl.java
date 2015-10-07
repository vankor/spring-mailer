package config;

import com.cribbstechnologies.clients.mandrill.exception.RequestFailedException;
import com.cribbstechnologies.clients.mandrill.model.*;
import com.cribbstechnologies.clients.mandrill.request.MandrillMessagesRequest;
import com.cribbstechnologies.clients.mandrill.request.MandrillRESTRequest;
import com.cribbstechnologies.clients.mandrill.util.MandrillConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import model.MailParams;
import model.MailSubscriber;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vankor on 9/18/15.
 */

@Service
public class MandrillEmailServiceImpl implements TransactionEmailService {

    private final Logger LOG = LoggerFactory.getLogger(MandrillEmailServiceImpl.class);

    private MandrillConfiguration config;
    private HttpClient httpClient;

    private final String apiKey = "3plGC5j05IoashwG7e5hfQ";
    private final String apiVersion = "1.0";
    private final String apiBaseUrl = "https://mandrillapp.com/api";
    private final Integer defaultSconnectionTimeout = 5000;
    private final Integer defaultSocketTimeout = 5000;



    @PostConstruct
    public void init() {
        config = new MandrillConfiguration();
        config.setApiKey(apiKey);
        config.setApiVersion(apiVersion);
        config.setBaseURL(apiBaseUrl);

        // setup HTTP client to use a timeout and no retries (so that user doesn't get double emails)
        Integer connectTimeout = defaultSconnectionTimeout;
        Integer socketTimeout = defaultSocketTimeout;

        RequestConfig requestConfig = RequestConfig.custom() //
                .setConnectTimeout(connectTimeout) //
                .setSocketTimeout(socketTimeout) //
                .build();

        httpClient = HttpClients.custom() //
                .setDefaultRequestConfig(requestConfig) //
                .disableAutomaticRetries() //
                .build();
    }

    public Boolean sendSingleTemplatedMessage(String templateNameKey, MailParams params, MailSubscriber subscriber) {

        MandrillTemplatedMessageRequest request = new MandrillTemplatedMessageRequest();
        MandrillMessage message = new MandrillMessage();
        if(params != null) {
            message.setFrom_email(params.getFromAdress());
            message.setFrom_name(params.getFromText());
            message.setSubject(params.getSubject());
        }
        Map<String, String> headers = new HashMap<String, String>();
        message.setHeaders(headers);

        MandrillRecipient[] recipients = {subscriber.getRecipient()};
        message.setTo(recipients);
        message.setTrack_clicks(true);
        message.setTrack_opens(true);
        request.setMessage(message);
        List<TemplateContent> content = new ArrayList<TemplateContent>();
        request.setTemplate_content(content);
        request.setTemplate_name(templateNameKey);
        message.setGlobal_merge_vars(subscriber.getMergeVars());

        try {
            LOG.debug("sending mail with Mandrill Template");
            sendEmail(request);
            LOG.debug("mail sent with Mandrill Template");
            return true;

        } catch (Throwable e) {
            LOG.error("failed to send templated message, adding it to the queue and try to send it again later!", e);
        }
        return false;
    }

    @Override
    public Boolean sendMultiTemplatedMessage(String mandrillTemplate, MailParams params, Map<String,String> recipientsMap) {
        List<MandrillRecipient> recipientsList = Lists.newArrayList();
        for(Map.Entry<String,String> entry : recipientsMap.entrySet()){
            recipientsList.add(new MandrillRecipient(entry.getValue(), entry.getKey()));
        }

        MandrillTemplatedMessageRequest request = new MandrillTemplatedMessageRequest();
        MandrillMessage message = new MandrillMessage();
        if(params != null) {
            message.setFrom_email(params.getFromAdress());
            message.setFrom_name(params.getFromText());
            message.setSubject(params.getSubject());
        }
        Map<String, String> headers = new HashMap<String, String>();
        message.setHeaders(headers);
        MandrillRecipient[] recipients = new MandrillRecipient[ recipientsList.size()];
        recipients =  recipientsList.toArray(recipients);
        message.setTo(recipients);
        message.setTrack_clicks(true);
        message.setTrack_opens(true);
        request.setMessage(message);
        List<TemplateContent> content = new ArrayList<TemplateContent>();
        request.setTemplate_content(content);
        request.setTemplate_name(mandrillTemplate);
        message.setGlobal_merge_vars(Lists.newArrayList());

        try {
            LOG.debug("sending mail with Mandrill Template");
            sendEmail(request);
            LOG.debug("mail sent with Mandrill Template");
            return true;

        } catch (Throwable e) {
            LOG.error("failed to send templated message, adding it to the queue and try to send it again later!", e);
        }
        return false;
    }

    private void sendEmail(MandrillTemplatedMessageRequest templatedMessage) {
        // send message to the mandrill rest api

        MandrillRESTRequest restRequest = new MandrillRESTRequest();
        restRequest.setConfig(config);
        restRequest.setHttpClient(httpClient);
        restRequest.setObjectMapper(new ObjectMapper());

        MandrillMessagesRequest messagesRequest = new MandrillMessagesRequest();
        messagesRequest.setRequest(restRequest);
        LOG.debug("MandrillRESTRequest created");

        try {
            messagesRequest.sendTemplatedMessage(templatedMessage);
            LOG.debug("MandrillRESTRequest sent");
        } catch (RequestFailedException e) {
            LOG.error("Error calling Mandrill API", e);
        }
    }
}

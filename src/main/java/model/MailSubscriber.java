package model;

import com.cribbstechnologies.clients.mandrill.model.MandrillRecipient;
import com.cribbstechnologies.clients.mandrill.model.MergeVar;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Andrey on 19.09.2015.
 */
public class MailSubscriber {

   private MandrillRecipient recipient;
   private List<MergeVar> mergeVars = Lists.newArrayList();

    public MandrillRecipient getRecipient() {
        return recipient;
    }

    public void setRecipient(MandrillRecipient recipient) {
        this.recipient = recipient;
    }

    public List<MergeVar> getMergeVars() {
        return mergeVars;
    }

    public void setMergeVars(List<MergeVar> mergeVars) {
        this.mergeVars = mergeVars;
    }
}

package config;

import com.cribbstechnologies.clients.mandrill.model.MandrillRecipient;
import com.cribbstechnologies.clients.mandrill.model.MergeVar;
import com.google.common.collect.BiMap;
import model.Column;
import model.MailParams;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrey on 19.09.2015.
 */
public interface ExcelImportService {


    /**
     * sends an email with an activationLink to the user
     */
    Map<String, String> getRecipientsFromFile(String filePath, BiMap<Column, String> colnames);

    Map<String, String> getRecipientsFromFile(File file, BiMap<Column, String> colnames);

    Map<String, String> getRecipientsFromFile(MultipartFile file, BiMap<Column, String> colnames);
}

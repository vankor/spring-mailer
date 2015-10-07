package config;

import com.cribbstechnologies.clients.mandrill.model.MandrillRecipient;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dto.ColumnDTO;
import dto.MailSendForm;
import model.Column;
import model.ColumnType;
import model.MailParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mail")
class MailController {

        @Autowired
        private TransactionEmailService transactionMailService;

        @Autowired
        private ExcelImportService excelService;

        @RequestMapping(method = RequestMethod.POST)
        public @ResponseBody
        ResponseEntity<Boolean> sendMail(@ModelAttribute MailSendForm mailForm) {
            BiMap<Column, String> colnames = HashBiMap.create();
            for(ColumnDTO columnDTO :mailForm.getColumns()){
                Column col = new Column();
                col.setType(ColumnType.findByName(columnDTO.getType()));
                colnames.put(col, columnDTO.getName());
            }
            Map<String, String> recipients = excelService.getRecipientsFromFile(mailForm.getFile(),colnames);
            MailParams params = null;
            Boolean isSended = transactionMailService.sendMultiTemplatedMessage(mailForm.getTemplate(), params, recipients);
            return new ResponseEntity<>(isSended, HttpStatus.OK);
        }



    }
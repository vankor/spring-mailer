
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.cribbstechnologies.clients.mandrill.model.MandrillRecipient;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import config.Application;
import model.Column;
import model.ColumnType;
import model.MailParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import config.ExcelImportService;
import config.ExcelImportServiceImpl;
import config.MandrillEmailServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class EmailTest {

    public static final String FILE_PATH = "C:\\mailer\\mails1.xls";

    @Test
    public void getHello() throws Exception {
        MandrillEmailServiceImpl transactionEmailService = new MandrillEmailServiceImpl();
        transactionEmailService.init();
        ExcelImportService importService = new ExcelImportServiceImpl();
        MailParams params = null;
        BiMap<Column, String> colnames = HashBiMap.create();
        Column col1 = new Column();
        col1.setType(ColumnType.EMAIL);
        Column col2 = new Column();
        col2.setType(ColumnType.NAME);
        colnames.put(col1, "E-mail");
        colnames.put(col2, "Name");
        List<MandrillRecipient> recipients = importService.getRecipientsFromFile(FILE_PATH, colnames);
//        Boolean isSended = transactionEmailService.sendMultiTemplatedMessage("FIRST TEMPLATE", params, recipients);
        assertThat(recipients.isEmpty(), equalTo(false));
    }
}
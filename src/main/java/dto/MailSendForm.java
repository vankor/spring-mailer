package dto;

import com.google.common.collect.Lists;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by vankor on 9/18/15.
 */
public class MailSendForm {

    private String template;
    private MultipartFile file;
    private List<ColumnDTO> columns = Lists.newArrayList();

    public String getTemplate() {
        return template;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<ColumnDTO> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDTO> columns) {
        this.columns = columns;
    }
}

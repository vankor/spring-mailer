package config;

import com.cribbstechnologies.clients.mandrill.model.MandrillRecipient;
import com.google.common.collect.BiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jxl.*;
import jxl.read.biff.BiffException;
import model.Column;
import model.ColumnType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Andrey on 19.09.2015.
 */
@Service
public class ExcelImportServiceImpl implements ExcelImportService {
    
    public static final Integer COLUMN_SCAN_COUNT = 100;

    java.util.Properties properties = System.getProperties();
    // get Operating System home directory
    String home = properties.get("user.home").toString();

    // get Operating System separator
    String separator = properties.get("file.separator").toString();
    
    @Override
    public Map<String, String> getRecipientsFromFile(String filePath, BiMap<Column, String> colnames) {
        File file = new File(filePath);
        return getRecipientsFromFile(file, colnames);
    }

    @Override
    public Map<String, String> getRecipientsFromFile(File file, BiMap<Column, String> colnames) {
        Workbook workbook = null;
        Map<String, String> recipients = Maps.newHashMap();
        try {
           workbook = Workbook.getWorkbook(file);
           Sheet sheet = workbook.getSheet(0);
            setColNumbers(sheet, colnames);
                Integer rownum = 1;
                String currentEmailValue = "";
                String currentNameValue = "";

                Map<ColumnType, List<Column>> colsByType =
                        colnames.keySet()
                            .stream()
                            .collect(
                                    Collectors.groupingBy(Column::getType));
                Integer rowCount = sheet.getRows();
                if(rowCount>1) {
                    do {
                        Map<Column, String> currentRow = new HashMap<>();

                        Column emailColumn = colsByType.get(ColumnType.EMAIL).get(0);
                        Column nameColumn = colsByType.get(ColumnType.NAME).get(0);
                        currentEmailValue = "";
                        currentNameValue = "";

                        if(emailColumn.getNumber() !=null) {
                            currentEmailValue = ((LabelCell) sheet.getCell(emailColumn.getNumber(), rownum)).getString();
                        }
                        if(nameColumn.getNumber() != null) {
                            currentNameValue = ((LabelCell) sheet.getCell(nameColumn.getNumber(), rownum)).getString();
                        }
                        if(currentEmailValue != ""){
                            MandrillRecipient mandrillRecipient = new MandrillRecipient(currentNameValue, currentEmailValue);
                            recipients.put(currentEmailValue, currentNameValue);
                        }

                        rownum++;
                    } while (rownum<rowCount);
                }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        return recipients;
    }

    @Override
    public Map<String, String> getRecipientsFromFile(MultipartFile multipartfile, BiMap<Column, String> colnames) {
        File file = null;
        Map<String, String> recipients = Maps.newHashMap();
        try {
            file = multipartToFile(multipartfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(file!=null){
            recipients = getRecipientsFromFile(file, colnames);
            file.delete();
        }

        return recipients;

    }

    private void setColNumbers(Sheet sheet, BiMap<Column, String> colnames){
        int col = 0;
        List<Integer> colnumbers = Lists.newArrayList();
        Integer colCount = sheet.getColumns();
        while(col<colCount){
            Cell current = null;
            current = sheet.getCell(col, 0);
            if (current.getType() == CellType.LABEL)
            {
                LabelCell lc = (LabelCell) current;
                String value = lc.getString();
                if(colnames.values().contains(value)){
                    Column column = colnames.inverse().get(value);
                    column.setNumber(current.getColumn());
                }
            }
            col++;
        }

    }

    private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException
    {
        File convDir = new File(home+separator+"tmp");
        convDir.mkdirs();
        File convFile = new File(convDir, multipart.getOriginalFilename());
        convFile.createNewFile();
        multipart.transferTo(convFile);
        return convFile;
    }





}

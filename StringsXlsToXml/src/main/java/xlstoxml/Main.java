package xlstoxml;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final String XML_FILE = "/home/zoli/workspace/paystation/src/main/res/values/strings.xml";
    private static final String XLS_FILE = "/home/zoli/Letöltések/Paystation_strings_DE_.xlsx";
    private static final String OUT_FILE = "/home/zoli/workspace/paystation/src/main/res/values-de/strings.xml";

    private static final boolean WRITE_OUT = false;
    private static final boolean PRINT_MISSING_KEYS = true;

    private static List<String> rowToList(Row row) {
        List<String> l = new ArrayList<String>();
        //For each row, iterate through all the columns
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String value = "";
            //Check the cell type and format accordingly
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    value = cell.getNumericCellValue() + "";
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
            }
            // Check value
            value = value.trim();
            if (value.equalsIgnoreCase("null")) value = "";
            // Store value
            l.add(value);
        }
        return l;
    }

    private static Map<String, String> readXls() throws Exception {
        FileInputStream file = new FileInputStream(new File(XLS_FILE));

        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        int line = 0, index;
        int keyIndex = -1, valueIndex = -1;
        String key, value;
        Map<String, String> values = new HashMap<String, String>();

        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            line++;
            Row row = rowIterator.next();

            index = 0;
            if (line == 1) {
                // Header
                for (String col : rowToList(row)) {
                    if (col.equalsIgnoreCase("Key")) {
                        keyIndex = index;
                    }
                    if (col.equalsIgnoreCase("Android")) {
                        valueIndex = index;
                    }
                    index++;
                }
            }
            else {
                // Content
                if (keyIndex == -1 || valueIndex == -1) {
                    System.exit(1);
                }
                key = value = null;
                for (String col : rowToList(row)) {
                    if (index == keyIndex) {
                        key = col;
                    }
                    if (index == valueIndex) {
                        value = col;
                    }
                    index++;
                }
                if (key != null && value != null) {
                    values.put(key, value);
                }
            }

        }
        file.close();
        return values;
    }

    private static Map<String, String> readXml(String file) throws Exception {
        Pattern pattern = Pattern.compile("<string\\s+name=\"(.*)\"\\s*>(.*)</string>");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Map<String, String> values = new LinkedHashMap<String, String>();
        String line;
        Matcher matcher;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                String key = matcher.group(1).trim();
                String value = matcher.group(2);
                values.put(key, value);
            }
        }
        reader.close();
        return values;
    }

    private static Map<String, String> read() throws Exception {
        Map<String,String> valuesNew = readXls();
        Map<String,String> valuesOld = readXml(XML_FILE);
        Map<String,String> valuesMerged = new LinkedHashMap<String, String>();
        Iterator<Map.Entry<String, String>> it = valuesOld.entrySet().iterator();
        String key, value, newValue;
        while (it.hasNext()) {
            Map.Entry<String,String> e = it.next();
            key = e.getKey();
            newValue = valuesNew.get(key);
            if (newValue != null && !newValue.isEmpty()) {
                value = newValue;
            }
            else {
                value = "*" + e.getValue();
            }
            valuesMerged.put(key, value);
        }
        return valuesMerged;
    }

    private static String generate() throws Exception {
        Map<String,String> values = read();
        Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        builder.append("<resources>\n");
        while (it.hasNext()) {
            Map.Entry<String,String> e = it.next();
            builder.append(String.format("\t<string name=\"%s\">%s</string>\n", e.getKey(), e.getValue()));
        }
        builder.append("</resources>");
        return builder.toString();
    }

    private static void write() throws Exception {
        if (!new File(XML_FILE).isFile() || !new File(XLS_FILE).isFile()) {
            System.err.println("Input file does not exist");
            return;
        }
        String content = generate();
        File outFile = new File(OUT_FILE);
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        Writer writer = new FileWriter(outFile, false);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private static void printMissingKeys() throws Exception {
        if (!new File(OUT_FILE).isFile()) {
            System.err.println("Output file does not exist");
            return;
        }
        Map<String, String> values = readXml(OUT_FILE);
        Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            Map.Entry<String, String> e = it.next();
            if (e.getValue().startsWith("*")) {
                if (count == 0) System.out.println("Missing keys:\n");
                System.out.println(e.getKey());
                count++;
            }
        }
        System.out.println(String.format("\nCount: %s / %s", count, values.size()));
    }

    public static void main(String[] args) throws Exception {
        if (WRITE_OUT) write();
        if (PRINT_MISSING_KEYS) printMissingKeys();
    }

}

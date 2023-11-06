import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipContentTests {
    private final static ClassLoader cl = ZipContentTests.class.getClassLoader();

    @Test
    void zipTest() throws IOException, CsvException {
        try (InputStream stream = cl.getResourceAsStream("TestZip.zip");
             ZipInputStream zis = new ZipInputStream(stream)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".pdf")) {
                    PDF pdf = new PDF(zis);
                    Assertions.assertTrue(pdf.text.contains("Test1 Test2 Test3"));
                } else if (entry.getName().contains(".xlsx")) {
                    XLS xls = new XLS(zis);
                    Assertions.assertEquals(
                            "Test1",
                            xls.excel.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
                } else if (entry.getName().contains(".csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = csvReader.readAll();
                    Assertions.assertEquals(3, content.size());
                    final String[] firstRow = content.get(0);
                    final String[] secondRow = content.get(1);
                    final String[] thirdRow = content.get(2);
                    Assertions.assertArrayEquals(new String[]{"Test1","1"}, firstRow);
                    Assertions.assertArrayEquals(new String[]{"Test2","2"}, secondRow);
                    Assertions.assertArrayEquals(new String[]{"Test3","3"}, thirdRow);

                }
            }
        }
    }
}

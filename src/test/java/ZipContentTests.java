import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipContentTests {
    private final static ClassLoader cl = ZipContentTests.class.getClassLoader();

    @Test
    void testPDFFileInZip() throws Exception {
        try (InputStream stream = cl.getResourceAsStream("TestZip.zip");
             ZipInputStream zis = new ZipInputStream(stream)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".pdf")) {
                    PDF pdf = new PDF(zis);
                    assertThat(pdf.text).contains("Test1 Test2 Test3");
                }
            }
        }
    }

    @Test
    void testExcelFileInZip() throws Exception {
        try (InputStream stream = cl.getResourceAsStream("TestZip.zip");
             ZipInputStream zis = new ZipInputStream(stream)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".xlsx")) {
                    XLS xls = new XLS(zis);
                    assertThat(xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue()).isEqualTo("Test1");
                }
            }
        }
    }

    @Test
    void testCSVFileInZip() throws Exception {
        try (InputStream stream = cl.getResourceAsStream("TestZip.zip");
             ZipInputStream zis = new ZipInputStream(stream)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = csvReader.readAll();
                    assertThat(content).hasSize(3);
                    assertThat(content.get(0)).containsExactly("Test1", "1");
                    assertThat(content.get(1)).containsExactly("Test2", "2");
                    assertThat(content.get(2)).containsExactly("Test3", "3");
                }
            }
        }
    }
}
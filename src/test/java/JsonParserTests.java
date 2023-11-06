import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.TestJson;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonParserTests {
    ObjectMapper mapper = new ObjectMapper();
    @Test
    public void jsonFileParseTest() throws Exception{

        File file = new File ("src/test/resources/Test.json");
        List<TestJson> testJsonList=mapper.readValue(file, new TypeReference<>() {});
        assertThat(testJsonList).hasSize(2);
        assertThat(testJsonList.get(0).getName()).isEqualTo("Adeel Solangi");
        assertThat(testJsonList.get(0).getLanguage()).isEqualTo("Sindhi");
        assertThat(testJsonList.get(0).getId()).isEqualTo("V59OF92YF627HFY0");
        assertThat(testJsonList.get(0).getBio()).isEqualTo("Donec lobortis eleifend condimentum. Cras dictum dolor lacinia lectus vehicula rutrum. Maecenas quis nisi nunc. Nam tristique feugiat est vitae mollis. Maecenas quis nisi nunc.");
        assertThat(testJsonList.get(0).getVersion()).isEqualTo("6.1");

    }
}

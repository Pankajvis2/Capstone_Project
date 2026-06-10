package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVUtility {
    public static Object[][] readCsv(String path) {
        List<Object[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to read CSV file: " + path, e);
        }
        return rows.toArray(new Object[0][]);
    }
}

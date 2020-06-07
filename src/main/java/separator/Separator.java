package separator;

import java.util.ArrayList;
import java.util.List;

public class Separator implements SeparatorInterface {
    private static final String COMA;

    static {
        COMA = ",";
    }

    public List<String> separate(String text) {
        List<String> result = new ArrayList<>();
        String[] words = text.split(COMA);
        for (int i = 0; i < words.length; i++) {
            String word = words[i].trim();
            result.add(word);
        }
        return result;
    }
}

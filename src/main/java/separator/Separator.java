package separator;

import java.util.ArrayList;
import java.util.List;

public class Separator implements SeparatorInterface {
    public List<String> separate(String text, String sign) {
        List<String> result = new ArrayList<>();
        String[] words = text.split(sign);
        for (int i = 0; i < words.length; i++) {
            String word = words[i].trim();
            result.add(word);
        }
        return result;
    }
}

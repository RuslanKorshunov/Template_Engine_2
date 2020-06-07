package stemmer;

import stemmer.entity.StemmerEntity;

import java.util.List;

public interface Stemmer {
    List<StemmerEntity> stem(String text, boolean append);
}
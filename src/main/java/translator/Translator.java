package translator;

import com.ibm.icu.text.Transliterator;

public class Translator implements TranslatorInterface {
    private static final String CYRILLIC_TO_LATIN;

    static {
        CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
    }

    @Override
    public String translate(String russianText) {
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        return toLatinTrans.transliterate(russianText);
    }
}

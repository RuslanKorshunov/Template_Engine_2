package stemmer.entity;

import java.util.Arrays;

public class StemmerEntity {
    private String word;
    private String[] values;

    public StemmerEntity(String word, String... values) {
        this.word = word;
        this.values = values;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String[] getValues() {
        return values;
    }

    public String getValue(int index) {
        return values[index];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StemmerEntity entity = (StemmerEntity) o;
        return Arrays.equals(values, entity.values);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < values.length; i++) {
            hash += values[i].hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return word + " - " + Arrays.toString(values);
    }
}

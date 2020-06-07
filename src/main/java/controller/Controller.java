package controller;

import java.util.List;
import java.util.Map;

public interface Controller {
    Map<String, String> parseData(List<String> values, int mode);

    Map<String, List<String>> getData(String id);

    void writeInScs();
}

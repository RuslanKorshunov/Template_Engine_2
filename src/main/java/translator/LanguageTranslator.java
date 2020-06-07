package translator;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

public class LanguageTranslator {
    private static final String URL;
    private static final String EN;
    private static final String POST_REQUEST;
    private static final String UTF;

    static {
        URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20150627T071448Z.117dacaac1e63b79.6b1b4bb84635161fcd400dace9fb2220d6f344ef";
        EN = "en";
        POST_REQUEST = "POST";
        UTF = "UTF-8";
    }

    public static String translate(String lang, String input) throws IOException {
        String urlStr = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20200321T131941Z.4b27121967eac435.1c55aee3acbd22aa1ed60319ac43907b31aaea1b";
        URL urlObj = new URL(urlStr);
        HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes("text=" + URLEncoder.encode(input, "UTF-8") + "&lang=" + lang);

        InputStream response = connection.getInputStream();
        String json = new java.util.Scanner(response).nextLine();
        int start = json.indexOf("[");
        int end = json.indexOf("]");
        String translated = json.substring(start + 2, end - 1);
        if (translated.equals(input)) {
            // if return equal of entered text - we need change direction of translation
            return translate("en", input);
        } else return translated;
    }
}

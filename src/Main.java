import com.sun.istack.internal.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        String url = "https://www.mvideo.ru/products/televizor-samsung-ue43n5000au-10018821";
        try {
            String html = downloadFromUrl(url);
            Document document = Jsoup.parse(html);
            Integer ga = getAllShops(document.text());
            out.println(ga);
            Boolean pick = isPickUp(document.text());
            out.println(pick);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String downloadFromUrl(String url) throws IOException {
        StringBuilder s = new StringBuilder();
        InputStream is = new URL(url).openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        boolean value = true;
        while (value) {
            inputLine = br.readLine();
            if (inputLine == null) {
                value = false;
            } else {
                s.append(inputLine);
            }
        }
        br.close();
        is.close();
        return s.toString();
    }

    private static int getAllShops(@NotNull String text) {
        try {
            int shops1 = toInt(firstMatch("из\\s+(\\d+)\\s+магазин(?:а|ов)", text));
            int shops2 =
                    toInt(firstMatch("(?:и)?\\s*из\\s+(\\d+)\\s+(?:позже|завтра|послезавтра)", text));
            return shops1 + shops2;
        } catch (Exception e) {
            return 0;
        }
    }

    private static int toInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            out.println(e);
            return 0;
        }
    }

    private static boolean isPickUp(@NotNull String text) {
        Matcher matcher =
                Pattern.compile("Доставка.*(завтра|начиная|сегодня)\\s+(с|и\\s+позже)").matcher(text);
        return matcher.find();
    }

    private static String firstMatch(@NotNull String regExp, @NotNull String text) {
        Matcher matcher = Pattern.compile(regExp).matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}

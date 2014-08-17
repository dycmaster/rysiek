package dycmaster.rysiek.logicService;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DefaultHtmlSender implements IHtmlSender {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DefaultHtmlSender.class);

    @Override
    public int senGet(String url) throws IOException {
        URL obj = new URL(url);
        log.info("\nSending 'GET' request to URL : " + url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode;
        try {
            responseCode = con.getResponseCode();
        }catch (Exception e){
            responseCode = -1;
        }
        if(responseCode>=200 && responseCode<300) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            log.debug("response is: " + response.toString());
        }
        return responseCode;
    }
}

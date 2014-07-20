package dycmaster.rysiek.logicService;


import java.io.IOException;
import java.net.MalformedURLException;

public interface IHtmlSender {
    int senGet(String url) throws IOException;
}

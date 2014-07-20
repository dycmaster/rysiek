package dycmaster.rysiek.logicService;



public class TestHtmlSender implements IHtmlSender {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TestHtmlSender.class);

    public int senGet(String url) {
        log.info("get would be send to: "+url);
        return 0;
    }
}

package dycmaster.rysiek.main;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;


public class Main {

    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";
    private static final String CONFIG_LOCATION = "dycmaster.rysiek.config";
    private static final String DEFAULT_PROFILE = "dev";

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.startJetty(8080);
    }

    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        ServletHolder dispatcher = new ServletHolder(new DispatcherServlet(context));
        dispatcher.setInitOrder(1);
        contextHandler.addServlet(dispatcher, MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(context));
//        contextHandler.setResourceBase(new ClassPathResource("webapp").getURI().toString());
        return contextHandler;
    }

    private static WebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);

        //context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE);
        return context;
    }

    private void startJetty(int port) throws Exception {
        Server server = new Server(port);
        ServletContextHandler servletContextHandler = getServletContextHandler(getContext());

        ServletHolder jerseyServlet = servletContextHandler.addServlet(com.sun.jersey.spi.spring.container.servlet.SpringServlet.class,
                "/processors/logicService1/*");
        jerseyServlet.setInitOrder(2);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "dycmaster.rysiek");
//        jerseyServlet.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
//        jerseyServlet.setInitParameter("contextConfigLocation", "dycmaster.rysiek.Config");
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }

}

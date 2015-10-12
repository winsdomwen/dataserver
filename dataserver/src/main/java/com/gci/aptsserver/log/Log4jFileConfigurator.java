package com.gci.aptsserver.log;


import java.net.URL;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.varia.ReloadingPropertyConfigurator;
 
public class Log4jFileConfigurator extends ReloadingPropertyConfigurator {
     
    PropertyConfigurator delegate = new PropertyConfigurator();
 
    @Override
    public void doConfigure(URL url, LoggerRepository repository) {
        delegate.configureAndWatch(url.getFile(), 1000);
    }
}
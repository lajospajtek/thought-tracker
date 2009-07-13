package logrunner1;

import org.apache.log4j.Logger;
import org.apache.log4j.ExtendedPropertyConfigurator;

/**
 */
public class Main {

  private static final Logger logger = Logger.getLogger(Main.class);

  public static void main(String[] args) {
    ExtendedPropertyConfigurator.configure("l02.properties");
    logger.debug("msg: debug");
    logger.info("msg: info");
    logger.warn("msg: PATTERN");
    logger.error("msg: warn");
    logger.fatal("msg: fatal");
  }
}

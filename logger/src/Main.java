import logger.LoggerClient;
import logger.impl.LoggerClientImpl;

public class Main {

    public static void main(String[] args) {
        final LoggerClient logger = new LoggerClientImpl();
        logger.start("1", System.currentTimeMillis());
        logger.print();
        logger.start("3", System.currentTimeMillis());
        logger.print();
        logger.end("1", System.currentTimeMillis());
        logger.start("2", System.currentTimeMillis());
        logger.end("2", System.currentTimeMillis());
        logger.end("3", System.currentTimeMillis());
        logger.print();
        logger.print();
        logger.print();
    }
}

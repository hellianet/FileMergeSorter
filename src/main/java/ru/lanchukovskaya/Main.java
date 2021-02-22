package ru.lanchukovskaya;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lanchukovskaya.sorter.Sorter;
import ru.lanchukovskaya.sorter.config.Config;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        try {
            ArgumentParser parser = new ArgumentParser();
            Config config = parser.parse(args);
            Sorter sorter = new Sorter(config);
            sorter.sort();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            System.out.println("Unexpected error");
        }

    }


}

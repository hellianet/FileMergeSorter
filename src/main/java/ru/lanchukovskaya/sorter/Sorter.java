package ru.lanchukovskaya.sorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lanchukovskaya.sorter.config.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Sorter {
    private static final Logger logger = LoggerFactory.getLogger(Sorter.class);

    private final Config config;
    private final Map<BufferedReader, InputData> data;
    private final Map<BufferedReader, String> fileNames;

    public Sorter(Config config) {
        this.config = Objects.requireNonNull(config);
        this.data = new HashMap<>(config.getFileNamesIn().size());
        this.fileNames = new HashMap<>(config.getFileNamesIn().size());
    }

    public void sort() {
        List<BufferedReader> readers = openAllInputFiles();
        for (BufferedReader reader : readers) {
            updateValue(reader, () -> {data.remove(reader); closeFile(reader);});
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(config.getFileNameOut()))) {
            runSorting(writer);
        } catch (IOException e) {
            logger.error("Error with output file={}", config.getFileNameOut(), e);
            notifyUser("Error with output file='" + config.getFileNameOut() + "'");
        }
    }

    private void runSorting(BufferedWriter writer) throws IOException {
        while (!data.isEmpty()) {
            InputData nextOutputValue = getNextOutputValue(data);
            for (Iterator<BufferedReader> iterator = data.keySet().iterator(); iterator.hasNext();) {
                BufferedReader reader = iterator.next();
                if (nextOutputValue.equals(data.get(reader))) {
                    writer.write(nextOutputValue + System.lineSeparator());
                    updateValue(reader, () -> {iterator.remove(); closeFile(reader);});
                }
            }
            writer.flush();
        }
    }

    private void updateValue(BufferedReader reader, Runnable errorCallback) {
        readLine(reader).ifPresentOrElse(s -> {
                    if (newValue(reader, s).isEmpty()){
                        errorCallback.run();
                    }
                },
                errorCallback);
    }


    private <T extends Comparable<T>> T getNextOutputValue(Map<BufferedReader, T> map) {
        switch (config.getSortOrder()){
            case ASCENDING: return Collections.min(map.values());
            case DESCENDING: return Collections.max(map.values());
            default: throw new AssertionError("Unknown order");
        }
    }

    private Optional<InputData> newValue(BufferedReader reader, String str) {
        try {
            InputData curValue = new InputData(str, config.getType());
            data.putIfAbsent(reader, curValue);
            InputData prevValue = data.get(reader);
            if (isWrongSortOrder(prevValue, curValue)) {
                logger.warn("Wrong sorting order in file= '{}', actual={}, prevValue={}, curValue={}",
                        fileNames.get(reader), config.getSortOrder(), prevValue,curValue);
                notifyUser("Wrong sorting order in file= '" +  fileNames.get(reader) +"'. File will be close");
                return Optional.empty();
            }
            data.put(reader, curValue);
            return Optional.of(curValue);
        } catch (IllegalArgumentException e) {
            logger.warn("Data type is integer, but received string='{}' from file='{}'", str,fileNames.get(reader), e);
            notifyUser("Data type is integer, but received string='" + str + "' from file='" + fileNames.get(reader) + "'. File will be close");
            return Optional.empty();
        }
    }

    private boolean isWrongSortOrder(InputData prevValue, InputData curValue){
        return config.getSortOrder().compare(prevValue, curValue) > 0;
    }

    private void closeFile(BufferedReader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            logger.warn("Closing error with file='{}'",fileNames.get(reader), e);
        }
        fileNames.remove(reader);
    }

    private List<BufferedReader> openAllInputFiles() {
        List<BufferedReader> readers = new ArrayList<>(config.getFileNamesIn().size());
        for (String fileName : config.getFileNamesIn()) {
            openFile(fileName).ifPresent(reader -> {
                readers.add(reader);
                fileNames.put(reader,fileName);
            });
        }
        return readers;
    }

    private Optional<String> readLine(BufferedReader reader) {
        try {
            return Optional.ofNullable(reader.readLine());
        } catch (IOException e) {
            logger.warn("Error while file = '{}' reading", fileNames.get(reader), e);
            notifyUser("Error while file = '" + fileNames.get(reader) + "' reading");
        }
        return Optional.empty();
    }



    private Optional<BufferedReader> openFile(String fileName) {
        try {
            return Optional.of(Files.newBufferedReader(Paths.get(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.warn("The file='{}' didn't open", fileName, e);
            notifyUser("The file='" + fileName + "' didn't open, so it won't end up in the output file");
        }
        return Optional.empty();
    }

    private void notifyUser(String message){
        System.out.println(message);
    }
}
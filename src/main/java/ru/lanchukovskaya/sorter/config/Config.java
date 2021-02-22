package ru.lanchukovskaya.sorter.config;

import java.util.List;
import java.util.Objects;

public class Config {
    private final DataType type;
    private final SortOrder sortOrder;
    private final String fileNameOut;
    private final List<String> fileNamesIn;


    public Config(DataType type, SortOrder sortOrder, String fileNameOut, List<String> fileNamesIn) {
        this.type = Objects.requireNonNull(type);
        this.sortOrder = Objects.requireNonNull(sortOrder);
        this.fileNameOut = Objects.requireNonNull(fileNameOut);
        this.fileNamesIn = List.copyOf(Objects.requireNonNull(fileNamesIn));
    }

    public DataType getType() {
        return type;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public String getFileNameOut() {
        return fileNameOut;
    }

    public List<String> getFileNamesIn() {
        return fileNamesIn;
    }
}


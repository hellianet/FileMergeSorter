package ru.lanchukovskaya;

import ru.lanchukovskaya.sorter.config.Config;
import ru.lanchukovskaya.sorter.config.DataType;
import ru.lanchukovskaya.sorter.config.SortOrder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ArgumentParser {
    private static final int MIN_COUNT_ARGUMENT = 3;

    private static final String ASCENDING = "-a";
    private static final String DESCENDING = "-d";
    private static final String STR_DATA = "-s";
    private static final String INT_DATA = "-i";

    public Config parse(String[] args) {
        validateArgumentsAmount(args);

        int index = 0;
        Optional<SortOrder> optionalOrder = parseOrder(args[index]);
        SortOrder sortMode = optionalOrder.orElse(SortOrder.ASCENDING);
        if (optionalOrder.isPresent()) {
            index++;
        }

        DataType type = parseDataType(args[index]);

        String fileNameOut = args[++index];

        List<String> fileNamesIn = parseInputFileNames(args, ++index);

        return new Config(type, sortMode, fileNameOut, fileNamesIn);
    }

    private void validateArgumentsAmount(String[] args) {
        Objects.requireNonNull(args, "Args is null");
        if (args.length < MIN_COUNT_ARGUMENT) {
            throw new IllegalArgumentException("Wrong count of arguments. Expected >= " + MIN_COUNT_ARGUMENT + ", actual = " + args.length);
        }
    }

    private List<String> parseInputFileNames(String[] args, int startIndex) {
        if (args.length - startIndex < 1) {
            throw new IllegalArgumentException("No input files");
        }
        return Arrays.asList(Arrays.copyOfRange(args, startIndex, args.length));
    }

    private DataType parseDataType(String arg) {
        switch (arg) {
            case INT_DATA: return DataType.INT_DATA;
            case STR_DATA: return DataType.STR_DATA;
            default: throw new IllegalArgumentException("No data type argument");
        }
    }

    private Optional<SortOrder> parseOrder(String arg) {
        switch (arg) {
            case ASCENDING: return Optional.of(SortOrder.ASCENDING);
            case DESCENDING: return Optional.of(SortOrder.DESCENDING);
            default: return Optional.empty();
        }
    }
}

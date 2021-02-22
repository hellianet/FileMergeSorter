package ru.lanchukovskaya.sorter;

import ru.lanchukovskaya.sorter.config.DataType;

import java.util.Objects;
import java.util.OptionalInt;

public class InputData implements Comparable<InputData>{
    private final String dataString;
    private Integer dataInt;
    private final DataType dataType;

    public InputData(String dataString, DataType dataType) {
        this.dataString = Objects.requireNonNull(dataString);
        this.dataType = Objects.requireNonNull(dataType);
        tryParseInteger(dataString, dataType);
    }

    private void tryParseInteger(String str, DataType type){
        if(type == DataType.INT_DATA){
            try {
                dataInt = Integer.parseInt(str,10);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("String is not integer", e);
            }
        }
    }

    public String getDataString() {
        return dataString;
    }

    public OptionalInt getDataInt() {
        return dataInt == null ? OptionalInt.empty() : OptionalInt.of(dataInt);
    }

    public DataType getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputData inputData = (InputData) o;
        return Objects.equals(dataString, inputData.dataString) && Objects.equals(dataInt, inputData.dataInt) && dataType == inputData.dataType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataString, dataInt, dataType);
    }

    @Override
    public int compareTo(InputData o) {
        if (dataType != o.dataType){
            throw new IllegalArgumentException("Data types are not equals");
        }
        switch (dataType){
            case INT_DATA: return dataInt.compareTo(o.dataInt);
            case STR_DATA: return dataString.compareTo(o.dataString);
            default:  throw new AssertionError("Unknown data type");
        }

    }

    @Override
    public String toString() {
        return dataString;
    }
}

package ru.lanchukovskaya.sorter.config;

public enum SortOrder {
    ASCENDING {
        public  <T extends Comparable<T>> int compare(T o1, T o2){
            return o1.compareTo(o2);
        }

    },
    DESCENDING{
        public  <T extends Comparable<T>> int compare(T o1, T o2){
            return o2.compareTo(o1);
        }

    };

    public abstract <T extends Comparable<T>> int compare(T o1, T o2);
}

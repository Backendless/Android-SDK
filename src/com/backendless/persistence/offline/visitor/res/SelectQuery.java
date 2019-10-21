package com.backendless.persistence.offline.visitor.res;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SelectQuery implements QueryPart {
    public Set<Field> selectList;
    public Table from;
    public Condition condition;
    public List<Field> groupBy;
    public Condition having;
    public List<SortField> orderBy;
    public Integer limit;
    public Integer offset;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT distinct ")
        .append(listToString(selectList))
        .append("\nFROM ")
        .append(from);

        if (condition != null)
            builder.append("\nWHERE ").append(condition);
        if (groupBy != null)
            builder.append("\nGROUP BY ").append(listToString(groupBy));

        if (having != null)
            builder.append("\nHAVING ").append(having);
//        if (orderBy != null)
//
//        if (limit != null)


        return builder.toString();
    }

    private String listToString(Collection<Field> list) {
        return list
            .stream()
            .map(new Function<Field, String>() {
                @Override
                public String apply(Field field) {
                    return field.getName();
                }
            })
            .collect(Collectors.joining(", "));
    }
}

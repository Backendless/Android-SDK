package com.backendless.persistence.offline;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

public class Transaction implements Serializable {
    private final String methodName;
    private final Object[] args;
    private final String id;
    private final String className;

    public Transaction(String methodName, Object[] args, String className) {
        this(methodName, args, UUID.randomUUID().toString(), className);
    }

    public Transaction(String methodName, Object[] args, String id, String className) {
        this.methodName = methodName;
        this.args = args;
        this.id = id;
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "methodName='" + methodName + '\'' +
            ", args=" + Arrays.toString(args) +
            ", id='" + id + '\'' +
            ", className='" + className + '\'' +
            '}';
    }
}

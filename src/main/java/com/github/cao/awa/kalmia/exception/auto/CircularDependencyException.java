package com.github.cao.awa.kalmia.exception.auto;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.util.List;

public class CircularDependencyException extends RuntimeException {
    public CircularDependencyException(List<String> configChain, String className) {
        super(formatErrorMessage(configChain,
                                 className
        ));
    }

    public CircularDependencyException(List<String> configChain, String className, Throwable cause) {
        super(formatErrorMessage(configChain,
                                 className
              ),
              cause
        );
    }

    protected CircularDependencyException(List<String> configChain, String className, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(formatErrorMessage(configChain,
                                 className
              ),
              cause,
              enableSuppression,
              writableStackTrace
        );
    }

    private static String formatErrorMessage(List<String> configChain, String className) {
        StringBuilder builder = new StringBuilder();
        builder.append("The '")
               .append(className)
               .append("' has dependency by itself, cannot be processes: ")
               .append("\n");
        builder.append("Dependencies:\n");
        builder.append("\t/-> ")
               .append(className);
        List<String> traces = ApricotCollectionFactor.arrayList();
        for (int i = configChain.size() - 1; i >= 0; i--) {
            String name = configChain.get(i);
            if (name.equals(className)) {
                break;
            }
            traces.add(name);
        }
        if (traces.isEmpty()) {
            traces.add(className);
        }
        int maxIndex = traces.size() - 1;
        for (int i = 0; i <= maxIndex; i++) {
            builder.append("\n");
            if (i == maxIndex) {
                builder.append("\t")
                       .append("|       |")
                       .append("\n");
                builder.append("\t")
                       .append("|       V")
                       .append("\n");
                builder.append("\t\\-< ")
                       .append(traces.get(i));
            } else {
                builder.append("\t")
                       .append("|       |")
                       .append("\n");
                builder.append("\t")
                       .append("|       V")
                       .append("\n");
                builder.append("\t|   ")
                       .append(traces.get(i));
            }
        }
        builder.append("\nTraces:");
        return builder.toString();
    }
}

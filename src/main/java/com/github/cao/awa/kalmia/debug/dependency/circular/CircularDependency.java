package com.github.cao.awa.kalmia.debug.dependency.circular;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.exception.auto.CircularDependencyException;

import java.util.Map;
import java.util.Stack;

public class CircularDependency {
    private final Map<Object, RequiredDependency> dependencies = ApricotCollectionFactor.hashMap();

    public void pushRequirement(Object target, RequiredDependency dependency) {
        if (this.dependencies.containsKey(target)) {
            RequiredDependency oldDep = this.dependencies.get(target);
            dependency.forEach(oldDep :: add);
        }
        this.dependencies.put(target,
                              dependency
        );

        checkCircular(target,
                      ApricotCollectionFactor.stack(),
                      false
        );
    }

    public void checkCircular(Object target, Stack<Object> o, boolean inner) {
        if (! this.dependencies.containsKey(target)) {
            return;
        }
        this.dependencies.get(target)
                         .forEach(required -> {
                             o.push(required);
                             Map<Object, Integer> map = ApricotCollectionFactor.hashMap();
                             for (Object object : o) {
                                 map.put(object,
                                         map.getOrDefault(object,
                                                          0
                                         ) + 1
                                 );

                                 if (map.get(object) == 2) {
                                     o.pop();
                                     throw new CircularDependencyException(o.stream()
                                                                            .map(Object :: toString)
                                                                            .toList(),
                                                                           object.toString()
                                     );
                                 }
                             }
                             checkCircular(required,
                                           o,
                                           true
                             );
                             if (inner) {
                                 o.pop();
                             }
                         });
    }
}

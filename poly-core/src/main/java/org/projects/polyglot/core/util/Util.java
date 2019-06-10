package org.projects.polyglot.core.util;

import java.util.Collections;
import java.util.List;

public class Util {

    public static <E> List<E> emptyIfNull(List<E> input) {
        return input != null ? input : Collections.emptyList();
    }

}

package org.projects.polyglot.core.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PriorityService {

    public List<Integer> getListOfPriorities() {
        return Arrays.asList(1, 2, 3, 4 ,5);
    }
}

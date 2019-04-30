package org.projects.polyglot.core.service;

import org.projects.polyglot.core.domain.WordType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordTypeService {

    public List<String> getAllWordTypes() {
        return Arrays.stream(WordType.values()).map(w -> w.toString()).collect(Collectors.toList());
    }
}

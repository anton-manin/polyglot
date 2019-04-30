package org.projects.polyglot.core.service;

import org.projects.polyglot.core.domain.Languages;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LanguageService {

    public List<String> getAllLanguages() {
        return Arrays.stream(Languages.values()).map(l -> l.toString()).collect(Collectors.toList());
    }
}

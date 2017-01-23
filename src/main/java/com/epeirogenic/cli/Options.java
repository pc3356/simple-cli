package com.epeirogenic.cli;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by philipcoates on 2017-01-20T12:34
 */
public class Options {

    private final Set<Option> options;

    public Options() {
        this.options = new HashSet<>();
    }

    public Options addOption(final Option option) {
        options.add(option);
        return this;
    }

    Map<String, Option> asMap() {

        final Map<String, Option> optionMap = new HashMap<>();
        for(final Option option : options) {
            final String arg = (option.getArg().startsWith("-") ? "" : "-") + option.getArg();
            optionMap.put(option.getArg(), option);
        }
        return optionMap;
    }
}

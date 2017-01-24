package com.epeirogenic.cli;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by philipcoates on 2017-01-20T12:22
 */
public class CommandLine {

    private final Map<String, String> values = new HashMap<>();
    private final Set<String> keys = new HashSet<>();
//    private final Map<String, String> defaults = new HashMap<>();

    private CommandLine() {}

//    void put(final String key, final String value, final String defaultValue) {
//
//        values.put(key, value);
//        if(defaultValue != null) {
//            defaults.put(key, defaultValue);
//        }
//    }

    void putKey(final String key) {
        keys.add(key);
    }

    void put(final String key, final String value) {
        keys.add(key);
        values.put(key, value);
    }

    public boolean hasOption(final String key) {
        return values.containsKey(key);
    }

    public Set<String> getArgumentsList() {
        return Collections.unmodifiableSet(keys);
    }

    public Map<String, String> getArguments() {
        return Collections.unmodifiableMap(values);
    }

    public String getOptionValue(final String key, final String defaultValue) {

        if(hasOption(key)) {
            return values.get(key);
        } else if(defaultValue != null) {
            return defaultValue;
        }

        return null;
    }

    public static CommandLine parse(final Options options, final String[] args) throws CLIParseException {

        final CommandLine commandLine = new CommandLine();
        if(args.length > 0) {

            final Map<String, Option> optionMap = options.asMap();

            int i = 0;
            while (i < args.length) {

                final String key = args[i++];

                if (key.startsWith("-")) {

                    final String arg = key.substring(1);
                    if (!optionMap.containsKey(arg)) {
                        throw new CLIParseException(arg + ": unexpected / unknown argument");
                    }

                    final Option option = optionMap.get(arg);
                    // args length - ignore for now and treat as always either one or zero
                    if(option.hasArgs()) {

                        final String value = args[i++];
                        commandLine.put(arg, value);

                    } else {

                        commandLine.putKey(arg);
                    }

                } else {
                    throw new CLIParseException(key + ": doesn't seem to be a valid argument - should start with '-'");
                }
            }

            for(final Option option : optionMap.values()) {

                if(option.required()) {
                    if(!commandLine.getArgumentsList().contains(option.getArg())) {
                        throw new CLIParseException("Required option '" + option.getArg() + "' missing");
                    }
                }
            }
        }
        return commandLine;
    }
}

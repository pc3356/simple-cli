package com.epeirogenic.cli;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by philipcoates on 2017-01-20T13:36
 */
public class CommandLineTest {

    @Test
    public void should_return_empty_command_line_with_no_args() throws Exception {

        final String[] args = {};
        final Options options = new Options();

        final CommandLine commandLine = CommandLine.parse(options, args);

        assertThat(commandLine.getArgumentsList().size(), is(0));
        assertThat(commandLine.getArguments().size(), is(0));
    }

    @Test
    public void should_be_ok_with_no_arg_options() throws Exception {

        final String[] args = {"-abc"};
        final Options options = new Options()
                .addOption(Option.builder().arg("abc").required().hasArgs(false).build());

        final CommandLine commandLine = CommandLine.parse(options, args);
        assertThat(commandLine.getArgumentsList().size(), is(1));
        assertThat(commandLine.getArguments().size(), is(0));
        assertTrue(commandLine.getArgumentsList().contains("abc"));
    }

    @Test
    public void more_complex_example() throws Exception {

        final String[] args = {
            "-abc",
            "value1",
            "-def",
            "value2",
            "-noargflag",
            "-ghi",
            "value3"
        };

        final Options options = new Options()
                .addOption(Option.builder().arg("abc").required().build())
                .addOption(Option.builder().arg("def").required().build())
                .addOption(Option.builder().arg("noargflag").required().hasArgs(false).build())
                .addOption(Option.builder().arg("ghi").required().build());

        final CommandLine commandLine = CommandLine.parse(options, args);

        assertThat(commandLine.getArgumentsList().size(), is(4));
        assertThat(commandLine.getArguments().size(), is(3));

        assertTrue(commandLine.getArguments().containsKey("abc"));
        assertTrue(commandLine.getArguments().containsKey("def"));
        assertTrue(commandLine.getArguments().containsKey("ghi"));

        assertTrue(commandLine.getArgumentsList().contains("abc"));
        assertTrue(commandLine.getArgumentsList().contains("def"));
        assertTrue(commandLine.getArgumentsList().contains("noargflag"));
        assertTrue(commandLine.getArgumentsList().contains("ghi"));

    }
}
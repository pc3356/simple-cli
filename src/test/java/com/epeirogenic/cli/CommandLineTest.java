package com.epeirogenic.cli;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by philipcoates on 2017-01-20T13:36
 */
public class CommandLineTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

        assertMultipleArgs(args);
    }

    @Test
    public void order_of_args_is_immaterial() throws Exception {

        final String[] args = {
                "-def",
                "value2",
                "-abc",
                "value1",
                "-ghi",
                "value3",
                "-noargflag"
        };

        assertMultipleArgs(args);
    }

    @Test
    public void extra_arg_causes_exception() throws Exception {

        expectedException.expect(CLIParseException.class);
        expectedException.expectMessage("def: unexpected / unknown argument");

        final Options options = new Options()
                .addOption(Option.builder().arg("abc").required().build());

        final String[] args = {
                "-abc",
                "value1",
                "-def",
                "value2"
        };

        CommandLine.parse(options, args);
        fail("Should have thrown a parsing exception");
    }

    @Test
    public void missing_required_argument_triggers_exception() throws Exception {

        expectedException.expect(CLIParseException.class);
        expectedException.expectMessage("Required option 'def' missing");

        final Options options = new Options()
                .addOption(Option.builder().arg("abc").required().build())
                .addOption(Option.builder().arg("def").required().build());

        final String[] args = {
                "-abc",
                "value1"
        };

        CommandLine.parse(options, args);
        fail("Should have thrown a parsing exception");
    }

    @Test
    public void arguments_should_start_with_hyphen() throws Exception {

        expectedException.expect(CLIParseException.class);
        expectedException.expectMessage("abc: doesn't seem to be a valid argument - should start with '-'");

        final String[] args = {
                "abc",
                "value1"
        };

        final Options options = new Options()
                .addOption(Option.builder().arg("abc").required().build())
                .addOption(Option.builder().arg("def").required().build());

        CommandLine.parse(options, args);
        fail("Should have thrown a parsing exception");
    }

    @Test
    public void should_return_default_value() throws Exception {

        final Options options = new Options()
                .addOption(Option.builder().arg("abc").build());

        final String[] args = {};

        final CommandLine commandLine = CommandLine.parse(options, args);

        final String expected = "abc";

        final String actual = commandLine.getOptionValue("a", "abc");

        assertThat(actual, is(expected));
    }

    @Test
    public void should_not_return_default_value() throws Exception {

        final Options options = new Options()
                .addOption(Option.builder().arg("a").build());

        final String[] args = {"-a", "123"};

        final CommandLine commandLine = CommandLine.parse(options, args);

        final String expected = "123";

        final String actual = commandLine.getOptionValue("a", "abc");

        assertThat(actual, is(expected));
    }

    private void assertMultipleArgs(final String ... args) throws Exception {

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
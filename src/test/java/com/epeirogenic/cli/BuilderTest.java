package com.epeirogenic.cli;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by philipcoates on 2017-01-23T18:25
 */
public class BuilderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void builder_should_fail_without_arg() throws Exception {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Must have at least one argument");

        Option.builder().build();
        fail("Should have thrown an IllegalArgumentException");
    }

    @Test
    public void builder_should_be_ok_with_arg() throws Exception {

        Option.builder().arg("abc").build();
    }

    @Test
    public void builder_should_be_ok_with_longOpt() throws Exception {

        Option.builder().longOpt("abc").build();
    }

    @Test
    public void hasArgs_should_toggle_args() throws Exception {

        final Option option1 = Option.builder().arg("a").build();
        assertThat(option1.getArgs(), is(1));

        final Option option2 = Option.builder().arg("a").hasArgs(false).build();
        assertThat(option2.getArgs(), is(0));
        assertFalse(option2.hasArgs());

        final Option option3 = Option.builder().arg("a").hasArgs(false).hasArgs().build();
        assertThat(option3.getArgs(), is(1));
        assertTrue(option3.hasArgs());
    }

    @Test
    public void builder_args_should_be_option_args() throws Exception {

        final Option option = Option.builder().arg("a").hasArgs(2).required().build();
        assertThat(option.getArg(), is("a"));
        assertTrue(option.required());
        assertThat(option.getArgs(), is(2));
    }

    @Test
    public void long_opt_should_not_overwrite_arg() throws Exception {

        final Option option = Option.builder().arg("a").longOpt("b").build();
        assertThat(option.getArg(), is("a"));
    }
}
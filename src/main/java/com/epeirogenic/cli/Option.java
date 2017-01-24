package com.epeirogenic.cli;

/**
 * Created by philipcoates on 2017-01-20T12:25
 */
public class Option {

    private final String arg;
    private final boolean required;
    private final int args;

    private Option(String arg, boolean required, int args) {
        this.arg = arg;
        this.required = required;
        this.args = args;
    }

    public String getArg() {
        return arg;
    }

    public boolean required() {
        return required;
    }

    public int getArgs() {
        return args;
    }

    public boolean hasArgs() {
        return args > 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String arg;
        private boolean required = false;
        private int args = 1;

        public Builder arg(final String arg) {
            this.arg = arg;
            return this;
        }

        public Builder hasArgs(final boolean hasArgs) {
            if(!hasArgs) {
                this.args = 0;
            }
            return this;
        }

        public Builder required(final boolean required) {
            this.required = required;
            return this;
        }

        public Builder required() {
            required(true);
            return this;
        }

        public Builder hasArgs(final int argsLength) {
            this.args = argsLength;
            return this;
        }

        public Builder hasArgs() {
            if(args == 0) {
                args = 1;
            }
            return this;
        }

        public Builder longOpt(final String opt) {
            if(arg == null) {
                arg = opt;
            }
            return this;
        }

        public Option build() {

            if(arg == null) {
                throw new IllegalArgumentException("Must have at least one argument");
            }
            return new Option(arg, required, args);
        }
    }
}

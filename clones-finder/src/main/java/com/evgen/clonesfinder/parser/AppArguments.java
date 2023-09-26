package com.evgen.clonesfinder.parser;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

public class AppArguments extends OptionsBase {
    @Option(
            name = "help",
            abbrev = 'h',
            help = "Show help",
            defaultValue = "true"
    )
    public boolean help;

    @Option(
            name = "port",
            abbrev = 'p',
            help = "Set port (positive, non-zero value).",
            category = "startup",
            defaultValue = "8080"
    )
    public int port;

    @Option(
            name = "address",
            abbrev = 'a',
            help = "The group multicast address",
            category = "startup",
            defaultValue = "224.0.0.0"
    )
    public String address;
}

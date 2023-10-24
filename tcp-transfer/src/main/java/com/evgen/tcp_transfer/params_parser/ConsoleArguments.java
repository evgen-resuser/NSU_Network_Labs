package com.evgen.tcp_transfer.params_parser;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

public class ConsoleArguments extends OptionsBase {
    @Option(
            name = "help",
            abbrev = 'h',
            help = "Show help",
            defaultValue = "false"
    )
    public boolean help;

    @Option(
            name = "server mode",
            abbrev = 's',
            help = "Enable server mode",
            defaultValue = "false",
            category = "modes"
    )
    public boolean serverMode;

    @Option(
            name = "client mode",
            abbrev = 'c',
            help = "Enable client mode",
            defaultValue = "false",
            category = "modes"
    )
    public boolean clientMode;

    @Option(
            name = "file path",
            abbrev = 'F',
            help = "Path to the file, that will be sent",
            defaultValue = "null",
            category = "client"
    )
    public String filePath;

    @Option(
            name = "address",
            abbrev = 'a',
            help = "The address of server",
            defaultValue = "localhost",
            category = "client"
    )
    public String addr;

    @Option(
            name = "port",
            abbrev = 'p',
            help = "Set port (positive, non-zero value)",
            defaultValue = "8080",
            category = "client/server"
    )
    public int port;

    @Option(
            name = "max threads",
            abbrev = 't',
            help = "Set max threads for threadpool(positive, non-zero value)",
            defaultValue = "10",
            category = "server"
    )
    public int threadsNum;
}

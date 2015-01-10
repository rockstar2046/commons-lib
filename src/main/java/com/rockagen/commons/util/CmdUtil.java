/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rockagen.commons.util;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;

/**
 * 
 * @author RA
 * @since JDK1.6
 * @since Commons CLI 1.2
 */
public class CmdUtil {

	// ~ Instance fields ==================================================

	/**
	 * Default parser (PosixParser)
	 */
	public final static Parser DEFAULT_PARSER = new PosixParser();


	// ~ Constructors ==================================================

	/**
	 */
	private CmdUtil() {

	}

	// ~ Methods ==================================================

	/**
	 * Print the help for <code>options</code> with the specified command line
	 * syntax. This method prints help information to System.out.
	 * 
	 * @param cmdLineSyntax
	 *            the syntax for this application
	 * @param options
	 *            the Options instance
	 * 
	 */
	public static void printHelp(String cmdLineSyntax, Options options) {
		printHelp(cmdLineSyntax, "", options, "");
	}

	/**
	 * Print the help for <code>options</code> with the specified command line
	 * syntax. This method prints help information to System.out.
	 * 
	 * @param cmdLineSyntax
	 *            the syntax for this application
	 * @param header
	 *            the banner to display at the begining of the help
	 * @param options
	 *            the Options instance
	 * @param footer
	 *            the banner to display at the end of the help
	 * 
	 */
	public static void printHelp(String cmdLineSyntax, String header,
			Options options, String footer) {
		HelpFormatter hf=new HelpFormatter();
		
		if (CommUtil.isBlank(cmdLineSyntax)) {
			cmdLineSyntax = "Command [options]...";
		}
		header = header +SysUtil.LINE_SEPARATOR+"Options:";
		footer = SysUtil.LINE_SEPARATOR + footer;
		hf.printHelp(cmdLineSyntax, header, options, footer);
	}
	
	/**
	 * Return a commandLine object by specifies default parser ,{@link Options},
	 * <code>arguments</code>,<code>stopAtNonOption</code>
	 * 
	 * @param options
	 *            the <code>Options</code>
	 * @param arguments
	 *            the <code>arguments</code>
	 * @return the <code>CommandLine</code>
	 * @throws ParseException
	 *             ParseException if an error occurs when parsing the arguments.
	 */
	public static CommandLine parse(Options options, String[] arguments)
			throws ParseException {

		CommandLine line = DEFAULT_PARSER.parse(options, arguments, false);
		return line;
	}

	/**
	 * Return a commandLine object by specifies default parser ,{@link Options},
	 * <code>arguments</code>,<code>stopAtNonOption</code>
	 * 
	 * @param options
	 *            the <code>Options</code>
	 * @param arguments
	 *            the <code>arguments</code>
	 * @param stopAtNonOption
	 *            specifies whether to stop interpreting the arguments when a
	 *            non option has been encountered and to add them to the
	 *            CommandLines args list.
	 * @return the <code>CommandLine</code>
	 * @throws ParseException
	 *             ParseException if an error occurs when parsing the arguments.
	 */
	public static CommandLine parse(Options options, String[] arguments,
			boolean stopAtNonOption) throws ParseException {

		CommandLine line = DEFAULT_PARSER.parse(options, arguments,
				stopAtNonOption);
		return line;
	}

	/**
	 * Return a commandLine object by specifies {@link Parser},{@link Options},
	 * <code>arguments</code>,<code>stopAtNonOption</code>
	 * 
	 * @param options
	 *            the <code>Options</code>
	 * @param arguments
	 *            the <code>arguments</code>
	 * @param properties
	 *            the <code>command line option name-value pairs</code>
	 * @return the <code>CommandLine</code>
	 * @throws ParseException
	 *             ParseException if an error occurs when parsing the arguments.
	 */
	public static CommandLine parse(Options options, String[] arguments,
			Properties properties) throws ParseException {

		CommandLine line = DEFAULT_PARSER.parse(options, arguments, properties,
				false);
		return line;
	}

	/**
	 * Return a commandLine object by specifies {@link Parser},{@link Options},
	 * <code>arguments</code>,<code>stopAtNonOption</code>
	 * 
	 * @param options
	 *            the <code>Options</code>
	 * @param arguments
	 *            the <code>arguments</code>
	 * @param properties
	 *            the <code>command line option name-value pairs</code>
	 * @param stopAtNonOption
	 *            specifies whether to stop interpreting the arguments when a
	 *            non option has been encountered and to add them to the
	 *            CommandLines args list.
	 * @return the <code>CommandLine</code>
	 * @throws ParseException
	 *             ParseException if an error occurs when parsing the arguments.
	 */
	public static CommandLine parse(Options options, String[] arguments,
			Properties properties, boolean stopAtNonOption)
			throws ParseException {

		CommandLine line = DEFAULT_PARSER.parse(options, arguments, properties,
				stopAtNonOption);
		return line;
	}

	/**
	 * Return a commandLine object by specifies {@link Parser},{@link Options},
	 * <code>arguments</code>,<code>stopAtNonOption</code>
	 * 
	 * @param parser
	 *            see abstract {@link Parser},and interface
	 *            {@link CommandLineParser}
	 * @param options
	 *            the <code>Options</code>
	 * @param arguments
	 *            the <code>arguments</code>
	 * @param stopAtNonOption
	 *            specifies whether to stop interpreting the arguments when a
	 *            non option has been encountered and to add them to the
	 *            CommandLines args list.
	 * @return the <code>CommandLine</code>
	 * @throws ParseException
	 *             ParseException if an error occurs when parsing the arguments.
	 */
	public static CommandLine parse(Parser parser, Options options,
			String[] arguments, boolean stopAtNonOption) throws ParseException {

		CommandLine line = parser.parse(options, arguments, stopAtNonOption);
		return line;
	}

	/**
	 * Return a commandLine object by specifies {@link Parser},{@link Options},
	 * <code>arguments</code>,<code>stopAtNonOption</code>
	 * 
	 * @param parser
	 *            see abstract {@link Parser},and interface
	 *            {@link CommandLineParser}
	 * @param options
	 *            the <code>Options</code>
	 * @param arguments
	 *            the <code>arguments</code>
	 * @param properties the <code>properties</code>
	 * @param stopAtNonOption
	 *            specifies whether to stop interpreting the arguments when a
	 *            non option has been encountered and to add them to the
	 *            CommandLines args list.
	 * @return the <code>CommandLine</code>
	 * @throws ParseException
	 *             ParseException if an error occurs when parsing the arguments.
	 */
	public static CommandLine parse(Parser parser, Options options,
			String[] arguments, Properties properties, boolean stopAtNonOption)
			throws ParseException {

		CommandLine line = parser.parse(options, arguments, properties,
				stopAtNonOption);
		return line;
	}

}

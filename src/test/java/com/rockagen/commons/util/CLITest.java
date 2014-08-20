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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author RA
 * @since JDK1.6
 */
public class CLITest {
	
	@Test
	@Ignore
	public void testCLI(){
		
		// create the Options
		Options options = new Options();
		options.addOption("h", "help",  false, "print help for the command.");
		options.addOption( "v", "verbose",false, "verbose" );
		OptionBuilder.withArgName("property=value" );
		OptionBuilder.hasArgs(2);
		OptionBuilder.withLongOpt("desc");
		OptionBuilder.withValueSeparator();
		OptionBuilder.withDescription( "use value for given property" );
		options.addOption(OptionBuilder
        .create("D"));
		
		OptionBuilder.withArgName("file1,file2..." );
		OptionBuilder.hasArgs();
		OptionBuilder.withLongOpt("input");
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withDescription( "file name" );
		options.addOption(OptionBuilder
		.create("i"));
		
		
		String formatstr = "CLITest [-h/--help][-v/--verbose]..";
		try {
			String[] args={"CLITest","--help","-v","-s","--desc","name=value","--input","file1","file2","file3"};
		    // parse the command line arguments
		    CommandLine line = CmdUtil.parse( options, args );

		    if(line.hasOption("h")) {
		    	CmdUtil.printHelp(formatstr,"weclome usa", options, "If you hava some quesion,please mail to agen@rockagen.com");
		    }if (line.hasOption("v")) {
				System.out.println("VERSION 0.0.1");
			}if (line.hasOption("D")) {
				System.out.println("hey,guys,you input "+ArrayUtil.toString(line.getOptionValues("D")));
			}if (line.hasOption("i")) {
				System.out.println("hey,guys,you input "+ArrayUtil.toString(line.getOptionValues("i")));
			}
			else{
				CmdUtil.printHelp( formatstr, options ); 
		    }
		}
		catch( ParseException exp ) {
			CmdUtil.printHelp( formatstr, options ); 
			System.err.println();
			System.err.println(exp.getMessage());
		}
		
	}
	

}

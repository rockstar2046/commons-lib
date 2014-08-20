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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.SystemUtils;

/**
 * System Utils
 * 
 * @author RA
 * @since JDK1.6
 * @since COMMONS.LANG3
 */
public class SysUtil extends SystemUtils {

	// ~ Constructors ==================================================

	/**
	 */
	private SysUtil() {
	}

	
	//~ Methods ==================================================
	
	/**
	 * open default browse with url
	 * 
	 * @param url 
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 * @throws IOException
	 * @throws NoSuchMethodException
	 */
	public static void browse(String url) throws ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException,
			InterruptedException, InvocationTargetException, IOException,
			NoSuchMethodException {
		if (OS_NAME.startsWith("Mac OS")) {
			Method openURL = ClassUtil.getDeclaredMethod(
					Class.forName("com.apple.eio.FileManager"), true,
					"openURL", new Class[] { String.class });
			openURL.invoke(null, new Object[] { url });
			
		} else if (OS_NAME.startsWith("Windows")) {
			
			Runtime.getRuntime().exec(
					"rundll32 url.dll,FileProtocolHandler " + url);
		} else { 
			// assume Unix or Linux
			String[] browsers = { "firefox", "opera", "konqueror", "epiphany",
					"mozilla", "netscape" };
			String browser = null;
			for (int count = 0; count < browsers.length && browser == null; count++)
				if (Runtime.getRuntime()
						.exec(new String[] { "which", browsers[count] })
						.waitFor() == 0)
					browser = browsers[count];
			if (browser == null)
				throw new NoSuchMethodException("Could not find web browser");
			else
				Runtime.getRuntime().exec(new String[] { browser, url });
		}
	}
	

	// wait add more

}

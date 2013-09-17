/*
 * Copyright 2013 the original author or authors.
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
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MessageDigest Utils
 * @author AGEN
 * @since JDK1.6
 * @since COMMONS.CODEC 1.7
 */
public class MDUtil extends DigestUtils{
	
	
	//~ Constructors ==================================================
	
	/**
	 */
	private MDUtil(){}
	
	
	//~ Methods ==================================================
	
    /**
     * Calculates the MD5 digest and returns the value as a 16 character hex string.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
	public String md5Hex16(final byte[] data){
		return md5Hex(data).substring(8, 24);
	}
	
    /**
     * Calculates the MD5 digest and returns the value as a 16 character hex string.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     * @throws IOException
     *             On error reading from the stream
     */
    public static String md5Hex16(final InputStream data) throws IOException {
        return md5Hex(data).substring(8, 24);
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 character hex string.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex16(final String data) {
        return md5Hex(data).substring(8, 24);
    }
    
	// wait add more

}

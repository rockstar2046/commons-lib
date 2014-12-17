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
package com.rockagen.commons.http;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockagen.commons.util.ArrayUtil;
import com.rockagen.commons.util.CharsetUtil;
import com.rockagen.commons.util.CommUtil;
import com.rockagen.commons.util.IOUtil;

/**
 * Http Connecter Utils
 * 
 * @author RA
 * @since JDK1.6
 * @since 4.1
 */
public class HttpConn {

	// ~ Instance fields ==================================================

	/** */
	private static final Logger log = LoggerFactory.getLogger(HttpConn.class);

	public final static int MAX_TOTAL_CONNECTIONS = 1000;

	public final static int MAX_ROUTE_CONNECTIONS = 1000;

	public final static int CONNECT_TIMEOUT = 3000;

	public final static int SO_TIMEOUT = 20000;

	private final static String ENCODING = CharsetUtil.UTF_8.name();

	private static final ThreadLocal<DefaultHttpClient> threadClient = new ThreadLocal<DefaultHttpClient>();

	// ~ Constructors ==================================================

	/**
	 */
	private HttpConn() {

	}

	// ~ Methods ==================================================

	/**
	 * Get HttpClient
	 * 
	 * @return DefaultHttpClient
	 */
	public static DefaultHttpClient getHttpClient() {
		DefaultHttpClient hc = threadClient.get();
		if (hc == null) {
			hc = initialize();
			threadClient.set(hc);

		}
		return hc;
	}

	/**
	 * Get new HttpClient
	 * 
	 * @return DefaultHttpClient
	 */
	public static DefaultHttpClient getNewHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient
				.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler());
		return httpClient;
	}

	/**
	 * Get new HttpClient
	 * 
	 * @param cm
	 *            {@link ClientConnectionManager}
	 * @return DefaultHttpClient by ClientConnectionManager
	 */
	public static DefaultHttpClient getNewHttpClient(ClientConnectionManager cm) {
		DefaultHttpClient httpClient = new DefaultHttpClient(cm);
		httpClient
				.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler());
		return httpClient;
	}

	/**
	 * initialize
	 * 
	 * @return DefaultHttpClient
	 */
	private static DefaultHttpClient initialize() {
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);

		DefaultHttpClient hc = new DefaultHttpClient(cm);

		hc.setHttpRequestRetryHandler(new StandardHttpRequestRetryHandler());
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				CONNECT_TIMEOUT);
		hc.getParams()
				.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);

		return hc;
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param target
	 *            target address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(String target) throws IOException {
		return sendGet(target, "");
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(InputStream keyStoreInputStream,
			char[] password, String target) throws IOException {
		return sendGet(keyStoreInputStream, password, target, "");
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param target
	 *            target address
	 * @param headers
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(String target, Map<String, String> headers)
			throws IOException {
		return sendGet(target, ENCODING, headers);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param headers
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(InputStream keyStoreInputStream,
			char[] password, String target, Map<String, String> headers)
			throws IOException {
		return sendGet(keyStoreInputStream, password, target, ENCODING, headers);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param headers
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			Map<String, String> headers) throws IOException {
		return sendGet(upc, keyStoreInputStream, password, target, ENCODING,
				headers);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param target
	 *            target address
	 * @param proxy
	 *            proxy address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(String target, String proxy)
			throws IOException {

		return sendGet(target, proxy, ENCODING);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param target
	 *            target address
	 * @param headers
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(String target, String encoding,
			Map<String, String> headers) throws IOException {

		return sendGet(target, null, encoding, headers);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param target
	 *            target address
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(String target, String proxy, String encoding)
			throws IOException {

		return sendGet(null, target, proxy, encoding, null);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param target
	 *            target address
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(String target, String proxy, String encoding,
			Map<String, String> headers) throws IOException {

		return sendGet(null, target, proxy, encoding, headers);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			String target, String encoding) throws IOException {

		return sendGet(upc, target, encoding, null);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc, String target)
			throws IOException {

		return sendGet(upc, target, ENCODING);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			String target, String encoding, Map<String, String> headers)
			throws IOException {

		return sendGet(upc, target, null, encoding, headers);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			String target, String proxy, String encoding,
			Map<String, String> headers) throws IOException {

		return sendGet(upc, null, null, target, proxy, encoding, headers);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target)
			throws IOException {
		return sendGet(upc, keyStoreInputStream, password, target, "");
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param headers
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(InputStream keyStoreInputStream,
			char[] password, String target, String encoding,
			Map<String, String> headers) throws IOException {

		return sendGet(keyStoreInputStream, password, target, null, encoding,
				headers);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param headers
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			String encoding, Map<String, String> headers) throws IOException {

		return sendGet(upc, keyStoreInputStream, password, target, null,
				encoding, headers);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param proxy
	 *            proxy address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(InputStream keyStoreInputStream,
			char[] password, String target, String proxy) throws IOException {

		return sendGet(keyStoreInputStream, password, target, proxy, ENCODING);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param proxy
	 *            proxy address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			String proxy) throws IOException {

		return sendGet(upc, keyStoreInputStream, password, target, proxy,
				ENCODING);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(InputStream keyStoreInputStream,
			char[] password, String target, String proxy, String encoding)
			throws IOException {

		return sendGet(null, keyStoreInputStream, password, target, proxy,
				encoding);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			String proxy, String encoding) throws IOException {

		return sendGet(upc, keyStoreInputStream, password, target, proxy,
				encoding, null);
	}

	/**
	 * Send HTTP GET request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(InputStream keyStoreInputStream,
			char[] password, String target, String proxy, String encoding,
			Map<String, String> headers) throws IOException {

		return sendGet(null, keyStoreInputStream, password, target, proxy,
				encoding, headers);
	}

	/**
	 * send HTTP GET request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			String proxy, String encoding, Map<String, String> headers)
			throws IOException {

		// TargetHost
		Object[] tmp = resolveUrl(target);
		HttpHost targetHost = (HttpHost) tmp[0];
		// URI
		String uri = (String) tmp[1];

		// ProxyHost
		HttpHost proxyHost = null;
		if (!CommUtil.isBlank(proxy)) {
			Object[] tmp1 = resolveUrl(proxy);
			proxyHost = (HttpHost) tmp1[0];
		}

		Header[] _headers = null;
		if (headers != null && headers.size() > 0) {
			_headers = new Header[headers.size()];
			for (Map.Entry<String, String> header : headers.entrySet()) {
				Header h = new BasicHeader(header.getKey(), header.getValue());
				ArrayUtil.add(_headers, h);
			}
		}

		return sendGet(upc, keyStoreInputStream, password, targetHost, uri,
				proxyHost, encoding, _headers);
	}

	/**
	 * send HTTP GET request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream (Custom SSL)
	 * @param password
	 *            keyStore password
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param proxyHost
	 *            HttpHost
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 *            (optional)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password,
			HttpHost targetHost, String uri, HttpHost proxyHost,
			String encoding, Header... headers) throws IOException {

		if (!uri.startsWith("/")) {
			uri = "/" + uri;
		}
		HttpGet httpGet = new HttpGet(uri);
		if (headers != null && headers.length > 0) {
			httpGet.setHeaders(headers);
		}
		log.debug("url: {} method: GET", getURL(targetHost, uri));

		return execute(targetHost, proxyHost, httpGet, encoding, upc,
				keyStoreInputStream, password);
	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(String target, byte[] body)
			throws IOException {

		return sendPost(target, body, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			String target, byte[] body) throws IOException {

		return sendPost(upc, target, body, "", null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			byte[] body) throws IOException {

		return sendPost(upc, keyStoreInputStream, password, target, body, "",
				null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(InputStream keyStoreInputStream,
			char[] password, String target, byte[] body) throws IOException {

		return sendPost(keyStoreInputStream, password, target, body, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @param proxy
	 *            proxy address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(String target, byte[] body, String proxy)
			throws IOException {

		return sendPost(target, body, proxy, ENCODING);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @param proxy
	 *            proxy address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(InputStream keyStoreInputStream,
			char[] password, String target, byte[] body, String proxy)
			throws IOException {

		return sendPost(keyStoreInputStream, password, target, body, proxy,
				ENCODING);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(String target, byte[] body, String proxy,
			String encoding) throws IOException {

		return sendPost(target, body, proxy, encoding, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			String target, byte[] body, String proxy, String encoding)
			throws IOException {

		return sendPost(upc, target, body, proxy, encoding, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(InputStream keyStoreInputStream,
			char[] password, String target, byte[] body, String proxy,
			String encoding) throws IOException {

		return sendPost(keyStoreInputStream, password, target, body, proxy,
				encoding, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			byte[] body, String proxy, String encoding) throws IOException {

		return sendPost(upc, keyStoreInputStream, password, target, body,
				proxy, encoding, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(String target, byte[] body, String proxy,
			String encoding, Map<String, String> headers) throws IOException {

		return sendPost(null, target, body, proxy, encoding, headers);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param target
	 *            target address
	 * @param params
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(String target, Map<String, String> params)
			throws IOException {
		
		return sendPost(target, params, null);
		
	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @param params
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			String target, Map<String, String> params) throws IOException {

		return sendPost(upc, target, params, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param params
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(InputStream keyStoreInputStream,
			char[] password, String target, Map<String, String> params)
			throws IOException {

		return sendPost(keyStoreInputStream, password, target, params, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param params
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			Map<String, String> params) throws IOException {

		return sendPost(upc, keyStoreInputStream, password, target, params,
				null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(String target, Map<String, String> params,
			String proxy) throws IOException {

		return sendPost(target, params, proxy, ENCODING);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			String target, Map<String, String> params, String proxy)
			throws IOException {

		return sendPost(upc, target, params, proxy, ENCODING);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(InputStream keyStoreInputStream,
			char[] password, String target, Map<String, String> params,
			String proxy) throws IOException {

		return sendPost(keyStoreInputStream, password, target, params, proxy,
				ENCODING);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			Map<String, String> params, String proxy) throws IOException {

		return sendPost(upc, keyStoreInputStream, password, target, params,
				proxy, ENCODING);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(String target, Map<String, String> params,
			String proxy, String encoding) throws IOException {

		return sendPost(target, params, proxy, encoding, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			String target, Map<String, String> params, String proxy,
			String encoding) throws IOException {

		return sendPost(upc, target, params, proxy, encoding, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(InputStream keyStoreInputStream,
			char[] password, String target, Map<String, String> params,
			String proxy, String encoding) throws IOException {

		return sendPost(keyStoreInputStream, password, target, params, proxy,
				encoding, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			Map<String, String> params, String proxy, String encoding)
			throws IOException {

		return sendPost(upc, keyStoreInputStream, password, target, params,
				proxy, encoding, null);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(String target, Map<String, String> params,
			String proxy, String encoding, Map<String, String> headers)
			throws IOException {

		return sendPost(null, target, params, proxy, encoding, headers);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			String target, byte[] body, String proxy, String encoding,
			Map<String, String> headers) throws IOException {

		return sendPost(upc, null, null, target, body, proxy, encoding, headers);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(InputStream keyStoreInputStream,
			char[] password, String target, byte[] body, String proxy,
			String encoding, Map<String, String> headers) throws IOException {

		return sendPost(null, keyStoreInputStream, password, target, body,
				proxy, encoding, headers);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			String target, Map<String, String> params, String proxy,
			String encoding, Map<String, String> headers) throws IOException {

		return sendPost(upc, null, null, target, params, proxy, encoding,
				headers);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(InputStream keyStoreInputStream,
			char[] password, String target, Map<String, String> params,
			String proxy, String encoding, Map<String, String> headers)
			throws IOException {

		return sendPost(null, keyStoreInputStream, password, target, params,
				proxy, encoding, headers);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param body
	 *            request body
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			byte[] body, String proxy, String encoding,
			Map<String, String> headers) throws IOException {

		return sendPost(upc, keyStoreInputStream, password, target, null, body,
				proxy, encoding, headers);

	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param target
	 *            target address
	 * @param params
	 * @param proxy
	 *            proxy address
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			Map<String, String> params, String proxy, String encoding,
			Map<String, String> headers) throws IOException {

		return sendPost(upc, keyStoreInputStream, password, target, params,
				null, proxy, encoding, headers);
	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param body
	 *            request body
	 * @param proxyHost
	 *            HttpHost
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password,
			HttpHost targetHost, String uri, byte[] body, HttpHost proxyHost,
			String encoding, Header... headers) throws IOException {

		return sendPost(upc, keyStoreInputStream, password, targetHost, uri,
				new ByteArrayEntity(body), proxyHost, encoding, headers);
	}

	private static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password, String target,
			Map<String, String> params, byte[] body, String proxy,
			String encoding, Map<String, String> headers) throws IOException {

		// TargetHost
		Object[] tmp = resolveUrl(target);
		HttpHost targetHost = (HttpHost) tmp[0];
		// URI
		String uri = (String) tmp[1];

		// ProxyHost
		HttpHost proxyHost = null;
		if (!CommUtil.isBlank(proxy)) {
			Object[] tmp1 = resolveUrl(proxy);
			proxyHost = (HttpHost) tmp1[0];
		}

		Header[] _headers = null;
		if (headers != null && headers.size() > 0) {
			_headers = new Header[headers.size()];
			for (Map.Entry<String, String> header : headers.entrySet()) {
				Header h = new BasicHeader(header.getKey(), header.getValue());
				ArrayUtil.add(_headers, h);
			}
		}

		if (body != null && body.length > 0) {
			return sendPost(upc, keyStoreInputStream, password, targetHost,
					uri, body, proxyHost, encoding, _headers);
		} else {
			List<NameValuePair> _params = new ArrayList<NameValuePair>();
			;
			if (params != null && params.size() > 0) {
				for (Map.Entry<String, String> param : params.entrySet()) {
					NameValuePair nvPair = new BasicNameValuePair(
							param.getKey(), param.getValue());
					_params.add(nvPair);
				}
			}
			return sendPost(upc, keyStoreInputStream, password, targetHost,
					uri, _params, proxyHost, encoding, _headers);
		}
	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param params
	 *            ( ArrayList NameValuePair)
	 * @param proxyHost
	 *            HttpHost
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password,
			HttpHost targetHost, String uri, List<NameValuePair> params,
			HttpHost proxyHost, String encoding, Header... headers)
			throws IOException {

		return sendPost(upc, keyStoreInputStream, password, targetHost, uri,
				new UrlEncodedFormEntity(params), proxyHost, encoding, headers);
	}

	/**
	 * Send HTTP POST request
	 * 
	 * @param upc
	 *            basic auth {@link UsernamePasswordCredentials}
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param entity
	 *            request {@link HttpEntity}
	 * @param proxyHost
	 *            HttpHost
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			InputStream keyStoreInputStream, char[] password,
			HttpHost targetHost, String uri, HttpEntity entity,
			HttpHost proxyHost, String encoding, Header... headers)
			throws IOException {

		if (!uri.startsWith("/")) {
			uri = "/" + uri;
		}
		HttpPost httpPost = new HttpPost(uri);
		if (headers != null && headers.length > 0) {
			httpPost.setHeaders(headers);
		}
		if (entity != null) {
			httpPost.setEntity(entity);
		}

		log.debug("url: {} method: POST", getURL(targetHost, uri));
		return execute(targetHost, proxyHost, httpPost, encoding, upc,
				keyStoreInputStream, password);

	}

	/**
	 * Handler main
	 * 
	 * @param httpRequestMethod
	 *            HttpGet or HttpPost
	 * @param targetHost
	 * @param proxy
	 * @param upc
	 * @param keyStoreInputStream
	 * @param password
	 * @return result String
	 * @throws IOException
	 * @throws KeyStoreException
	 * 
	 */
	protected static String execute(HttpHost targetHost, HttpHost proxy,
			HttpRequestBase httpRequestMethod, String encoding,
			UsernamePasswordCredentials upc, InputStream keyStoreInputStream,
			char[] password) throws IOException {
		DefaultHttpClient httpClient = getHttpClient();
		if (proxy != null) {
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
		if (keyStoreInputStream != null) {

			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(keyStoreInputStream, password);
				SSLSocketFactory socketFactory = new SSLSocketFactory(
						trustStore);
				Scheme sch = new Scheme(targetHost.getSchemeName(),
						targetHost.getPort(), socketFactory);
				httpClient.getConnectionManager().getSchemeRegistry()
						.register(sch);
			} catch (KeyStoreException e) {
				log.error("{}", e.getMessage(), e);
			} catch (NoSuchAlgorithmException e) {
				log.error("{}", e.getMessage(), e);
			} catch (CertificateException e) {
				log.error("{}", e.getMessage(), e);
			} catch (KeyManagementException e) {
				log.error("{}", e.getMessage(), e);
			} catch (UnrecoverableKeyException e) {
				log.error("{}", e.getMessage(), e);
			} finally {

				keyStoreInputStream.close();
			}

		}

		HttpResponse response;

		if (upc != null) {

			httpClient.getCredentialsProvider().setCredentials(
					new AuthScope(targetHost.getHostName(),
							targetHost.getPort()), upc);
			// Get AuthCache instance
			AuthCache authCache = new BasicAuthCache();

			// Generate BASIC scheme object and add it to the local
			if (authCache.get(targetHost) == null) {
				authCache.put(targetHost, new BasicScheme());
			}
			// Add AuthCache to the execution context
			BasicHttpContext localcontext = new BasicHttpContext();
			localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

			response = httpClient.execute(targetHost, httpRequestMethod,
					localcontext);

		}

		else {
			response = httpClient.execute(targetHost, httpRequestMethod);
		}

		return getResponse(httpClient, response, encoding);

	}

	/**
	 * Handle response (resolve response to String,httpClient close,etc.)
	 * 
	 * @param httpClient
	 * @param response
	 * @param encoding
	 * @return result String
	 * @throws IOException
	 */
	public static String getResponse(HttpClient httpClient,
			HttpResponse response, String encoding) throws IOException {
		log.debug("status: {}", response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		String retval="";
		if (entity != null) {
			try {
				if (CommUtil.isBlank(encoding)) {
					encoding = ENCODING;
				}
				
				retval=IOUtil.toString(entity.getContent(), encoding);
								
			} finally {
				EntityUtils.consume(entity);
				
			}
		}
		return retval;
	}

	/**
	 * 
	 * Get UsernamePasswordCredentials
	 * 
	 * @param usernameSamePassword
	 * @return UsernamePasswordCredentials
	 */
	public static UsernamePasswordCredentials getUPC(String usernameSamePassword) {
		if (CommUtil.isBlank(usernameSamePassword)) {
			return null;
		}
		return new UsernamePasswordCredentials(usernameSamePassword);
	}

	/**
	 * Get UsernamePasswordCredentials
	 * 
	 * @param username
	 * @param password
	 * @return UsernamePasswordCredentials
	 */
	public static UsernamePasswordCredentials getUPC(String username,
			String password) {
		if (CommUtil.isBlank(username) && CommUtil.isBlank(password)) {
			return null;
		}
		return new UsernamePasswordCredentials(username, password);
	}

	/**
	 * Get url
	 * 
	 * @param targetHost
	 * @param uri
	 * @return new url string
	 */
	public static String getURL(HttpHost targetHost, String uri) {
		if (targetHost != null && !CommUtil.isBlank(targetHost.getSchemeName())
				&& !CommUtil.isBlank(targetHost.getHostName())
				&& targetHost.getPort() > 0) {
			return targetHost + uri;
		}
		return "null" + uri;
	}

	/**
	 * <p>
	 * Resolve String to Object Array
	 * </p>
	 * <p>
	 * Array length is 2,by default return http://localhost:80/
	 * </p>
	 * <li>[0]--> HttpHost</li> <li>[1]--> URI</li>
	 * 
	 * @param str
	 * @return object array [0]= HttpHost [1]=String(uri)
	 */
	private static Object[] resolveUrl(String str) {

		String scheme = "http", host = "localhost", uri = "/";
		int port = 80;

		Object[] obj = new Object[2];

		try {
			if (str.length() >= 10) {
				String temp = str.substring(0, str.indexOf(":"));
				if (!CommUtil.isBlank(temp)) {
					if (temp.equalsIgnoreCase("HTTP")
							|| temp.equalsIgnoreCase("HTTPS")) {
						scheme = temp;
						String temp1 = str.substring(temp.length() + 3);
						if (temp1.indexOf("/") > 0) {
							String temp2 = temp1.substring(0,
									temp1.indexOf("/"));
							if (temp2.indexOf(":") > 0) {
								String[] temp3 = temp2.split(":");
								if (temp3.length > 1
										&& temp3[1].matches("[0-9]*")) {
									port = Integer.parseInt(temp3[1]);
									host = temp3[0];
								}

							} else {
								host = temp2;
								if (temp.equalsIgnoreCase("HTTP")) {
									port = 80;
								} else if (temp.equalsIgnoreCase("HTTPS")) {
									port = 443;
								}
							}
							uri = temp1.substring(temp2.length());
						}

						else {

							if (temp1.indexOf(":") > 0) {
								String[] temp3 = temp1.split(":");
								if (temp3[1].matches("[0-9]*")) {
									port = Integer.parseInt(temp3[1]);
									host = temp3[0];

								}

							} else {
								host = temp1;
								if (temp.equalsIgnoreCase("HTTP")) {
									port = 80;
								} else if (temp.equalsIgnoreCase("HTTPS")) {
									port = 443;
								}
							}
							uri = "/";
						}
					}

				}

			}
		} catch (Exception e) {
			log.error("{}", e.getMessage(), e);
		}

		HttpHost targetHost = new HttpHost(host, port, scheme);
		obj[0] = targetHost;
		obj[1] = uri;
		log.debug("The parsed Object Array {}", Arrays.toString(obj));
		return obj;
	}

}

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
package com.rockagen.commons.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockagen.commons.util.CharsetUtil;
import com.rockagen.commons.util.CommUtil;

/**
 * Http Connecter Utils
 * 
 * @author AGEN
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
	 * Get new HttpClient
	 * 
	 * @return DefaultHttpClient by ClientConnectionManager
	 */
	public static DefaultHttpClient getNewHttpClient(ClientConnectionManager cm) {
		DefaultHttpClient httpClient = new DefaultHttpClient(cm);
		httpClient
				.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler());
		return httpClient;
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
	 * send HTTP POST request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(HttpHost targetHost, String uri,
			Header... headers) throws IOException {
		return sendPost(targetHost, uri, "", headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param upc
	 *            basic
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			HttpHost targetHost, String uri, Header... headers)
			throws IOException {
		return sendPost(upc, targetHost, uri, "", headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param upc
	 *            basic
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			HttpHost targetHost, HttpHost proxy, String uri, Header... headers)
			throws IOException {
		return sendPost(upc, targetHost, proxy, uri, "", headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(HttpHost targetHost, HttpHost proxy,
			String uri, Header... headers) throws IOException {
		return sendPost(targetHost, proxy, uri, "", headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param postParameters
	 *            postParameters
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(HttpHost targetHost, String uri,
			ArrayList<NameValuePair> postParameters, Header... headers)
			throws IOException {
		return sendPost(targetHost, uri, ENCODING, postParameters, headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param postParameters
	 *            postParameters
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(HttpHost targetHost, HttpHost proxy,
			String uri, ArrayList<NameValuePair> postParameters,
			Header... headers) throws IOException {
		return sendPost(targetHost, proxy, uri, ENCODING, postParameters,
				headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param upc
	 *            basic
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param postParameters
	 *            postParameters
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			HttpHost targetHost, String uri,
			ArrayList<NameValuePair> postParameters, Header... headers)
			throws IOException {
		return sendPost(upc, targetHost, uri, ENCODING, postParameters, headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param upc
	 *            basic
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param postParameters
	 *            postParameters
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			HttpHost targetHost, HttpHost proxy, String uri,
			ArrayList<NameValuePair> postParameters, Header... headers)
			throws IOException {
		return sendPost(upc, targetHost, proxy, uri, ENCODING, postParameters,
				headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param postParameters
	 * 
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(HttpHost targetHost, String uri,
			String encoding, ArrayList<NameValuePair> postParameters,
			Header... headers) throws IOException {
		return sendPost(false, null, targetHost, uri, encoding, null,
				postParameters, headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param postParameters
	 * 
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(HttpHost targetHost, HttpHost proxy,
			String uri, String encoding,
			ArrayList<NameValuePair> postParameters, Header... headers)
			throws IOException {
		return sendPost(false, null, targetHost, proxy, uri, encoding, null,
				postParameters, headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param upc
	 *            basicUsernamePasswordCredentials
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param postParameters
	 * 
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			HttpHost targetHost, String uri, String encoding,
			ArrayList<NameValuePair> postParameters, Header... headers)
			throws IOException {
		return sendPost(true, upc, targetHost, uri, encoding, null,
				postParameters, headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param upc
	 *            basicUsernamePasswordCredentials
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param postParameters
	 * 
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			HttpHost targetHost, HttpHost proxy, String uri, String encoding,
			ArrayList<NameValuePair> postParameters, Header... headers)
			throws IOException {
		return sendPost(true, upc, targetHost, proxy, uri, encoding, null,
				postParameters, headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param message
	 *            requestbody
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(HttpHost targetHost, String uri,
			String message, Header... headers) throws IOException {
		return sendPost(false, null, targetHost, uri, ENCODING, message, null,
				headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param message
	 *            requestbody
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(HttpHost targetHost, HttpHost proxy,
			String uri, String message, Header... headers) throws IOException {
		return sendPost(false, null, targetHost, proxy, uri, ENCODING, message,
				null, headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param upc
	 *            basicUsernamePasswordCredentials
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param message
	 *            requestbody
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			HttpHost targetHost, String uri, String message, Header... headers)
			throws IOException {
		return sendPost(true, upc, targetHost, uri, ENCODING, message, null,
				headers);

	}

	/**
	 * send HTTP POST request
	 * 
	 * @param upc
	 *            basicUsernamePasswordCredentials
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param message
	 *            requestbody
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			HttpHost targetHost, HttpHost proxy, String uri, String message,
			Header... headers) throws IOException {
		return sendPost(true, upc, targetHost, proxy, uri, ENCODING, message,
				null, headers);

	}

	/**
	 * send HTTP POST request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param message
	 *            requestbody
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(HttpHost targetHost, String uri,
			String encoding, String message, Header... headers)
			throws IOException {
		return sendPost(false, null, targetHost, uri, encoding, message, null,
				headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param message
	 *            requestbody
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(HttpHost targetHost, HttpHost proxy,
			String uri, String encoding, String message, Header... headers)
			throws IOException {
		return sendPost(false, null, targetHost, proxy, uri, encoding, message,
				null, headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param upc
	 *            basicUsernamePasswordCredentials
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param message
	 *            requestbody
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			HttpHost targetHost, String uri, String encoding, String message,
			Header... headers) throws IOException {
		return sendPost(true, upc, targetHost, uri, encoding, message, null,
				headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param upc
	 *            basicUsernamePasswordCredentials
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param message
	 *            requestbody
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(UsernamePasswordCredentials upc,
			HttpHost targetHost, HttpHost proxy, String uri, String encoding,
			String message, Header... headers) throws IOException {
		return sendPost(true, upc, targetHost, proxy, uri, encoding, message,
				null, headers);
	}

	/**
	 * send HTTP POST request
	 * 
	 * @param basicAuth
	 *            is basic auth ?
	 * @param upc
	 *            basicUsernamePasswordCredentials
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param message
	 *            requestbody
	 * @param postParameters
	 *            ( ArrayList NameValuePair)
	 * 
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(boolean basicAuth,
			UsernamePasswordCredentials upc, HttpHost targetHost, String uri,
			String encoding, String message,
			ArrayList<NameValuePair> postParameters, Header... headers)
			throws IOException {

		return sendPost(basicAuth, upc, targetHost, null, uri, encoding,
				message, postParameters, headers);

	}

	/**
	 * send HTTP POST request
	 * 
	 * @param basicAuth
	 *            is basic auth ?
	 * @param upc
	 *            basicUsernamePasswordCredentials
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param message
	 *            requestbody
	 * @param postParameters
	 *            ( ArrayList NameValuePair)
	 * 
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(boolean basicAuth,
			UsernamePasswordCredentials upc, HttpHost targetHost,
			HttpHost proxy, String uri, String encoding, String message,
			ArrayList<NameValuePair> postParameters, Header... headers)
			throws IOException {

		if (!uri.startsWith("/")) {
			uri = "/" + uri;
		}
		HttpPost httpPost = new HttpPost(uri);
		if (headers.length > 0) {
			httpPost.setHeaders(headers);
		}
		if (!CommUtil.isBlank(message)) {
			httpPost.setEntity(new StringEntity(message));
		}
		if (postParameters != null && postParameters.size() > 0) {
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
		}
		log.debug("url: {} method: POST", getURL(targetHost, uri));
		return sendPost(basicAuth, upc, false, null, null, targetHost, proxy,
				uri, encoding, message, postParameters, headers);

	}

	/**
	 * send HTTP POST request
	 * 
	 * @param basicAuth
	 *            is basic auth ?
	 * @param upc
	 *            basicUsernamePasswordCredentials
	 * @param customSSL
	 *            is customSSL ?
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param message
	 *            requestbody
	 * @param postParameters
	 *            ( ArrayList NameValuePair)
	 * 
	 * @param headers
	 * 
	 * @return result String
	 * @throws IOException
	 */
	public static String sendPost(boolean basicAuth,
			UsernamePasswordCredentials upc, boolean customSSL,
			InputStream keyStoreInputStream, char[] password,
			HttpHost targetHost, HttpHost proxy, String uri, String encoding,
			String message, ArrayList<NameValuePair> postParameters,
			Header... headers) throws IOException {

		if (!uri.startsWith("/")) {
			uri = "/" + uri;
		}
		HttpPost httpPost = new HttpPost(uri);
		if (headers.length > 0) {
			httpPost.setHeaders(headers);
		}
		if (!CommUtil.isBlank(message)) {
			httpPost.setEntity(new StringEntity(message));
		}
		if (postParameters != null && postParameters.size() > 0) {
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
		}
		log.debug("url: {} method: POST", getURL(targetHost, uri));
		return execute(targetHost, proxy, httpPost, encoding, basicAuth, upc,
				customSSL, keyStoreInputStream, password);

	}

	/**
	 * send HTTP GET request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param headers
	 *            (optional)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(HttpHost targetHost, String uri,
			Header... headers) throws IOException {

		return sendGet(targetHost, uri, ENCODING, headers);
	}

	/**
	 * send HTTP GET request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param headers
	 *            (optional)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(HttpHost targetHost, HttpHost proxy,
			String uri, Header... headers) throws IOException {

		return sendGet(targetHost, proxy, uri, ENCODING, headers);
	}

	/**
	 * send HTTP GET request
	 * 
	 * @param upc
	 *            basic
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param headers
	 *            (optional)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			HttpHost targetHost, String uri, Header... headers)
			throws IOException {

		return sendGet(true, upc, targetHost, uri, ENCODING, headers);
	}

	/**
	 * send HTTP GET request
	 * 
	 * @param upc
	 *            basic
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param headers
	 *            (optional)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(UsernamePasswordCredentials upc,
			HttpHost targetHost, HttpHost proxy, String uri, Header... headers)
			throws IOException {

		return sendGet(true, upc, targetHost, proxy, uri, ENCODING, headers);
	}

	/**
	 * send HTTP GET request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param headers
	 *            (optional)
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(HttpHost targetHost, String uri,
			String encoding, Header... headers) throws IOException {

		return sendGet(false, null, targetHost, uri, encoding, headers);
	}

	/**
	 * send HTTP GET request
	 * 
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param headers
	 *            (optional)
	 * @param encoding
	 *            (default UTF-8)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(HttpHost targetHost, HttpHost proxy,
			String uri, String encoding, Header... headers) throws IOException {

		return sendGet(false, null, targetHost, proxy, uri, encoding, headers);
	}

	/**
	 * send HTTP GET request
	 * 
	 * @param basicAuth
	 *            is basic auth ?
	 * @param upc
	 *            basic (UsernamePasswordCredentials)
	 * @param targetHost
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 *            (optional)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(boolean basicAuth,
			UsernamePasswordCredentials upc, HttpHost targetHost, String uri,
			String encoding, Header... headers) throws IOException {

		return sendGet(basicAuth, upc, targetHost, null, uri, encoding, headers);
	}

	/**
	 * send HTTP GET request
	 * 
	 * @param basicAuth
	 *            is basic auth ?
	 * @param upc
	 *            basic (UsernamePasswordCredentials)
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 *            (optional)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(boolean basicAuth,
			UsernamePasswordCredentials upc, HttpHost targetHost,
			HttpHost proxy, String uri, String encoding, Header... headers)
			throws IOException {

		return sendGet(basicAuth, upc, false, null, null, targetHost, proxy,
				uri, encoding, headers);
	}

	/**
	 * send HTTP GET request
	 * 
	 * @param basicAuth
	 *            is basic auth ?
	 * @param upc
	 *            basic (UsernamePasswordCredentials)
	 * @param customSSL
	 *            is customSSL ?
	 * @param keyStoreInputStream
	 *            keyStore InputStream
	 * @param password
	 *            keyStore password
	 * @param targetHost
	 *            HttpHost
	 * @param proxy
	 *            HttpHost
	 * @param uri
	 *            URI
	 * @param encoding
	 *            (default UTF-8)
	 * @param headers
	 *            (optional)
	 * @return result String
	 * @throws IOException
	 */
	public static String sendGet(boolean basicAuth,
			UsernamePasswordCredentials upc, boolean customSSL,
			InputStream keyStoreInputStream, char[] password,
			HttpHost targetHost, HttpHost proxy, String uri, String encoding,
			Header... headers) throws IOException {

		if (!uri.startsWith("/")) {
			uri = "/" + uri;
		}
		HttpGet httpGet = new HttpGet(uri);
		if (headers.length > 0) {
			httpGet.setHeaders(headers);
		}
		log.debug("url: {} method: GET", getURL(targetHost, uri));

		return execute(targetHost, proxy, httpGet, encoding, basicAuth, upc,
				customSSL, keyStoreInputStream, password);
	}

	/**
	 * handler main
	 * 
	 * @param httpRequestMethod
	 *            HttpGet or HttpPost
	 * @param basicAuth
	 * @param targetHost
	 * @param proxy
	 * @param upc
	 * @param customSSL
	 * @param keyStoreInputStream
	 * @param password
	 * @return result String
	 * @throws IOException
	 * @throws KeyStoreException
	 * 
	 */
	protected static String execute(HttpHost targetHost, HttpHost proxy,
			HttpRequestBase httpRequestMethod, String encoding,
			boolean basicAuth, UsernamePasswordCredentials upc,
			boolean customSSL, InputStream keyStoreInputStream, char[] password)
			throws IOException {
		DefaultHttpClient httpClient = getHttpClient();
		if (proxy != null) {
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
		if (customSSL) {

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
				log.error("", e);
			} catch (NoSuchAlgorithmException e) {
				log.error("", e);
			} catch (CertificateException e) {
				log.error("", e);
			} catch (KeyManagementException e) {
				log.error("", e);
			} catch (UnrecoverableKeyException e) {
				log.error("", e);
			} finally {

				keyStoreInputStream.close();
			}

		}

		HttpResponse response;

		if (basicAuth) {

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
	 * handle response (resolve response to String,httpClient close,etc.)
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
		StringBuffer buf = new StringBuffer();
		if (entity != null) {
			try {
				InputStream instream = entity.getContent();

				if (CommUtil.isBlank(encoding)) {
					encoding = ENCODING;
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						instream, encoding));
				String line;
				while (null != (line = br.readLine())) {
					buf.append(line).append("\n");
				}

			} finally {
				EntityUtils.consume(entity);

			}
		}
		return buf.toString();
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
	 * @return object array [0]= HttpHOst [1]=String(uri)
	 */
	public static Object[] resolveString(String str) {

		String scheme = "http", host = "localhost", uri = "/";
		int port = 80;

		Object[] obj = new Object[2];

		try {
			if (str.length() >= 10) {
				String temp = str.substring(0, str.indexOf(":"));
				if (!CommUtil.isBlank(temp)) {
					if (temp.equalsIgnoreCase("HTTP")
							|| temp.equalsIgnoreCase("HTTPS")) {
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
			// do not
		}

		HttpHost targetHost = new HttpHost(host, port, scheme);
		obj[0] = targetHost;
		obj[1] = uri;
		log.warn("Your HttpHost has been resolved,but may not be correct,please use new HttpHost(host,port,scheme) instead.");
		log.debug("The parsed Object Array {}", Arrays.toString(obj));
		return obj;
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

}

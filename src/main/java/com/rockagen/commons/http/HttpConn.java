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

import com.rockagen.commons.util.ArrayUtil;
import com.rockagen.commons.util.CharsetUtil;
import com.rockagen.commons.util.CommUtil;
import com.rockagen.commons.util.IOUtil;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Http Connecter Utils
 *
 * @author RA
 * @since 4.3
 */
public class HttpConn {

    // ~ Instance fields ==================================================

    /** */
    private static final Logger log = LoggerFactory.getLogger(HttpConn.class);

    public final static int CONNECT_TIMEOUT = 10000;

    public final static int SO_TIMEOUT = 30000;

    private final static String ENCODING = CharsetUtil.UTF_8.name();


    // ~ Constructors ==================================================

    /**
     */
    private HttpConn() {

    }

    // ~ Methods ==================================================

    /**
     * Send a http request with explicit params
     *
     * @param target target address
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(String target) throws IOException {
        return send(target, RequestMethod.GET);
    }

    /**
     * Send a http request with explicit params
     *
     * @param target target address
     * @param method {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(String target, RequestMethod method) throws IOException {
        return send(target, "", method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(InputStream keystore,
                              char[] password, String target, RequestMethod method) throws IOException {
        return send(keystore, password, target, "", method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param target  target address
     * @param headers headers
     * @param method  {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(String target, Map<String, String> headers, RequestMethod method)
            throws IOException {
        return send(target, ENCODING, headers, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(InputStream keystore,
                              char[] password, String target, Map<String, String> headers, RequestMethod method)
            throws IOException {
        return send(keystore, password, target, ENCODING, headers, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc,
                              InputStream keystore, char[] password, String target,
                              Map<String, String> headers, RequestMethod method) throws IOException {
        return send(upc, keystore, password, target, ENCODING,
                headers, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param target target address
     * @param proxy  proxy address
     * @param method {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(String target, String proxy, RequestMethod method)
            throws IOException {

        return send(target, proxy, ENCODING, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param target   target address
     * @param headers headers
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(String target, String encoding,
                              Map<String, String> headers, RequestMethod method) throws IOException {

        return send(target, null, encoding, headers, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param target   target address
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(String target, String proxy, String encoding, RequestMethod method)
            throws IOException {

        return send(null, target, proxy, encoding, null, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param target   target address
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(String target, String proxy, String encoding,
                              Map<String, String> headers, RequestMethod method) throws IOException {

        return send(null, target, proxy, encoding, headers, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param target   target address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc,
                              String target, String encoding, RequestMethod method) throws IOException {

        return send(upc, target, encoding, null, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param upc    basic auth {@link UsernamePasswordCredentials}
     * @param target target address
     * @param method {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc, String target, RequestMethod method)
            throws IOException {

        return send(upc, target, ENCODING, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param target   target address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc,
                              String target, String encoding, Map<String, String> headers, RequestMethod method)
            throws IOException {

        return send(upc, target, null, encoding, headers, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param target   target address
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc,
                              String target, String proxy, String encoding,
                              Map<String, String> headers, RequestMethod method) throws IOException {

        return send(upc, null, null, target, proxy, encoding, headers, method);
    }

    /**
     * Send a http request with explicit params
     * @param upc {@link org.apache.http.auth.UsernamePasswordCredentials}
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc,
                              InputStream keystore, char[] password, String target, RequestMethod method)
            throws IOException {
        return send(upc, keystore, password, target, "", method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param headers headers
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(InputStream keystore,
                              char[] password, String target, String encoding,
                              Map<String, String> headers, RequestMethod method) throws IOException {

        return send(keystore, password, target, null, encoding,
                headers, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param headers headers
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc,
                              InputStream keystore, char[] password, String target,
                              String encoding, Map<String, String> headers, RequestMethod method) throws IOException {

        return send(upc, keystore, password, target, null,
                encoding, headers, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param proxy    proxy address
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(InputStream keystore,
                              char[] password, String target, String proxy, RequestMethod method) throws IOException {

        return send(keystore, password, target, proxy, ENCODING, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param proxy    proxy address
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc,
                              InputStream keystore, char[] password, String target,
                              String proxy, RequestMethod method) throws IOException {

        return send(upc, keystore, password, target, proxy,
                ENCODING, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(InputStream keystore,
                              char[] password, String target, String proxy, String encoding, RequestMethod method)
            throws IOException {

        return send(null, keystore, password, target, proxy,
                encoding, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc,
                              InputStream keystore, char[] password, String target,
                              String proxy, String encoding, RequestMethod method) throws IOException {

        return send(upc, keystore, password, target, proxy,
                encoding, null, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(InputStream keystore,
                              char[] password, String target, String proxy, String encoding,
                              Map<String, String> headers, RequestMethod method) throws IOException {

        return send(null, keystore, password, target, proxy,
                encoding, headers, method);
    }

    /**
     * Send a http request with explicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream (Custom SSL)
     * @param password keyStore password
     * @param target   target address
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc,
                              InputStream keystore, char[] password, String target,
                              String proxy, String encoding, Map<String, String> headers, RequestMethod method)
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

        return send(upc, keystore, password, targetHost, uri,
                proxyHost, encoding, method, _headers);
    }

    /**
     * Send a http request with explicit params
     *
     * @param upc        basic auth {@link UsernamePasswordCredentials}
     * @param keystore   keyStore InputStream (Custom SSL)
     * @param password   keyStore password
     * @param targetHost HttpHost
     * @param uri        URI
     * @param proxyHost  HttpHost
     * @param encoding   (default UTF-8)
     * @param method     {@link com.rockagen.commons.http.RequestMethod}
     * @param headers headers    (optional)
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String send(UsernamePasswordCredentials upc,
                              InputStream keystore, char[] password,
                              HttpHost targetHost, String uri, HttpHost proxyHost,
                              String encoding, RequestMethod method, Header... headers) throws IOException {

        if (!uri.startsWith("/")) {
            uri = "/" + uri;
        }
        HttpRequest hm = getHttpMethod(method, uri);
        if (headers != null && headers.length > 0) {
            hm.setHeaders(headers);
        }
        log.debug("url: {} method: {}", getURL(targetHost, uri), method);

        return execute(targetHost, proxyHost, hm, encoding, upc,
                keystore, password);
    }

    /**
     * Send a http request with implicit params
     *
     * @param target target address
     * @param body   request body
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(String target, byte[] body)
            throws IOException {

        return sendBody(target, body, RequestMethod.POST);

    }

    /**
     * Send a http request with implicit params
     *
     * @param target target address
     * @param body   request body
     * @param method {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(String target, byte[] body, RequestMethod method)
            throws IOException {

        return sendBody(target, body, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc    basic auth {@link UsernamePasswordCredentials}
     * @param target target address
     * @param body   request body
     * @param method {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  String target, byte[] body, RequestMethod method) throws IOException {

        return sendBody(upc, target, body, "", null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param body     request body
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  InputStream keystore, char[] password, String target,
                                  byte[] body, RequestMethod method) throws IOException {

        return sendBody(upc, keystore, password, target, body, "",
                null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param body     request body
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(InputStream keystore,
                                  char[] password, String target, byte[] body, RequestMethod method) throws IOException {

        return sendBody(keystore, password, target, body, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param target target address
     * @param body   request body
     * @param proxy  proxy address
     * @param method {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(String target, byte[] body, String proxy, RequestMethod method)
            throws IOException {

        return sendBody(target, body, proxy, ENCODING, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param body     request body
     * @param proxy    proxy address
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(InputStream keystore,
                                  char[] password, String target, byte[] body, String proxy, RequestMethod method)
            throws IOException {

        return sendBody(keystore, password, target, body, proxy,
                ENCODING, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param target   target address
     * @param body     request body
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(String target, byte[] body, String proxy,
                                  String encoding, RequestMethod method) throws IOException {

        return sendBody(target, body, proxy, encoding, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param target   target address
     * @param body     request body
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  String target, byte[] body, String proxy, String encoding, RequestMethod method)
            throws IOException {

        return sendBody(upc, target, body, proxy, encoding, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param body     request body
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(InputStream keystore,
                                  char[] password, String target, byte[] body, String proxy,
                                  String encoding, RequestMethod method) throws IOException {

        return sendBody(keystore, password, target, body, proxy,
                encoding, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param body     request body
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  InputStream keystore, char[] password, String target,
                                  byte[] body, String proxy, String encoding, RequestMethod method) throws IOException {

        return sendBody(upc, keystore, password, target, body,
                proxy, encoding, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param target   target address
     * @param body     request body
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(String target, byte[] body, String proxy,
                                  String encoding, Map<String, String> headers, RequestMethod method) throws IOException {

        return sendBody(null, target, body, proxy, encoding, headers, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param target target address
     * @param params parameters
     * @param method {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(String target, Map<String, String> params, RequestMethod method)
            throws IOException {

        return sendBody(target, params, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc    basic auth {@link UsernamePasswordCredentials}
     * @param target target address
     * @param params parameters
     * @param method {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  String target, Map<String, String> params, RequestMethod method) throws IOException {

        return sendBody(upc, target, params, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param params parameters
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(InputStream keystore,
                                  char[] password, String target, Map<String, String> params, RequestMethod method)
            throws IOException {

        return sendBody(keystore, password, target, params, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param params parameters
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  InputStream keystore, char[] password, String target,
                                  Map<String, String> params, RequestMethod method) throws IOException {

        return sendBody(upc, keystore, password, target, params,
                null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param target target address
     * @param params parameters
     * @param proxy  proxy address
     * @param method {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(String target, Map<String, String> params,
                                  String proxy, RequestMethod method) throws IOException {

        return sendBody(target, params, proxy, ENCODING, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc    basic auth {@link UsernamePasswordCredentials}
     * @param target target address
     * @param params parameters
     * @param proxy  proxy address
     * @param method {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  String target, Map<String, String> params, String proxy, RequestMethod method)
            throws IOException {

        return sendBody(upc, target, params, proxy, ENCODING, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param params parameters
     * @param proxy    proxy address
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(InputStream keystore,
                                  char[] password, String target, Map<String, String> params,
                                  String proxy, RequestMethod method) throws IOException {

        return sendBody(keystore, password, target, params, proxy,
                ENCODING, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param params parameters
     * @param proxy    proxy address
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  InputStream keystore, char[] password, String target,
                                  Map<String, String> params, String proxy, RequestMethod method) throws IOException {

        return sendBody(upc, keystore, password, target, params,
                proxy, ENCODING, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param target   target address
     * @param params parameters
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(String target, Map<String, String> params,
                                  String proxy, String encoding, RequestMethod method) throws IOException {

        return sendBody(target, params, proxy, encoding, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param target   target address
     * @param params parameters
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  String target, Map<String, String> params, String proxy,
                                  String encoding, RequestMethod method) throws IOException {

        return sendBody(upc, target, params, proxy, encoding, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param params parameters
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(InputStream keystore,
                                  char[] password, String target, Map<String, String> params,
                                  String proxy, String encoding, RequestMethod method) throws IOException {

        return sendBody(keystore, password, target, params, proxy,
                encoding, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param params parameters
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  InputStream keystore, char[] password, String target,
                                  Map<String, String> params, String proxy, String encoding, RequestMethod method)
            throws IOException {

        return sendBody(upc, keystore, password, target, params,
                proxy, encoding, null, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param target   target address
     * @param params parameters
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(String target, Map<String, String> params,
                                  String proxy, String encoding, Map<String, String> headers, RequestMethod method)
            throws IOException {

        return sendBody(null, target, params, proxy, encoding, headers, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param target   target address
     * @param body     request body
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  String target, byte[] body, String proxy, String encoding,
                                  Map<String, String> headers, RequestMethod method) throws IOException {

        return sendBody(upc, null, null, target, body, proxy, encoding, headers, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param body     request body
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(InputStream keystore,
                                  char[] password, String target, byte[] body, String proxy,
                                  String encoding, Map<String, String> headers, RequestMethod method) throws IOException {

        return sendBody(null, keystore, password, target, body,
                proxy, encoding, headers, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param target   target address
     * @param params parameters
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  String target, Map<String, String> params, String proxy,
                                  String encoding, Map<String, String> headers, RequestMethod method) throws IOException {

        return sendBody(upc, null, null, target, params, proxy, encoding,
                headers, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param params parameters
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(InputStream keystore,
                                  char[] password, String target, Map<String, String> params,
                                  String proxy, String encoding, Map<String, String> headers, RequestMethod method)
            throws IOException {

        return sendBody(null, keystore, password, target, params,
                proxy, encoding, headers, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param body     request body
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  InputStream keystore, char[] password, String target,
                                  byte[] body, String proxy, String encoding,
                                  Map<String, String> headers, RequestMethod method) throws IOException {

        return sendBody(upc, keystore, password, target, null, body,
                proxy, encoding, headers, method);

    }

    /**
     * Send a http request with implicit params
     *
     * @param upc      basic auth {@link UsernamePasswordCredentials}
     * @param keystore keyStore InputStream
     * @param password keyStore password
     * @param target   target address
     * @param params parameters
     * @param proxy    proxy address
     * @param encoding (default UTF-8)
     * @param headers headers
     * @param method   {@link com.rockagen.commons.http.RequestMethod}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  InputStream keystore, char[] password, String target,
                                  Map<String, String> params, String proxy, String encoding,
                                  Map<String, String> headers, RequestMethod method) throws IOException {

        return sendBody(upc, keystore, password, target, params,
                null, proxy, encoding, headers, method);
    }

    /**
     * Send a http request with implicit params
     *
     * @param upc        basic auth {@link UsernamePasswordCredentials}
     * @param keystore   keyStore InputStream
     * @param password   keyStore password
     * @param targetHost HttpHost
     * @param uri        URI
     * @param body       request body
     * @param proxyHost  HttpHost
     * @param encoding   (default UTF-8)
     * @param method     {@link com.rockagen.commons.http.RequestMethod}
     * @param headers headers
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  InputStream keystore, char[] password,
                                  HttpHost targetHost, String uri, byte[] body, HttpHost proxyHost,
                                  String encoding, RequestMethod method, Header... headers) throws IOException {

        return sendBody(upc, keystore, password, targetHost, uri,
                new ByteArrayEntity(body), proxyHost, encoding, method, headers);
    }

    private static String sendBody(UsernamePasswordCredentials upc,
                                   InputStream keystore, char[] password, String target,
                                   Map<String, String> params, byte[] body, String proxy,
                                   String encoding, Map<String, String> headers, RequestMethod method) throws IOException {

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
            return sendBody(upc, keystore, password, targetHost,
                    uri, body, proxyHost, encoding, method, _headers);
        } else {
            List<NameValuePair> _params = new ArrayList<NameValuePair>();

            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    NameValuePair nvPair = new BasicNameValuePair(
                            param.getKey(), param.getValue());
                    _params.add(nvPair);
                }
            }
            return sendBody(upc, keystore, password, targetHost,
                    uri, _params, proxyHost, encoding, method, _headers);
        }
    }

    /**
     * Send a http request with implicit params
     *
     * @param upc        basic auth {@link UsernamePasswordCredentials}
     * @param keystore   keyStore InputStream
     * @param password   keyStore password
     * @param targetHost HttpHost
     * @param uri        URI
     * @param params parameters     ( ArrayList NameValuePair)
     * @param proxyHost  HttpHost
     * @param encoding   (default UTF-8)
     * @param method     {@link com.rockagen.commons.http.RequestMethod}
     * @param headers headers
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  InputStream keystore, char[] password,
                                  HttpHost targetHost, String uri, List<NameValuePair> params,
                                  HttpHost proxyHost, String encoding, RequestMethod method, Header... headers)
            throws IOException {

        return sendBody(upc, keystore, password, targetHost, uri,
                new UrlEncodedFormEntity(params), proxyHost, encoding, method, headers);
    }

    /**
     * Send a http request with implicit params
     *
     * @param upc        basic auth {@link UsernamePasswordCredentials}
     * @param keystore   keyStore InputStream
     * @param password   keyStore password
     * @param targetHost HttpHost
     * @param uri        URI
     * @param entity     request {@link HttpEntity}
     * @param proxyHost  HttpHost
     * @param encoding   (default UTF-8)
     * @param method     {@link com.rockagen.commons.http.RequestMethod}
     * @param headers headers
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String sendBody(UsernamePasswordCredentials upc,
                                  InputStream keystore, char[] password,
                                  HttpHost targetHost, String uri, HttpEntity entity,
                                  HttpHost proxyHost, String encoding, RequestMethod method, Header... headers)
            throws IOException {

        if (!uri.startsWith("/")) {
            uri = "/" + uri;
        }

        HttpEntityEnclosingRequestBase hm = getHttpEntityMethod(method, uri);
        if (headers != null && headers.length > 0) {
            hm.setHeaders(headers);
        }
        if (entity != null) {
            hm.setEntity(entity);
        }

        log.debug("url: {} method: {}", getURL(targetHost, uri), method);
        return execute(targetHost, proxyHost, hm, encoding, upc,
                keystore, password);

    }

    /**
     * Get Http method instance by {@link com.rockagen.commons.http.RequestMethod}
     *
     * @param method {@link RequestMethod}
     * @param uri  the uri
     * @return {@link HttpRequestBase}
     */
    private static HttpRequestBase getHttpMethod(RequestMethod method, String uri) {
        HttpRequestBase hm;
        if (method != null) {
            switch (method) {
                case POST:
                    hm = new HttpPost(uri);
                    break;
                case GET:
                    hm = new HttpGet(uri);
                    break;
                case PUT:
                    hm = new HttpPut(uri);
                    break;
                case DELETE:
                    hm = new HttpDelete(uri);
                    break;
                case HEAD:
                    hm = new HttpHead(uri);
                    break;
                case OPTIONS:
                    hm = new HttpOptions(uri);
                    break;
                case TRACE:
                    hm = new HttpTrace(uri);
                    break;
                case PATCH:
                    hm = new HttpPatch(uri);
                    break;
                default:
                    hm = new HttpGet(uri);
                    break;
            }
        } else hm = new HttpGet(uri);
        return hm;
    }

    /**
     * Get Http method instance by {@link com.rockagen.commons.http.RequestMethod}
     *
     * @param method {@link RequestMethod}
     * @param uri the uri
     * @return {@link HttpEntityEnclosingRequestBase}
     */
    private static HttpEntityEnclosingRequestBase getHttpEntityMethod(RequestMethod method, String uri) {
        HttpEntityEnclosingRequestBase hm;
        if (method != null) {
            switch (method) {
                case POST:
                    hm = new HttpPost(uri);
                    break;
                case PUT:
                    hm = new HttpPut(uri);
                    break;
                case PATCH:
                    hm = new HttpPatch(uri);
                    break;
                default:
                    hm = new HttpPost(uri);
                    break;
            }
        } else hm = new HttpPost(uri);
        return hm;
    }

    /**
     * Handler main
     *
     * @param targetHost target {@link HttpHost}
     * @param proxyHost proxy {@link HttpHost}
     * @param httpRequestMethod HttpGet or HttpPost...
     * @param encoding encoding
     * @param upc {@link UsernamePasswordCredentials}
     * @param keystore keystore stream
     * @param password keystore password
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    protected static String execute(HttpHost targetHost, HttpHost proxyHost,
                                    HttpRequest httpRequestMethod, String encoding,
                                    UsernamePasswordCredentials upc, InputStream keystore,
                                    char[] password) throws IOException {

        HttpClientBuilder hcb = HttpClients.custom();
        hcb.setDefaultRequestConfig(getRequestConfig());
        if (proxyHost != null) {
            hcb.setProxy(proxyHost);
        }
        if (keystore != null) {

            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore
                        .getDefaultType());
                trustStore.load(keystore, password);
                SSLContext sslcontext = SSLContexts.custom()
                        .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                        .build();
                SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                        sslcontext);
                hcb.setSSLSocketFactory(ssf);
            } catch (KeyStoreException e) {
                log.error("{}", e.getMessage(), e);
            } catch (CertificateException e) {
                log.error("{}", e.getMessage(), e);
            } catch (NoSuchAlgorithmException e) {
                log.error("{}", e.getMessage(), e);
            } catch (KeyManagementException e) {
                log.error("{}", e.getMessage(), e);
            } finally {
                keystore.close();
            }

        }


        if (upc != null) {
            CredentialsProvider cp = new BasicCredentialsProvider();

            AuthScope as = new AuthScope(targetHost);

            cp.setCredentials(as, upc);
            hcb.setDefaultCredentialsProvider(cp);
        }

        CloseableHttpClient chc = hcb.build();
        try {
            CloseableHttpResponse response = chc.execute(targetHost, httpRequestMethod);
            return getResponse(response, encoding);
        } finally {
            chc.close();
        }

    }

    /**
     * Handle response (resolve response to String,httpClient close,etc.)
     *
     * @param response {@link HttpResponse}
     * @param encoding the encoding,default is {@link #ENCODING}
     * @return result String
     * @throws IOException  if an I/O error occurs
     */
    public static String getResponse(HttpResponse response, String encoding) throws IOException {
        log.debug("status: {}", response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        String retval = "";
        if (entity != null) {
            try {
                if (CommUtil.isBlank(encoding)) {
                    encoding = ENCODING;
                }
                retval = IOUtil.toString(entity.getContent(), encoding);

            } finally {
                EntityUtils.consume(entity);

            }
        }
        return retval;
    }

    /**
     * Get UsernamePasswordCredentials
     *
     * @param usernameSamePassword the same string of username and password
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
     * @param username username
     * @param password password
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
     * @param targetHost target {@link org.apache.http.HttpHost}
     * @param uri the uri
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
     * @param str the http url
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
                        } else {

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

    /**
     * Overrid this method if you want configure http connection parameters
     *
     * @return {@link org.apache.http.client.config.RequestConfig}
     */
    protected static RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SO_TIMEOUT).build();
    }

}

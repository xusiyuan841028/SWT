/*
 * Created on May 5, 2013 10:18:59 PM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package http;

import static utils.FileUtils.EXCEL_2003_FILE_AFFIX;
import static utils.FileUtils.TEMP_EXCEL_PATH;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import jodd.jerry.Jerry;
import jodd.lagarto.dom.Node;
import model.Account;
import model.QueryParameter;
import model.QueryProfile;
import ocr.CaptchaOCR;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import utils.FileUtils;

/**
 * @author Phoenix Xu
 */
public class HttpDownloader implements Closeable {

    private static final Logger     logger              = LoggerFactory.getLogger(HttpDownloader.class);

    private static final String     COOKIE_NAME_SESSION = "JSESSIONID";

    private static final String     LAST_MONTH          = "12";

    private DefaultHttpClient       client;

    private Account                 account;

    private QueryParameter          query;

    private String                  sessionId;

    private String                  sessionIdyhl;

    private ResponseHandler<String> responseHandler;

    /**
     * Create an HttpClient for handling HTTPS request.
     * 
     * @return An HttpClient for handling HTTPS request.
     * @throws HttpException
     */
    private DefaultHttpClient createHttpsClient() throws HttpException {
        DefaultHttpClient client = new DefaultHttpClient();
        SSLContext ctx = null;
        X509TrustManager tm = new TrustAnyTrustManager();
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { tm }, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new HttpException("Can't create a Trust Manager.", e);
        }
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, ssf));
        return client;
    }

    /**
     * HttpDownloader Constructor
     * 
     * @param account
     * @param query
     * @throws HttpException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public HttpDownloader(QueryProfile profile) throws HttpException {
        this.account = profile.account;
        this.query = profile.parameter;
        this.client = this.createHttpsClient();
        this.responseHandler = new BasicResponseHandler();
        this.loginSystem();
    }

    /**
     * Log in system.
     * 
     * @throws HttpException
     */
    public void loginSystem() throws HttpException {
        HttpGet httpget = new HttpGet(HttpURL.getURLByCity(this.account.city));
        try {
            String responseBody = this.client.execute(httpget, this.responseHandler);
            Jerry jerry = Jerry.jerry().enableHtmlMode().parse(responseBody);
            Node[] nodes = jerry.$("input[name=\"sessid\"]").get();
            this.sessionIdyhl = nodes[0].getAttribute("value");

            // TODO: Ensure get session id.
            List<Cookie> cookies = this.client.getCookieStore().getCookies();
            // Get session id
            this.sessionId = getSessionId(cookies);
            // Captcha OCR
            String captchaCode = this.getCaptchaCode();

            HttpPost httppost = new HttpPost(HttpURL.getLoginURL());
            List<NameValuePair> loginFields = new ArrayList<NameValuePair>();
            loginFields.add(new BasicNameValuePair("sessidyhl", this.sessionId));
            loginFields.add(new BasicNameValuePair("sessid", this.sessionId));
            loginFields.add(new BasicNameValuePair("loginWay", "0"));
            loginFields.add(new BasicNameValuePair("accountCode", this.account.accountNum));
            loginFields.add(new BasicNameValuePair("password", this.account.password));
            loginFields.add(new BasicNameValuePair("verifyCode", captchaCode));
            loginFields.add(new BasicNameValuePair("action", "initFrom"));
            loginFields.add(new BasicNameValuePair("parammenu", ""));
            loginFields.add(new BasicNameValuePair("loginFlag", ""));
            loginFields.add(new BasicNameValuePair("pageFlag", "0"));
            loginFields.add(new BasicNameValuePair("guestphone", ""));
            httppost.setEntity(new UrlEncodedFormEntity(loginFields, "UTF-8"));
            // Login

            this.client.execute(httppost, this.responseHandler);

            // TODO: forcedly login
        } catch (UnsupportedEncodingException e) {
            throw new HttpException("Encoding error.", e);
        } catch (IOException e) {
            throw new HttpException("Can't login system.", e);
        }
    }

    /**
     * Download all Excel files.
     * 
     * @return
     */
    public ArrayList<QueryParameter> downloadExcel() {
        ArrayList<QueryParameter> queries = new ArrayList<QueryParameter>(this.query.interval);
        for (int i = 0; i < this.query.interval; i++) {
            QueryParameter query = new QueryParameter(this.query.year - i);
            queries.add(query);
            this.downloadSingleExcel(query);
        }
        return queries;
    }

    /**
     * Download an Excel file.
     * 
     * @param query
     */
    public void downloadSingleExcel(QueryParameter query) {
        try {
            // Get excel
            HttpPost httppost = new HttpPost(HttpURL.getBillURL());
            List<NameValuePair> exportExcelFields = new ArrayList<NameValuePair>();
            exportExcelFields.add(new BasicNameValuePair("reservedProp(timeYear)", query.getYearString()));
            exportExcelFields.add(new BasicNameValuePair("reservedProp(timeMonth)", LAST_MONTH));
            exportExcelFields.add(new BasicNameValuePair("reservedProp(yearId)", query.getYearString()));
            exportExcelFields.add(new BasicNameValuePair("reservedProp(chars)", ""));
            exportExcelFields.add(new BasicNameValuePair("reservedProp(hiddenDate)", query.getYearString() + LAST_MONTH));
            exportExcelFields.add(new BasicNameValuePair("reservedProp(type)", "power"));
            exportExcelFields.add(new BasicNameValuePair("errorInfo", "power"));
            exportExcelFields.add(new BasicNameValuePair("maxrow", "12"));
            exportExcelFields.add(new BasicNameValuePair("action", "expExcel"));
            httppost.setEntity(new UrlEncodedFormEntity(exportExcelFields, "UTF-8"));
            HttpEntity entity = this.client.execute(httppost).getEntity();
            this.saveExcelFile(entity, getTempExcelName(this.account, query));
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get temp excel file name.
     * 
     * @param account
     * @param query
     * @return
     */
    public static String getTempExcelName(Account account, QueryParameter query) {
        return query.getYearString() + FileUtils.FILE_NAME_INTERNAL_SEPARATOR + account.accountNum;
    }

    /**
     * Get JSESSIONID from cookies.
     * 
     * @param cookies
     * @return
     */
    public static String getSessionId(List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_NAME_SESSION)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Do OCR for captcha.
     * 
     * @param client
     * @return
     */
    public String getCaptchaCode() {
        HttpGet httpget = new HttpGet(HttpURL.getCaptchaURL());
        try {
            HttpEntity entity = this.client.execute(httpget).getEntity();

            if (entity != null) {
                InputStream is = entity.getContent();
                File captchaImage = new File(CaptchaOCR.CAPTCHA_IMAGE_PATH);
                OutputStream os = new FileOutputStream(captchaImage);
                IOUtils.copy(is, os);
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }
            EntityUtils.consume(entity);
            return CaptchaOCR.doOcr();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get iRand
     * 
     * @return
     */
    public String getRandCode() {
        HttpGet httpget = new HttpGet(HttpURL.getRandURL());
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = null;
        try {
            responseBody = this.client.execute(httpget, responseHandler);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return responseBody;
    }

    /**
     * Save excel file.
     * 
     * @param entity HTTP Entity
     * @param fileName The file name
     */
    public void saveExcelFile(HttpEntity entity, String fileName) {
        Assert.notNull(entity);
        File excelFile = new File(TEMP_EXCEL_PATH + fileName + EXCEL_2003_FILE_AFFIX);
        try {
            InputStream is = entity.getContent();
            OutputStream os = new FileOutputStream(excelFile);
            IOUtils.copy(is, os);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Log off system.
     * 
     * @throws HttpException
     */
    public void logoffSystem() throws HttpException {
        HttpGet httpget = new HttpGet(HttpURL.getLogoffURL(this.sessionIdyhl));
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            this.client.execute(httpget, responseHandler);
        } catch (IOException e) {
            throw new HttpException("Can't logoff system.", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() {
        try {
            this.logoffSystem();
        } catch (HttpException e) {
            logger.error("Can't log off the system.", e);
        }
        this.client.getConnectionManager().shutdown();
    }
}

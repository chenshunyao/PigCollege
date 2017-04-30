
package com.xnf.henghenghui.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.content.pm.PackageManager;

import com.xnf.henghenghui.util.NetworkUtil;

public class UpdateManager {
    @SuppressWarnings("unused")
    private PackageManager packageManager = null;
    int i = 0;

    // public static final String UPDATE_SERVER = "http://qdk.me/apps/defens/upload/detron.xml";
    // public static final String UPDATE_SERVER_EN =
    // "http://qdk.me/apps/defens/upload/detron_en.xml";
    // public static final String UPDATE_SERVER_TW =
    // "http://qdk.me/apps/defens/upload/detron_tw.xml";
    // public static final String DOWN_LOAD_SERVER = "http://qdk.me/apps/defens/upload/";
    public static final String UPDATE_SERVER = "http://111.202.38.147:8080/upload/detron.xml";
    public static final String UPDATE_SERVER_EN = "http://111.202.38.147:8080/upload/detron.xml";
    public static final String UPDATE_SERVER_TW = "http://111.202.38.147:8080/upload/detron.xml";
    public static final String DOWN_LOAD_SERVER = "http://111.202.38.147:8080/download/";

    private String updateServerUrl = null;
    public static final String UPDATE_CONFIG_FILE_MAIN_NODE = "main";
    public static final String UPDATE_CONFIG_FILE_MAIN_NAME_NODE = "main_name";
    public static final String UPDATE_CONFIG_FILE_MAIN_APK_NODE = "main_apk";
    public static final String UPDATE_CONFIG_FILE_MAIN_APK_SIZE_NODE = "main_apk_size";
    public static final String UPDATE_CONFIG_FILE_MAIN_VERSION_NODE = "main_version";
    public static final String UPDATE_CONFIG_FILE_MAIN_VERSION_NAME_NODE = "main_version_name";
    public static final String UPDATE_CONFIG_FILE_MAIN_DESCRIPTION_NODE = "main_description";

    public static final String UPDATE_CONFIG_FILE_APPS_NODE = "apps";
    public static final String UPDATE_CONFIG_FILE_APP_NODE = "app";
    public static final String UPDATE_CONFIG_FILE_APP_NAME_NODE = "apk_name";
    public static final String UPDATE_CONFIG_FILE_APK_NODE = "apk";
    public static final String UPDATE_CONFIG_FILE_APK_SIZE_NODE = "apk_size";
    public static final String UPDATE_CONFIG_FILE_VERSION_NODE = "version";
    public static final String UPDATE_CONFIG_FILE_VERSION_NAME_NODE = "version_name";
    public static final String UPDATE_CONFIG_FILE_DESCRIPTION_NODE = "description";
    public static final String UPDATE_CONFIG_FILE_PACKAGE_NODE = "package";

    public static final String UPDATE_CONFIG_FILE_TIPS = "tips";
    public static final String UPDATE_CONFIG_FILE_TIP_TITLE = "tip_title";
    public static final String UPDATE_CONFIG_FILE_TIP_CONTENTS = "tip_contents";
    private Map<String, String> mainUpdateInfo = null;
    private Map<String, String> appUpdateInfo = null;
    private List<Map<String, String>> appsUpdateInfo = null;
    private Map<String, String> tips = null;
    private StringBuilder sb = new StringBuilder();
    Context mContext;
    int localecode;

    public UpdateManager(Context context, int localeCode) {
        this.mContext = context;
        this.packageManager = mContext.getPackageManager();
        this.updateServerUrl = getUrlByCode(localeCode);
    }

    private String getUrlByCode(int localcode) {
        if (localcode == 0) {
            return UPDATE_SERVER;
        } else if (localcode == 1) {
            return UPDATE_SERVER_TW;
        } else {
            return UPDATE_SERVER_EN;
        }
    }

    public static String getFileNameByCode(int localcode) {
        if (localcode == 0) {
            return "/version_cn.xml";
        } else if (localcode == 1) {
            return "/version_tw.xml";
        } else {
            return "/version_en.xml";
        }
    }

    private List<Map<String, String>> parseUpdateConfigFile() throws IOException {

        try {
            URL url = new URL(updateServerUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            InputStream is = conn.getInputStream();
            File file = new File(mContext.getFilesDir().toString() + getFileNameByCode(localecode));
            if (file.exists()) {
                file.delete();
            }
            inputstreamtofile(is, file);
            return parseDataFromFile(file);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private List<Map<String, String>> parseDataFromFile(File file) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        if (!file.exists()) {
            return null;
        }
        try {
            SAXParser parser;
            parser = factory.newSAXParser();

            parser.parse(file, new DefaultHandler() {

                @Override
                public void startDocument() throws SAXException {
                    super.startDocument();
                }

                @Override
                public void startElement(String uri, String localName,
                                         String qName, Attributes attributes)
                        throws SAXException {
                    super.startElement(uri, localName, qName, attributes);
                    sb.setLength(0);
                    if (localName.equals(UPDATE_CONFIG_FILE_APP_NODE)) {
                        appUpdateInfo = new HashMap<String, String>();
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_MAIN_NODE)) {
                        mainUpdateInfo = new HashMap<String, String>();
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_APPS_NODE)) {
                        appsUpdateInfo = new ArrayList<Map<String, String>>();
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_TIPS)) {
                        tips = new HashMap<String, String>();
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length)
                        throws SAXException {
                    sb.append(ch, start, length);
                    if (sb == null || sb.equals("")) {
                        return;
                    }
                    super.characters(ch, start, length);
                }

                @Override
                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {
                    super.endElement(uri, localName, qName);
                    String value = sb.toString();
                    if (localName.equals(UPDATE_CONFIG_FILE_MAIN_NAME_NODE)) {
                        mainUpdateInfo.put(UPDATE_CONFIG_FILE_MAIN_NAME_NODE,
                                value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_MAIN_APK_NODE)) {
                        mainUpdateInfo.put(UPDATE_CONFIG_FILE_MAIN_APK_NODE,
                                value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_MAIN_APK_SIZE_NODE)) {
                        mainUpdateInfo.put(
                                UPDATE_CONFIG_FILE_MAIN_APK_SIZE_NODE, value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_MAIN_VERSION_NODE)) {
                        mainUpdateInfo.put(
                                UPDATE_CONFIG_FILE_MAIN_VERSION_NODE, value);
                    }
                    if (localName
                            .equals(UPDATE_CONFIG_FILE_MAIN_VERSION_NAME_NODE)) {
                        mainUpdateInfo.put(
                                UPDATE_CONFIG_FILE_MAIN_VERSION_NAME_NODE,
                                value);
                    }
                    if (localName
                            .equals(UPDATE_CONFIG_FILE_MAIN_DESCRIPTION_NODE)) {
                        mainUpdateInfo
                                .put(UPDATE_CONFIG_FILE_MAIN_DESCRIPTION_NODE,
                                        value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_APK_NODE)) {
                        appUpdateInfo.put(UPDATE_CONFIG_FILE_APK_NODE, value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_APK_SIZE_NODE)) {
                        appUpdateInfo.put(UPDATE_CONFIG_FILE_APK_SIZE_NODE,
                                value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_VERSION_NODE)) {
                        appUpdateInfo.put(UPDATE_CONFIG_FILE_VERSION_NODE,
                                value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_VERSION_NAME_NODE)) {
                        appUpdateInfo.put(UPDATE_CONFIG_FILE_VERSION_NAME_NODE,
                                value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_DESCRIPTION_NODE)) {
                        appUpdateInfo.put(UPDATE_CONFIG_FILE_DESCRIPTION_NODE,
                                value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_PACKAGE_NODE)) {
                        appUpdateInfo.put(UPDATE_CONFIG_FILE_PACKAGE_NODE,
                                value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_APP_NAME_NODE)) {
                        appUpdateInfo.put(UPDATE_CONFIG_FILE_APP_NAME_NODE,
                                value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_TIP_TITLE)) {
                        tips.put(UPDATE_CONFIG_FILE_TIP_TITLE, value);
                    }
                    if (localName.equals(UPDATE_CONFIG_FILE_TIP_CONTENTS)) {
                        tips.put(UPDATE_CONFIG_FILE_TIP_CONTENTS, value);
                    }

                    if (localName.equals(UPDATE_CONFIG_FILE_APP_NODE)) {
                        appsUpdateInfo.add(appUpdateInfo);
                    }
                }

            });
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appsUpdateInfo;
    }

    private void inputstreamtofile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8 * 1024];
            while ((bytesRead = ins.read(buffer, 0, 8 * 1024)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getTempFile() {
        File file = new File(mContext.getFilesDir().toString()
                + UpdateManager.getFileNameByCode(localecode));
        return file;
    }

    public List<Map<String, String>> getAppsUpdateInfo() throws IOException {
        if (appsUpdateInfo == null) {
            List<Map<String, String>> data = NetworkUtil.enableConneting(mContext) ? parseUpdateConfigFile()
                    : null;
            appsUpdateInfo = data != null ? data : parseDataFromFile(getTempFile());
            if (appsUpdateInfo == null || appsUpdateInfo.isEmpty()) {
                return null;
            } else {
                return appsUpdateInfo;
            }
        }
        return appsUpdateInfo;
    }

    public Map<String, String> getTips() throws IOException {
        if (tips == null) {
            appsUpdateInfo = parseUpdateConfigFile();
            if (appsUpdateInfo == null || appsUpdateInfo.isEmpty()) {
                return null;
            } else {
                return tips;
            }
        }
        return tips;
    }



}


package com.xnf.henghenghui.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

import com.xnf.henghenghui.config.Constants;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppManager {
    private PackageManager packageManager = null;
    private List<Map<String, Object>> appsInfo = null;
    private Map<String, Object> appInfo = null;

    private static final String APP = "app";
    public static final String APP_NAME = "name";
    public static final String APP_PACKAGE = "package";
    public static final String APP_ACTIVITY = "activity";

    public static final String APP_XML_LOCATION = Constants.fileLocation
            + "download/xml/";
    public static final String APK_FILE_LOCATION = Constants.fileLocation
            + "download/";
    public static final String APK_FILE_TEMP_LOCATION = Constants.fileLocation
            + "download/temp/";

    public static final String APP_FILE_NAME = "defensplus.xml";
    public static final String APP_FILE_NAME_TEMP = "defensplus.temp";
    private Context context = null;

    private static final String TAG = "AppManager";

    public AppManager(Context context) {
        super();
        this.packageManager = context.getPackageManager();
        this.context = context;

    }

    public List<Map<String, Object>> getAppsInfo() {
        try {
            FileInputStream fis = context.openFileInput(AppManager.APP_FILE_NAME);
            ;
            parseAppConfigFile(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appsInfo;
    }

    private void parseAppConfigFile(InputStream is) throws IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            parser.parse(is, new DefaultHandler() {
                String currentNodeName = null;

                @Override
                public void startDocument() throws SAXException {
                    super.startDocument();
                    appsInfo = new ArrayList<Map<String, Object>>();
                }

                @Override
                public void startElement(String uri, String localName,
                                         String qName, Attributes attributes)
                        throws SAXException {
                    currentNodeName = localName;
                    super.startElement(uri, localName, qName, attributes);
                    if (currentNodeName.equals(APP)) {
                        appInfo = new HashMap<String, Object>();
                    }
                }

                @Override
                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {
                    super.endElement(uri, localName, qName);
                    if (localName.equals(APP)) {
                        appsInfo.add(appInfo);
                    }

                }

                @Override
                public void characters(char[] ch, int start, int length)
                        throws SAXException {
                    String str = new String(ch, start, length).trim();
                    if (str == null || str.equals("")) {
                        return;
                    }
                    if (currentNodeName.equals(APP_NAME)) {
                        appInfo.put(APP_NAME, str);
                    }
                    if (currentNodeName.equals(APP_PACKAGE)) {
                        appInfo.put(APP_PACKAGE, str);
                    }
                    if (currentNodeName.equals(APP_ACTIVITY)) {
                        appInfo.put(APP_ACTIVITY, str);
                    }
                    super.characters(ch, start, length);
                }

            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到应用程序的图标
     *
     * @param packageName
     * @return
     */
    public Drawable getAppIcon(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.applicationInfo.loadIcon(packageManager);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查一个应用是否已安装
     *
     * @param packageName
     * @return
     */
    public boolean ifAppExist(String packageName) {
        return getAppIcon(packageName) != null;
    }

    /**
     * 获得本机应用的版本号
     *
     * @return
     * @throws NameNotFoundException
     */
    public int getAppVersionCode(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获得本机应用的版本名称
     *
     * @return
     */
    public String getAppVersionName(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查是否需要安装
     *
     * @param packageName
     * @return
     */
    public boolean isNeedInstall(String packageName) {
        return this.getAppIcon(packageName) == null;
    }

    /**
     * 检查是否需要更新
     *
     * @param packageName
     * @param remoteVersion
     * @return
     */
    public boolean isNeedUpdate(String packageName, String remoteVersion) {
        boolean flag = false;
        int localVersion = this.getAppVersionCode(packageName);
        int remoteVersionValue = Integer.parseInt(remoteVersion);
        if (localVersion < remoteVersionValue) {
            flag = true;
        }
        return flag;
    }

    /**
     * 检查是否需要下载
     *
     * @param apkFile
     * @return
     */
    public boolean isNeedDownload(String apkFile, String packageName,
                                  String remoteVersion) {
        String filePath = AppManager.APK_FILE_LOCATION + remoteVersion + "_"
                + apkFile;
        File file = new File(filePath);
        if (file.exists()) {
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(filePath, 0);
            if (packageInfo != null && remoteVersion.equals(packageInfo.versionCode + "")) {
                Log.i(TAG, "AppManager>>" + filePath
                        + "------- is exists,not need download");
                return false;
            }
            return true;
        }
        Log.i(TAG, "AppManager>>" + filePath
                + "------- is not exists,need download");
        return true;
    }

}

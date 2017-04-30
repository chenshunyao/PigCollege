package com.xnf.henghenghui.io;

import static android.os.Environment.MEDIA_MOUNTED;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

//import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.xnf.henghenghui.HengHengHuiAppliation;
import com.xnf.henghenghui.util.L;

/**
 * UserInfo Files Disc Cache ,include image,audio,video,VCard,etc..
 * 
 * @author laotian
 * 
 */
public class MyDiscCache implements DiskCache {
    
	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
	
	private static final String cachePath = "cmcc/qixin" ;
	
	public MyDiscCache(){
		
	}

	@Override
	public File getDirectory() {
		return null;
	}

	@Override
	public boolean save(String imageUri, InputStream imageStream, IoUtils.CopyListener listener) throws IOException {
		return false;
	}

	@Override
	public boolean save(String imageUri, Bitmap bitmap) throws IOException {
		return false;
	}

	@Override
	public boolean remove(String imageUri) {
		return false;
	}

	@Override
	public void close() {

	}

	@Override
	public File get(String key) {
		return getCacheFile(FileType.Image, key);
	}

	public static File getCacheDirectory() {
/*		if(TextUtils.isEmpty(cachePath)){
			return StorageUtils.getCacheDirectory(QixinApplication.getInstance()
					.getApplicationContext());
		}*/
		return getCacheDirectory(HengHengHuiAppliation.getInstance()
				.getApplicationContext(),cachePath);
	}

	public static File getCacheDirectory(FileType fileType) {
		File file = new File(getCacheDirectory(), fileType.getSubDirectory());
		if (!file.exists())
			file.mkdir();
		return file;
	}

	public  static File getCacheFile(FileType fileType, String key)
	{
	    if(fileType==null || key==null )
	        throw new IllegalArgumentException("FileType and key can't be null");
		FileNameGenerator fileNameGenerator=new Md5FileNameGenerator();
		String fileName = fileNameGenerator.generate(key);
		//Modified for zip download and unzip; 2014-06-20 laotian
		//The best way is implement FileNameGenerator just as DetailFileNameGenerator
		if(fileType==FileType.ZIP &&!TextUtils.isEmpty(fileName))
		{
		    fileName+=".zip";
		}
		return new File(getCacheDirectory(fileType), fileName);
	}

	/**
	 * <B>CLEAR ALL OF USER CACHE FILES IN DISK, IT"S DANGEROUS!</B>
	 * 
	 * @author laotian
	 */
	@Override
	public void clear() {
		File cacheDir = getCacheDirectory();
		deleteDir(cacheDir);
	}
	
	private void deleteDir(File dir)
	{
		if(!dir.exists()) return;		
		File[] files = dir.listFiles();
		if (files != null) {
			for (File f : files) {
				if(f.isFile())	f.delete();
				if(f.isDirectory()) deleteDir(f);
			}
		}
	}
	
    /*@Override
    public void put(String key, File file) {
        // TODO Auto-generated method stub
        
    }*/
	
	@SuppressLint("SdCardPath")
    public static File getCacheDirectory(Context context,String path) {
		File appCacheDir = null;
		if (MEDIA_MOUNTED
				.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
			appCacheDir = getExternalCacheDir(path);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
			L.w("Can't define system cache directory! '%s' will be used.", cacheDirPath);
			appCacheDir = new File(cacheDirPath);
		}
		return appCacheDir;
	}
	
	private static File getExternalCacheDir(String path) {
		File appCacheDir = new File(Environment.getExternalStorageDirectory(), path);
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				L.w("DisckCache","Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				L.i("DisckCache","Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}

}

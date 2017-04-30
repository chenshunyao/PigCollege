package com.xnf.henghenghui.config;

public class ErrorCode {


  /** =======================网络请求错误码========================== */


  /** =======================下载错误码========================== */
  public final static int ERROR_DOWNLOAD_INTERNET = 0x01; // 网络错误，包含网站本身ping不通或者本地网络问题，不作区分

  public final static int ERROR_DOWNLOAD_DISKFULL = 0x02;// 磁盘已满

  public final static int ERROR_DOWNLOAD_OTHER = 0x10;// 未知错误


  /** =======================本地操作错误码========================== */

}

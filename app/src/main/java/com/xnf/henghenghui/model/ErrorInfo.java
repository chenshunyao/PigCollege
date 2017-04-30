package com.xnf.henghenghui.model;

/**
 * Created by Administrator on 2016/4/2.
 */
public class ErrorInfo {

    private Response response;

    public void setResponse(Response response){

        this.response = response;

    }

    public Response getResponse(){

        return this.response;

    }

    public class Response {

        private int succeed;


        private String errorCode;


        private String errorInfo;


        public void setSucceed(int succeed){

            this.succeed = succeed;

        }

        public int getSucceed(){

            return this.succeed;

        }

        public void setErrorCode(String errorCode){

            this.errorCode = errorCode;

        }

        public String getErrorCode(){

            return this.errorCode;

        }

        public void setErrorInfo(String errorInfo){

            this.errorInfo = errorInfo;

        }

        public String getErrorInfo(){

            return this.errorInfo;

        }

    }
}

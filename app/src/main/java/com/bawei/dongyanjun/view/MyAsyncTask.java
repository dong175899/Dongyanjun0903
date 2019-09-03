package com.bawei.dongyanjun.utils;

import android.os.AsyncTask;

import com.bawei.dongyanjun.contract.IHomeContract;

import java.io.IOException;

public class MyAsyncTask extends AsyncTask<Integer,Integer,String> {
    String path;
    IHomeContract.Imodel.IModelCallBack iModelCallBack;
    private String data;

    public MyAsyncTask(String path, IHomeContract.Imodel.IModelCallBack iModelCallBack) {
        this.path=path;
        this.iModelCallBack=iModelCallBack;
    }

    @Override
    protected String doInBackground(Integer... integers) {
        try {
            data = NetUtils.getInstance().getData(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s!=null) {
            iModelCallBack.onHomeSuccess(s);
        }else {
            iModelCallBack.onHomeError("失败");
        }
    }
}

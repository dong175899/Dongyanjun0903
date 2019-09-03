package com.bawei.dongyanjun;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bawei.dongyanjun.adapter.MyAdapter;
import com.bawei.dongyanjun.base.BaseActivity;
import com.bawei.dongyanjun.bean.ShopBean;
import com.bawei.dongyanjun.contract.IHomeContract;
import com.bawei.dongyanjun.presenter.HomePresenter;
import com.google.gson.Gson;
import com.qy.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements IHomeContract.Iview {
    private Handler handler=new Handler();
    private int page=1;
    String path="http://blog.zhaoliang5156.cn/api/shop/shop"+page+".json";
    private List<ShopBean.DataBean> datas=new ArrayList<>();
    private XListView xlv;
    private HomePresenter homePresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        xlv = findViewById(R.id.xlv);
    }

    @Override
    protected void initData() {
        homePresenter = new HomePresenter();
        homePresenter.attachView(MainActivity.this);
        homePresenter.getPresenter(path);
    }

    @Override
    public void onHomeSuccess(String data) {
        Gson gson = new Gson();
        ShopBean shopBean = gson.fromJson(data, ShopBean.class);
        List<ShopBean.DataBean> data1 = shopBean.getData();
        datas.addAll(data1);

        xlv.setPullLoadEnable(true);
        xlv.setPullRefreshEnable(true);
        getServerData(page);
        xlv.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page=1;
                        getServerData(page);

                        xlv.stopRefresh();
                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getServerData(page);
                        xlv.stopLoadMore();
                    }
                },2000);
            }
        });
    }

    private void getServerData(int page) {
        MyAdapter adapter = new MyAdapter(MainActivity.this,datas);
        xlv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onHomeError(String e) {

    }
}

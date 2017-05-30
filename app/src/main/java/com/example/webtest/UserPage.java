package com.example.webtest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;


public class UserPage extends BaseActivity {

    private List<HelpEachOther> helpEachOtherListGiven = new ArrayList<>();
    private List<HelpEachOther> helpEachOtherListGot = new ArrayList<>();
    private UserAdapter adapterGiven, adapterGot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        initUserGiven();
        RecyclerView recyclerViewGiven = (RecyclerView) findViewById(R.id.recyclerViewGiven);
        LinearLayoutManager layoutManagerGiven = new LinearLayoutManager(this);
        recyclerViewGiven.setLayoutManager(layoutManagerGiven);
        adapterGiven = new UserAdapter(this, helpEachOtherListGiven);
        recyclerViewGiven.setAdapter(adapterGiven);

        initUserGot();
        RecyclerView recyclerViewGot = (RecyclerView) findViewById(R.id.recyclerViewGot);
        LinearLayoutManager layoutManagerGot = new LinearLayoutManager(this);
        recyclerViewGot.setLayoutManager(layoutManagerGot);
        adapterGot = new UserAdapter(this, helpEachOtherListGot);
        recyclerViewGot.setAdapter(adapterGot);
    }

    private void initUserGiven() {
        helpEachOtherListGiven.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Help");
        avQuery.whereLessThanOrEqualTo("history", 1);
        avQuery.orderByDescending("createdAt");
        avQuery.include("release");
        avQuery.include("accept");

        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getAVUser("release").getUsername()
                                .equals(AVUser.getCurrentUser().getUsername())) {

                            HelpEachOther helpEachOther = new HelpEachOther(null, null
                                    , null, null
                                    , 0, 0
                                    , 0, 0, null
                                    , null, null, null);
                            helpEachOther.setContent(list.get(i).getString("content"));
                            helpEachOther.setRemark(list.get(i).getString("remark"));
                            helpEachOther.setLocation(list.get(i).getString("location"));
                            helpEachOther.setReleaseUser(list.get(i).getAVUser("release"));
                            helpEachOther.setLatitude(list.get(i).getDouble("latitude"));
                            helpEachOther.setLongitude(list.get(i).getDouble("longitude"));
                            helpEachOther.setCreatedAt(list.get(i).getCreatedAt());
                            helpEachOther.setUpdatedAt(list.get(i).getUpdatedAt());
                            helpEachOther.setObjectIDnumber(list.get(i).getObjectId());
                            helpEachOther.setTalkUser(1);
                            helpEachOther.setHistory(list.get(i).getInt("history"));
                            helpEachOther.setAcceptUser(list.get(i).getAVUser("accept"));

                            helpEachOtherListGiven.add(helpEachOther);
                            adapterGiven.notifyDataSetChanged();
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initUserGot() {
        helpEachOtherListGot.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Help");
        avQuery.whereEqualTo("history", 1);
        avQuery.orderByDescending("updatedAt");
        avQuery.include("release");
        avQuery.include("accept");

        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getAVUser("accept").getUsername()
                                .equals(AVUser.getCurrentUser().getUsername())) {
                            HelpEachOther helpEachOther = new HelpEachOther(null, null
                                    , null, null
                                    , 0, 0
                                    , 0, 0, null
                                    , null, null, null);
                            helpEachOther.setContent(list.get(i).getString("content"));
                            helpEachOther.setRemark(list.get(i).getString("remark"));
                            helpEachOther.setLocation(list.get(i).getString("location"));
                            helpEachOther.setReleaseUser(list.get(i).getAVUser("release"));
                            helpEachOther.setLatitude(list.get(i).getDouble("latitude"));
                            helpEachOther.setLongitude(list.get(i).getDouble("longitude"));
                            helpEachOther.setCreatedAt(list.get(i).getCreatedAt());
                            helpEachOther.setUpdatedAt(list.get(i).getUpdatedAt());
                            helpEachOther.setObjectIDnumber(list.get(i).getObjectId());
                            helpEachOther.setAcceptUser(list.get(i).getAVUser("accept"));
                            helpEachOther.setTalkUser(2);
                            helpEachOtherListGot.add(helpEachOther);
                            adapterGot.notifyDataSetChanged();
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}

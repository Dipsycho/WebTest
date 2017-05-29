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

public class AcceptPage extends BaseActivity {

    private List<HelpEachOther> helpEachOtherList = new ArrayList<>();
    private HelpAcceptAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_page);

        initHelp();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyItemDecoration());
        adapter = new HelpAcceptAdapter(this,helpEachOtherList);
        recyclerView.setAdapter(adapter);

    }

    private void initHelp() {
        helpEachOtherList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Help");
        avQuery.whereEqualTo("history", 0);
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
                        } else {
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
                            helpEachOtherList.add(helpEachOther);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

    }


}

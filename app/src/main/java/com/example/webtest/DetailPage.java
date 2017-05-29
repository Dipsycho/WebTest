package com.example.webtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;

public class DetailPage extends AppCompatActivity {

    private String objectIdNo;
    private TextView dpContent;
    private TextView dpRemark;
    private TextView dpRelease;
    private TextView dpAccept;
    private TextView dpLocation;
    private TextView dpCreated;
    private TextView dpUpdated;
    private TextView dpID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        objectIdNo=getIntent().getStringExtra("OBJECT_ID_NO");

        dpContent=(TextView)findViewById(R.id.dpContent);
        dpRemark=(TextView)findViewById(R.id.dpRemark);
        dpRelease=(TextView)findViewById(R.id.dpRelease);
        dpAccept=(TextView)findViewById(R.id.dpAccept);
        dpLocation=(TextView)findViewById(R.id.dpLocation);
        dpCreated=(TextView)findViewById(R.id.dpCreated);
        dpUpdated=(TextView)findViewById(R.id.dpUpdated);
        dpID=(TextView)findViewById(R.id.dpID);

        AVQuery<AVObject> avQuery=new AVQuery<>("Help");
        avQuery.include("release");
        avQuery.include("accept");
        avQuery.getInBackground(objectIdNo, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                dpContent.setText("内容："+avObject.getString("content"));
                dpRemark.setText("备注："+avObject.getString("remark"));
                dpRelease.setText("发布者："+avObject.getAVUser("release").getUsername());
                if (avObject.getAVUser("accept").getUsername().equals(AVUser.getCurrentUser().getUsername())&&avObject.getInt("history")==0)
                {
                    dpAccept.setText("接受者：NULL");
                }else {
                    dpAccept.setText("接受者：" + avObject.getAVUser("accept").getUsername());
                }
                dpLocation.setText("位置："+avObject.getString("location"));
                dpCreated.setText("发布时间："+avObject.getCreatedAt());
                dpUpdated.setText("更新时间："+avObject.getUpdatedAt());
                dpID.setText("编号："+avObject.getObjectId());
            }
        });
    }
}

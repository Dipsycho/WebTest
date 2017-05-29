package com.example.webtest;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class HelpPage extends BaseActivity implements View.OnClickListener {

    private List<String> helpItem;
    private ArrayAdapter<String> adapter;
    private double latitude = 0, longitude = 0;
    private EditText editText;
    private String content;
    private AVGeoPoint point;
    private String location;
//    private HelpEachOther helpEachOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);

        Button button = (Button) findViewById(R.id.button);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        editText = (EditText) findViewById(R.id.editText);

        helpItem = new ArrayList<String>();
        helpItem.add("取快递");
        helpItem.add("取外卖");
        helpItem.add("取自行车");
        helpItem.add("其他");
        adapter = new ArrayAdapter<String>
                (this, R.layout.support_simple_spinner_dropdown_item, helpItem);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                content=spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                content="其他";
            }
        });


        latitude=getIntent().getDoubleExtra("latitude_data",0);
        longitude=getIntent().getDoubleExtra("longitude_data",0);
        point=new AVGeoPoint(latitude,longitude);
        location= getIntent().getStringExtra("location_data");

        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        AVObject help = new AVObject("Help");

        help.put("release", AVUser.getCurrentUser());
        help.put("accept",AVUser.getCurrentUser());
        help.put("location",location);
        help.put("point",point);
        help.put("latitude", latitude);
        help.put("longitude", longitude);
        help.put("remark", editText.getText().toString());
        help.put("content",content);
        help.put("history",0);

        help.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(HelpPage.this, "发布成功", Toast.LENGTH_SHORT).show();
                    HelpPage.this.finish();
                } else {
                    Toast.makeText(HelpPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

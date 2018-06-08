package com.yaphets.dock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yaphets.dock.DockApplication;
import com.yaphets.dock.R;
import com.yaphets.dock.ui.view.TitleLayout;
import com.yaphets.storage.database.dao.MySqlDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditBaseActivity extends AppCompatActivity {
    private int _type;
    private String _data;

    private EditText _editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_base);

        Intent intent = getIntent();
        _type = intent.getIntExtra("type", 0);
        _data = intent.getStringExtra("data");

        final TitleLayout title = findViewById(R.id.eba_title_bar);
        if (_type == IndividualActivity.MODIFY_NAME) {
            title.setTitle("更改名字");
        }

        title.setSaveCallBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nData = _editText.getText().toString();
                if ("".equals(nData)) {
                    Toast.makeText(EditBaseActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadData(nData);
                    }
                }).start();
            }
        });
        title.enableSaveBtn(false);

        _editText = findViewById(R.id.eba_edit);
        _editText.setText(_data);
        _editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(_data)) {
                    title.enableSaveBtn(false);
                } else {
                    title.enableSaveBtn(true);
                }
            }
        });
    }

    /**
     * 直接通过DAO更新数据库
     * @param nData
     *      新编辑的数据
     */
    private void uploadData(String nData) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = MySqlDAO.getConnection();
            String sql;
            if (_type == IndividualActivity.MODIFY_NAME) {
                sql = "UPDATE user SET nickname=? WHERE email=?";
            } else {
                return;
            }
            ps = con.prepareStatement(sql);
            ps.setString(1, nData);
            ps.setString(2, DockApplication._email);
            ps.executeUpdate();

            //如果成功，则返回
            Intent resIntent = new Intent();
            resIntent.putExtra("resp", nData);
            setResult(RESULT_OK,resIntent);
            finish();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(EditBaseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            MySqlDAO.release(con, ps);
        }
    }
}

package com.yaphets.dock.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yaphets.dock.DockApplication;
import com.yaphets.dock.R;
import com.yaphets.dock.database.dao.MySqlDAO;
import com.yaphets.dock.model.entity.UserInfo;
import com.yaphets.dock.util.PhotoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IndividualActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "IndividualActivity";

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int CROP_IMAGE = 3;

    public static final int MODIFY_NAME = 4;

    private File fileUri;
    private File fileCropUri;
    private Uri imageUri;
    private Uri cropImageUri;

    private RelativeLayout _root_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);

        initView();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        UserInfo info = UserInfo.getInstance();
        ImageView iv_thumb = _root_layout.findViewById(R.id.ia_iv_thumb);
        iv_thumb.setImageBitmap(info.getThumbBitmap());
        TextView tv_nickname = _root_layout.findViewById(R.id.ia_tv_nickname);
        tv_nickname.setText(info.getNickname());

        fileUri = new File(getExternalCacheDir() + "/photo.jpg");
        fileCropUri = new File(getExternalCacheDir() + "/crop_thumb.jpg");
    }

    private void initView() {
        _root_layout = findViewById(R.id.ia_layout_root);

        RelativeLayout _thumb_layout = findViewById(R.id.ia_layout_thumb);
        _thumb_layout.setOnClickListener(this);

        RelativeLayout _name_layout = findViewById(R.id.ia_layout_name);
        _name_layout.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ia_layout_thumb:
                if (ContextCompat.checkSelfPermission(IndividualActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(IndividualActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                break;
            case R.id.ia_layout_name: {
                Intent intent = new Intent(this, EditBaseActivity.class);
                intent.putExtra("type", MODIFY_NAME);
                intent.putExtra("data", UserInfo.getInstance().getNickname());
                startActivityForResult(intent, MODIFY_NAME);
                break;
            }
            default:
        }
    }

    /**
     * 调用系统相册
     */
    private void openAlbum() {
        /*Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);*/
        PhotoUtils.openPic(IndividualActivity.this, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CROP_IMAGE);
                    break;
                case CHOOSE_PHOTO:
                    /*if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }*/

                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)     //即sdk>=24
                            newUri = FileProvider.getUriForFile(this, "com.yaphets.dock.activity.IndividualActivity.provider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CROP_IMAGE);
                    } else {
                        Toast.makeText(IndividualActivity.this, "设备没有SD卡!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CROP_IMAGE:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    setThumbImage(bitmap);
                    setResult(RESULT_OK);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadThumb();
                        }
                    }).start();
                    break;
                case MODIFY_NAME:
                    String nData1 = data.getStringExtra("resp");
                    updataPersonInfo(nData1,MODIFY_NAME);
                    setResult(RESULT_OK);
                    break;
                default:
            }
        }
    }

    private void setThumbImage(Bitmap image) {
        if (image != null) {
            ImageView iv_thumb = _root_layout.findViewById(R.id.ia_iv_thumb);
            iv_thumb.setImageBitmap(image);
            UserInfo.getInstance().setThumbBitmap(image);
        } else {
            Toast.makeText(this, "fail to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * @param data
     *      更新返回的新数据
     * @param type
     *      确认修改的资料种类
     */
    private void updataPersonInfo(String data, int type) {
        if (data != null) {
            UserInfo info = UserInfo.getInstance();
            if (type == MODIFY_NAME) {
                TextView tv_nickname = _root_layout.findViewById(R.id.ia_tv_nickname);
                tv_nickname.setText(data);
                info.setNickname(data);
            }
        } else {
            Toast.makeText(this, "fail to modify", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadThumb() {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = MySqlDAO.getConnection();
            String sql = "UPDATE user SET thumb=? WHERE email=?";
            ps = con.prepareStatement(sql);
            ps.setString(2, DockApplication._email);
            InputStream in = new FileInputStream(fileCropUri);
            // 设置Blob
            ps.setBlob(1, in);

            ps.executeUpdate();
        } catch (FileNotFoundException | SQLException e) {
            Log.e(TAG, "uploadThumb: " + e.getMessage(), e);
        } finally {
            MySqlDAO.release(con, ps);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}

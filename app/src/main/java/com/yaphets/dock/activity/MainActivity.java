package com.yaphets.dock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.yaphets.dock.DockApplication;
import com.yaphets.dock.R;
import com.yaphets.dock.model.entity.UserInfo;
import com.yaphets.dock.ui.fragment.BaseFragment;
import com.yaphets.dock.ui.fragment.FragmentFactory;
import com.yaphets.dock.ui.view.MyViewPager;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int CODE_MODIFY_DATA = 1;

    private String[] mTilte = {"发现", "排行榜", "我的游戏"};
    private int[] mIcon = {R.drawable.tab_home, R.drawable.tab_rank, R.drawable.tab_mygames};

    private ViewPager mViewPager;
    private BottomNavigationBar mBottomNavigationBar;

    private View _headerView;
    private DrawerLayout _drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _drawerLayout = findViewById(R.id.ma_drawer_layout);

        //BottomNavigationBar 和 ViewPager
        mBottomNavigationBar = findViewById(R.id.ma_navigation_bar);

        mViewPager = findViewById(R.id.ma_viewpager);
        DockApplication.setAttribute("viewPager", mViewPager);
    }

    private void initData() {
        //tool bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ma_tb_menu);
        }

        _drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                ((MyViewPager)DockApplication.getAttribute("viewPager")).setNoScroll(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                ((MyViewPager)DockApplication.getAttribute("viewPager")).setNoScroll(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        NavigationView navView = findViewById(R.id.nav_view);
        _headerView = navView.getHeaderView(0);
        CircleImageView vThumb = _headerView.findViewById(R.id.nav_thumb);
        TextView vNickname = _headerView.findViewById(R.id.nav_nickname);
        TextView vEmail = _headerView.findViewById(R.id.nav_email);
        UserInfo info = UserInfo.getInstance();
        Glide.with(this).load(info.getThumb()).into(vThumb);
        //vThumb.setImageBitmap(info.getThumbBitmap());
        vNickname.setText(info.getNickname());
        vEmail.setText(info.getEmail());

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //TODO 处理菜单点击逻辑
                switch (item.getItemId()) {
                    case R.id.nav_individualData:
                        Intent intent = new Intent(MainActivity.this, IndividualActivity.class);
                        startActivityForResult(intent, CODE_MODIFY_DATA);
                        break;
                    case R.id.nav_commonSetting:
                        break;
                    case R.id.nav_about:
                        break;
                    default:
                }

                _drawerLayout.closeDrawers();
                return true;
            }
        });

        //navigation bar
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);


        mBottomNavigationBar
                .setActiveColor(R.color.selectedTab) //设置选中的颜色
                .setInActiveColor(R.color.colorPrimary);//未选中颜色

        mBottomNavigationBar.addItem(new BottomNavigationItem(mIcon[0], mTilte[0]))
                .addItem(new BottomNavigationItem(mIcon[1], mTilte[1]))
                .addItem(new BottomNavigationItem(mIcon[2], mTilte[2]))
                .initialise();
        //设置点击事件
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){

            @Override
            public void onTabSelected(int position) {
                if (mViewPager.getCurrentItem() != position) {
                    mViewPager.setCurrentItem(position, false);
                }

            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            String[] titles = {"Dock", "排行榜", "我的游戏"};

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mBottomNavigationBar.getCurrentSelectedPosition() != position) {
                    mBottomNavigationBar.selectTab(position);
                }
                getSupportActionBar().setTitle(titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ma_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(this, "搜索功能敬请期待", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                _drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_MODIFY_DATA:
                    UserInfo info = UserInfo.getInstance();
                    CircleImageView vThumb = _headerView.findViewById(R.id.nav_thumb);
                    TextView vNickname = _headerView.findViewById(R.id.nav_nickname);
                    Glide.with(this).load(info.getThumb()).into(vThumb);
                    vNickname.setText(info.getNickname());
                    break;
                default:
            }
        }
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        MainPagerAdapter(FragmentManager fm) {
            super(fm);
            //mTilte = getResources().getStringArray(R.array.tab_short_Title);
            //images = new int[]{R.drawable.tab_home, R.drawable.tab_message};
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public BaseFragment getItem(int position) {
            return FragmentFactory.createFragment(position);
        }

        @Override
        public int getCount() {
            return FragmentFactory.FRAGMENT_COUNT;
        }
    }
}

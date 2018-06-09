package com.yaphets.dock.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.yaphets.dock.R;
import com.yaphets.dock.adapter.GameAdapter;
import com.yaphets.dock.database.dao.CategoryDAO;
import com.yaphets.dock.database.dao.GameDAO;
import com.yaphets.dock.database.dao.GameFirmDAO;
import com.yaphets.dock.model.entity.Category;
import com.yaphets.dock.model.entity.Game;
import com.yaphets.dock.model.entity.Game_Firm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    private Spinner mFirmSpinner;
    private Spinner mCategorySpinner;
    private RecyclerView mTypeGamesList;
    private ProgressBar mProgressView;

    private List<Category> mCategories;
    private List<Game_Firm> mGameFirms;
    private List<Game> mTypeGames;
    private GameAdapter mGameAdapter;

    private QueryGameTask mQueryGameTask;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    protected void loadData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mTypeGamesList.setLayoutManager(linearLayoutManager);
        mTypeGamesList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mTypeGames = new ArrayList<>();
        mGameAdapter = new GameAdapter(mContext, mTypeGames);
        mTypeGamesList.setAdapter(mGameAdapter);

        setSpinnerListener();

        //加载分类，公司购买记录
        new LoadDataTask().execute();

        GameDAO gameDAO = new GameDAO();
        //显示progress动画
        showProgress(true);
        //先加载所有游戏
        mQueryGameTask = new QueryGameTask(gameDAO);
        mQueryGameTask.execute(-1, -1);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mFirmSpinner = root.findViewById(R.id.spinner_firm);
        mCategorySpinner = root.findViewById(R.id.spinner_category);
        mTypeGamesList = root.findViewById(R.id.rv_game_list);
        mProgressView = root.findViewById(R.id.loading_progress);
        return root;
    }

    private void setSpinnerListener() {
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            GameDAO gameDAO = new GameDAO();
            boolean isFirst = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst) {
                    isFirst = false;
                    return;
                }

                if (mQueryGameTask != null) {
                    mQueryGameTask.cancel(true);
                    mQueryGameTask = null;
                }
                int cId = mCategories.get(position).getId();
                int fId = mGameFirms.get(mFirmSpinner.getSelectedItemPosition()).getId();
                //显示progress动画
                showProgress(true);
                mQueryGameTask = new QueryGameTask(gameDAO);
                mQueryGameTask.execute(cId, fId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFirmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            GameDAO gameDAO = new GameDAO();
            boolean isFirst = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst) {
                    isFirst = false;
                    return;
                }
                if (mQueryGameTask != null) {
                    mQueryGameTask.cancel(true);
                    mQueryGameTask = null;
                }

                int cId = mCategories.get(mCategorySpinner.getSelectedItemPosition()).getId();
                int fId = mGameFirms.get(position).getId();
                //显示progress动画
                showProgress(true);
                mQueryGameTask = new QueryGameTask(gameDAO);
                mQueryGameTask.execute(cId, fId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            CategoryDAO ctgDao = new CategoryDAO();
            GameFirmDAO gameFirmDAO = new GameFirmDAO();

            mCategories = ctgDao.findAll();
            mGameFirms = gameFirmDAO.findAll();

            //载入所有游戏公司
            Game_Firm.GameFirm.putAll(mGameFirms.stream().collect(Collectors.toMap(Game_Firm::getId, a->a)));
            //载入所有游戏分类
            Category.Categories.putAll(mCategories.stream().collect(Collectors.toMap(Category::getId, a->a)));

            /*插入<所有>标签*/
            Category allCtg = new Category(-1, null, null, "所有游戏");
            mCategories.add(0,allCtg);
            Game_Firm allGF = new Game_Firm(-1, "所有", null);
            mGameFirms.add(0, allGF);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            ArrayAdapter<Category> cAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, mCategories);
            cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCategorySpinner.setAdapter(cAdapter);

            ArrayAdapter<Game_Firm> fAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, mGameFirms);
            cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mFirmSpinner.setAdapter(fAdapter);
        }
    }

    class QueryGameTask extends AsyncTask<Integer, Void, List<Game>> {

        GameDAO _dao;

        QueryGameTask(GameDAO dao) {
            this._dao = dao;

        }

        @Override
        protected List<Game> doInBackground(Integer... values) {
            List<Game> tmp = null;
            String place1 = String.valueOf(values[0]), place2 = String.valueOf(values[1]);
             if (values[0] == -1) { //-1代表所有类型分类
                place1 = "%";
            }
            if (values[1] == -1) {  //-1代表所有公司分类
                 place2 = "%";
            }
            tmp = _dao.findByProperty("category_id LIKE ? and firm_id LIKE ?",
                    place1, place2);
            return tmp;
        }

        @Override
        protected void onPostExecute(List<Game> games) {
            showProgress(false);
            mTypeGames.clear();
            mTypeGames.addAll(games);
            mGameAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mTypeGamesList.setVisibility(show ? View.GONE : View.VISIBLE);
        mTypeGamesList.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mTypeGamesList.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

package com.yaphets.dock.ui.fragment;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    private Spinner mFirmSpinner;
    private Spinner mCategorySpinner;
    private RecyclerView mTypeGamesList;

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
        /*ArrayList<String> list = new ArrayList<>();
        list.add("RPG");
        list.add("STG");
        list.add("MOBA");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mTypeGamesList.setLayoutManager(linearLayoutManager);
        mTypeGamesList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mTypeGames = new ArrayList<>();
        mGameAdapter = new GameAdapter(mTypeGames);
        mTypeGamesList.setAdapter(mGameAdapter);

        new LoadDataTask().execute();

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            GameDAO gameDAO = new GameDAO();
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mQueryGameTask != null) {
                    mQueryGameTask.cancel(true);
                    mQueryGameTask = null;
                }
                int cId = mCategories.get(position).getId();
                int fId = mGameFirms.get(mFirmSpinner.getSelectedItemPosition()).getId();
                mQueryGameTask = new QueryGameTask(gameDAO);
                mQueryGameTask.execute(cId, fId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFirmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            GameDAO gameDAO = new GameDAO();

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mQueryGameTask != null) {
                    mQueryGameTask.cancel(true);
                    mQueryGameTask = null;
                }

                int cId = mCategories.get(mCategorySpinner.getSelectedItemPosition()).getId();
                int fId = mGameFirms.get(position).getId();
                mQueryGameTask = new QueryGameTask(gameDAO);
                mQueryGameTask.execute(cId, fId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mFirmSpinner = root.findViewById(R.id.spinner_firm);
        mCategorySpinner = root.findViewById(R.id.spinner_category);
        mTypeGamesList = root.findViewById(R.id.rv_game_list);
        return root;
    }

    class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            CategoryDAO ctgDao = new CategoryDAO();
            GameFirmDAO gameFirmDAO = new GameFirmDAO();

            mCategories = ctgDao.findAll();
            mGameFirms = gameFirmDAO.findAll();

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
            List<Game> tmp = _dao.findByProperty("category_id=? and firm_id=?",
                    values[0], values[1]);
            return tmp;
        }

        @Override
        protected void onPostExecute(List<Game> games) {
            mTypeGames.clear();
            mTypeGames.addAll(games);
            mGameAdapter.notifyDataSetChanged();
        }

    }
}

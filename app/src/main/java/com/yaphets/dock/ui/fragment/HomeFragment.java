package com.yaphets.dock.ui.fragment;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yaphets.dock.R;
import com.yaphets.dock.database.dao.CategoryDAO;
import com.yaphets.dock.database.dao.GameFirmDAO;
import com.yaphets.dock.database.dao.GenericDAO;
import com.yaphets.dock.model.entity.Category;
import com.yaphets.dock.model.entity.Game_Firm;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    private Spinner mFirmSpinner;
    private Spinner mCategorySpinner;

    private List<Category> mCategories;
    private List<Game_Firm> mGameFirms;

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
        new LoadDataTask().execute();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mFirmSpinner = root.findViewById(R.id.spinner_firm);
        mCategorySpinner = root.findViewById(R.id.spinner_category);
        return root;
    }

    class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            CategoryDAO dao = new CategoryDAO();
            GameFirmDAO gameFirmDAO = new GameFirmDAO();
            mCategories = dao.findAll();
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
}

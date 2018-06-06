package com.yaphets.dock.ui.fragment;

public class FragmentFactory {
    //private static HashMap<Integer, BaseFragment> mBaseFragments = new HashMap<>();
    public static final int FRAGMENT_COUNT = 3;
    private static BaseFragment[] mBaseFragments = new BaseFragment[FRAGMENT_COUNT];


    public static BaseFragment createFragment(int pos) {
        //BaseFragment baseFragment = mBaseFragments.get(pos);
        BaseFragment baseFragment = mBaseFragments[pos];

        if (baseFragment == null) {
            switch (pos) {
                case 0:
                    baseFragment = new HomeFragment();
                    break;
                case 1:
                    baseFragment = new RankFragment();
                    break;
                case 2:
                    baseFragment = new MyGameFragment();
                    break;
            }
            //mBaseFragments.put(pos, baseFragment);
            mBaseFragments[pos] = baseFragment;
        }
        return baseFragment;
    }

    public static HomeFragment getHomeFragmentInstance() {
        HomeFragment hf = (HomeFragment) mBaseFragments[0];
        if (hf == null) {
            hf = new HomeFragment();
        }
        return hf;
    }

    public static RankFragment getRankFragmentInstance() {
        RankFragment rf = (RankFragment) mBaseFragments[1];
        if (rf == null) {
            rf = new RankFragment();
        }
        return rf;
    }

    public static MyGameFragment getMyGameFragmentInstance() {
        MyGameFragment mgf = (MyGameFragment) mBaseFragments[2];
        if (mgf == null) {
            mgf = new MyGameFragment();
        }
        return mgf;
    }
}

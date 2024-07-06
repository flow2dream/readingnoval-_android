package com.homework.mynoval;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MyPgaerAdapter extends FragmentStateAdapter {

    List<PagerFragment> fragmentList;
    public MyPgaerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setFragmentList(List<PagerFragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

    public List<PagerFragment> getFragmentList() {
        return fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

}

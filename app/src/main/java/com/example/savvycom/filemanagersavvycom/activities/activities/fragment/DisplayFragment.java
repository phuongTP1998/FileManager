package com.example.savvycom.filemanagersavvycom.activities.activities.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.savvycom.filemanagersavvycom.R;
import com.example.savvycom.filemanagersavvycom.activities.activities.adapter.DisplayFragmentAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by savvycom on 12/15/2017.
 */

public class DisplayFragment extends Fragment{
    private RecyclerView recyclerView;
    private File path;
    private List<File> filesAndFolders;
    private Toolbar toolbar;
    private DisplayFragmentAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(path.getName());


        adapter = new DisplayFragmentAdapter(filesAndFolders,onItemClickListenerCallback,getActivity());

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private DisplayFragmentAdapter.OnItemClickListener onItemClickListenerCallback = new DisplayFragmentAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

        }

        @Override
        public void onItemLongClick(View view, int position) {

        }

        @Override
        public void onIconClick(View view, int position) {

        }
    }
}

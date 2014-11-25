package com.inlusion.view.main_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.inlusion.controller.util.HistoryUtils;
import com.inlusion.maiavoip.R;
import com.inlusion.model.HistoryListAdapter;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Linas Martusevicius on 14.10.2.
 */
public class HistoryFragment extends Fragment {

    ViewGroup rootView;
    ListView listView;
    HistoryListAdapter hla;
    HistoryUtils hu;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_history, container, false);
        listView = (ListView) rootView.findViewById(R.id.history_listView);
        hu = new HistoryUtils(getActivity());
        hla = new HistoryListAdapter(getActivity(), hu.getDummyHistoryList());

        listView.setAdapter(hla);
        return rootView;
    }

}

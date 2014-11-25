package com.inlusion.view.main_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inlusion.maiavoip.R;
import com.inlusion.view.MainActivity;

/**
 * Created by Linas Martusevicius on 14.10.2.
 */
public class SettingsFragment extends Fragment {
    ViewGroup rootView;

    LinearLayout register_linear_layout;
    LinearLayout unregister_linear_layout;

    View.OnClickListener registerListener;
    View.OnClickListener unregisterListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        register_linear_layout = (LinearLayout) rootView.findViewById(R.id.settings_register_ll);
        unregister_linear_layout = (LinearLayout) rootView.findViewById(R.id.settings_unregister_ll);

        createClickListeners();

        return rootView;
    }

    /**
     * Creates and sets click listener for all SettingsFragment actions.
     */
    public void createClickListeners() {
        registerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAcc();
            }
        };

        unregisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterAcc();
            }
        };

        register_linear_layout.setOnClickListener(registerListener);
        unregister_linear_layout.setOnClickListener(unregisterListener);
    }

    /**
     * Registers the current local SipProfile with the SIP service provider's registrar.
     */
    public void registerAcc() {
        ((MainActivity) getActivity()).getRu().setAction(true);
        ((MainActivity) getActivity()).getRu().run();
    }

    /**
     * Unregisters the current local SipProfile from the SIP service provider's registrar.
     */
    public void unregisterAcc() {
        ((MainActivity) getActivity()).getRu().setAction(false);
        ((MainActivity) getActivity()).getRu().run();
    }
}

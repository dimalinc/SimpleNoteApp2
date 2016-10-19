package com.okason.simplenotepad.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.okason.simplenotepad.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteLinedEditorFragment extends Fragment {


    public NoteLinedEditorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_lined_editor, container, false);
    }


}

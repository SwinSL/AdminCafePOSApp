package com.example.admincafeposapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincafeposapp.Adapters.TableRecyclerViewAdapter;
import com.example.admincafeposapp.Model.Tables;
import com.example.admincafeposapp.R;

import java.util.ArrayList;

public class TablesFragment extends Fragment {

    private RecyclerView table_recyclerView;
    private TableRecyclerViewAdapter table_Adapter;
    private ArrayList<Tables> tablesArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tables, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //DEMO Only
        tablesArrayList = new ArrayList<>();
        tablesArrayList.add(new Tables("A01", 6, "Available"));
        tablesArrayList.add(new Tables("A02", 5, "Available"));
        tablesArrayList.add(new Tables("A03", 4, "Unavailable"));

        table_recyclerView = view.findViewById(R.id.recyclerView_table);
        table_recyclerView.setHasFixedSize(true);
        table_recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        table_Adapter = new TableRecyclerViewAdapter(this.getContext(), tablesArrayList);
        table_recyclerView.setAdapter(table_Adapter);
        table_recyclerView.addItemDecoration(new DividerItemDecoration(table_recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }
}

package com.rechargoyo.formula;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class FormulaFragment extends Fragment {

    private LinearLayout quest, rel, tictac, mem;
    private int i;

    public FormulaFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_formula, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        quest = view.findViewById(R.id.quest);
        rel = view.findViewById(R.id.rel);
        tictac = view.findViewById(R.id.tictac);
        mem = view.findViewById(R.id.mem);
        ArrayList<LinearLayout> layouts = new ArrayList<>();
        layouts.add(quest);
        layouts.add(rel);
        layouts.add(tictac);
        layouts.add(mem);
        for (i = 0; i < layouts.size(); i++){
            layouts.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId() == R.id.quest){
                        Intent create = new Intent(requireContext(), PregOpc.class);
                        create.putExtra("type",0);
                        startActivity(create);
                    } else if(v.getId() == R.id.rel){
                        Intent create = new Intent(requireContext(), Preg1a1.class);
                        create.putExtra("type",1);
                        startActivity(create);
                    } else if(v.getId() == R.id.tictac){
                        Intent create = new Intent(requireContext(), PregOpc.class);
                        create.putExtra("type",2);
                        startActivity(create);
                    } else if(v.getId() == R.id.mem){
                        Intent create = new Intent(requireContext(), Preg1a1.class);
                        create.putExtra("type",3);
                        startActivity(create);
                    } else{
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
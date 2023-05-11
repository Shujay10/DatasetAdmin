package com.example.datasetadmin.tab;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.datasetadmin.Model;
import com.example.datasetadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class UpdateFragment extends Fragment {

    ProgressBar bar;
    Spinner tool;
    Spinner stage;
    EditText sheet;

    RadioButton rInt;
    RadioButton rExt;

    Button request;

    ArrayList<String> tools;
    ArrayList<String> stages;

    ArrayAdapter toolAdapter;
    ArrayAdapter stageAdapter;
    FirebaseFirestore mStore;
    FirebaseDatabase mReal;
    Model model;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_update, container, false);

        mStore = FirebaseFirestore.getInstance();
        mReal = FirebaseDatabase.getInstance();
        tools = new ArrayList<>();
        stages = new ArrayList<>();
        model = new Model();

        bar = root.findViewById(R.id.updateBar);
        bar.setVisibility(View.GONE);
        tool = root.findViewById(R.id.rTool);
        stage = root.findViewById(R.id.rStagr);
        sheet = root.findViewById(R.id.rComments);
        rInt = root.findViewById(R.id.rInt);
        rExt = root.findViewById(R.id.rExt);
        request = root.findViewById(R.id.req);

        setAdapter();

        tool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                model.setTool(tools.get(position));
                getStages(tools.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                model.setStage(stages.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getCStage()&&getMode()&&getSheet()){
                    bar.setVisibility(View.VISIBLE);
                    request.setEnabled(false);
                    setData();
                }else {
                    Toast.makeText(getContext(),"Fill all Fields",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;

    }

    private void setData(){


        System.out.println(model);

        mStore.collection(model.getTool()).document(model.getStage())
                .update(model.getMode().toLowerCase(Locale.ROOT),model.getComments())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            bar.setVisibility(View.GONE);
                            request.setEnabled(true);
                            Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
                            clearData();
                        }else {
                            bar.setVisibility(View.GONE);
                            request.setEnabled(true);
                            Toast.makeText(getContext(),task.toString(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void clearData() {

        sheet.setText("");

    }

    boolean getCStage(){

        model.setStage(stage.getSelectedItem().toString());
        return true;
    }

    boolean getMode(){

        if(rInt.isChecked()){
            model.setMode("Internal");
            return true;
        } else if (rExt.isChecked()) {
            model.setMode("External");
            return true;
        }else {
            return false;
        }
    }

    boolean getSheet(){

        String field = sheet.getText().toString();
        if(field.isEmpty()){
            return false;
        }else {
            model.setComments(field);
            return true;
        }

    }

    private void setAdapter(){

        toolAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, tools);
        toolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tool.setAdapter(toolAdapter);

        stageAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, stages);
        stageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stage.setAdapter(stageAdapter);

        getData();
    }

    private void getData(){

        mReal.getReference("Tools").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                tools.clear();
                String tool;

                for (DataSnapshot shot : snapshot.getChildren()){
                    tool = shot.getValue().toString();
                    tools.add(tool);
                }

                HashSet<String > set = new HashSet<>(tools);

                tools.clear();
                tools.addAll(set);

                toolAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getStages(String s) {

        stages.clear();

        mReal.getReference(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                stages.clear();
                String stage;

                for (DataSnapshot shot : snapshot.getChildren()){
                    stage = shot.getValue().toString();
                    stages.add(stage);
                }

                System.out.println(stages);
                stageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
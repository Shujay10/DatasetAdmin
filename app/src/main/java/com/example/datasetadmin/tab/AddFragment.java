package com.example.datasetadmin.tab;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.datasetadmin.AddModel;
import com.example.datasetadmin.Model;
import com.example.datasetadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class AddFragment extends Fragment {

    ProgressBar bar;
    EditText tool;
    EditText stage;
    EditText internal;
    EditText external;

    Button add;

    FirebaseFirestore mStore;
    FirebaseDatabase mReal;

    FirebaseStorage mDrive;
    Model model;
    AddModel addModel;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add, container, false);

        mStore = FirebaseFirestore.getInstance();
        mReal = FirebaseDatabase.getInstance();
        mDrive = FirebaseStorage.getInstance();
        model = new Model();
        addModel = new AddModel();

        bar = root.findViewById(R.id.addBar);
        bar.setVisibility(View.GONE);
        tool = root.findViewById(R.id.rTool);
        stage = root.findViewById(R.id.rStagr);
        internal = root.findViewById(R.id.intLink);
        external = root.findViewById(R.id.extLink);
        add = root.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getTool()&&getStage()&&getInternal()&&getExternal()){
                    bar.setVisibility(View.VISIBLE);
                    add.setEnabled(false);
                    setData();
                }else {
                    Toast.makeText(getContext(),"Fill all Fields",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;

    }

    private void setData(){

//        DatabaseReference toolRef = mReal.getReference("Tools");
//        String toolKey = toolRef.push().getKey();

        mReal.getReference("Tools").child(model.getTool()).setValue(model.getTool());

        mReal.getReference(model.getTool()).child(model.getStage()).setValue(model.getStage());

        mDrive.getReference().child(model.getTool()).child(model.getStage())
                .child("Internal").child("Delete").putBytes(new byte[10])
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

            }
        });
        mDrive.getReference().child(model.getTool()).child(model.getStage()).child("External").child("Delete").putBytes(new byte[10])
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    }
                });

        mStore.collection(model.getTool()).document(model.getStage())
                .set(addModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            bar.setVisibility(View.GONE);
                            add.setEnabled(true);
                            Toast.makeText(getContext(),"Added",Toast.LENGTH_SHORT).show();
                            clearData();
                        }else {
                            bar.setVisibility(View.GONE);
                            add.setEnabled(true);
                            Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    private void clearData() {

        tool.setText("");
        stage.setText("");
        internal.setText("");
        external.setText("");

    }

    boolean getTool(){

        String field = tool.getText().toString();
        if(field.isEmpty()){
            return false;
        }else {
            model.setTool(field);
            return true;
        }

    }

    boolean getStage(){

        String field = stage.getText().toString();
        if(field.isEmpty()){
            return false;
        }else {
            model.setStage(field);
            return true;
        }

    }

    boolean getInternal(){

        String field = internal.getText().toString();
        if(field.isEmpty()){
            addModel.setInternal("NA");
        }else {
            addModel.setInternal(field);
        }
        return true;

    }

    boolean getExternal(){

        String field = external.getText().toString();
        if(field.isEmpty()){
            addModel.setExternal("NA");
        }else {
            addModel.setExternal(field);
        }
        return true;

    }

}
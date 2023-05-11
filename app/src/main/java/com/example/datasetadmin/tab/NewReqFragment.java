package com.example.datasetadmin.tab;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.datasetadmin.Model;
import com.example.datasetadmin.R;
import com.example.datasetadmin.adapter.NewReqAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

public class NewReqFragment extends Fragment {

    RecyclerView newReq;

    ArrayList<Model> list;
    NewReqAdapter adapter;

    FirebaseFirestore mStore;

    SwipeRefreshLayout refresh;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_newreq, container, false);

        list = new ArrayList<>();
        mStore = FirebaseFirestore.getInstance();

        refresh = root.findViewById(R.id.refresh);
        newReq = root.findViewById(R.id.newReq);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                getData();
            }
        });

        setAdapter();
        getData();

        return root;
    }

    private void getData(){

        CollectionReference sportsRef = mStore.collection("Request");
        Query query = sportsRef.whereEqualTo("satisfied", 0);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                adapter.notifyDataSetChanged();
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Model model = document.toObject(Model.class);
                        list.add(model);
                        adapter.notifyDataSetChanged();
                    }

                    if(list.isEmpty()){
                        Toast.makeText(getContext(),"No new Request",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAdapter(){
        adapter = new NewReqAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        newReq.setItemAnimator(new DefaultItemAnimator());
        newReq.setLayoutManager(layoutManager);
        newReq.setAdapter(adapter);

    }

}
package com.example.datasetadmin.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datasetadmin.Model;
import com.example.datasetadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NewReqAdapter extends RecyclerView.Adapter<NewReqAdapter.MyViewHolder> {

    Dialog upload;
    Button yes;
    Button no;
    Button np;

    FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    ArrayList<Model> list;


    public NewReqAdapter(ArrayList<Model> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NewReqAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewReqAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tool.setText(list.get(position).getTool());
        holder.stage.setText(list.get(position).getStage());
        holder.mode.setText(list.get(position).getMode());
        holder.comments.setText(list.get(position).getComments());

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                upload = new Dialog(v.getContext());
                upload.setContentView(R.layout.confirm_sat);
                upload.show();

                yes = upload.findViewById(R.id.yes);
                no = upload.findViewById(R.id.no);
                np = upload.findViewById(R.id.notPossible);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStore.collection("Request").document(list.get(position)
                                .getId()).update("satisfied",1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    upload.dismiss();
                                    Toast.makeText(v.getContext(),"Done",Toast.LENGTH_SHORT).show();
                                }else {
                                    upload.dismiss();
                                    Toast.makeText(v.getContext(),"Failed",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });

                np.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStore.collection("Request").document(list.get(position)
                                        .getId()).update("satisfied",2)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            list.remove(position);
                                            notifyDataSetChanged();
                                            upload.dismiss();
                                            Toast.makeText(v.getContext(),"Done",Toast.LENGTH_SHORT).show();
                                        }else {
                                            upload.dismiss();
                                            Toast.makeText(v.getContext(),"Failed",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upload.dismiss();
                    }
                });

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView tool;
        TextView stage;
        TextView mode;
        TextView comments;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card);
            tool = itemView.findViewById(R.id.viewTool);
            stage = itemView.findViewById(R.id.viewStage);
            mode = itemView.findViewById(R.id.viewMode);
            comments = itemView.findViewById(R.id.viewComments);

        }
    }
}

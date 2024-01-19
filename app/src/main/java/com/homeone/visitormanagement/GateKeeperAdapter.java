package com.homeone.visitormanagement;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.homeone.visitormanagement.databinding.RequestLayoutBinding;
import com.homeone.visitormanagement.modal.RequestData;

import java.util.List;

public class GateKeeperAdapter extends RecyclerView.Adapter<GateKeeperAdapter.ViewHolder>{

    private List<RequestData> dataModelList;
    private Context context;

    public GateKeeperAdapter(List<RequestData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
    }

    @NonNull
    @Override
    public GateKeeperAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        RequestLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.request_layout, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RequestData dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.deleteImg.setOnClickListener( v -> {
            String id = String.valueOf(dataModel.getUuid());
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("requests");
            myRef.child(id).removeValue();
            dataModelList.remove(dataModel);
        });

        if (dataModel.getStatus().equals("approved")) {
            holder.itemRowBinding.statusImg.setImageResource(R.drawable.ic_baseline_check_circle_24);
        }else if (dataModel.getStatus().equals("denied")) {
            holder.itemRowBinding.statusImg.setImageResource(R.drawable.ic_baseline_cancel_24);
        }else
            holder.itemRowBinding.statusImg.setImageResource(android.R.color.transparent);

    }


    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RequestLayoutBinding itemRowBinding;

        public ViewHolder(RequestLayoutBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.data, obj);
            itemRowBinding.executePendingBindings();
        }
    }

}

package com.homeone.visitormanagement;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.homeone.visitormanagement.databinding.RequestOwnerBinding;
import com.homeone.visitormanagement.modal.RequestOwnerData;
import com.kv.popupimageview.PopupImageView;

import java.util.List;

public class OwnerAdapter extends RecyclerView.Adapter<OwnerAdapter.ViewHolder>{

    private List<RequestOwnerData> dataModelList;
    private Context context;

    public OwnerAdapter(List<RequestOwnerData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
    }

    @Override
    public OwnerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        RequestOwnerBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.request_owner, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(OwnerAdapter.ViewHolder holder, int position) {
        RequestOwnerData dataModel = dataModelList.get(position);
        holder.itemRowBinding.approveBtn.setOnClickListener( v -> {
            if (!TextUtils.isEmpty(dataModel.getUuid().toString())) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("requests").child(dataModel.getUuid().toString());
                ref.child("status").setValue("approved");
                dataModel.setStatus("approved");
                Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemRowBinding.denyBtn.setOnClickListener( v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("requests").child(dataModel.getUuid().toString());
            ref.child("status").setValue("denied");
            dataModel.setStatus("denied");
            Toast.makeText(context,"Denied",Toast.LENGTH_SHORT).show();
        });

        Glide.with(context).load(dataModel.getProfileUrl()).placeholder(R.drawable.sample_avatar).into(holder.itemRowBinding.imageReq);

        holder.itemRowBinding.imageReq.setOnClickListener( v -> {
            if (dataModel.getProfileUrl() != null) {
                new PopupImageView(context, v, dataModel.getProfileUrl());
            }
        });

        holder.bind(dataModel);
    }

    public Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RequestOwnerBinding itemRowBinding;

        public ViewHolder(RequestOwnerBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.data, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}

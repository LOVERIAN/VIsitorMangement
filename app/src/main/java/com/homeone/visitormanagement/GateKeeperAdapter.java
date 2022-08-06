package com.homeone.visitormanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.homeone.visitormanagement.databinding.RequestLayoutBinding;
import com.homeone.visitormanagement.modal.RequestData;

import java.util.List;

public class GateKeeperAdapter  extends RecyclerView.Adapter<GateKeeperAdapter.ViewHolder>{

    private List<RequestData> dataModelList;
    private Context context;

    public GateKeeperAdapter(List<RequestData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
    }

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
    }


    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

package com.homeone.visitormanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.homeone.visitormanagement.GateKeeperAdapter;
import com.homeone.visitormanagement.R;
import com.homeone.visitormanagement.modal.PromotionData;
import com.homeone.visitormanagement.modal.RequestData;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder>{

    private final List<PromotionData> dataModelList;
    private final Context context;

    public PromotionAdapter(List<PromotionData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
    }
    @NonNull
    @Override
    public PromotionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PromotionData data = dataModelList.get(position);
        Glide.with(context).load(data.getImg_link()).placeholder(R.drawable.banner_placeholder).into(holder.imageView);
        holder.imageView.setOnClickListener( v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(data.getPromotion_link()));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView promotionCardView;
        ImageView imageView;

        public ViewHolder(View view)
        {
            super(view);

            // initialise TextView with id
            promotionCardView = (CardView) view
                    .findViewById(R.id.promotion_card);
            imageView = view.findViewById(R.id.banner_ad);
        }
    }
}

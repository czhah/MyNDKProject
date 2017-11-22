package com.thedream.cz.myndkproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.db.entity.ProductEntity;

/**
 * Created by Administrator on 2017/11/22.
 */

public class TextAdapter extends BaseAdapter<ProductEntity, TextAdapter.MyHolder> {


    public TextAdapter(Context context) {
        super(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_text_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        ProductEntity entity = data.get(position);
        holder.tvText.setText(entity.getName() + "  " + entity.getDescription());
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        private final TextView tvText;

        public MyHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
}

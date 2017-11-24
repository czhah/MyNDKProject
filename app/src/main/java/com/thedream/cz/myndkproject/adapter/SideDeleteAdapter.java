package com.thedream.cz.myndkproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thedream.cz.myndkproject.R;
import com.thedream.cz.myndkproject.widget.recyclerview.ItemTouchHelperAdapterCallback;

/**
 * Created by chenzhuang on 2017/10/24.
 * Describe:
 */

public class SideDeleteAdapter extends BaseAdapter<String, SideDeleteAdapter.MyHolder> implements ItemTouchHelperAdapterCallback {

    public SideDeleteAdapter(Context context) {
        super(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_side_delete_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tvSide.setText(data.get(position));
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("cz", "删除");

            }
        });
    }

    public static class MyHolder extends RecyclerView.ViewHolder{

        private TextView tvSide;
        private TextView tvDelete;

        public MyHolder(View itemView) {
            super(itemView);
            tvSide = (TextView) itemView.findViewById(R.id.tv_side_text);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_side_delete);
        }
    }


    @Override
    public void onSwiped(int position) {
        if(position > 0 && position < data.size()) {
//            Collections.swap(data, position, 0);

            String remove = data.remove(position);
            notifyItemRemoved(position);
            data.add(0, remove);
            notifyItemInserted(0);
//            notifyItemMoved(0, position);
        }
    }
}

package com.yesipov.gusto.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yesipov.gusto.Models.Specials;
import com.yesipov.gusto.R;

import java.util.List;

    public class SpecialsAdapter extends RecyclerView.Adapter<SpecialsAdapter.ViewHolder> {
        public interface OnSpecialClickListener{
            void onSpecialClick(Specials special, int position);
        }

        private final OnSpecialClickListener onClickListener;
        private final LayoutInflater inflater;
        private final List<Specials> specials;

        public SpecialsAdapter(Context context, List<Specials> specials, OnSpecialClickListener onClickListener) {
                this.onClickListener = onClickListener;
                this.specials = specials;
                this.inflater = LayoutInflater.from(context);
        }

        @Override
        public SpecialsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.special_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SpecialsAdapter.ViewHolder holder, int position) {
            Specials special = specials.get(position);
            holder.flagView.setImageResource(special.getFlagResource());
            holder.titleView.setText(special.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    onClickListener.onSpecialClick(special, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return specials.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView flagView;
            final TextView titleView;
            ViewHolder(View view){
                super(view);
                flagView = (ImageView)view.findViewById(R.id.special_image);
                titleView = (TextView) view.findViewById(R.id.special_title);
            }
        }
    }
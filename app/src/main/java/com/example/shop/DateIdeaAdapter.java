package com.example.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DateIdeaAdapter
        extends RecyclerView.Adapter<DateIdeaAdapter.ViewHolder>
        implements Filterable {
    private ArrayList<DateIdea> dateIdeas;
    private ArrayList<DateIdea> allDateIdeas;
    private Context mContext;
    private int lastPosition = -1;

    DateIdeaAdapter(Context context, ArrayList<DateIdea> itemsData) {
        this.dateIdeas = itemsData;
        this.allDateIdeas = itemsData;
        this.mContext = context;
    }

    @Override
    public DateIdeaAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DateIdeaAdapter.ViewHolder holder, int position) {
        DateIdea currentItem = dateIdeas.get(position);
        holder.bindTo(currentItem);
        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return dateIdeas.size();
    }

    @Override
    public Filter getFilter() {
        return dateFilter;
    }

    private Filter dateFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<DateIdea> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = allDateIdeas.size();
                results.values = allDateIdeas;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (DateIdea item : allDateIdeas) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            dateIdeas = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateName;
        private TextView datePrice;

        ViewHolder(View itemView) {
            super(itemView);

            dateName = itemView.findViewById(R.id.dateName);
            datePrice = itemView.findViewById(R.id.datePrice);
        }

        void bindTo(DateIdea currentItem) {
            dateName.setText(currentItem.getName());
            datePrice.setText(currentItem.getPrice());

            itemView.findViewById(R.id.modifyDate).setOnClickListener(view -> ((DateListActivity) mContext).updateDate(currentItem));
            itemView.findViewById(R.id.delete).setOnClickListener(view -> ((DateListActivity) mContext).deleteItem(currentItem));
        }
    }
}

package pt.ua.hackaton.smartmove.recyclers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.recyclers.utils.DayOfWeekRecyclerItem;

public class DaysOfWeekRecyclerViewAdapter extends RecyclerView.Adapter<DaysOfWeekRecyclerViewAdapter.ViewHolder>  {

    private final List<DayOfWeekRecyclerItem> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public DaysOfWeekRecyclerViewAdapter(Context context, List<DayOfWeekRecyclerItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public DaysOfWeekRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.day_recycler_item, parent, false);
        return new DaysOfWeekRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(DaysOfWeekRecyclerViewAdapter.ViewHolder holder, int position) {

        DayOfWeekRecyclerItem dayOfWeekRecyclerItem = mData.get(position);
        LocalDateTime date = dayOfWeekRecyclerItem.getLocalDateTime();
        boolean isActive = dayOfWeekRecyclerItem.isActive();

        holder.dayOfMonthTextView.setText(String.valueOf(date.getDayOfMonth()));
        holder.monthTextView.setText(String.valueOf(date.toLocalDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.US)));

        if (isActive) {
            holder.dayCard.setCardBackgroundColor(dayOfWeekRecyclerItem.getActiveColorResourceId());
        } else {
            holder.dayCard.setCardBackgroundColor(dayOfWeekRecyclerItem.getInactiveColorResourceId());
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dayOfMonthTextView;
        TextView monthTextView;
        CardView dayCard;

        ViewHolder(View itemView) {
            super(itemView);
            dayOfMonthTextView = itemView.findViewById(R.id.dayOfMonthTxt);
            monthTextView = itemView.findViewById(R.id.monthTxt);
            dayCard = itemView.findViewById(R.id.recyclerDayCardView);
            itemView.setOnClickListener(this);
            Log.d("DEBUG", String.valueOf(dayOfMonthTextView));
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    DayOfWeekRecyclerItem getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}

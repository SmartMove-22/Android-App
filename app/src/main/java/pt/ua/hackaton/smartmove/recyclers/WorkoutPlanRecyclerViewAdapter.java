package pt.ua.hackaton.smartmove.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.Exercise;

public class WorkoutPlanRecyclerViewAdapter extends RecyclerView.Adapter<WorkoutPlanRecyclerViewAdapter.ViewHolder> {

    private List<? extends Exercise> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public WorkoutPlanRecyclerViewAdapter(Context context, List<? extends Exercise> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void setData(List<?extends Exercise> data) {
        mData = (List<Exercise>) data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.exercise_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Exercise exercise = mData.get(position);
        holder.myTextView.setText(exercise.getName());
        // TODO: Change this by the exercise image name.
        holder.viewHolderImage.setImageResource(R.drawable.lift_weight);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        else
            return 0;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView myTextView;
        ImageView viewHolderImage;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.exerciseCardNameTxt);
            viewHolderImage = itemView.findViewById(R.id.exerciseCardImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Exercise getItem(int id) {
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

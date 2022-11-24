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
import pt.ua.hackaton.smartmove.data.AssignedExercise;

public class TraineesOverviewRecyclerViewAdapter extends RecyclerView.Adapter<TraineesOverviewRecyclerViewAdapter.ViewHolder> {

    private final List<AssignedExercise> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public TraineesOverviewRecyclerViewAdapter(Context context, List<AssignedExercise> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public TraineesOverviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.performed_exercise_recycler_item, parent, false);
        return new TraineesOverviewRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TraineesOverviewRecyclerViewAdapter.ViewHolder holder, int position) {
        AssignedExercise assignedExercise = mData.get(position);

        if (!assignedExercise.isCompleted())
            holder.completeExerciseTextView.setVisibility(View.INVISIBLE);

        holder.exerciseNameTextView.setText(String.valueOf(assignedExercise.getName()));
        holder.performanceTextView.setText(String.valueOf(assignedExercise.getPerformance()));
        holder.correctnessTextView.setText(String.valueOf(assignedExercise.getCorrectness()));
        holder.caloriesBurnTextView.setText(String.valueOf(assignedExercise.getCaloriesBurn()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView exerciseNameTextView;
        TextView completeExerciseTextView;
        TextView performanceTextView;
        TextView correctnessTextView;
        TextView caloriesBurnTextView;

        ViewHolder(View itemView) {

            super(itemView);

            exerciseNameTextView = itemView.findViewById(R.id.performedExerciseNameTextView);
            completeExerciseTextView = itemView.findViewById(R.id.performedExerciseCompletedTextView);
            performanceTextView = itemView.findViewById(R.id.performedExercisePerformanceTextView);
            caloriesBurnTextView = itemView.findViewById(R.id.performedExerciseCaloriesTextView);
            correctnessTextView = itemView.findViewById(R.id.performedExerciseCorrectnessTextView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    AssignedExercise getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}

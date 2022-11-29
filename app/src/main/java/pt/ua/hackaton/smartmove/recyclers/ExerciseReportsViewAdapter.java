package pt.ua.hackaton.smartmove.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.Exercise;
import pt.ua.hackaton.smartmove.data.database.entities.ExerciseReportEntity;
import pt.ua.hackaton.smartmove.data.mocks.ExercisesMocks;

public class ExerciseReportsViewAdapter extends RecyclerView.Adapter<ExerciseReportsViewAdapter.ViewHolder>{

    private final List<ExerciseReportEntity> mData;
    private final LayoutInflater mInflater;
    private TraineesOverviewRecyclerViewAdapter.ItemClickListener mClickListener;

    public ExerciseReportsViewAdapter(Context context, List<ExerciseReportEntity> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ExerciseReportsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.exercise_report_recycler_item, parent, false);
        return new ExerciseReportsViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseReportsViewAdapter.ViewHolder holder, int position) {

        ExerciseReportEntity exerciseReport = mData.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        Exercise exerciseInReport = ExercisesMocks.getExerciseById(exerciseReport.exerciseId);

        LocalDateTime exerciseDate = exerciseReport.timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        holder.exerciseNameTextView.setText(String.valueOf(exerciseInReport.getName()));
        holder.exerciseDateTextView.setText(exerciseDate.format(DateTimeFormatter.ofPattern("hh:mm a dd MMM")));
        holder.exerciseTimeTextView.setText(decimalFormat.format(exerciseReport.exerciseDuration));
        holder.correctnessTextView.setText(decimalFormat.format(exerciseReport.exerciseCorrectness));
        holder.caloriesBurnTextView.setText(decimalFormat.format(exerciseReport.caloriesBurn));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView exerciseNameTextView;
        TextView exerciseDateTextView;
        TextView completeExerciseTextView;
        TextView exerciseTimeTextView;
        TextView correctnessTextView;
        TextView caloriesBurnTextView;

        ViewHolder(View itemView) {

            super(itemView);

            exerciseNameTextView = itemView.findViewById(R.id.performedExerciseNameTextView);
            exerciseDateTextView = itemView.findViewById(R.id.performedExerciseDateTextView);
            completeExerciseTextView = itemView.findViewById(R.id.performedExerciseCompletedTextView);
            exerciseTimeTextView = itemView.findViewById(R.id.performedExerciseTimeTextView);
            caloriesBurnTextView = itemView.findViewById(R.id.performedExerciseCaloriesTextView);
            correctnessTextView = itemView.findViewById(R.id.performedExerciseCorrectnessTextView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    ExerciseReportEntity getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(TraineesOverviewRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}

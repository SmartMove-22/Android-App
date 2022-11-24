package pt.ua.hackaton.smartmove.recyclers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ua.hackaton.smartmove.CoachAddExercisesFragment;
import pt.ua.hackaton.smartmove.CoachTraineeOverviewFragment;
import pt.ua.hackaton.smartmove.R;
import pt.ua.hackaton.smartmove.data.Trainee;

public class TraineesRecyclerViewAdapter extends RecyclerView.Adapter<TraineesRecyclerViewAdapter.ViewHolder> {

    private List<Trainee> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public TraineesRecyclerViewAdapter(Context context, List<Trainee> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public TraineesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.trainee_recycler_item, parent, false);
        return new TraineesRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(TraineesRecyclerViewAdapter.ViewHolder holder, int position) {
        Trainee trainee = mData.get(position);
        holder.traineeUsernameTextView.setText(trainee.getUsername());
        holder.traineeEmailTextView.setText(trainee.getEmail());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView traineeUsernameTextView;
        TextView traineeEmailTextView;
        Button manageTraineeButton;
        Button addExercisesTraineeButton;

        ViewHolder(View itemView) {

            super(itemView);

            traineeUsernameTextView = itemView.findViewById(R.id.traineeUsernamePlaceholder);
            traineeEmailTextView = itemView.findViewById(R.id.traineeEmailPlaceholder);
            manageTraineeButton = itemView.findViewById(R.id.manageTraineeBtn);
            addExercisesTraineeButton = itemView.findViewById(R.id.addExercisesTraineeBtn);

            FragmentManager fragmentManager = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();

            addExercisesTraineeButton.setOnClickListener(view -> fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CoachAddExercisesFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("add_trainees_exercises_fragment")
                    .commit());

            manageTraineeButton.setOnClickListener(view -> fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, CoachTraineeOverviewFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("manage_trainee_fragment")
                    .commit());

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Trainee getItem(int id) {
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

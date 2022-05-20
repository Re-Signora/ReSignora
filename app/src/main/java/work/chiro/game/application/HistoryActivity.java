package work.chiro.game.application;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import work.chiro.game.compatible.HistoryImplAndroid;
import work.chiro.game.history.HistoryObject;
import work.chiro.game.utils.Utils;

public class HistoryActivity extends AppCompatActivity {

    HistoryImplAndroid getHistory() {
        return HistoryImplAndroid.getInstance(HistoryActivity.this);
    }

    static class HistoryObjectItemHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView name;
        TextView score;
        TextView difficulty;

        public HistoryObjectItemHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.textViewTime);
            name = itemView.findViewById(R.id.textViewName);
            score = itemView.findViewById(R.id.textViewScore);
            difficulty = itemView.findViewById(R.id.textViewDifficulty);
        }

        void setFromHistoryObject(HistoryObject historyObject) {
            time.setText(historyObject.getTimeString());
            name.setText(historyObject.getName());
            score.setText(Utils.convertDoubleToString(historyObject.getScore()));
            difficulty.setText(Utils.difficultyToString(historyObject.getDifficulty()));
        }
    }

    class HistoryListAdapter extends RecyclerView.Adapter<HistoryObjectItemHolder> {
        @NonNull
        @Override
        public HistoryObjectItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(getApplicationContext(), R.layout.history_item, null);
            return new HistoryObjectItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryObjectItemHolder holder, int position) {
            HistoryObject historyObject = getHistory().getAll().get(position);
            holder.setFromHistoryObject(historyObject);
        }

        @Override
        public int getItemCount() {
            return getHistory().getAll().size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setAdapter(new HistoryListAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
package tiago.ninemenmorris.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tiago.ninemenmorris.DB.DBHandler;
import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Game;
import tiago.ninemenmorris.model.GameMetaData;

public class LoadActivity extends AppCompatActivity {

    private static final String TAG = "Load";

    public final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        // Fetches Game meta-data from the database and updates the recycle view
        (new AsynkTaskFetch()).execute();
    }

    @Override
    public void onBackPressed() {
        // Removes this activity from the activity stack
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.load_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.db_flush:
                (new AsynkTaskClear()).execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * AsynkTask that fetches a list of uncompleted game sessions and adds them to the
     * RecycleView.
     */
    private class AsynkTaskFetch extends AsyncTask<Void, Void, List<GameMetaData>> {
        @Override
        protected List<GameMetaData> doInBackground(Void... voids) {
            return DBHandler.getInstance().getAllGamesMetaData();
        }

        @Override
        protected void onPostExecute(List<GameMetaData> result) {
            RecyclerView recyclerView = findViewById(R.id.loadRecycleView);
            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new GameAdapter(result, context));
        }
    }

    /**
     * Flushes the database and clears the screen.
     */
    private class AsynkTaskClear extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            DBHandler.getInstance().flush();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            RecyclerView recyclerView = findViewById(R.id.loadRecycleView);
            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new GameAdapter(new ArrayList<GameMetaData>(), context));
        }
    }

    /**
     * Attempts to load the selected game and restart a previous game session.
     */
    private class AsynkTaskLoad extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            Log.w(TAG, "loading game " + integers[0] + "...");
            DBHandler.getInstance().loadGame(integers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent(context, GameActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
        // List of items to be displayed
        private final List<GameMetaData> items;
        private final Context contex;

        public GameAdapter(List<GameMetaData> items, Context context) {
            this.items = items;
            this.contex = context;
        }

        @NonNull
        @Override
        public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game, parent, false);
            return new GameAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull GameAdapter.ViewHolder holder, int position) {
            GameMetaData data = items.get(position);
            holder.gameId.setText("" + data.getId());
            holder.gameLabel.setText(data.getPlayer1() + " vs " + data.getPlayer2());
            holder.gameTimestamp.setText(data.getTimestamp());

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
            public TextView gameId;
            public TextView gameTimestamp;
            public TextView gameLabel;

            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                gameId = itemView.findViewById(R.id.gameId);
                gameTimestamp = itemView.findViewById(R.id.gameTimestamp);
                gameLabel = itemView.findViewById(R.id.gameLabel);
            }

            @Override
            public void onClick(View v) {
                GameMetaData data = items.get(getAdapterPosition());
                Log.d(TAG, "onClick: " + data.toString());
                (new AsynkTaskLoad()).execute(data.getId());
            }
        }
    }
}

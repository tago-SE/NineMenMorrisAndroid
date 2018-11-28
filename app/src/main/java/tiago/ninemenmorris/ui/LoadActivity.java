package tiago.ninemenmorris.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tiago.ninemenmorris.DB.ExampleLoadCode;
import tiago.ninemenmorris.R;
import tiago.ninemenmorris.model.Game;
import tiago.ninemenmorris.model.GameMetaData;

public class LoadActivity extends AppCompatActivity {

    private static final String TAG = "Load";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        RecyclerView recyclerView = findViewById(R.id.loadRecycleView);

        // Change this
        ExampleLoadCode example = new ExampleLoadCode();
        List<GameMetaData> list = (List<GameMetaData>) example.getGames();
        Log.w(TAG, list.toString());

        // Publishing the adapter
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new GameAdapter(list, this));

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

                // Replace start with load here
                int gameId = data.getId();
                Game.getInstance().start();
                // Start next activity
                Intent intent = new Intent(contex, GameActivity.class);
                startActivity(intent);
                finish();   // prevents the stack from returning to MainActivity

            }
        }
    }

}

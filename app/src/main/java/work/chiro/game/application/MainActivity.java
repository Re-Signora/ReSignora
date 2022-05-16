package work.chiro.game.application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import work.chiro.game.config.Difficulty;
import work.chiro.game.config.RunningConfig;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<Integer, Difficulty> buttonMap = Map.of(
                R.id.button_mode_easy, Difficulty.Easy,
                R.id.button_mode_medium, Difficulty.Medium,
                R.id.button_mode_hard, Difficulty.Hard
        );

        buttonMap.forEach((id, difficulty) -> {
            Button button = findViewById(id);
            button.setOnClickListener(v -> {
                RunningConfig.difficulty = difficulty;
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            });
        });
    }
}
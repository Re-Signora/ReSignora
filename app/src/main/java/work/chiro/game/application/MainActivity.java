package work.chiro.game.application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import work.chiro.game.compatible.ResourceProviderAndroid;
import work.chiro.game.config.Difficulty;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.x.compatible.ResourceProvider;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ResourceProvider.setInstance(new ResourceProviderAndroid() {
            @Override
            protected Context getContext() {
                return MainActivity.this;
            }
        });

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

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

        CheckBox musicEnable = findViewById(R.id.musicEnableCheckBox);
        musicEnable.setChecked(RunningConfig.musicEnable);
        musicEnable.setOnCheckedChangeListener((buttonView, isChecked) -> RunningConfig.musicEnable = isChecked);
    }
}
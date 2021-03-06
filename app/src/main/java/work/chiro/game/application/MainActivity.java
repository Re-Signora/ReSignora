package work.chiro.game.application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import work.chiro.game.compatible.ResourceProviderAndroid;
import work.chiro.game.config.Difficulty;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.utils.UtilsAndroid;
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
                R.id.button_start, Difficulty.Easy
                // R.id.button_mode_medium, Difficulty.Medium,
                // R.id.button_mode_hard, Difficulty.Hard
        );

        EditText editText = findViewById(R.id.editTextRemoteAddress);
        editText.setText(RunningConfig.targetServerHost);

        buttonMap.forEach((id, difficulty) -> {
            Button button = findViewById(id);
            button.setOnClickListener(v -> {
                RunningConfig.difficulty = difficulty;
                if (editText.getText() == null || (editText.getText() != null && editText.getText().length() == 0)) {
                    RunningConfig.targetServerHost = null;
                } else {
                    if (editText.getText() != null && editText.getText().length() != 0) {
                        RunningConfig.targetServerHost = String.valueOf(editText.getText());
                    }
                }
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            });
        });

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

        CheckBox musicEnable = findViewById(R.id.musicEnableCheckBox);
        musicEnable.setChecked(RunningConfig.musicEnable);
        musicEnable.setOnCheckedChangeListener((buttonView, isChecked) -> RunningConfig.musicEnable = isChecked);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UtilsAndroid.setScreen(this);
    }
}
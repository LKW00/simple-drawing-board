package com.cookandroid.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawingView drawingView;
    private Button backButton, saveButton, clearButton, applyThicknessButton;
    private ImageButton penButton, eraserButton, blackButton, blueButton, redButton;
    private EditText thicknessInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        drawingView = findViewById(R.id.drawingView);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
        clearButton = findViewById(R.id.clearButton);
        penButton = findViewById(R.id.penButton);
        eraserButton = findViewById(R.id.eraserButton);
        blackButton = findViewById(R.id.blackButton);
        blueButton = findViewById(R.id.blueButton);
        redButton = findViewById(R.id.redButton);
        thicknessInput = findViewById(R.id.thicknessInput);
        applyThicknessButton = findViewById(R.id.applyThicknessButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDrawing();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clear();
            }
        });

        penButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setPen();
            }
        });

        eraserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setEraser();
            }
        });

        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor("#000000");
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor("#0000FF");
            }
        });

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setColor("#FF0000");
            }
        });

        applyThicknessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thicknessStr = thicknessInput.getText().toString();
                if (!thicknessStr.isEmpty()) {
                    try {
                        float thickness = Float.parseFloat(thicknessStr);
                        drawingView.setThickness(thickness);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "유효한 숫자를 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("filePath")) {
            String filePath = intent.getStringExtra("filePath");
            File file = new File(filePath);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                drawingView.loadBitmap(bitmap);
            } else {
                Toast.makeText(this, "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDrawing() {
        Bitmap bitmap = drawingView.getBitmap();
        File path = new File(getFilesDir(), "drawings");
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, System.currentTimeMillis() + ".jpg");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Toast.makeText(this, "그림이 저장되었습니다: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "저장 중 오류가 발생했습니다.", e);
            Toast.makeText(this, "저장 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}

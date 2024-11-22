package com.cookandroid.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;

public class OpenActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> fileNames;

    private Button backButton;
    private Button deleteAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        deleteAllButton = findViewById(R.id.deleteAllButton);
        deleteAllButton.setOnClickListener(v -> {
            new AlertDialog.Builder(OpenActivity.this)
                    .setTitle("전체 삭제")
                    .setMessage("모든 파일을 삭제하시겠습니까?")
                    .setPositiveButton("예", (dialog, which) -> {
                        File path = new File(getFilesDir(), "drawings");
                        if (path.exists()) {
                            File[] files = path.listFiles();
                            if (files != null) {
                                boolean allDeleted = true;
                                for (File file : files) {
                                    if (!file.delete()) {
                                        allDeleted = false;
                                    }
                                }
                                fileNames.clear();
                                adapter.notifyDataSetChanged();
                                if (allDeleted) {
                                    Toast.makeText(OpenActivity.this, "모든 파일이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(OpenActivity.this, "일부 파일 삭제 실패.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    })
                    .setNegativeButton("아니요", null)
                    .show();
        });

        listView = findViewById(R.id.listView);
        fileNames = new ArrayList<>();

        loadFileList();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String fileName = fileNames.get(position);
            File file = new File(getFilesDir(), "drawings/" + fileName);
            if (file.exists()) {
                Intent intent = new Intent(OpenActivity.this, MainActivity.class);
                intent.putExtra("filePath", file.getAbsolutePath());
                startActivity(intent);
            } else {
                Toast.makeText(OpenActivity.this, "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            String fileName = fileNames.get(position);
            new AlertDialog.Builder(OpenActivity.this)
                    .setTitle("파일 삭제")
                    .setMessage("파일을 삭제하시겠습니까?")
                    .setPositiveButton("예", (dialog, which) -> {
                        File file = new File(getFilesDir(), "drawings/" + fileName);
                        if (file.exists() && file.delete()) {
                            fileNames.remove(position);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(OpenActivity.this, "파일이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OpenActivity.this, "파일 삭제 실패.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("아니요", null)
                    .show();
            return true;
        });
    }

    private void loadFileList() {
        File path = new File(getFilesDir(), "drawings");
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    fileNames.add(file.getName());
                }
            }
        }
    }
}

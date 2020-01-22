package com.rhl.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.os.Bundle;

import com.rhl.assignment.adapter.FileAdapter;
import com.rhl.assignment.bean.FileBean;
import com.rhl.assignment.viewmodel.FileViewModel;

import java.util.List;

public class FileListActivity extends AppCompatActivity {
    private FileViewModel fileViewModel;
    private FileAdapter fileAdapter;
    private Context mContext;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        initView();
        subscribeModel();
    }

    private void subscribeModel() {
        fileViewModel.getFileData().observe(this, fileBeans -> {
            if(fileBeans!=null){
                fileAdapter.addAll(fileBeans);
            }
        });
    }

    private void initView() {
        mContext = this;
        fileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);
        mRecyclerView = findViewById(R.id.rv_file);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(manager);
        fileAdapter = new FileAdapter(mContext);
        mRecyclerView.setAdapter(fileAdapter);
        mRecyclerView.setHasFixedSize(true);
        fileViewModel.getFileData(mContext);

    }
}

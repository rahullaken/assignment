package com.rhl.assignment.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.rhl.assignment.bean.FileBean;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FileViewModel extends AndroidViewModel {


    FileRepository fileRepository;
    private MutableLiveData<List<FileBean>> fileData = new MutableLiveData<>();
    private MutableLiveData<Boolean> saveData = new MutableLiveData<>();

    public FileViewModel(@NonNull Application application) {
        super(application);
        fileRepository = new FileRepository();
    }

    public MutableLiveData<List<FileBean>> getFileData() {
        return fileData;
    }

    public void saveFile(Context mContext, FileBean fileBean) {
        fileRepository.saveData(mContext, fileBean).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(longs -> {
            saveData.setValue(true);
        }, throwable -> {
            saveData.setValue(false);
        });
    }

    public void getFileData(Context mContext) {
        fileRepository.getFileList(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileBeans -> {
                    fileData.setValue(fileBeans);
                }, throwable -> {
                    fileData.setValue(null);
                });
    }


}

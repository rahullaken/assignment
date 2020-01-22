package com.rhl.assignment.viewmodel;

import android.content.Context;

import com.rhl.assignment.bean.FileBean;
import com.rhl.assignment.db.DatabaseClient;

import java.util.List;

import rx.Observable;

public class FileRepository {

    Observable<List<FileBean>> getFileList(final Context mContext) {
        return Observable.create(subscriber -> {
            List<FileBean> advertList = DatabaseClient.getInstance(mContext).getAppDatabase()
                    .fileDao().getAll();
            subscriber.onNext(advertList);
            subscriber.onCompleted();
        });
    }

    Observable<List<Long>> saveData(Context mContext, FileBean fileBean) {
        return Observable.create(subscriber -> {
            List<Long> longs = DatabaseClient.getInstance(mContext).getAppDatabase()
                    .fileDao()
                    .insert(fileBean);
            subscriber.onNext(longs);
            subscriber.onCompleted();
        });
    }
}

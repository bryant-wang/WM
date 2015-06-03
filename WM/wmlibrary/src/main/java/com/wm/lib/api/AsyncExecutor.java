package com.wm.lib.api;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

/**
 * Created by WM on 2015/6/2.
 */
@EBean
public class AsyncExecutor<T> {

    @SuppressWarnings("rawtypes")
    AsyncCallBack mListener;

    public <T> void execute(AsyncCallBack<T> listener) {
        mListener = listener;
        execute();
    }

    @UiThread
    <T> void execute() {
        try {
            mListener.onPreExecute();
            executing();
        } catch (Exception e) {
            executeFail(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Background
    <T> void executing() {
        try {
            T resut = (T) mListener.onExecute();
            executeSuccess(resut);
        } catch (Exception e) {
            executeFail(e);
        }
    }

    @SuppressWarnings("unchecked")
    @UiThread
    <T> void executeSuccess(T result) {
        try {
            mListener.onSuccess(result);
        } catch (Exception e) {
            executeFail(e);
        }
    }

    @UiThread
    void executeFail(Exception e) {
        mListener.onFail(e);
    }

    public static interface AsyncCallBack<T> {

        public void onPreExecute();

        public void onSuccess(T result);

        public void onFail(Exception e);

        public T onExecute();

    }

}

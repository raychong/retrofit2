package com.pentagon.retrofitexample.subscribers;

import android.content.Context;
import android.widget.Toast;

import com.pentagon.retrofitexample.progress.ProgressCancelListener;
import com.pentagon.retrofitexample.progress.ProgressDialogHandler;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * show ProgressDialog when request start
 * dismiss ProgressDialog when request ended
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;
    private boolean showProgressDialog = false;


    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context,boolean showProgressDialog) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.showProgressDialog = showProgressDialog;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        if(showProgressDialog)
            showProgressDialog();
    }

    @Override
    public void onCompleted() {
        if(showProgressDialog)
            dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        String msg = "Connection timeout";
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(showProgressDialog)
            dismissProgressDialog();

    }

    /**
     * Let the subscriber handle onNext event itself
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }

    /**
     * When ProgressDialog get dismissed，un-subscribe observable & http request
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
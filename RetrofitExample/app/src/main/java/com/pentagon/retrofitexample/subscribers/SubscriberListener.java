package com.pentagon.retrofitexample.subscribers;

public interface SubscriberListener<T> {
    void onStart();
    void onNext(T t);
    void onError(Throwable e);
    void onCompleted();
}

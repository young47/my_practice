package com.young.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

public class Client {
    public class AsyncCallback implements AsyncMethodCallback<WorkerThrift.AsyncClient.submitPushJob_call>{
        @Override
        public void onComplete(WorkerThrift.AsyncClient.submitPushJob_call response) {
//              response.getResult()
        }

        @Override
        public void onError(Exception e) {

        }
    }
}

package com.example.brent.films.Class;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

public class MyQueue extends RequestQueue {

    private static MyQueue myQueue;

    public MyQueue(Cache cache, Network network) {
        super(cache, network);
    }

    public static MyQueue GetInstance(Context context){
        if (myQueue == null){
            myQueue = new MyQueue(new DiskBasedCache(context.getCacheDir(), 1024*1024),
                                    new BasicNetwork(new HurlStack()));
            myQueue.start();
        }

        return myQueue;
    }

}

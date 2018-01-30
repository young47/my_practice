package com.young.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by young on 17/11/2.
 */
public class ReferenceTest {

    public static void main(String[] args)throws Exception{
        weakTest();
        System.out.println("");
        //softTest();
        System.out.println("");
        //PhanTest();
    }
    private static void weakTest() throws Exception{
        ReferenceQueue<User> referenceQueue = new ReferenceQueue<>();
        WeakReference<User> weakReference = new WeakReference<>(new User("weak"), referenceQueue);
        System.out.println("-----WeakReference-----");
        System.out.println("Before GC : "+weakReference.get());
        System.out.println("Before GC poll: "+referenceQueue.poll());

        int i = 0;
        System.gc();
       /* try {
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        System.runFinalization();
        System.out.println("After GC : "+weakReference.get());
        System.out.println("Weak Ref enqueued : "+weakReference.isEnqueued());
        System.out.println("After GC poll: "+referenceQueue.poll());
    }

    private static void softTest() {
        //是否回收的条件：
        //clock - timestamp <= freespace * SoftRefLRUPolicyMSPerMB
        ReferenceQueue<User> referenceQueue = new ReferenceQueue<>();
        SoftReference<User> softReference = new SoftReference<>(new User("soft"), referenceQueue);
        System.out.println("-----SoftReference-----");
        System.out.println("Before GC : "+softReference);
        System.out.println("Before GC poll: "+referenceQueue.poll());//
        System.gc();
        //System.runFinalization();
        System.out.println("After GC : "+softReference.get());
        System.out.println("Soft Ref enqueued : "+softReference.isEnqueued());
        System.out.println("After GC poll: "+referenceQueue.poll());
    }

    private static void PhanTest(){
        ReferenceQueue<User> referenceQueue = new ReferenceQueue<>();
        PhantomReference<User> phantomReference = new PhantomReference<>(new User("phantom"), referenceQueue);
        System.out.println("-----PhantomReference-----");
        System.out.println("Before GC : "+phantomReference);
        int i = 0;
        System.gc();
        //System.runFinalization();
        System.out.println("After GC : "+phantomReference.get());
        System.out.println("Phantom Ref enqueued : "+phantomReference.isEnqueued());
        System.out.println("After GC poll: "+referenceQueue.poll());

    }

    static class User {
        private String name;
        public User(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        /*@Override
        protected void finalize(){
            System.out.println("finalize() is executed : "+name);
        }*/
    }
}

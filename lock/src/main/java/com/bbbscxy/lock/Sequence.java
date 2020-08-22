package com.bbbscxy.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用condition顺序调用
 */
public class Sequence {

    static class Resource{

        private int num = 1;
        private ReentrantLock lock = new ReentrantLock();
        private Condition c1 = lock.newCondition();
        private Condition c2 = lock.newCondition();
        private Condition c3 = lock.newCondition();

        public void print1(){
            lock.lock();
            try {
                while (num != 1){
                    c1.await();
                }
                System.out.println(Thread.currentThread().getName()+": 1");
                TimeUnit.SECONDS.sleep(1);
                num = 2;
                c2.signal();
            }catch (Exception e){

            }finally {
                lock.unlock();
            }
        }

        public void print2(){
            lock.lock();
            try {
                while (num != 2){
                    c2.await();
                }
                System.out.println(Thread.currentThread().getName()+": 1");
                System.out.println(Thread.currentThread().getName()+": 2");
                TimeUnit.SECONDS.sleep(1);
                num = 3;
                c3.signal();
            }catch (Exception e){

            }finally {
                lock.unlock();
            }
        }

        public void print3(){
            lock.lock();
            try {
                while (num != 3){
                    c3.await();
                }
                System.out.println(Thread.currentThread().getName()+": 1");
                System.out.println(Thread.currentThread().getName()+": 2");
                System.out.println(Thread.currentThread().getName()+": 3");
                TimeUnit.SECONDS.sleep(1);
                num = 1;
                c1.signal();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Resource resource = new Resource();
        new Thread(()->resource.print1()).start();
        new Thread(()->resource.print2()).start();
        new Thread(()->resource.print3()).start();
    }
}

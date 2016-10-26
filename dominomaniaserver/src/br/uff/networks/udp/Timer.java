/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.networks.udp;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Gustavo
 */
public class Timer {
    private Thread thread;
    private float time;
    private float initialTime;
    private boolean permitCount=true;
    private boolean timeOut=false;
    private Sender sender;
    private Monitor monitor;
    public Timer(float time,Sender sender){
        this.time=time;
        this.sender=sender;
        this.monitor=new Monitor();
    }
    public synchronized void start(){
        this.stop();
        timeOut=false;
        permitCount=true;
        thread=new Thread(new Runnable() {

            @Override
            public void run() {
                initialTime=System.nanoTime();
                while(System.nanoTime()-initialTime<time&&permitCount){ 
                    //System.out.println("ok");
                }
                monitor.conditionMonitor();
                timeOut=true;
            }
        });
        thread.start();
    }
    public boolean getTimeOut(){
        return timeOut;
    }
    public void stop(){
        monitor.stopMonitor();
    }
    public boolean isRunning(){
        if(thread==null)return false;
        return thread.isAlive()&&permitCount;
    }
    
    private class Monitor{
        private final Lock lock=new ReentrantLock();
        private void stopMonitor(){
            lock.lock();
            try {
                permitCount=false;
            } finally {
                lock.unlock();
            }
        }
        private void conditionMonitor(){
            lock.lock();
            try {
                if(permitCount){
                    //System.out.println("tempo acabou "+permitCount);
                    sender.sendOfLast();
                }
            } finally {
                lock.unlock();
            }
        }
    }
}

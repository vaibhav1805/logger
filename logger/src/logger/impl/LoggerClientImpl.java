package logger.impl;

import logger.LoggerClient;
import logger.pojo.Process;

import java.util.Comparator;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LoggerClientImpl implements LoggerClient {
    /**
     * processMap stores process against processId providing O(1) lookup.
     * Process post ending get added to deadQueue
     * pendingPollCounter increments if a print is called and deadQueue is empty.
     * end checks for pendingPollCounter, if true calls a print.
     */

    private final PriorityBlockingQueue<Process> processQueue;
    private final ConcurrentHashMap<String, Process> processMap;
    private final BlockingQueue<Process> deadQueue;
    private final Lock lock;
    private final ExecutorService executorService;
    private int pendingPollCount;

    public LoggerClientImpl() {
        lock = new ReentrantLock();
        processMap = new ConcurrentHashMap<>();
        processQueue = new PriorityBlockingQueue<>(512, Comparator.comparingLong(Process::getEndTime));
        executorService = Executors.newFixedThreadPool(10);
        deadQueue = new LinkedBlockingQueue<>();
        pendingPollCount = 0;
    }

    @Override
    public void start(String processId, long startTime) {
        executorService.execute(() -> {
            lock.lock();
            try{
                Process p =  new Process(processId, startTime);
                processMap.put(processId, p);
                processQueue.add(p);
                System.out.println("Added process : " + processId);
            }catch (Exception e){
                System.out.println("Error : " + processId);
            }finally {
                lock.unlock();
            }
        });
    }

    @Override
    public void end(String processId, long endTime) {
        executorService.execute(() -> {
            lock.lock();
            try{
                processMap.get(processId).setEndTime(endTime);
                deadQueue.add(processMap.get(processId));

                if(pendingPollCount > 0){
                    pendingPollCount--;
                    print();
                }
                System.out.println("Ended process: " + processId);
            }catch (Exception e){

            }finally {
                lock.unlock();
            }
        });
    }

    @Override
    public void print() {
        executorService.execute(() -> {
            lock.lock();
            try {
                if(!deadQueue.isEmpty()){
                    Process p = deadQueue.remove();
                    System.out.println(String.format("Process id: %s, started: %d, ended %d: " , p.getProcessId(), p.getStartTime(), p.getEndTime()));
                }else{

                    pendingPollCount++;
                }
            }catch (Exception e){

            }finally {
                lock.unlock();
            }
        });
    }

}

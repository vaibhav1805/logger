package logger.pojo;

public class Process implements Comparable<Process>{
    private String processId;
    private Long startTime;
    private Long endTime;
    private String test;

    public Process(){}

    public Process(String processId, Long startTime){
        this.processId = processId;
        this.startTime = startTime;
        this.endTime = Long.MAX_VALUE;
    }

    public String getTest(){
        return this.test;
    }

    public String getProcessId(){
        return this.processId;
    }

    public Long getStartTime(){
        return this.startTime;
    }

    public Long getEndTime(){
        return this.endTime;
    }

    public void setEndTime(Long endTime){
        this.endTime =  endTime;
    }

    @Override
    public int compareTo(Process o) {
        return this.getEndTime().compareTo(o.getEndTime());
    }
}

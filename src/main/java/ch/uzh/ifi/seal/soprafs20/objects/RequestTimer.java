package ch.uzh.ifi.seal.soprafs20.objects;

public class RequestTimer implements java.lang.Runnable{

    @Override
    public void run() {
        this.runTimer();
    }

    private int requests=0;
    private boolean running=false;
    private int time;

    public void runTimer(){
        this.running=true;
        time = 60;
        while (time>0){
            //System.out.println("Remaining: "+time+" seconds");
            try {
                time--;
                Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
            }
            catch (InterruptedException e) {
                //I don't think you need to do anything for your particular problem
            }
        }
        this.running=false;
    }

    public RequestTimer resetTimer(){
        this.time=60;
        this.requests=0;
        return this;
    }



    public void addRequest(){
        this.requests+=1;
    }

    public int getRequests(){
        return this.requests;
    }

    public boolean isRunning() {
        return running;
    }

    public int getTime() {
        return time;
    }

}

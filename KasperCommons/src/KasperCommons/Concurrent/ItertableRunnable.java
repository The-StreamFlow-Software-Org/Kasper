package KasperCommons.Concurrent;

public abstract class ItertableRunnable implements Runnable{
    public int index;
    public ItertableRunnable(int n) {
        this.index = n;
    }


}

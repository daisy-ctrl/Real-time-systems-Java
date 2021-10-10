/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tt;

/**
 *
 * @author emran_arch
 */
import org.openjdk.jmh.annotations.*;
import java.util.LinkedList;
import java.util.concurrent.*;

public class factory {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    
    @Warmup(iterations=2)
    @Measurement(iterations=1)
    @Fork(value=1)
    
    public void test() {
        ExecutorService TomatoSauceFactory = Executors.newCachedThreadPool();
        int bottleID = 1;
        
//        long start = System.nanoTime();
//        while(bottleID <= 20) {
//            
//                TomatoSauceBottle bottle = new TomatoSauceBottle(bottleID);
//                try {
//                    Future<TomatoSauceBottle> cleanedBottle = TomatoSauceFactory.submit(new Cleaning(bottle));
//                    Future<TomatoSauceBottle> filledCan = TomatoSauceFactory.submit(new Filling(cleanedBottle.get()));
//                    Future<TomatoSauceBottle> sealedCan = TomatoSauceFactory.submit(new Sealing(filledCan.get()));
//                    Future<TomatoSauceBottle> labelledCan = TomatoSauceFactory.submit(new Labelling(sealedCan.get()));
//
//                    TomatoSauceBottle finalCan = labelledCan.get();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                ++bottleID;
//                //System.out.println("bottle: " + bottleID);
//            
//        }
//        long end = System.nanoTime() - start;
//        double total = (double)end/100000000.0;
//         System.out.println("Total time: " + total);
//        TomatoSauceFactory.shutdown();
        
                LinkedBlockingQueue<TomatoSauceBottle>bottleInClean = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<TomatoSauceBottle>bottleOutClean= new LinkedBlockingQueue<>();
   
    LinkedBlockingQueue<TomatoSauceBottle>bottleOutFill= new LinkedBlockingQueue<>();
    
    LinkedBlockingQueue<TomatoSauceBottle>bottleOutSeal= new LinkedBlockingQueue<>();

        fill f = new fill(bottleOutClean, bottleOutFill);
        seal s = new seal(bottleOutFill, bottleOutSeal);
        clean c = new clean(bottleInClean, bottleOutClean);
        
        new Thread(f).start();
        new Thread(s).start();
        new Thread(c).start();
        for (int i =1; i<21;i++){
            try{
                TomatoSauceBottle ts = new TomatoSauceBottle(bottleID);
               
                bottleInClean.put(ts);
               
                bottleID++;
            }
            catch (Exception e) {}
        }
        
        for (int i =1; i<16;i++){
            try{
                TomatoSauceBottle ts = bottleOutSeal.take();
               
            }
            catch (Exception e) {}
        }
      
    }
}

class TomatoSauceBottle {
    private int id;
    private boolean isCleaned;
    private boolean isFilled;
    private boolean isSealed;
    private boolean isLabelled;

    TomatoSauceBottle(int id){
        this.id = id;
        this.isFilled = false;
        this.isSealed = false;
        this.isLabelled = false;
        this.isCleaned = false;
    }

    public void setCleaned(boolean flag) {
        this.isCleaned = flag;
    }

    public void setFilled(boolean flag) {
        this.isFilled = flag;
    }

    public void setSealed(boolean flag) {
        this.isSealed = flag;
    }

    public void setLabelled(boolean flag) {
        this.isLabelled = flag;
    }
}

class Cleaning implements Callable<TomatoSauceBottle> {
    private TomatoSauceBottle tomatoSauceBottle;
    Cleaning(TomatoSauceBottle tomatoSauceBottle) {
        this.tomatoSauceBottle = tomatoSauceBottle;
    }
    @Override
    public TomatoSauceBottle call() throws Exception {
        //System.out.println("Got the Tomato Sauce Bottle for Processing....Cleaning the Tomato Sauce Bottle.....");
               tomatoSauceBottle.setFilled(true);
        //System.out.println("Tomato Sauce Bottle is Cleaned. Passing the Bottle to the next Stage");
        return tomatoSauceBottle;
    }
}

class Filling implements Callable<TomatoSauceBottle> {
    private TomatoSauceBottle tomatoSauceBottle;
    Filling(TomatoSauceBottle tomatoSauceBottle) {
        this.tomatoSauceBottle = tomatoSauceBottle;
    }
    @Override
    public TomatoSauceBottle call() throws Exception {
        //System.out.println("Got the Tomato Sauce Bottle for Processing....Filling with Tomato Sauce.....");
      
        tomatoSauceBottle.setFilled(true);
       // System.out.println("Can is Filled. Passing the Bottle to the next Stage");
        return tomatoSauceBottle;
    }
}

class Sealing implements Callable<TomatoSauceBottle> {
    private TomatoSauceBottle tomatoSauceBottle;
    Sealing(TomatoSauceBottle tomatoSauceBottle) {
        this.tomatoSauceBottle = tomatoSauceBottle;
    }
    @Override
    public TomatoSauceBottle call() throws Exception {
      //  System.out.println("Got the Tomato Sauce Bottle for Processing....Sealing the Tomato Sauce Bottle.....");
       
        tomatoSauceBottle.setFilled(true);
       // System.out.println("Tomato Sauce Bottle is Sealed. Passing the Bottle to the next Stage");
        return tomatoSauceBottle;
    }
}

class Labelling implements Callable<TomatoSauceBottle> {
    private TomatoSauceBottle tomatoSauceBottle;
    Labelling(TomatoSauceBottle tomatoSauceBottle) {
        this.tomatoSauceBottle = tomatoSauceBottle;
    }
    @Override
    public TomatoSauceBottle call() throws Exception {
       // System.out.println("Got the Tomato Sauce Bottle for Processing....Labelling the Tomato Sauce Bottle.....");
        
        tomatoSauceBottle.setFilled(true);
      //  System.out.println("Tomato Sauce Bottle is Labelled. Passing the Bottle to the next Stage");
        return tomatoSauceBottle;
    }
}




class fill implements Runnable {
     LinkedBlockingQueue<TomatoSauceBottle>bottleIn;
    LinkedBlockingQueue<TomatoSauceBottle>bottleOut;

    public fill(LinkedBlockingQueue<TomatoSauceBottle> bottleIn, LinkedBlockingQueue<TomatoSauceBottle> bottleOut) {
        this.bottleIn = bottleIn;
        this.bottleOut = bottleOut;
    }
    
    
    @Override
    public void run() {
        for (int i =1;i<21;i++){
            try{
                TomatoSauceBottle t = bottleIn.take();
                t.setFilled(true);
                bottleOut.put(t);
            }
            catch (Exception e){}
        }
    }
    
    
}

class seal implements Runnable {
    LinkedBlockingQueue<TomatoSauceBottle>bottleIn;
    LinkedBlockingQueue<TomatoSauceBottle>bottleOut;

    public seal(LinkedBlockingQueue<TomatoSauceBottle> bottleIn, LinkedBlockingQueue<TomatoSauceBottle> bottleOut) {
        this.bottleIn = bottleIn;
        this.bottleOut = bottleOut;
    }
    
    
    
    @Override
    public void run() {
        for (int i =1;i<21;i++){
            try{
                TomatoSauceBottle t = bottleIn.take();
                t.setSealed(true);
                bottleOut.put(t);
            }
            catch (Exception e){}
        }
    }
    
    
}

class clean implements Runnable {
    LinkedBlockingQueue<TomatoSauceBottle>bottleIn;
    LinkedBlockingQueue<TomatoSauceBottle>bottleOut;

    public clean(LinkedBlockingQueue<TomatoSauceBottle> bottleIn, LinkedBlockingQueue<TomatoSauceBottle> bottleOut) {
        this.bottleIn = bottleIn;
        this.bottleOut = bottleOut;
    }
    
    
    @Override
    public void run() {
        for (int i =1;i<21;i++){
            try{
                //System.out.println("taking bottle to clean");
                TomatoSauceBottle t = bottleIn.take();
                //System.out.println("cleaned bottle");
                t.setCleaned(true);
                bottleOut.put(t);
            }
            catch (Exception e){}
        }
    }
    
    
}
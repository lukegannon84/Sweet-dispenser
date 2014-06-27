import java.util.concurrent.locks.*;

class Sweets {

	private Lock bagLock = new ReentrantLock();
	private Condition notFull  = bagLock.newCondition(); 
	private Condition notEmpty = bagLock.newCondition(); 


    	private int theBag = 0; // initially no sweets
    	private final int maxSweets = 100;
                      
    	void put() {
    		bagLock.lock();
    		try {
    			while (theBag==maxSweets) 
    				try { notFull.await(); }
    				catch (InterruptedException e) {};
        		theBag++;
        		notEmpty.signal();
    			} finally {
    				bagLock.unlock();
    		
    		}
    }
    
    void take() {
    		bagLock.lock();
    		try {
    			while (theBag==0) 
    				try { notEmpty.await(); }
    				catch (InterruptedException e) {};
        		theBag--;
        		notFull.signal();
    		} finally {
    			bagLock.unlock();
    				
    		}
    	}
}

class Child extends Thread { 

    private Sweets bag;
    public Child(Sweets bag) {
        this.bag = bag;
    }

    public void run() {
	
        while(true) {
            bag.take();
            
        }
        
    }
}

class Parent extends Thread { 

    private Sweets bag;

    public Parent(Sweets bag) {
        this.bag = bag;
    }

    public void run() {
		
        while(true) {
            bag.put();
            
        }
    }
}

class SweetsTest{
	public static void main (String[] args) {
		
		Sweets bag = new Sweets();
        Child c = new Child(bag);
        Parent p = new Parent(bag);

        c.start(); 
        p.start();
       
		
		
    }
}







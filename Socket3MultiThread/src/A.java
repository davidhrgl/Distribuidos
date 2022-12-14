import java.lang.Thread;
import java.lang.Exception;

class A extends Thread{
    static long n;
    static Object obj = new Object(); //Syncronized
    public void run(){
        for (int i = 0; i < 100000; i++)
            synchronized(obj){  //Syncronized
                n++;
            }
    }
    public static void main(String[] args) throws Exception{
        A t1 = new A();
        A t2 = new A();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(n);
    }
}
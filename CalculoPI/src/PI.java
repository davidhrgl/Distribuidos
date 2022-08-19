import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class PI
{
    static final Object lock = new Object();
    static double pi = 0;
    static class Worker extends Thread
    {
        Socket conexion;
        Worker(Socket conexion)
        {
            this.conexion = conexion;
        }
        public void run()
        {
            // Algoritmo 1
            try {
                DataInputStream entrada =  new DataInputStream(this.conexion.getInputStream());
                double x;
                x = entrada.readDouble();
                synchronized (lock){
                    pi += x ;
                }
                entrada.close();
                conexion.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }
    public static void main(String[] args) throws Exception
    {
        if (args.length != 1)
        {
            System.err.println("Uso:");
            System.err.println("java PI <nodo>");
            System.exit(0);
        }
        int nodo = Integer.parseInt(args[0]);
        if (nodo == 0)
        {
            // Algoritmo 2

            ServerSocket server = new ServerSocket(50000);
            Worker[] w = new Worker[4];
            for(int i=0; i<4 ;i++){
                Socket conexion =  server.accept();
                w[i] = new Worker(conexion);
                w[i].start();
            }
            for(int i=0; i<4; i++){
                w[i].join();
            }
            System.out.println("El valor calculado de PI es: " + pi);
        }
        else
        {
            // Algoritmo 3
            Socket conexion = null;
            while(true){
                try{
                    conexion = new Socket("localhost",50000);
                    break;
                }catch (Exception e){
                    Thread.sleep(100);
                }
            }
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            double suma = 0;
            for(int i=0;i<10000000;i++){
                suma+= 4.0 / (8 * i + 2 * (nodo-2) + 3);
            }
            suma = (nodo%2 ==0) ? - suma : suma;
            salida.writeDouble(suma);
            salida.close();
            conexion.close();

        }
    }
}
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static class Worker extends Thread{
        static Socket conexion;
        Worker(Socket conexion){
            this.conexion = conexion;
        }

        public void run() {
            try {
                DataInputStream  entrada =  new DataInputStream(conexion.getInputStream());
                double value = entrada.readDouble();
                System.out.println("Este es el valor: "+  value);
                entrada.close();
                conexion.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    public static void main(String[] args) throws Exception {
        ServerSocket server =  new ServerSocket(50000);
        while(true){
            Socket conexion = server.accept();
            Worker w =  new Worker(conexion);
            w.start();
        }
    }
}

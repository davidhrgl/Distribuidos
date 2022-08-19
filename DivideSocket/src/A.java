import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class A {
    static class WorkerA extends Thread {
        Socket conecction;
        long numero;
        long numero_min;
        long numero_max;
        String resultado = "NO DIVIDE";
        WorkerA(Socket conexion)
        {
            this.conecction = conexion;
        }
        @Override
        public void run() {
            DataInputStream input;
            try {
                input = new DataInputStream(conecction.getInputStream());
                PrintWriter pw = new PrintWriter(conecction.getOutputStream(), true);
                numero = input.readLong();
                numero_min = input.readLong();
                numero_max = input.readLong();
                for(long n = numero_min;n<=numero_max;n++){
                    //Si NÚMERO%n==0 entonces n divide a NÚMERO
                    System.out.println(numero + "%" +n+"="+(numero % n));
                    int valor = (int) (numero % n);
                    if (valor== 0) {
                        resultado = "DIVIDE";
                        break;
                    }
                }
                System.out.println(resultado);
                pw.println(resultado);
                input.close();
                pw.close();
                conecction.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Se debe de pasar el numero de puerto que se le asignara.");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        try {
            ServerSocket server = new ServerSocket(port);
            while(true){
                try {
                    Socket connection = server.accept();
                    System.out.println("Servidor A en el puerto:" + port );
                    WorkerA w = new WorkerA(connection);
                    w.start();
                    w.join();
                } catch (IOException e) {
                    Thread.sleep(100);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

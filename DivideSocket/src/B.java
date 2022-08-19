import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class B {

    static class Instance implements Runnable {
        private volatile String value;
        private int port;
        private long numero;
        private long numero_inicial;
        private long numero_final;
        Instance(int port,long numero,long numero_inicial,long numero_final){
            this.port = port;
            this.numero =numero;
            this.numero_inicial = numero_inicial;
            this.numero_final = numero_final;
        }

        @Override
        public void run() {
            DataOutputStream output;
            Socket connection_out;
            while (true) {
                try {
                    connection_out = new Socket("localhost", 50000 + port);
                    output = new DataOutputStream(connection_out.getOutputStream());
                    output.writeLong(numero);
                    output.writeLong(numero_inicial);
                    output.writeLong(numero_final);
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection_out.getInputStream()));
                    value = in.readLine();
                    break;
                } catch (IOException e) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        public String getValue() {
            return value;
        }
    }
    static class WorkerB extends Thread {
        Socket connection_input;
        String result = "ES PRIMO";
        WorkerB(Socket conexion)
        {
            this.connection_input = conexion;
        }
        @Override
        public void run() {
            try {
                long numero = 0L;
                DataOutputStream output;
                DataInputStream input;
                input = new DataInputStream(connection_input.getInputStream());
                PrintWriter pw = new PrintWriter(connection_input.getOutputStream(), true);
                numero = input.readLong();
                System.out.println("Numero recibido: "+numero+ "\n");
                for(int i=1;i<=3;i++){
                    long numero_min = 0L;
                    long numero_max = 0L;
                    long k = Math.round(numero/3);
                    if(i==1){
                        numero_min = 2;
                        numero_max = k;
                    } else if (i==2) {
                        numero_min = k+1;
                        numero_max = 2*k;
                    }else {
                        numero_min = 2*(k+1);
                        numero_max = numero-1;
                    }

                    System.out.println("Instancia "+i+":");
                    System.out.println(numero+","+numero_min+","+numero_max);
                    Instance instance = new Instance(i,numero,numero_min,numero_max);
                    Thread thread = new Thread(instance);
                    thread.start();
                    thread.join();
                    String value = instance.getValue();
                    System.out.println(value);
                    if(Objects.equals(value, "DIVIDE")){
                        result="NO ES PRIMO";
                    }
                }
                pw.println(result);
                input.close();
                connection_input.close();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) {
        try {
            int port = 50000;
            ServerSocket server = new ServerSocket(port);
            int id = 1;
            while(true){
                try {
                    Socket connection = server.accept();
                    System.out.println("Cliente:" + id + " conectado al Servidor B");
                    WorkerB w = new WorkerB(connection);
                    w.start();
                    w.join();
                    id++;
                } catch (IOException e) {
                    Thread.sleep(100);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

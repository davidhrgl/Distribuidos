import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Token {

    static String[] hosts =  new String[10];
    static int[] puertos =  new int[10];
    static int num_nodos;
    static int nodo;
    static int max_nodo;


    static class Worker extends Thread {
        Socket connection_input;
        Worker(Socket conexion)
        {
            this.connection_input = conexion;
        }
        @Override
        public void run() {
            try {
                long token;
                DataOutputStream output;
                DataInputStream input;
                input = new DataInputStream(connection_input.getInputStream());
                token = input.readLong();
                System.out.println("Token: "+token+ "\n");
                if (token >= 1000){
                   System.out.println("Exit Program");
                   System.exit(1);
                }else{
                    token++;
                    Socket connection_out;
                    while (true) {
                        try {
                            int node = nodo+1;
                            if(max_nodo == nodo){
                                node = 0;
                            }
                            //System.out.println("**Host:"+hosts[node]+"Puerto: "+puertos[node]+ "\n");
                            connection_out = new Socket(hosts[node], puertos[node]);
                            output = new DataOutputStream(connection_out.getOutputStream());
                            output.writeLong(token);
                            break;
                        } catch (IOException e) {
                            Thread.sleep(100);
                        }
                    }
                    connection_out.close();
                    output.close();
                }

                input.close();
                connection_input.close();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Servidor extends Thread {
        @Override
        public void run() {
            try {
                //System.out.println("***Puerto: "+puertos[nodo]+ "\n");
                ServerSocket server = new ServerSocket(puertos[nodo]);
                while(true){
                    try {
                        Socket connection = server.accept();
                        Worker w = new Worker(connection);
                        w.start();
                        w.join();
                        //break;
                    } catch (IOException e) {
                        Thread.sleep(100);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        if (args.length <= 3) {
            System.err.println("Se debe pasar como " +
                    "parametros el numero de nodo y el " +
                    "host de los siguientes nodos con su respectivo puerto");
            System.exit(1);
        }

        nodo = Integer.parseInt(args[0]);
        num_nodos = args.length - 1;
        for(int i=1; i< args.length ;i++){
            max_nodo = i-1;
            hosts[i-1] = args[i].toString().split(":")[0];
            puertos[i-1] = Integer.parseInt(args[i].toString().split(":")[1]);
        }

        Servidor s = new Servidor();
        s.start();
        //System.out.println("Nodo:"+nodo+ "\n");
        if(nodo==0){
            Socket connection_out;
            DataOutputStream output;
            while (true) {
                try {
                    //System.out.println("****Host:"+hosts[nodo+1]+"Puerto: "+puertos[nodo+1]+ "\n");
                    connection_out = new Socket(hosts[nodo+1],puertos[nodo+1]);
                    output = new DataOutputStream(connection_out.getOutputStream());
                    long token = 0L;
                    output.writeLong(token);
                    break;
                } catch (IOException e) {
                    Thread.sleep(100);
                }
            }
            output.close();
            connection_out.close();
        }
        s.join();
    }
}
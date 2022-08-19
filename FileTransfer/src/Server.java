import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    public static void main(String []args) throws Exception {
        String serverDirectory = "Subidas"; // Esta es la carpeta donde se almacenaran los archivos recibidos

        ServerSocket listenerSocket;
        listenerSocket = new ServerSocket(5000);

        try {
            int id = 1;
            while (true) {
                Socket clientSocket = listenerSocket.accept();
                System.out.println("Cliente:" + id + " , conectado desde: " + clientSocket.getInetAddress().getHostName());
                Thread worker = new Worker(clientSocket, serverDirectory);
                id++;
                worker.start();
            }
        } catch (Exception ex) {
            try {
                listenerSocket.close();
            } catch (IOException e) {
                System.out.println("SocketClose: " + e.getMessage());
            }
        }



    }
}


class Worker extends Thread {
    private Socket connectionSocket;
    private int m;
    private String dirName;
    private String name;
    public Worker(Socket socket, String directory) {
        connectionSocket = socket;
        dirName = directory;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            InputStream inFromClient = connectionSocket.getInputStream();
            System.out.println("Esperando a recibir un archivo.......");
            name = in.readLine();
            System.out.println("Recibiendo archivo: " +name+ " ............");
            try {
                boolean completado = true;
                File directory = new File(dirName);
                if (!directory.exists()) {
                    directory.mkdir();
                    System.out.println("Directorio creado");
                }

                int size = 100000000; //Fragmentos de 100MB
                byte[] data = new byte[size];
                File fc = new File(directory, name);
                FileOutputStream fileOut = new FileOutputStream(fc);
                DataOutputStream dataOut = new DataOutputStream(fileOut);

                while (completado) {
                    m = inFromClient.read(data, 0, data.length);
                    if (m == -1) {
                        completado = false;
                        System.out.println("Subida Completa..");
                    } else {
                        dataOut.write(data, 0, m);
                        dataOut.flush();
                    }
                }
                fileOut.close();
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                connectionSocket.close();
            } catch (IOException e) {
                System.out.println("SocketClose: " + e.getMessage());
            }
        }
    }

}

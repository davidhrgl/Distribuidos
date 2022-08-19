import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Se debe de pasar el nombre del archivo, que se enviara al Servidor");
            System.exit(1);
        }
        String name,path;
        Socket cliente;
        OutputStream outStr;
        PrintWriter pw;
        String[] archivos = new String[50];

        for(int i=0; i< args.length ;i++){
            archivos[i] = args[i].toString();
        }

        for(int i=0;i<args.length;i++){
            try {
                cliente = new Socket("localhost", 5000);
                pw = new PrintWriter(cliente.getOutputStream(), true);
                outStr = cliente.getOutputStream();
                name = archivos[i];

                FileInputStream file = null;
                BufferedInputStream bis = null;

                File f = new File(name);
                String absolute = f.getAbsolutePath();
                boolean fileExists = true;
                path = absolute.replace("\\","\\\\"); // Esto es solo para Windows
                try {
                    file = new FileInputStream(path);
                    bis = new BufferedInputStream(file);
                } catch (FileNotFoundException excep) {
                    fileExists = false;
                    System.out.println("FileNotFoundException:" + excep.getMessage());
                }
                if (fileExists) {
                    pw.println(name);
                    System.out.println("Subiendo archivo: "+name+".....");
                    sendBytes(bis, outStr);
                    System.out.println("Completado");
                    bis.close();
                    file.close();
                    outStr.close();
                }
            }
            catch (Exception exc) {
                System.out.println("Exception: " + exc.getMessage());
            }
        }
    }

    private static void sendBytes(BufferedInputStream input , OutputStream output) throws Exception {
        int size = 100000000; //Fragmentos de 100MB
        byte[] data = new byte[size];
        int c = input.read(data, 0, data.length);
        output.write(data, 0, c);
        output.flush();
    }
}
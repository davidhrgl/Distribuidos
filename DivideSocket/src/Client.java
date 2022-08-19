import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Se debe de pasar el numero que se enviara al Servidor");
            System.exit(1);
        }
        int numero = Integer.parseInt(args[0]);
        try {
            Socket connection_out = new Socket("localhost", 50000);
            DataOutputStream output = new DataOutputStream(connection_out.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(connection_out.getInputStream()));

            output.writeLong(numero);
            String value = in.readLine();
            System.out.println(value);
            output.close();
            in.close();
            connection_out.close();
        }
        catch (Exception exc) {
            System.out.println("Exception: " + exc.getMessage());
        }
    }
}
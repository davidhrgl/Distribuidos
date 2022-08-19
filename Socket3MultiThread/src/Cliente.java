import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws InterruptedException {
        Socket connection = null;
        while (true){
            try {
                connection = new Socket("localhost", 50000);
                DataOutputStream salida = new DataOutputStream(connection.getOutputStream());
                salida.writeDouble(1222.1232);
                break;
            }catch (Exception e){
                Thread.sleep(100);
            }
        }
    }
}

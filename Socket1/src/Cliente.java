import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Cliente {
    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
        while(longitud > 0){
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }
    public static void main(String[] args) throws Exception {
        Socket conexion =  new Socket("localhost",50000);
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());

        salida.writeInt(123);
        salida.writeDouble(1234567890.1234567890);
        salida.write("hola desde el cliente".getBytes());

        //Leer desde el Servidor
        byte[] buffer =  new byte[22];
        read(entrada,buffer,0,22);
        System.out.println(new String(buffer,"UTF-8"));

        //Enviar un paquete de dobles en un Bybuffer
        ByteBuffer b = ByteBuffer.allocate(5*8);
        b.putDouble(1.1);
        b.putDouble(1.2);
        b.putDouble(1.3);
        b.putDouble(1.4);
        b.putDouble(1.5);
        byte[] a = b.array();

        salida.write(a);

        salida.close();
        entrada.close();
        conexion.close();


    }
}

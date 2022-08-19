import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Servidor {
    public static void main(String[] args) throws Exception {
        ServerSocket servidor =  new ServerSocket(50000);
        Socket conexion =  servidor.accept();
        DataOutputStream salida =  new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada =  new DataInputStream(conexion.getInputStream());

        //Recibe un entero de 32 Bits
        int n =  entrada.readInt();
        System.out.println(n);

        //Recibe un flotande 64 bits
        double x = entrada.readDouble();
        System.out.println(x);

        //Recibe cadena de 4 caracteres
        byte[] buffer = new byte[21];
        read(entrada,buffer,0,21);
        System.out.println(new String(buffer,"UTF-8"));

        //Envia una cadena
        salida.write("Hola desde el Servidor".getBytes());

        //Recibe un arreglo de Bytes
        byte[] a = new byte[5*8];
        read(entrada,a,0,5*8);
        ByteBuffer b = ByteBuffer.wrap(a);
        for(int i=0;i<5;i++) System.out.println(b.getDouble());

        salida.close();
        entrada.close();
        conexion.close();

    }


    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception
    {
        while (longitud > 0)
        {
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }
}


import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws Exception {
        ServerSocket servidor = new ServerSocket(50000);
        Socket conexion =  servidor.accept();
        DataInputStream entrada =  new DataInputStream(conexion.getInputStream());
        long t1 = System.currentTimeMillis();
        //Entradas
//        byte[] a = new byte[10000*8];
//        read(entrada,a,0,10000*8);
//        ByteBuffer b = ByteBuffer.wrap(a);
        //Entradas
        double x[] = new double[10000];
        for (int i=0; i<10000;i++){
            x[i] = entrada.readDouble();
        }

        long t2 = System.currentTimeMillis();
        System.out.println("Tiempo: " + (t2-t1) + "ms");
        for(int i=0;i<10000;i++) System.out.println(x[i]);
//        for(int i=0;i<10000;i++) System.out.println(b.getDouble());

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
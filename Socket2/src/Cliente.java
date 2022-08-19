import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws Exception {
        Socket conexion = new Socket("localhost",50000);

        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());

        long t1 = System.currentTimeMillis();
        //Salida
//        ByteBuffer b = ByteBuffer.allocate(10000*8);
//        for (int i=0;i<10000;i++){
//            b.putDouble((double) i);
//        }
//        byte[] a = b.array();
//        salida.write(a);

        //Salida
        for(int i = 0;i<10000;i++){
            salida.writeDouble((double) i);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Tiempo: " + (t2-t1)+ " ms");
    }
}
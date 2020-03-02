import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        InterficieHorari horari = null;

        Scanner scan = new Scanner(System.in);

        String ip, ciutat, hora;



        /*

         * inicializar connexió.

         */

        while (true) {

            System.out.println("Indiqui la IP del servidor");

            ip = scan.nextLine();

            System.out.println();

            /*

             * Revisa que la direcció IP sigui correcte, en cas afirmatiu es

             * es connecta

             */

            try {

                /*

                 * creant el stub

                 */

                Registry registro = LocateRegistry.getRegistry(ip,5550);

                horari = (InterficieHorari) registro.lookup("Horari");

                break;

            } catch (NotBoundException e) {

                System.err.println("Quin error es? "+e);

            } catch (RemoteException e) {

                System.err.println("ERROR: Dirección IP " + ip + " Time Out");

            }

        }



        /*

         * Interacció client servidor

         */

        while (true) {

            System.out.println("Introdueixi el nom de la poblacio (adeu per sortir)");

            ciutat = scan.nextLine();

            System.out.println();


            try {
                System.out.println(horari.donaHora(ciutat) + "\n");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            /*

             * En cas que el client es despedeixi

             */

            if (ciutat.toLowerCase().equals("adeu"))

                break;

            /*

             * En cas que la ciutat no existeixi, s'ha de crear

             */

        }

    }

}

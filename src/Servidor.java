import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Servidor {

    public static void main(String[] args) {

        Registry reg;

        {
            try {
                reg = LocateRegistry.createRegistry(5550);

                System.out.println("Creant l'objecte servidor i inscribint-lo al registre");

                Horari horari = new Horari("ZonesHoraries.json");

                reg.rebind( "Horari", (InterficieHorari) UnicastRemoteObject.exportObject(horari,0) );

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /*

        enllaç de la interfície remota "InterficieHoraria" amb el nom identificador "Horari" en el registre del servidor

        */


    }

}

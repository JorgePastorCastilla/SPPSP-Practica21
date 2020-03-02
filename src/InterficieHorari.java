
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author jaumecampsfornari
 */
public interface InterficieHorari extends Remote {
    /*
     * Metode encarregat de retornar l'hora i si no existeix i crear la BBDD en cas que no existeixi
     */
    public String donaHora(String ciutat)throws RemoteException;

    /*
     * Metode eencarregat d'introduir les ciutats no existens a la BBDD
     */
    public boolean novaCiutat(String ciutat, String hora)throws RemoteException;

}
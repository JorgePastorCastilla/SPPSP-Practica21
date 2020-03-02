import java.rmi.RemoteException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;



/**
 *
 * @author jaumecampsfornari
 */
public class Horari implements InterficieHorari{
    private String urlJson;

    public Horari(String urlJson) {
        this.urlJson = urlJson;
    }

    @Override
    public String donaHora(String ciutat) throws RemoteException {
        String dbString = "";
        String resposta = "";
        BufferedReader in = null;

        try{
            /*
             * Revisa que la "base de dades" existeix, sinò la crea
             */
            try {
                in = new BufferedReader(new FileReader(urlJson));
            } catch(IOException e) {

                try (BufferedWriter out = new BufferedWriter(new FileWriter(urlJson));){
                    out.write("{ciutats{}}");
                    out.flush();
                } catch (IOException x) {
                    System.err.println(urlJson);
                    x.printStackTrace();
                }

                in = new BufferedReader(new FileReader(urlJson));
            }
            String s;
            while((s = in.readLine()) != null) {
                dbString += s;
            }
            /*
             * Inicialització de la base de dades
             */
            JSONObject db = new JSONObject(dbString);
            JSONObject ciutats = new JSONObject(db.get("ciutats").toString());

            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT" + ciutats.get(ciutat.toLowerCase())));
            resposta = format.format(new Date());
            return resposta;
        }catch (IOException e) {
            System.err.println("Error amb el servidor");
        }catch (JSONException e) {
            throw new RemoteException("Ciutat no existent a la base de dades");
        }

        return resposta;
    }

    @Override
    public boolean novaCiutat(String ciutat, String hora) throws RemoteException {

        SimpleDateFormat format = new SimpleDateFormat("HH");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        int gmtHora = Integer.parseInt(format.format(new Date()));

        format = new SimpleDateFormat("dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        int gmtDia = Integer.parseInt(format.format(new Date()));

        format = new SimpleDateFormat("MM");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        int gmtMes = Integer.parseInt(format.format(new Date()));

        format = new SimpleDateFormat("yyyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        int gmtAny = Integer.parseInt(format.format(new Date()));

        String[] temps = hora.split(" ");

        int h, dia, any, mes;

        try {
            h = Integer.parseInt(temps[1].split(":")[0]);
            dia = Integer.parseInt(temps[0].split("/")[0]);
            mes = Integer.parseInt(temps[0].split("/")[1]);
            any = Integer.parseInt(temps[0].split("/")[2]);
        }catch(Exception e) {
            return false;
        }

        if(any == gmtAny && mes == gmtMes && dia > gmtDia) h += 24;
        else if(any == gmtAny && mes == gmtMes && dia < gmtDia) gmtHora += 24;
        else if(any > gmtAny || mes > gmtMes) h += 24;
        else if(any < gmtAny || mes < gmtMes) gmtHora += 24;

        int zonaHoraria = h - gmtHora;

        if (zonaHoraria > 14 || zonaHoraria < -12) return false;

        try{
            BufferedReader in = new BufferedReader(new FileReader(urlJson));

            String s;
            String dbString = "";
            while((s = in.readLine()) != null) {
                dbString += s;
            }

            JSONObject db = new JSONObject(dbString);
            JSONObject ciutats = new JSONObject(db.get("ciutats").toString());

            if(zonaHoraria >= 0){
                ciutats.put(ciutat.toLowerCase(),"+" + zonaHoraria);
            }else{
                ciutats.put(ciutat,zonaHoraria);
            }
            db.put("ciutats", ciutats);

            BufferedWriter out = new BufferedWriter(new FileWriter(urlJson));
            out.write(db.toString());
            out.flush();

            in.close();
            out.close();
            return true;
        }catch(IOException e) {
            System.err.println("Error amb el servidor");
        } catch (JSONException ex) {
            Logger.getLogger(Horari.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
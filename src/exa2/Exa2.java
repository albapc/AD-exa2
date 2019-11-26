package exa2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Alba
 */
public class Exa2 {

    /**
     * este exercicio ten a mesma estructura de exa15 pero agora o ficheiro de
     * referencia e o xml, a composición dos pratos atopase no ficheiro de texto
     * delimitado e os seus compoñentes na táboa de oracle .tratase de xerar un
     * ficheiro serializado cos resultados
     */
    public static Connection conexion = null;

    public static Connection getConexion() throws SQLException {
        String usuario = "hr";
        String password = "hr";
        String host = "localhost";
        String puerto = "1521";
        String sid = "orcl";
        String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;

        conexion = DriverManager.getConnection(ulrjdbc);
        return conexion;
    }

    public static void closeConexion() throws SQLException {
        conexion.close();
    }

    public static void main(String[] args) throws XMLStreamException, FileNotFoundException, IOException, SQLException, ClassNotFoundException {
        Exa2.getConexion();
        XMLInputFactory xif = XMLInputFactory.newInstance();
        XMLStreamReader xsr = xif.createXMLStreamReader(new FileInputStream("platos.xml"));

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serial.dat"));
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("serial.dat"));

        Platos plato;

        String[] leido;
        String linea, codp = null, codc = null, nomep;
        int peso, graxa, total, graxa_parcial = 0;

        while (xsr.hasNext()) {
            total = 0;
            xsr.next();
            BufferedReader br = new BufferedReader(new FileReader("composicion.txt"));

            switch (xsr.getEventType()) {

                case XMLStreamConstants.START_ELEMENT:
                    String localName = xsr.getLocalName();

                    if (localName.equals("Plato")) {
                        codp = xsr.getAttributeValue(0);
                    }
                    break;

                case XMLStreamConstants.CHARACTERS: //contenido de las etiquetas

                    nomep = xsr.getText();

                    while ((linea = br.readLine()) != null) {
                        leido = linea.split("#");

                        if (leido[0].equals(codp)) {
                            codc = leido[1];
                            peso = Integer.parseInt(leido[2]);

                            String sql = "select graxa from componentes where codc='" + codc + "'";
                            Statement stmt = conexion.createStatement();
                            ResultSet rs = stmt.executeQuery(sql);

                            while (rs.next()) {
                                graxa = rs.getInt("graxa");
                                graxa_parcial = (graxa * peso) / 100;
                                total = total + graxa_parcial;

                            }
                        }

                    }

                    //System.out.println(codp + " " + nomep + " " + total);
                    plato = new Platos(codp, nomep, total);
                    //System.out.println(plato);
                    oos.writeObject(plato);

                    break;

                default:
                    break;
            }

            br.close();

        }

        oos.writeObject(null);
        oos.close();

        while ((plato = (Platos) ois.readObject()) != null) {
            System.out.println(plato.toString());
        }

        ois.close();
        xsr.close();
        Exa2.closeConexion();
    }

}

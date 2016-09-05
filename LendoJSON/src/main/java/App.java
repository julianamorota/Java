/**
 * Created by juliana.morota on 02/09/2016.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;

public class App {
    public static void main(String[] args) {

        Gson gson = new Gson();

        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\exemplo.json"));

            //Converte String JSON para objeto Java
            Exemplo obj = gson.fromJson(br, Exemplo.class);

            System.out.println("id: " + obj.getId());

            System.out.println("\nfamilia:\n\tnome: " + obj.getFamilia().getNome());

            //byte[] decoded = Base64.decodeBase64(obj.getCodigo());
            //System.out.println("\ncodigo: " + new String(decoded, "UTF-8"));

            System.out.println("\ncodigo: " + obj.getCodigo());

            System.out.println("\npropriedades:\n\tcor: " + obj.getPropriedade().getCor());
            System.out.println("\taltura: " + obj.getPropriedade().getAltura());
            System.out.println("\tpeso: " + obj.getPropriedade().getPeso());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Exemplo {
    String id;
    Familia familia;
    String codigo;
    Propriedade propriedades;

    public String getId() {
        return id;
    }

    public Familia getFamilia() {
        return familia;
    }

    public String getCodigo() throws UnsupportedEncodingException {
        byte[] decoded = Base64.decodeBase64(codigo);
        return new String(decoded, "UTF-8");
    }
    /*
    public String getCodigo()  {
        return codigo;
    }
    */
    public Propriedade getPropriedade() {
        return propriedades;
    }
}

class Familia{
    String nome;

    public String getNome() {
        return nome;
    }
}

class Propriedade{
    String cor;
    int altura;
    int peso;

    public String getCor() {
        return cor;
    }

    public int getAltura() {
        return altura;
    }

    public int getPeso() {
        return peso;
    }
}

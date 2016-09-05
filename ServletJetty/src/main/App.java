/**
 * Created by juliana.morota on 05/09/2016.
 */
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;

public class App extends HttpServlet{

    public static void main(String[] args) throws Exception {
        Server server = new Server(8083);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/hello");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new HelloServlet()), "/*");
        server.start();
    }

    public static class HelloServlet extends HttpServlet  {

        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader("src\\exemplo.json"));
        Exemplo obj = gson.fromJson(br, Exemplo.class);
        public HelloServlet() throws FileNotFoundException {}

        protected void doGet(HttpServletRequest request,
                             HttpServletResponse response) throws ServletException,
                IOException {
            
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("id: " + obj.getId() + "<br>");
            response.getWriter().println("familia: nome: " + obj.getFamilia().getNome()  + "<br>");
            response.getWriter().println("codigo: " + obj.getCodigo() + "<br>");
            response.getWriter().println("propriedades: cor: " + obj.getPropriedade().getCor() + "<br>");
            response.getWriter().println("altura: " + obj.getPropriedade().getAltura() + "<br>");
            response.getWriter().println("peso: " + obj.getPropriedade().getPeso() + "<br>");
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
}
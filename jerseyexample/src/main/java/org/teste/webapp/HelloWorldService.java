package org.teste.webapp;
import javax.json.Json;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;


@Path("/teste")
public class HelloWorldService extends HttpServlet  {
    @GET
    @Produces(("application/json; charset=UTF-8"))
    public Response getMessage()throws ServletException,IOException {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader("src\\exemplo.json"));
        Exemplo obj = gson.fromJson(br, Exemplo.class);

        String json = gson.toJson(obj);
        JSONObject jsonObject = new JSONObject(json);


        String jsonFormatted = "{\"id\":\""+obj.getId()+"\"," +
                "\"familia\":{" +
                "\"nome\":\"" + obj.getFamilia().getNome() +"\"}," +
                "\"codigo\":\"" + obj.getCodigo() + "\", " +
                "\"propriedades\":{" +
                "\"cor\":\"" + obj.getPropriedade().getCor()+ "\", " +
                "\"altura\":" + obj.getPropriedade().getAltura() + ", " +
                "\"peso\":" + obj.getPropriedade().getPeso() + "}} ";

        return Response.status(200).entity(jsonFormatted).build();
        }

    @GET
    @Path("/query")
    @Produces(("application/json; charset=UTF-8"))
    public Response alteraAlturaPeso(
            @QueryParam("altura") String altura,
            @QueryParam("peso") String peso) throws UnsupportedEncodingException, FileNotFoundException {
        try{

        int alturaInt = Integer.parseInt(altura);
        int pesoInt = Integer.parseInt(peso);

    } catch (NumberFormatException e) {
        return Response.status(200).entity("Altura/peso não é um numero!").build();
    }

        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader("src\\exemplo.json"));
        Exemplo obj = gson.fromJson(br, Exemplo.class);

        String json = gson.toJson(obj);
        JSONObject jsonObject = new JSONObject(json);
        String codigo = jsonObject.getString("codigo");


        String jsonFormatted = "{\"id\":\""+obj.getId()+"\"," +
                "\"familia\":{" +
                "\"nome\":\"" + obj.getFamilia().getNome() +"\"}," +
                "\"codigo\":\"" + obj.getCodigo() + "\", " +
                "\"propriedades\":{" +
                "\"cor\":\"" + obj.getPropriedade().getCor()+ "\", " +
                "\"altura\":" + altura + ", " +
                "\"peso\":" + peso + "}} ";

        return Response.status(200).entity(jsonFormatted).build();
    }

    @POST
    @Path("/post")
    @Consumes("application/json")
    @Produces(("application/json; charset=UTF-8"))
    public Response LeJSON(String json) throws UnsupportedEncodingException {

        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(json);


        try{
            String id = (String) jsonObject.get("id");
            JSONObject familia = (JSONObject) jsonObject.get("familia");
            String nome = (String) familia.get("nome");
            String codigo = (String) jsonObject.get("codigo");
            JSONObject propriedades = (JSONObject) jsonObject.get("propriedades");
            String cor = (String) propriedades.get("cor").toString();
            int altura = (Integer.parseInt(propriedades.get("altura").toString()));
            int peso = (Integer.parseInt(propriedades.get("peso").toString()));


            String jsonFormatted = "{\"id\":\""+id+"\"," +
                    "\"familia\":{" +
                    "\"nome\":\"" + nome +"\"}," +
                    "\"codigo\":\"" + codigo + "\", " +
                    "\"propriedades\":{" +
                    "\"cor\":\"" + cor + "\", " +
                    "\"altura\":" + altura + ", " +
                    "\"peso\":" + peso + "}} ";
            return Response.status(201).entity(jsonFormatted).build();
        } catch (NumberFormatException e) {
            return Response.status(200).entity("Dados inválidos").build();
        }catch (ClassCastException e) {
            return Response.status(200).entity("Dados inválidos").build();
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

        //return new String(familia, "UTF-8");
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

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getPeso() {

        return peso;
    }
}

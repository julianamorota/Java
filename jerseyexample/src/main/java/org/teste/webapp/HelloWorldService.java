package org.teste.webapp;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;

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
    public Response getUsers(
            @QueryParam("altura") int altura,
            @QueryParam("peso") int peso) throws UnsupportedEncodingException, FileNotFoundException {

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

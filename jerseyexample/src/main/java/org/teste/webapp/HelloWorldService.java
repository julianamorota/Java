package org.teste.webapp;
import javax.persistence.*;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final Logger LOGGER = Logger.getLogger("JPA");

    public static void main(String[] args) {
        HelloWorldService main = new HelloWorldService();
        main.run();
    }

    public void run() {
        EntityManagerFactory factory = null;
        EntityManager entityManager = null;
        try {
            factory = Persistence.createEntityManagerFactory("PersistenceUnit");
            entityManager = factory.createEntityManager();
            //persistPerson(entityManager, "sayuri", "morota");
            Person person = new Person();
            person.setFirstName("testeJu1");
            person.setLastName("testeJuSobre1");
            //persistPerson(entityManager, person );
            //loadData(entityManager);
            //loadPersons(entityManager);
            loadData(entityManager);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }
    }


    private void persistData(EntityManager entityManager, Exemplo exem) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(exem);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    private void persistPerson(EntityManager entityManager,Person person) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }


    private void loadData(EntityManager entityManager) {
        entityManager.clear();
        TypedQuery<Exemplo> query = entityManager.createQuery("from Exemplo ", Exemplo.class);
        List<Exemplo> resultList = query.getResultList();
        for (Exemplo exemplo : resultList) {
            StringBuilder sb = new StringBuilder();
            sb.append(exemplo.getId()).append(" ");
            LOGGER.info(sb.toString());
        }
        //System.out.println(resultList);
    }
    private void loadPersons(EntityManager entityManager) {
        entityManager.clear();
        TypedQuery<Person> query = entityManager.createQuery("from Person p inner join Familia f on p.", Person.class);
        List<Person> resultList = query.getResultList();
        System.out.println(query.toString());
        for (Person person : resultList) {
            StringBuilder sb = new StringBuilder();
            sb.append(person.getFirstName()).append(" ").append(person.getLastName());
            LOGGER.info(sb.toString());
        }
        //System.out.println(resultList);
    }


    @GET
    @Produces(("application/json; charset=UTF-8"))
    public Response getMessage()throws ServletException,IOException {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader("src\\exemplo.json"));
        Exemplo obj = gson.fromJson(br, Exemplo.class);

        String jsonFormatted = "{\"id\":\""+obj.getId()+"\"," +
                "\"familia\":{" +
                "\"nome\":\"" + obj.getFamilia().getNome() +"\"}," +
                "\"codigo\":\"" + obj.getCodigo() + "\", " +
                "\"propriedades\":{" +
                "\"cor\":\"" + obj.getPropriedades().getCor()+ "\", " +
                "\"altura\":" + obj.getPropriedades().getAltura() + ", " +
                "\"peso\":" + obj.getPropriedades().getPeso() + "}} ";

        return Response.status(200).entity(jsonFormatted).build();
    }

    @GET
    @Path("/query")
    @Produces(("application/json; charset=UTF-8"))
    public Response alteraAlturaPeso(
            @QueryParam("altura") String altura,
            @QueryParam("peso") String peso) throws UnsupportedEncodingException, FileNotFoundException {

        int alturaInt, pesoInt;
        //verificar se dá erro ao ser convertido (se sim, então não é Integer)
        try{
            alturaInt = Integer.parseInt(altura);
            pesoInt = Integer.parseInt(peso);

        }catch (NumberFormatException e) {
            return Response.status(200).entity("Altura/peso não é um numero!").build();
        }

        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader("src\\exemplo.json"));
        Exemplo obj = gson.fromJson(br, Exemplo.class);

        String jsonFormatted = "{\"id\":\""+obj.getId()+"\"," +
                "\"familia\":{" +
                "\"nome\":\"" + obj.getFamilia().getNome() +"\"}," +
                "\"codigo\":\"" + obj.getCodigo() + "\", " +
                "\"propriedades\":{" +
                "\"cor\":\"" + obj.getPropriedades().getCor()+ "\", " +
                "\"altura\":" + alturaInt + ", " +
                "\"peso\":" + pesoInt + "}} ";

        return Response.status(200).entity(jsonFormatted).build();
    }

    @POST
    @Path("/post")
    @Consumes("application/json")
    @Produces("application/json; charset=UTF-8")
    public Response LeJSON(String json) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(json);
        Exemplo exem = gson.fromJson(json, Exemplo.class);

        try{

            String id = (String) jsonObject.get("id");
            JSONObject familia = (JSONObject) jsonObject.get("familia");
            String nome = (String) familia.get("nome");
            String codigo = (String) jsonObject.get("codigo");
            JSONObject propriedades = (JSONObject) jsonObject.get("propriedades");
            String cor = (String) propriedades.get("cor");
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

            EntityManagerFactory factory = null;
            EntityManager entityManager = null;
            try {
                factory = Persistence.createEntityManagerFactory("PersistenceUnit");
                entityManager = factory.createEntityManager();

                Exemplo teste = new Exemplo();
                Familia testefamilia = new Familia();
                Propriedade testepropriedade = new Propriedade();
                testefamilia.setNome(nome);
                testepropriedade.setCor(cor);
                testepropriedade.setAltura(altura);
                testepropriedade.setPeso(peso);
                teste.setId(id);
                teste.setCodigo(codigo);
                teste.setFamilia(testefamilia);
                teste.setPropriedades(testepropriedade);

                persistData(entityManager, teste);
                loadData(entityManager);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (entityManager != null) {
                    entityManager.close();
                }
                if (factory != null) {
                    factory.close();
                }
            }

            return Response.status(201).entity(jsonFormatted).build();

        } catch (NumberFormatException e) {
            return Response.status(200).entity("Dados inválidos").build();
        }catch (ClassCastException e) {
            return Response.status(200).entity("Dados inválidos").build();
        }
    }
}

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class Exemplo {
    @Id
    @GeneratedValue
    private Long idGerado;
    @Column(name = "id")
    private String id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "familia")
    private Familia familia;
    @Column(name = "codigo")
    private String codigo;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "propriedade")
    private Propriedade propriedades;

    public void setId(String id) {this.id = id;}

    public void setFamilia(Familia familia) {this.familia = familia;  }

    public void setCodigo(String codigo) { this.codigo = codigo;  }

    public void setPropriedades(Propriedade propriedades) {this.propriedades = propriedades;  }

    public String getCodigo() throws UnsupportedEncodingException {
        byte[] decoded = Base64.decodeBase64(codigo);
        return new String(decoded, "UTF-8");
    }

    public String getId() { return id; }

    public Familia getFamilia() { return familia; }

    public Propriedade getPropriedades() { return propriedades; }
}
//-----------------------------------------------------
@Entity
class Familia{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGeneratedFam;
    @Column(name = "nome")
    private String nome;

    public void setNome(String nome) { this.nome = nome; }

    public String getNome() {  return nome; }
}
//-----------------------------------------------------
@Entity
class Propriedade{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGeneratedPro;
    @Column(name = "cor")
    String cor;
    @Column(name = "altura")
    int altura;
    @Column(name = "peso")
    int peso;

    public String getCor() { return cor; }

    public void setCor(String cor) { this.cor = cor;  }

    public int getAltura() { return altura;  }

    public void setAltura(int altura) { this.altura = altura; }

    public int getPeso() { return peso; }

    public void setPeso(int peso) { this.peso = peso; }
}

//============================TESTE
@Entity
class Person {
    private Long id;
    private String firstName;
    private String lastName;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

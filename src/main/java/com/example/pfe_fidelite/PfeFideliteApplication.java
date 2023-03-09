package com.example.pfe_fidelite;

import com.example.pfe_fidelite.entities.Fidelite;



import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;


public class PfeFideliteApplication {


    public static void main(String[] args) {


        String Url = "https://mar4-prod-e3e5e8f9.cloud.maxxing.com/MPCM/Web/api/rest/5.6/CustomerInformationServer/getCustomerByCardKey";
        String Username = "Wissal_ELHAMMOURI";
        String Password = "PROD_12345";

        //String param = "3000000724756";
        //String payload = "{ \"storeChain\": \"0\",\"card_key\":"+param+"}";
        // corps de la requête au format JSON


        // Configuration du DataSource
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@192.168.99.154:15212:HPRD");
        dataSource.setUsername("cagnottefid");
        dataSource.setPassword("8qxW*r3iT9JC");

        // Crée un objet JdbcTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // Exécute une requête SQL
        String sql1 = "select b.prenom , b.nomuse , b.matcle, a.codfid from HR.ZYZC a , hr.ZY00 b, hr.ZYES c where a.nudoss = b.nudoss and a.nudoss = c.nudoss and c.datsor > sysdate ";
        //String sql = "select * from HR.ZYZC ";
       /* for (int i=0 ; i<10 ; i++ ) {
            jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("NUDOSS"))
                    .forEach(System.out::println);


        }*/
        List<Fidelite> results = jdbcTemplate.query(sql1, new RowMapper<Fidelite>() {
            public Fidelite mapRow(ResultSet rs, int rowNum) throws SQLException {
                Fidelite obj = new Fidelite();
                obj.setPrenom(rs.getString("prenom"));
                obj.setMatcle(rs.getString("matcle"));
                obj.setNomuse(rs.getString("nomuse"));
                obj.setCodfid(rs.getString("codfid"));
               // obj.setCptfid(rs.getString("cptfid"));


                return obj;
            }
        });
        // Utilisation du tableau dans le corps du main

        Fidelite[] myArray = results.toArray(new Fidelite[results.size()]);
        for (Fidelite obj : myArray) {
            //String params = myArray[0].getCodfid().toString();
            //System.out.println(obj.getPrenom() + ", " + obj.getCodfid() + ", " + obj.getMatcle() +"," + obj.getNomuse() );
            // obj.getCodfid().indexOf(1);
            //System.out.println(params);




        String payload = "{ \"storeChain\": \"0\",\"card_key\":" + obj.getCodfid() + "}";
        try {
            String encodedCredentials = Base64.getEncoder().encodeToString((Username + ":" + Password).getBytes());

            URL apiUrl = new URL(Url);
            HttpURLConnection connexion = (HttpURLConnection) apiUrl.openConnection();
            connexion.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            connexion.setRequestMethod("POST");
            connexion.setDoOutput(true);


            byte[] postData = payload.getBytes(StandardCharsets.UTF_8);
            connexion.setRequestProperty("Content-Type", "application/json");
            connexion.setRequestProperty("Content-Length", String.valueOf(postData.length));
            connexion.getOutputStream().write(postData);

            // Envoyez la requête HTTP et lisez la réponse
            BufferedReader in = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            String inputLine;
           StringBuffer response = new StringBuffer();
            String responseString = response.toString();



            while ((inputLine = in.readLine()) != null) {
               //System.out.println( inputLine);
                JSONObject obj1;
                //System.out.println(responseString);
                obj1 = new JSONObject(inputLine);


                //System.out.println(obj1.get("customer"));

                JSONObject obj2;
                 obj2 = new JSONObject(  obj1.get("customer").toString());


                String compte = obj2.get("account_key").toString();
                String codefid =obj2.get("card_key").toString();
                String CustomerId =obj2.get("cust_id").toString();
                System.out.println(" Customer Id = " +CustomerId+ " Compte =  " +compte + " Code Fid = " + codefid);
               // String updateQuery = "UPDATE HR.ZYZC SET CPTFID='"+compte+ "' where CODFID='"+ codefid+"'";
                //jdbcTemplate.update(updateQuery);





                // Check if the JSONValue is a JSONArray


            }


            // Fermez la connexion


            // Affichez la réponse de l'API
            System.out.println(response.toString());

        } catch (Exception E) {
            E.printStackTrace();

        }
        //System.out.println(params[i]);


    }




    }
}

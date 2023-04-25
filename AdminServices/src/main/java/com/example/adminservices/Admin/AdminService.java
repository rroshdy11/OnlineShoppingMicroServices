package com.example.adminservices.Admin;

import com.example.adminservices.Admin.Admin;


import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Path("/v1/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class AdminService implements Serializable {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();


    @POST
    @Path("/register")
    public String register( Admin admin ){
        try {
            entityManager.getTransaction().begin();
            //if user exists return error
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 != null) {
                return "User already exists";
            }
            entityManager.persist(admin);
            entityManager.getTransaction().commit();
            return "Registered Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while registering";
        }


    }
    @GET
    @Path("/login")
    public String login(Admin admin){
        try {
            entityManager.getTransaction().begin();
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 == null) {
                entityManager.getTransaction().rollback();
                return "User does not exist";
            }
            if (admin1.getPassword().equals(admin.getPassword())) {
                entityManager.getTransaction().commit();
                return "Logged in Successfully ";
            }

            return "Wrong password";

        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while logging in";
        }
    }
    @GET
    @Path("/getAllAdmins")
    public Response getAllAdmins() {
        return Response.ok(entityManager.createQuery("SELECT a FROM Admin a", Admin.class).getResultList()).build();
    }



    @DELETE
    @Path("/deleteAdmin/{username}")
    public String deleteAdmin(@PathParam("username") String username) {
        try {
            entityManager.getTransaction().begin();
            Admin admin = entityManager.find(Admin.class, username);
            if (admin == null) {
                entityManager.getTransaction().rollback();
                return "User does not exist";
            }
            entityManager.remove(admin);
            entityManager.getTransaction().commit();
            return "User Deleted Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while deleting";
        }
    }


    @PUT
    @Path("/updateAdmin")
    public String updateAdmin(Admin admin) {


        try {
            entityManager.getTransaction().begin();
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 == null) {
                entityManager.getTransaction().rollback();
                return "User does not exist";
            }
            entityManager.merge(admin);
            entityManager.getTransaction().commit();
            return "User Updated Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while updating";
        }
    }
    @POST
    @Path("/addSellingCompany")
    public String addSellingCompany(String sellingCompany) {

        //send HTTPrequest to selling company to add selling company
        String url = "http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/sellingCompany/add";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .POST(HttpRequest.BodyPublishers.ofString(sellingCompany))
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @GET
    @Path("/getAllSellingCompanies")
    public String getAllSellingCompanies() {
        //send HTTPrequest to selling company to get all selling companies
        String url = "http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/sellingCompany/getAllSellingCompanies";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .GET()
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @POST
    @Path("/addShippingCompany")
    public String addShippingCompany(String shippingCompany) {
        //send HTTPrequest to shipping company to add shipping company
        String url = "http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/shippingCompany/add";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .POST(HttpRequest.BodyPublishers.ofString(shippingCompany))
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GET
    @Path("/getAllShippingCompanies")
    public String getAllShippingCompanies() {
        //send HTTPrequest to shipping company to get all shipping companies
        String url = "http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/shippingCompany/getAll";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .GET()
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //GET ALL Customers from Customer Service
    @GET
    @Path("/getAllCustomers")
    public String getAllCustomers() {
        //send HTTPrequest to customer service to get all customers
        String url = "http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/customer/getAll";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(java.time.Duration.ofMinutes(1))
                    .GET()
                    .build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
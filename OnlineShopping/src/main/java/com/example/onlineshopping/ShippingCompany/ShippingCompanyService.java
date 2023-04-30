package com.example.onlineshopping.ShippingCompany;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Path("/v1/shippingCompany")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class ShippingCompanyService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();


    @GET
    @Path("/getAll")
    public List<ShippingCompany> getAllShippingCompany() {
        return entityManager.createQuery("SELECT s FROM ShippingCompany s", ShippingCompany.class).getResultList();
    }

    @POST
    @Path("/add")
    public String addShippingCompany( ShippingCompany shippingCompany) {
        try {
            entityManager.getTransaction().begin();
            //if user exists return error
            ShippingCompany shippingCompany1 = entityManager.find(ShippingCompany.class, shippingCompany.getUsername());
            if (shippingCompany1 != null) {
                return "Shipping Company already exists";
            }
            shippingCompany.setPassword(generatePassword());
            entityManager.persist(shippingCompany);
            entityManager.getTransaction().commit();
            return "Shipping Company added successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while registering";
        }


    }
    private String generatePassword(){
        String password="";
        //generate random password of lenght 10 contains letters and numbers
        for (int i = 0; i < 10; i++) {
            int random = (int) (Math.random() * 3);
            switch (random) {
                case 0:
                    //generate random uppercase letter
                    password += (char) (Math.random() * 26 + 65);
                    break;
                case 1:
                    //generate random lowercase letter
                    password += (char) (Math.random() * 26 + 97);
                    break;
                case 2:
                    //generate random number
                    password += (int) (Math.random() * 10);
                    break;
            }
        }
        return password;
    }
    @POST
    @Path("/login")
    public Response login(ShippingCompany ShippingCompany){
        try {
            entityManager.getTransaction().begin();
            ShippingCompany ShippingCompany1 = entityManager.find(ShippingCompany.class, ShippingCompany.getUsername());
            if (ShippingCompany1 == null) {
                entityManager.getTransaction().rollback();
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            if (ShippingCompany1.getPassword().equals(ShippingCompany.getPassword())) {
                entityManager.getTransaction().commit();
                return Response.ok().entity(ShippingCompany1).build();
            }

            return Response.status(Response.Status.UNAUTHORIZED).build();

        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/deleteShippingCompany/{name}")
    public String deleteShippingCompany(@PathParam("name") String name) {
        try {
            entityManager.getTransaction().begin();
            ShippingCompany ShippingCompany = entityManager.find(ShippingCompany.class, name);
            if (ShippingCompany == null) {
                entityManager.getTransaction().rollback();
                return "Shipping Company does not exist";
            }
            entityManager.remove(ShippingCompany);
            entityManager.getTransaction().commit();
            return "Shipping Company Deleted Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while deleting";
        }
    }

    @GET
    @Path("/getShippingCompanyForGeoLocation/{location}")
    public List<ShippingCompany> getShippingCompanyForGeoLocation(@PathParam("location") String location) {
        return entityManager.createQuery("SELECT s FROM ShippingCompany s WHERE s.geography = :location", ShippingCompany.class).setParameter("location", location).getResultList();
    }
    @GET
    @Path("/getAllShippingRequestForShippingCompany/{name}")
    public String getAllShippingRequestForShippingCompany(@PathParam("name") String name) {
        //find the shipping company
        ShippingCompany shippingCompany = entityManager.find(ShippingCompany.class, name);
        if (shippingCompany == null) {
            return "Shipping Company does not exist";
        }
        //make Http request to get all shipping requests for this shipping company /getShippingRequests/{Geo}
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/order/getShippingRequests/"+shippingCompany.getGeography()))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        //send the request
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while getting shipping requests";
        }
    }
}

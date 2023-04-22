package com.example.shippingservice;


import ShippingCompany.shippingCompany;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/v1/shippingcompany")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class ShippingCompanyService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();

    @GET
    public String hello() {
        return "Hello, Worlddd!";
    }


    @GET
    @Path("/getAllShippingCompanies")
    public List<shippingCompany> getAllShippingCompany() {
        return entityManager.createQuery("SELECT s FROM shippingCompany s", shippingCompany.class).getResultList();
    }

    @POST
    @Path("/register")
    public String register( shippingCompany ShippingCompany) {
        try {
            entityManager.getTransaction().begin();
            //if user exists return error
            shippingCompany ShippingCompany1 = entityManager.find(shippingCompany.class, ShippingCompany.getName());
            if (ShippingCompany1 != null) {
                return "Shipping Company already exists";
            }
            entityManager.persist(ShippingCompany);
            entityManager.getTransaction().commit();
            return "Registered Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while registering";
        }


    }
    @GET
    @Path("/login")
    public String login(shippingCompany ShippingCompany){
        try {
            entityManager.getTransaction().begin();
            shippingCompany ShippingCompany1 = entityManager.find(shippingCompany.class, ShippingCompany.getName());
            if (ShippingCompany1 == null) {
                entityManager.getTransaction().rollback();
                return "Shipping Company does not exist";
            }
            if (ShippingCompany1.getPassword().equals(ShippingCompany.getPassword())) {
                entityManager.getTransaction().commit();
                return "Logged in Successfully ";
            }

            return "Wrong password";

        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while logging in";
        }
    }

    @DELETE
    @Path("/deleteShippingCompany/{name}")
    public String deleteShippingCompany(@PathParam("name") String name) {
        try {
            entityManager.getTransaction().begin();
            shippingCompany ShippingCompany = entityManager.find(shippingCompany.class, name);
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
    @Path("/checkGeo/{name}/{reqGeo}")
    public String checkGeo(@PathParam("name") String name, @PathParam("reqGeo") String reqGeo) {
        try {
            entityManager.getTransaction().begin();
            shippingCompany ShippingCompany = entityManager.find(shippingCompany.class, name);
            if (ShippingCompany == null) {
                entityManager.getTransaction().rollback();
                return "Shipping Company does not exist";
            }
            String[] Geo= ShippingCompany.getGeography().split(",");
            for (int i = 0 ; i < Geo.length ; i++) {
                if (Geo[i].equals(reqGeo)) {
                    entityManager.getTransaction().commit();
                    return "Will be Shipped";
                }
            }

            return "out of range";

        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while shipping process";
        }
    }
}

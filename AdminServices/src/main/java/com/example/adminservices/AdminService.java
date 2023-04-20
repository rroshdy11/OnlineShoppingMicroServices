package com.example.adminservices;

import Admin.Admin;


import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import jakarta.transaction.*;

import java.util.List;


@Path("/v1/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class AdminService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();

    @GET
    public String hello() {
        return "Hello, Worlddd!";
    }


    @POST
    @Path("/register")
    public String register( Admin admin) {
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
    public List<Admin> getAllAdmins() {
        return entityManager.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
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

}
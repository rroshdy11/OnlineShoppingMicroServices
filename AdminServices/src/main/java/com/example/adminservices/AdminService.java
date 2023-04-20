package com.example.adminservices;

import Admin.Admin;


import jakarta.annotation.Resource;
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
public class AdminService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();

    @Resource
    private UserTransaction userTransaction;
    @GET
    public String hello() {
        return "Hello, Worlddd!";
    }


    @POST
    @Path("/register")
    public String register( Admin admin) {
        try {
            userTransaction.begin();
            //if user exists return error
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 != null) {
                return "User already exists";
            }
            entityManager.persist(admin);
            userTransaction.commit();
            return "Registered Successfully";
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException e) {
            e.printStackTrace();
            return "Error while registering";
        } catch (NotSupportedException e) {
            throw new RuntimeException(e);
        }


    }
    @GET
    @Path("/login")
    public String login(Admin admin){
        try {
            userTransaction.begin();
            Admin admin1 = entityManager.find(Admin.class, admin.getUsername());
            if (admin1 == null) {
                userTransaction.rollback();
                return "User does not exist";
            }
            if (admin1.getPassword().equals(admin.getPassword())) {
                userTransaction.commit();
                return "Logged in Successfully ";
            }

            return "Wrong password";

        } catch (HeuristicRollbackException | SecurityException | IllegalStateException | SystemException e) {
            e.printStackTrace();
            return "Error while logging in";
        } catch (NotSupportedException e) {
            throw new RuntimeException(e);
        } catch (HeuristicMixedException e) {
            throw new RuntimeException(e);
        } catch (RollbackException e) {
            throw new RuntimeException(e);
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
            userTransaction.begin();
            Admin admin = entityManager.find(Admin.class, username);
            if (admin == null) {
                userTransaction.rollback();
                return "User does not exist";
            }
            entityManager.remove(admin);
            userTransaction.commit();
            return "User Deleted Successfully";
        } catch (HeuristicRollbackException | SecurityException | IllegalStateException | SystemException e) {
            e.printStackTrace();
            return "Error while deleting";
        } catch (NotSupportedException e) {
            throw new RuntimeException(e);
        } catch (HeuristicMixedException e) {
            throw new RuntimeException(e);
        } catch (RollbackException e) {
            throw new RuntimeException(e);
        }
    }

}
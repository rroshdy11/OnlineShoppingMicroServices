package com.example.onlineshopping.Customer;

import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.io.Serializable;
import java.util.List;

@Path("/v1/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SessionScoped
public class CustomerService implements Serializable {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();

    @POST
    @Path("/register")
    public String register(Customer customer){
        try {
            //CHECK IF CUSTOMER ALREADY EXISTS
            entityManager.getTransaction().begin();
            Customer customer1 = entityManager.find(Customer.class,customer.getUsername());
            if (customer1 != null) {
                entityManager.getTransaction().rollback();
                return "Customer already exists";
            }
            entityManager.persist(customer);
            entityManager.getTransaction().commit();
            return "Customer registered successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while registering customer";
        }
    }
    @GET
    @Path("/login")
    public String login(@Context HttpServletRequest request , Customer customer){
        try {
            entityManager.getTransaction().begin();
            Customer customer1 = entityManager.find(Customer.class,customer.getUsername());
            if (customer1 == null) {
                entityManager.getTransaction().rollback();
                return "Customer does not exist";
            }
            if (customer1.getPassword().equals(customer.getPassword())) {
                request.getSession(true).setAttribute("userName",customer1.getUsername());
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
    @Path("/getAllCustomers")
    public List<Customer> getAllCustomers(){
        try {
            entityManager.getTransaction().begin();
            List<Customer> customers = entityManager.createQuery("SELECT c FROM Customer c").getResultList();
            entityManager.getTransaction().commit();
            return customers;
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }



}

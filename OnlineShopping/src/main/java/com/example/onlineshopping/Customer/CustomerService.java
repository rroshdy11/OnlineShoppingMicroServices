package com.example.onlineshopping.Customer;

import com.example.onlineshopping.Product.Product;
import jakarta.ejb.EJB;
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
import java.util.ArrayList;
import java.util.List;

@Path("/v1/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SessionScoped
@Stateful
public class CustomerService implements Serializable {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();
    @EJB
    private Customer customer;

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
            customer1.setCart(new ArrayList<Product>());
            if (customer1 == null) {
                entityManager.getTransaction().rollback();
                return "Customer does not exist";
            }
            if (customer1.getPassword().equals(customer.getPassword())) {
                request.getSession(true).setAttribute("userName",customer1.getUsername());
                this.customer=customer1;
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
    @Path("/logout")
    public String logout(@Context HttpServletRequest request){
        request.getSession().invalidate();
        return "Logged out successfully";
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
    @GET
    @Path("/getCustomer")
    public Customer getCustomer(@Context HttpServletRequest request){
        if(request.getSession(false).getAttribute("userName")!=null) {
            return customer;
        }
        return null;
    }
    @POST
    @Path("/addProductToCart/{productID}")
    public String addProductToCart(@Context HttpServletRequest request,@PathParam("productID") int productID){
        if(request.getSession(false).getAttribute("userName")==null){
            return "You are not logged in";
        }
        try {
               Product product = entityManager.find(Product.class,productID);
                if(product==null){
                     entityManager.getTransaction().rollback();
                     return "Product does not exist";
                }
                customer.getCart().add(product);
            return "Product added to cart successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while adding product to cart";
        }
    }
    @DELETE
    @Path("/removeProductFromCart/{productID}")
    public String removeProductFromCart(@Context HttpServletRequest request,@PathParam("productID") int productID){
        if(request.getSession(false).getAttribute("userName")==null){
            return "You are not logged in";
        }
        try {
            Product product = entityManager.find(Product.class,productID);
            if(product==null){
                entityManager.getTransaction().rollback();
                return "Product does not exist";
            }
            customer.getCart().remove(product);
            return "Product removed from cart successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while removing product from cart";
        }
    }


}

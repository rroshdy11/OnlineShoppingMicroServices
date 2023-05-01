package com.example.onlineshopping.Customer;

import com.example.onlineshopping.Notifications.CustomerNotification;
import com.example.onlineshopping.Product.Product;
import jakarta.ejb.EJB;
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
    @EJB
    private CustomerBean customerBean;

    @POST
    @Path("/register")
    public String register(Customer customer){
       return customerBean.create(customer);
    }
    @GET
    @Path("/login")
    public String login(@Context HttpServletRequest request , Customer customer){
        return customerBean.validate(request,customer);
    }
    @DELETE
    @Path("/logout")
    public String logout(@Context HttpServletRequest request){
        return customerBean.destroy(request);
    }
    @GET
    @Path("/getAll")
    public List<Customer> getAllCustomers(){
        return (List<Customer>) entityManager.createQuery("SELECT c FROM Customer c").getResultList();
    }
    @GET
    @Path("/getCustomer")
    public Customer getCustomer(@Context HttpServletRequest request){
        return this.customerBean.getLoggedIn(request);
    }
    @POST
    @Path("/addProductToCart/{productID}")
    public String addProductToCart(@Context HttpServletRequest request,@PathParam("productID") int productID){
       return customerBean.addToCart(request,productID);
    }
    @DELETE
    @Path("/removeProductFromCart/{productID}")
    public String removeProductFromCart(@Context HttpServletRequest request,@PathParam("productID") int productID){
        return customerBean.removeFromCart(request,productID);
    }
    @GET
    @Path("/getCart")
    public List<Product> getCart(@Context HttpServletRequest request){
        return customerBean.getCart(request);
    }
    @PUT
    @Path("/updateCustomer")
    public String updateCustomer(@Context HttpServletRequest request,Customer customer){
        return customerBean.update(request,customer);
    }
    @GET
    @Path("/BuyProducts")
    public String BuyProducts(@Context HttpServletRequest request){
        return customerBean.checkout(request);
    }



}

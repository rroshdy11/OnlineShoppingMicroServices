package com.example.onlineshopping.Customer;

import com.example.onlineshopping.Product.Product;
import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.SessionScoped;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
            if (customer1 == null) {
                entityManager.getTransaction().rollback();
                return "Customer does not exist";
            }
            if (customer1.getPassword().equals(customer.getPassword())) {
                customer1.setCart(new ArrayList<Product>());
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
    @Path("/getAll")
    public List<Customer> getAllCustomers(){
        return (List<Customer>) entityManager.createQuery("SELECT c FROM Customer c").getResultList();
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
            if(product==null||product.getProductStock()==0){
                entityManager.getTransaction().rollback();
                return "Product does not exist or is out of stock";
            }
            //update the product quantity
            entityManager.getTransaction().begin();
            product.setProductStock(product.getProductStock()-1);
            entityManager.getTransaction().commit();
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
        if(request.getSession().getAttribute("userName")==null){
            return "You are not logged in";
        }
        try {
            Product product = entityManager.find(Product.class,productID);
            if(product==null || !customer.getCart().contains(product)){
                entityManager.getTransaction().rollback();
                return "Product does not exist or is not in cart";
            }
            //update the product quantity
            entityManager.getTransaction().begin();
            product.setProductStock(product.getProductStock()+1);
            entityManager.getTransaction().commit();
            customer.getCart().remove(product);
            return "Product removed from cart successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while removing product from cart";
        }
    }
    @GET
    @Path("/getCart")
    public List<Product> getCart(@Context HttpServletRequest request){
        if(request.getSession().getAttribute("userName")==null){
            return null;
        }
        return customer.getCart();
    }
    @PUT
    @Path("/updateCustomer")
    public String updateCustomer(@Context HttpServletRequest request,Customer customer){
        if(request.getSession(false).getAttribute("userName")==null){
            return "You are not logged in";
        }
        try {
            entityManager.getTransaction().begin();
            Customer customer1 = entityManager.find(Customer.class,customer.getUsername());
            if(customer1==null){
                entityManager.getTransaction().rollback();
                return "Customer does not exist";
            }
            customer1.setAddress(customer.getAddress());
            customer1.setBalance(customer.getBalance());
            customer1.setEmail(customer.getEmail());
            customer1.setFirstName(customer.getFirstName());
            customer1.setSecondName(customer.getSecondName());
            customer1.setPhone(customer.getPhone());
            this.customer.setEmail(customer.getEmail());
            this.customer.setAddress(customer.getAddress());
            this.customer.setBalance(customer.getBalance());
            this.customer.setFirstName(customer.getFirstName());
            this.customer.setSecondName(customer.getSecondName());
            this.customer.setPhone(customer.getPhone());

            entityManager.merge(customer1);
            entityManager.getTransaction().commit();
            return "Customer updated successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while updating customer";
        }
    }
    @GET
    @Path("/BuyProducts")
    public String BuyProducts(@Context HttpServletRequest request){
        if(request.getSession(false).getAttribute("userName")==null){
            return "You are not logged in";
        }
        try {
            entityManager.getTransaction().begin();
            Customer customer1 = entityManager.find(Customer.class,customer.getUsername());
            if(customer1==null){
                entityManager.getTransaction().rollback();
                return "Customer does not exist";
            }
            //check if the customer has enough balance
            if(customer1.getBalance()<customer.getCart().stream().mapToDouble(Product::getProductPrice).sum()){
                entityManager.getTransaction().rollback();
                return "Not enough balance";
            }
            //update the balance and the cart
            customer1.setBalance((float) (customer1.getBalance()-customer.getCart().stream().mapToDouble(Product::getProductPrice).sum()));
            for (Product product : customer.getCart()) {
                //add the product price to the selling company balance /addBalance/{companyname}/{balance}
                HttpRequest request1 = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/sellingCompany/addBalance/"+product.getSellingCompany().getName()+"/"+product.getProductPrice()))
                        .POST(HttpRequest.BodyPublishers.ofString(""))
                        .header("Content-Type", "application/json")
                        .build();
                //add an order for each product
                HttpRequest request2 = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/order/add"))
                        .POST(HttpRequest.BodyPublishers.ofString("{" +
                                "\"customerName\":\"" + customer1.getUsername() + "\"," +
                                "\"productId\":\"" + product.getProductId() + "\"," +
                                "\"sellingCompanyName\":\"" + product.getSellingCompany().getName() + "\"," +
                                "\"shippingAddress\":\"" + customer1.getAddress() + "\"," +
                                "\"shippingState\":\"" + "Shipping Request" + "\"" + "}"

                        ))
                        .header("Content-Type", "application/json")
                        .build();
                HttpResponse<String> response = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
                HttpResponse<String> response1 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
                if(response.statusCode()!=200||response1.statusCode()!=200){
                    entityManager.getTransaction().rollback();
                    return "Error while buying products";
                }
            }
            this.customer.setCart(new ArrayList<>());
            this.customer.setBalance(customer1.getBalance());

            entityManager.merge(customer1);
            entityManager.getTransaction().commit();
            return "Products bought successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while buying products";
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @GET
    @Path("/getNotifications/{customerName}")
    public List<CustomerNotification> getNotifications(@PathParam("customerName") String customerName){
        //get the Customer from the database
        Customer customer1 = entityManager.find(Customer.class,customerName);
        if(customer==null){
            return null;
        }
        //get the notifications from the database
        return new ArrayList<>(customer1.getCustomerNotifications());
    }
}

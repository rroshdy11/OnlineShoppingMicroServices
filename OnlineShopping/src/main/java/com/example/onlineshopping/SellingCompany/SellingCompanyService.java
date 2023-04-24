
package com.example.onlineshopping.SellingCompany;

import com.example.onlineshopping.Product.Product;
import jakarta.ejb.Stateless;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/v1/sellingCompany")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class SellingCompanyService {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();

    @POST
    @Path("/add")
    public String addSellingCompany(SellingCompany sellingCompany) {
        try {
            entityManager.getTransaction().begin();
            String sellingCompanyPassword=generatePassword();
            sellingCompany.setPassword(sellingCompanyPassword);
            sellingCompany.setBalance(0);
            //if user exists return error
            SellingCompany sellingCompany1 = entityManager.find(SellingCompany.class, sellingCompany.getName());
            if (sellingCompany1 != null) {
                return "Selling Company already exists";
            }
            sellingCompany.setProducts(new ArrayList<Product>());
            entityManager.persist(sellingCompany);
            entityManager.getTransaction().commit();
            return "Selling Company added successfully" ;
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while adding Selling Company";
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


    @GET
    @Path("/getAllSellingCompanies")
    public List<SellingCompany> getAllSellingCompanies() {
        return entityManager.createQuery("SELECT s FROM SellingCompany s", SellingCompany.class).getResultList();
    }
    @DELETE
    @Path("/delete/{companyname}")
    public String deleteSellingCompany(@PathParam("companyname") String name) {
        try {
            entityManager.getTransaction().begin();
            SellingCompany sellingCompany = entityManager.find(SellingCompany.class, name);
            if (sellingCompany == null) {
                entityManager.getTransaction().rollback();
                return "Selling Company does not exist";
            }
            entityManager.remove(sellingCompany);
            entityManager.getTransaction().commit();
            return "Selling Company Deleted Successfully";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while deleting";
        }
    }
    @GET
    @Path("/login")
    public String login(SellingCompany sellingCompany) {
        try {
            entityManager.getTransaction().begin();
            SellingCompany sellingCompany1 = entityManager.find(SellingCompany.class, sellingCompany.getName());
            if (sellingCompany1 == null) {
                return "Selling Company does not exist";
            }
            if(sellingCompany1.getPassword().equals(sellingCompany.getPassword())){
                return "Login Successfully";
            }
            return "Wrong Password";
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while logging in";
        }

    }
    @POST
    @Path("/addProduct/{companyname}")
    //add product to selling company
    public String addProduct(@PathParam("companyname") String companyName ,Product product) {
        try {
            entityManager.getTransaction().begin();
            SellingCompany sellingCompany = entityManager.find(SellingCompany.class, companyName);
            if (sellingCompany == null) {
                return "Selling Company does not exist";
            }
            product.setSellingCompany(sellingCompany);
            sellingCompany.getProducts().add(product);
            sellingCompany.setProducts(sellingCompany.getProducts());
            entityManager.merge(sellingCompany);
            entityManager.persist(product);
            entityManager.getTransaction().commit();
            return "Product added successfully" ;
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while adding product";
        }

    }
    @GET
    @Path("/getProducts/{companyname}")
    public List<Product> getProducts(@PathParam("companyname") String companyName) {
        try {
            entityManager.getTransaction().begin();
            SellingCompany sellingCompany = entityManager.find(SellingCompany.class, companyName);
            if (sellingCompany == null) {
                return null;
            }
            return sellingCompany.getProducts();
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return null;
        }

    }
    @POST
    @Path("/addBalance/{companyname}/{balance}")
    public String addBalance(@PathParam("companyname") String companyName ,@PathParam("balance") float balance) {
        try {
            entityManager.getTransaction().begin();
            SellingCompany sellingCompany = entityManager.find(SellingCompany.class, companyName);
            if (sellingCompany == null) {
                return "Selling Company does not exist";
            }
            sellingCompany.setBalance(sellingCompany.getBalance()+balance);
            entityManager.merge(sellingCompany);
            entityManager.getTransaction().commit();
            return "Balance added successfully" ;
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
            return "Error while adding balance";
        }

    }
}

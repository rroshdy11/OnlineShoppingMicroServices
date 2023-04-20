package com.example.sellingcompany;

import SellingCompany.SellingCompany;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/v1/sellingCompany")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class SellingCompanyResource {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
    private EntityManager entityManager = emf.createEntityManager();

    @POST
    @Path("/add/{sellingCompanyName}")
    public String addSellingCompany(@PathParam("sellingCompanyName") String sellingCompanyName) {
        try {
            entityManager.getTransaction().begin();
            String sellingCompanyPassword=generatePassword();
            SellingCompany sellingCompany = new SellingCompany(sellingCompanyName,sellingCompanyPassword);
            //if user exists return error
            SellingCompany sellingCompany1 = entityManager.find(SellingCompany.class, sellingCompany.getName());
            if (sellingCompany1 != null) {
                return "Selling Company already exists";
            }
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
    @Path("/login")
    public String login(SellingCompany sellingCompany){
        try {
            entityManager.getTransaction().begin();
            SellingCompany sellingCompany1 = entityManager.find(SellingCompany.class,sellingCompany.getName());
            if (sellingCompany1 == null) {
                entityManager.getTransaction().rollback();
                return "Selling Company does not exist";
            }
            if (sellingCompany1.getPassword().equals(sellingCompany.getPassword())) {
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
}
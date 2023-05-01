
package com.example.onlineshopping.SellingCompany;

import com.example.onlineshopping.Product.Product;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/v1/sellingCompany")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class SellingCompanyService {
    @EJB
    private SellingCompanyBean sellingCompanyBean;

    @POST
    @Path("/add")
    public String addSellingCompany(SellingCompany sellingCompany) {
        return sellingCompanyBean.create(sellingCompany);
    }



    @GET
    @Path("/getAllSellingCompanies")
    public List<SellingCompany> getAllSellingCompanies() {
        return sellingCompanyBean.getAllSelling();
    }
    @DELETE
    @Path("/delete/{companyname}")
    public String deleteSellingCompany(@PathParam("companyname") String name) {
        return sellingCompanyBean.deleteSelling(name);
    }
    @POST
    @Path("/login")
    public Response login(SellingCompany sellingCompany) {
        return sellingCompanyBean.validate(sellingCompany);
    }
    @POST
    @Path("/addProduct/{companyname}")
    //add product to selling company
    public String addProduct(@PathParam("companyname") String companyName ,Product product) {
        return sellingCompanyBean.addProduct(companyName,product);
    }
    @GET
    @Path("/getProducts/{companyname}")
    public List<Product> getProducts(@PathParam("companyname") String companyName) {
        return sellingCompanyBean.getProducts(companyName);
    }
    @POST
    @Path("/addBalance/{companyname}/{balance}")
    public String addBalance(@PathParam("companyname") String companyName ,@PathParam("balance") float balance) {
        return sellingCompanyBean.addBalance(companyName,balance);

    }
    @GET
    @Path("/getBalance/{companyname}")
    public float getBalance(@PathParam("companyname") String companyName) {
        return sellingCompanyBean.getBalance(companyName);
    }
    @GET
    @Path("/getCompany/{companyname}")
    public SellingCompany getCompany(@PathParam("companyname") String companyName) {
        return sellingCompanyBean.getSellingCompany(companyName);
    }
    @DELETE
    @Path("/deleteProduct/{companyname}/{productname}")
    public String deleteProduct(@PathParam("companyname") String companyName,@PathParam("productname") String productName) {
        return sellingCompanyBean.deleteProduct(companyName,productName);
    }
}

using ShippingCompanyServices.Models;

namespace ShippingCompanyServices.Repository
{
    public interface ShippingCompanyInterface
    {
        public Task<List<ShippingCompany>> GetAll();
        public string AddShippingCompany(ShippingCompany shippingCompany);
        public string UpdateShippingCompany(ShippingCompany shippingCompany);
        public string DeleteShippingCompany(int id);
        public string checkGeo(int id, string reqGeo);

    }
}

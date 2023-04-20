using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using ShippingCompanyServices.Models;
using ShippingCompanyServices.Repository;

namespace ShippingCompanyServices.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ShippingCompanyController : ControllerBase
    {
        ShippingCompanyInterface _ShippingCompanyInterface;
        public ShippingCompanyController(ShippingCompanyInterface shippingCompanyInterface)
        {

            _ShippingCompanyInterface = shippingCompanyInterface;
        }
        [HttpGet]
        public Task<List<ShippingCompany>> GetAll()
        {
            return _ShippingCompanyInterface.GetAll();
        }
        [HttpPost]
        public string Add(ShippingCompany shippingCompany)
        {
            return _ShippingCompanyInterface.AddShippingCompany(shippingCompany);
        }

        [HttpPut]
        public string Update(ShippingCompany shippingCompany)
        {
            return _ShippingCompanyInterface.UpdateShippingCompany(shippingCompany);
        }

        [HttpDelete]
        public string Delete(int id)
        {
            return _ShippingCompanyInterface.DeleteShippingCompany(id);
        }
        [HttpGet]
        [Route("checkGeo")]
        public string checkGeo(int id, string reqGeo)
        {
            return _ShippingCompanyInterface.checkGeo(id,reqGeo);
        }
        [HttpPost]
        [Route("login")]
        public string login(ShippingCompany shippingCompany)
        {
            return _ShippingCompanyInterface.login(shippingCompany);
        }
    }
}

using Microsoft.EntityFrameworkCore;
using ShippingCompanyServices.Models;

namespace ShippingCompanyServices.Repository
{
    public class ShippingCompanyRepository:ShippingCompanyInterface
    {
        Context _db;
        public ShippingCompanyRepository(Context db)
        {
            _db = db;
        }
        public async Task<List<ShippingCompany>> GetAll()
        {
            return await _db.ShippingCompany.ToListAsync();
        }
        public string AddShippingCompany(ShippingCompany shippingCompany)
        {
            ShippingCompany ship = new ShippingCompany();
            ship.Name = shippingCompany.Name;
            ship.Geography = shippingCompany.Geography;
            try
            {
                _db.ShippingCompany.Add(ship);
                _db.SaveChanges();
                return "Done " + ship.Name;
            }
            catch (Exception ex)
            {
                return ex.Message;
            }
        }
        public string UpdateShippingCompany(ShippingCompany shippingCompany)
        {
            var ship = _db.ShippingCompany.Where(p => p.ID == shippingCompany.ID).FirstOrDefault();
            if (ship != null)
            {
                ship.Name = shippingCompany.Name;
                ship.Geography = shippingCompany.Geography;
                try
                {
                    _db.SaveChanges();
                    return "Updated";
                }
                catch (Exception ex)
                {
                    return ex.Message;
                }
            }
            return "No Such User";
        }
        public string DeleteShippingCompany(int id)
        {
            var ship = _db.ShippingCompany.Where(p => p.ID == id).FirstOrDefault();
            if (ship != null)
            {
                try
                {
                    _db.ShippingCompany.Remove(ship);
                    _db.SaveChanges();
                    return "Done";
                }
                catch (Exception ex)
                {
                    return ex.Message;
                }
            }
            return "No Such ID";
        }
        public string checkGeo(int id, string reqGeo)
        {
            var ship = _db.ShippingCompany.Where(p => p.ID == id).FirstOrDefault();
            if(ship != null)
            {
                string geo=ship.Geography.ToString();
                string[] geoList = geo.Split(",");
                foreach (string geography in geoList)
                {
                    if (reqGeo.Equals(geography))
                    {
                        return "Okay";
                    }
                }
                return "Not in Shipping Company Geography";
            }
            return "No Such Shipping Company";
        }
    }
}

using System.ComponentModel.DataAnnotations;

namespace ShippingCompanyServices.Models
{
    public class ShippingCompany
    {
        [Required]
        public int ID { get; set; }
        public string Name { get; set; }
        public string Password { get; set; }
        public string Geography { get; set; }
    }
}

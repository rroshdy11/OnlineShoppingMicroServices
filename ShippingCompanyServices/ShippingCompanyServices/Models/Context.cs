using Microsoft.EntityFrameworkCore;

namespace ShippingCompanyServices.Models
{
    public class Context:DbContext
    {
        public Context() { }
        public Context(DbContextOptions<Context> options) : base(options)
        {
        }
        public DbSet<ShippingCompany> ShippingCompany { get; set; }
    }
}

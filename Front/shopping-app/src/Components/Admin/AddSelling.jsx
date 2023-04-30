import React, { useState } from "react";
export const AddSelling=()=>{
    var [company, setCompany] = useState({
        name:"",
        email:"",
        phone:""

    });
    const handleSubmit =async  () => {
        const res = await fetch(
            "http://localhost:8080/AdminServices-1.0-SNAPSHOT/api/v1/admin/addSellingCompany",
            {
              method: "POST",
              body: JSON.stringify(company),
              headers: {
                "Content-Type": "application/json"
              }
            }
          );
          const data = await res.text();
          if (data === "Selling Company added successfully") {
            window.location.href = "/AllSelling";
          } else {
            alert(data+",try another Company Name");
          }
    };

    return (
        <div className="Auth-form-container">
        <div className="Auth-form"  >
          <div className="Auth-form-content">
            <h3 className="Auth-form-title">Add New Selling Company</h3>
            <div className="form-group mt-3">
              <label>Company Name</label>
              <input
                type="text"
                value={company.name}
              onChange={(e) => setCompany({...company,name:e.target.value})}
                className="form-control mt-1"
                placeholder="Enter Company Name"
              />
            </div>
            <div className="form-group mt-3">
              <label>Company Email</label>
              <input
                value={company.companyEmail}
                  onChange={(e) => setCompany({...company,email:e.target.value})}
                type="email"
                className="form-control mt-1"
                placeholder="Enter Company Email"
              />
            </div>
            <div className="form-group mt-3">
              <label>Phone</label>
                <input
                value={company.phone}
                  onChange={(e) => setCompany({...company,phone:e.target.value})}
                type="text"
                className="form-control mt-1"
                placeholder="Enter Company Phone"
              />
              </div>
            <div className="d-grid gap-2 mt-3">
              <button type="submit" className="btn btn-primary" onClick={handleSubmit}>
                Submit
              </button>
            </div>
          </div>
        </div>
      </div>
    );
}
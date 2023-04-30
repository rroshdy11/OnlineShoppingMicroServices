import { useState } from "react";
import "./SignIn.css";
import { Navigate } from "react-router-dom";
export const SignIn = () => {
  var [username, setUsername] = useState("");
  var [pass, setPass] = useState("");
  var [role, setRole] = useState("user");

async function handleSubmit () {
    const user={
        username:username,
        password:pass
    }
    if (role==="admin") {
        // Show successful login alert
        await fetch("http://localhost:8080/AdminServices-1.0-SNAPSHOT/api/v1/admin/login",{
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body:JSON.stringify(user)
        }).then((response)=>{
            response.json().then((result)=>{
                if(response.status===200){
                    localStorage.setItem("userName",result.username)
                    localStorage.setItem("password",result.password)
                    localStorage.setItem("fristName",result.firstName)
                    localStorage.setItem("secondName",result.secondName)
                    localStorage.setItem("phone",result.phone)
                    localStorage.setItem("email",result.email)
                    localStorage.setItem("role",role)
                    alert("Login successful")
                    window.location.href="/AdminHome"
                }
                else{
                    alert("Invalid username or password")
                }
            })
        }).catch((err)=>{
            console.log(err)
        }

        )
    }
    else if(role==="delivery"){
       // Show successful login alert
       await fetch("http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/shippingCompany/login",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify(user)
    }).then((response)=>{
        response.json().then((result)=>{
            if(response.status===200){
                localStorage.setItem("userName",result.username)
                localStorage.setItem("password",result.password)
                localStorage.setItem("geography",result.geography)
                localStorage.setItem("phone",result.phone)
                localStorage.setItem("email",result.email)
                localStorage.setItem("role",role)
                alert("Login successful")
                window.location.href="/ShippingHome"
            }
            else{
                alert("Invalid username or password")
            }
        })
    }).catch((err)=>{
        console.log(err)
    }

    )

    }

}
  return (
    <div className="Auth-form-container">
      <div className="Auth-form"  >
        <div className="Auth-form-content">
          <h3 className="Auth-form-title">Sign In</h3>
          <div className="form-group mt-3">
            <label>Email address</label>
            <input
              type="text"
              value={username}
            onChange={(e) => setUsername(e.target.value)}
              className="form-control mt-1"
              placeholder="Enter email"
            />
          </div>
          <div className="form-group mt-3">
            <label>Password</label>
            <input
              value={pass}
                onChange={(e) => setPass(e.target.value)}
              type="password"
              className="form-control mt-1"
              placeholder="Enter password"
            />
          </div>
          <div className="form-group mt-3">
            <label>Role</label>
          <select className="form-select mt-3" aria-label="Default select example" onChange={(e) => setRole(e.target.value)}>
                <option value="user">User</option>
                <option value="admin">Admin</option>
                <option value="seller">Seller</option>
                <option value="delivery">Delivery</option>
            </select>
            </div>
          <div className="d-grid gap-2 mt-3">
            <button type="submit" className="btn btn-primary" onClick={handleSubmit}>
              Submit
            </button>
          </div>
           
        
          <p className="forgot-password text-right mt-2">
            Forgot <a href="#">password?</a>
          </p>
        </div>
      </div>
    </div>
  );
};


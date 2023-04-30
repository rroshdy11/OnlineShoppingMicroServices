import Table from 'react-bootstrap/Table';
import { useState,useEffect } from "react";
function AllSelling() {
    if(localStorage.getItem("role")!=="admin"){
        window.location.href="/SignIn"
    }
    const [data, setData] = useState([]);
    
    useEffect(() => {
        fetch("http://localhost:8080/AdminServices-1.0-SNAPSHOT/api/v1/admin/getAllSellingCompanies",{
            method:"GET",
            headers:{
                "Content-Type":"application/json"
            }
        }).then((response)=>{
            response.json().then((result)=>{
                setData(result)
            })
        }).catch((err)=>{
            console.log(err)
        }
        )
    }, [])
    console.log(data)
    function makeTable(){
        return data.map((item,index)=>{
            return(
                <tr key={index} >
                    <td>{index}</td>
                    <td>{item.name}</td>
                    <td>{item.email}</td>
                    <td>{item.phone}</td>
                    <td>{item.balance}</td>
                    <td>{item.password}</td>
                    <td><button className="btn btn-danger" onClick={()=>{deleteCompany(item.name)}}>Delete</button></td>
                </tr>
            )
        })
    }
    async function deleteCompany(name){
        const res = await fetch(
            `http://localhost:8080/AdminServices-1.0-SNAPSHOT/api/v1/admin/deleteSellingCompany/${name}`,
            {
                method: "Delete",
                headers: {
                "Content-Type": "application/json"
                }
            }
            );
            const data = await res.text();
            if (data === "Selling Company Deleted Successfully") {
                alert(data);
                window.location.href = "/AllSelling";
            } else {
                alert(data);
            }
    }

  return (
    
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>#</th>
          <th>Company Name</th>
          <th>Company Email</th>
          <th>Company Phone</th>
          <th>Company Balance</th>
            <th>Company Password</th>
            <th>Delete</th>
        </tr>
      </thead>
      <tbody>
        {
            makeTable()
        }
      </tbody>
    </Table>
  );
}

export default AllSelling;
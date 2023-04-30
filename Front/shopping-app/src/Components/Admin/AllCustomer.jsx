import Table from 'react-bootstrap/Table';
import { useState,useEffect } from "react";
function AllCustomer() {
    if(localStorage.getItem("role")!=="admin"){
        window.location.href="/SignIn"
    }
    const [data, setData] = useState([]);
    
    useEffect(() => {
        fetch("http://localhost:8080/AdminServices-1.0-SNAPSHOT/api/v1/admin/getAllCustomers",{
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
                    <td>{item.username}</td>
                    <td>{item.address}</td>
                    <td>{item.email}</td>
                    <td>{item.phone}</td>
                    <td>{item.firstName}</td>
                    <td>{item.secondName}</td>
                    <td>{item.balance}</td>
                </tr>
            )
        })
    }
  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>#</th>
          <th>Customer UserName</th>
          <th>Customer Address</th>
            <th>Customer Email</th>
            <th>Customer Phone</th>
            <th>Customer First Name</th>
            <th>Customer Second Name</th>
            <th>Customer Balance</th>
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

export default AllCustomer;
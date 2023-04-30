import React, { useEffect, useState } from "react";
import Table from 'react-bootstrap/Table';

export function ShippingRequests() {
    const [requests, setRequests] = useState([]);
    const name=localStorage.getItem("userName")
    
    useEffect(() => {
        fetch(`http://localhost:8080/OnlineShopping-1.0-SNAPSHOT/api/v1/shippingCompany/getAllShippingRequestForShippingCompany/${name}`,{
            method:"GET",
            headers:{
                "Content-Type":"application/json"
            }
        }).then((response)=>{
            response.json().then((result)=>{
                setRequests(result)
            })
        }).catch((err)=>{
            console.log(err)
        }
        )
    }, [])  
    function makeTable(){
        return requests.map((item,index)=>{
            return(
                <tr key={index} >
                    <td>{item.productId}</td>
                    <td>{item.sellingCompanyName}</td>
                    <td>{item.customerName}</td>
                    <td>{item.shippingAddress}</td>
                    <td>{item.shippingState}</td>
                    <td></td>
                </tr>
            )
        })
    }

                    

    return (
    
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Product ID</th>
              <th>Selling Company Name</th>
              <th>customer Name</th>
              <th>shippingAddress</th>
                <th>Shipping State</th>
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
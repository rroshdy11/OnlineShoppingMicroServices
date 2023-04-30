import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';

function BasicExample() {
  //get the role from local storage
  var role = localStorage.getItem("role");
  //if the role is admin
  if (role === "admin") {
    return (
      <Navbar bg="light" expand="lg" >
        <Container>
          <Navbar.Brand href="/AdminHome">Admin View</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav" className="justify-content-end">
            <Nav className="me-auto">
              <Nav.Link href="/AllSelling">All Selling Companies</Nav.Link>
              <Nav.Link href="/AllShipping">All Shipping Companies</Nav.Link>
              <Nav.Link href="/AllCustomer">All Customers</Nav.Link>
              <Nav.Link href="/AddSelling">Add Selling Company</Nav.Link>
              <Nav.Link href="/AddShipping">Add Shipping Company</Nav.Link>
              <Nav.Link style={{color:"red"}} href="/SignIn" onClick={()=>{localStorage.clear()}}>Logout</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    );
  }
  else if(role==="delivery"){
    return(
      <Navbar bg="light" expand="lg" >
      <Container>
        <Navbar.Brand href="/ShippingHome">Shipping Company View</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav" className="justify-content-end">
          <Nav className="me-auto">
            <Nav.Link href="/ShippingHome">Home</Nav.Link>
            <Nav.Link href="/ShippingRequests">All Shipping Requests</Nav.Link>
            <Nav.Link style={{color:"red"}} href="/SignIn" onClick={()=>{localStorage.clear()}}>Logout</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
    )
  }
  else if(role===null){
  return (
    <Navbar bg="light" expand="lg">
      <Container>
        <Navbar.Brand href="#home">React-Bootstrap</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="#home">Home</Nav.Link>
            <Nav.Link href="/SignIn">SignIn</Nav.Link> 
            <Nav.Link href="#link">SignUp</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
  }
}

export default BasicExample;
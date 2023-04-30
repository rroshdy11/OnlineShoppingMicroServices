import logo from './logo.svg';
import './App.css';
import NavBar from './Components/NavBar/NavBar';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import {SignIn}  from './Components/SignIn/SignIn';
import AdminHome from './Components/Admin/AdminHome';
import AllSelling from './Components/Admin/AllSelling';
import AllShipping from './Components/Admin/AllShipping';
import AllCustomer from './Components/Admin/AllCustomer';
import { AddSelling } from './Components/Admin/AddSelling';
import { AddShipping } from './Components/Admin/AddShipping';
import ShippingHome from './Components/Shipping/ShippingHome';
import {ShippingRequests} from './Components/Shipping/ShippingRequests';
function App() {
  
  return (
    <div className="App">
      <NavBar />
      <br />
      <BrowserRouter>
      <Routes>
        <Route path="/SignIn" element={
          <SignIn />
        } />
        <Route path="/AdminHome" element={
          <AdminHome/>
        } />
        <Route path="/AllSelling" 
        element={
          <AllSelling/>} 
          />
          <Route path="/AllShipping" 
        element={
          <AllShipping/>} 
          />
        <Route path="/AllCustomer" element={
          <AllCustomer />
        } />
        <Route path="/AddSelling" element={
          <AddSelling />
        } />
        <Route path="/AddShipping" element={
          <AddShipping />
        } />
        <Route path="/ShippingHome" element={
          <ShippingHome />
        } />
        <Route path="/ShippingRequests" element={
          <ShippingRequests />
        } />
        

      </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;

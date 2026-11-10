import { useState } from 'react'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import './App.css'
import { BrowserRouter, Navigate, Routes,Route } from 'react-router-dom';
import ShipmentDetail from './pages/ShipmentDetail';

function App() {
  const [isLoggedIn,setIsLoggedIn] = useState(!!localStorage.getItem('jwtToken'));

  return (
   <BrowserRouter>
    <Routes>
      <Route path="/login" element={isLoggedIn ? <Navigate to="/dashboard" replace/> : <Login onLogin={() => setIsLoggedIn(true)}/>}/>
      <Route path="/dashboard" element ={ isLoggedIn ? <Dashboard/> : <Navigate to="/login" replace/>}/>
      <Route path="/api/shipments/:id" element={ isLoggedIn ? <ShipmentDetail /> : <Navigate to="/login" replace/>}/>
      <Route path="*" element={<Navigate to={isLoggedIn ? '/dashboard' : '/login'} replace/>}/>
    </Routes>
   </BrowserRouter>
  )


}

export default App

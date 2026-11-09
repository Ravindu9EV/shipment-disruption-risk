import { useState } from 'react'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import './App.css'

function App() {
  const [isLoggedIn,setIsLoggedIn] = useState(!!localStorage.getItem('jwtToken'));

  return (
    <div>
      {isLoggedIn ? <Dashboard/> : <Login onLogin={() => setIsLoggedIn(true)}/>}
    </div>
  )


}

export default App

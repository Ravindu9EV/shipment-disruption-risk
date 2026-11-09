import { useState } from "react";
import axiosClient from "../api/axiosClient";

export default function Login({ onLogin}){
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (e)=> {
    e.preventDefault();
    setError('');
    try{
      const response = await axiosClient.post('/auth/login', { username, password});
      localStorage.setItem('jwtToken', response.data.token);
      onLogin();
    }catch(err){
      setError('Invalid username or password');
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '100px auto', textAlign: 'center'}}>
      <h2>Shipment Risk Login</h2>
      {error && <p style={{ color: 'red'}}>{error}</p>}
      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
        <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} required/>
        <input type="password" placeholder="Password" value={password} onChange={(e)=> setPassword(e.target.value)} required />
        <button type="submit">Login</button>
      </form>
    </div>
  );
}



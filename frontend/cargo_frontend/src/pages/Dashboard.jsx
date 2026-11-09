import { useState, useEffect } from 'react';
import axiosClient from '../api/axiosClient';

export default function Dashboard() {
  const [page,setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [shipments, setShipments] = useState([]);
  const [loading, setLoading] =useState(true);

  useEffect(()=> {
    axiosClient.get(`/shipments?page=${page}&size=10`).then(response => {
      setShipments(response.data.content);
      setShipments(response.data.content);
      setTotalPages(response.data.totalPages);
      setLoading(false);
     // console.log(response.data);

    })
    .catch(err=> {
      console.error(err);
      setLoading(false);
    });
  },[page]);

  if(loading) return <h2 style={{ textAlign: 'center', marginTop: '50px' }}>Loading data...</h2>;

  return(
    <div style={{ padding: '20px'}}>
      <h2>Shipment Risk Dashboard</h2>
      <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px'}}>
        <thead>
          <tr style={{ backgroundColor: '#f4f4f4'}}>
            <th style={{ padding: '10px', border: '1px solid #ddd'}}>Shipment ID</th>
             <th style={{ padding: '10px', border: '1px solid #ddd'}}>Origin</th>
              <th style={{ padding: '10px', border: '1px solid #ddd'}}>Destination</th>
               <th style={{ padding: '10px', border: '1px solid #ddd'}}>Distance</th>
                <th style={{ padding: '10px', border: '1px solid #ddd'}}>Risk</th>
          </tr>
        </thead>

        <tbody>
          {shipments.map(s => (
            <tr key={s.id}>
              <td style={{ padding: '10px', border: '1px solid #ddd'}}>{s.shipmentId}</td>
              <td style={{ padding: '10px', border: '1px solid #ddd'}}>{s.originPort}</td>
              <td style={{ padding: '10px', border: '1px solid #ddd'}}>{s.destination}</td>
              <td style={{ padding: '10px', border: '1px solid #ddd'}}>{s.distance}</td>
              <td style={{ padding: '10px', border: '1px solid #ddd'}}> <span style={{ padding: '5px 10px', borderRadius: '12px', color: 'white', backgroundColor: s.riskLevel === 'HIGH' ? '#dc3545' : s.riskLevel === 'MEDIUM' ? '#ffc107': '#28a745'}}>{s.riskLevel}</span> </td>
            </tr>
          ))}
        </tbody>
      </table>

      <button disabled={page===0} onClick={()=> setPage(p=> p-1)}>Prev</button>
      <span>Page {page + 1} of {totalPages}</span>
      <button disabled={page >=totalPages - 1} onClick={()=> setPage(p => p+1)}>Next</button>
    </div>
  )
}

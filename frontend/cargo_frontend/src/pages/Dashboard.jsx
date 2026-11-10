import { useState, useEffect } from 'react';
import axiosClient from '../api/axiosClient';
import { useNavigate } from 'react-router-dom';

export default function Dashboard() {
  const [page,setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [shipments, setShipments] = useState([]);
  const [loading, setLoading] =useState(true);
  const [error,setError] = useState('');
  const [riskFilter, setRiskFilter] = useState('');
  const [summary, setSummary] = useState(null);

  const navigate = useNavigate();


  useEffect(()=> {
    setLoading(true);
    setError('');
    const params = new URLSearchParams({ page: String(page), size: '10'});
    if(riskFilter) params.append('riskLevel', riskFilter);

    axiosClient.get(`/shipments?${params.toString()}`).then(response => {
      setShipments(response.data.content);
      setTotalPages(response.data.totalPages);
      setLoading(false);
      console.log(response.data);

    })
    .catch(err=> {
      console.error(err);
      setError(err.message || 'Failed to load shipments');
      setLoading(false);
    });
  },[page,riskFilter]);

  // fetch summary
  useEffect(()=> {
    axiosClient.get('/risk/summary').then(res=> setSummary(res.data)).catch(err => console.error('Summary fetch failed:', err));
  },[]);

  const applyFilter = (level)=> {
    setPage(0);
    setRiskFilter(level);
  }

  if(loading) return <h2 style={{ textAlign: 'center', marginTop: '50px' }}>Loading data...</h2>;

  if(error) return (
    <div style={{ maxWidth: 600, margin: '50px auto', textAlign: 'center' }}>
            <p style={{ color: '#dc3545' }}>{error}</p>
            <button onClick={() => window.location.reload()}>Retry</button>
        </div>
  );
  return(
    <div style={{ padding: '20px'}}>
      <h2>Shipment Risk Dashboard</h2>

      <div style={{
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 20
}}>
    <h2 style={{ margin: 0 }}>Shipment Risk Dashboard</h2>
    <button
        onClick={() => {
            localStorage.removeItem('jwtToken');
            window.location.href = '/login';
        }}
        style={{
            padding: '8px 16px',
            background: '#dc3545',
            color: 'white',
            border: 'none',
            borderRadius: 4,
            cursor: 'pointer'
        }}
    >
        Logout
    </button>
</div>
{summary && (
                <>
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 15, marginBottom: 12 }}>
                        <div
                            onClick={() => applyFilter('')}
                            style={{
                                padding: 20, borderRadius: 8, background: '#f8f9fa', border: '1px solid #dee2e6',
                                cursor: 'pointer', outline: riskFilter === '' ? '2px solid #333' : 'none'
                            }}
                        >
                            <div style={{ fontSize: 13, color: '#666', marginBottom: 4 }}>Total Shipments</div>
                            <div style={{ fontSize: 28, fontWeight: 700 }}>{summary.total}</div>
                        </div>

                        <div
                            onClick={() => applyFilter(riskFilter === 'HIGH' ? '' : 'HIGH')}
                            style={{
                                padding: 20, borderRadius: 8, background: '#f8d7da', border: '1px solid #f5c2c7',
                                cursor: 'pointer', outline: riskFilter === 'HIGH' ? '2px solid #dc3545' : 'none'
                            }}
                        >
                            <div style={{ fontSize: 13, color: '#721c24', marginBottom: 4 }}>HIGH Risk</div>
                            <div style={{ fontSize: 28, fontWeight: 700, color: '#721c24' }}>
                                {summary.byLevel?.HIGH ?? 0}
                            </div>
                        </div>

                        <div
                            onClick={() => applyFilter(riskFilter === 'MEDIUM' ? '' : 'MEDIUM')}
                            style={{
                                padding: 20, borderRadius: 8, background: '#fff3cd', border: '1px solid #ffe69c',
                                cursor: 'pointer', outline: riskFilter === 'MEDIUM' ? '2px solid #ffc107' : 'none'
                            }}
                        >
                            <div style={{ fontSize: 13, color: '#856404', marginBottom: 4 }}>MEDIUM Risk</div>
                            <div style={{ fontSize: 28, fontWeight: 700, color: '#856404' }}>
                                {summary.byLevel?.MEDIUM ?? 0}
                            </div>
                        </div>

                        <div
                            onClick={() => applyFilter(riskFilter === 'LOW' ? '' : 'LOW')}
                            style={{
                                padding: 20, borderRadius: 8, background: '#d4edda', border: '1px solid #c3e6cb',
                                cursor: 'pointer', outline: riskFilter === 'LOW' ? '2px solid #28a745' : 'none'
                            }}
                        >
                            <div style={{ fontSize: 13, color: '#155724', marginBottom: 4 }}>LOW Risk</div>
                            <div style={{ fontSize: 28, fontWeight: 700, color: '#155724' }}>
                                {summary.byLevel?.LOW ?? 0}
                            </div>
                        </div>
                    </div>

                    <p style={{ color: '#666', margin: '0 0 20px 0', fontSize: 14 }}>
                        Average risk score: <strong>{summary.averageScore}</strong>
                    </p>
                </>
            )}

            <div style={{ marginBottom: 15 }}>
                <label style={{ marginRight: 8, color: '#666', fontSize: 14 }}>Filter:</label>
                <select
                    value={riskFilter}
                    onChange={e => applyFilter(e.target.value)}
                    style={{ padding: '6px 12px', borderRadius: 4, border: '1px solid #ccc' }}
                >
                    <option value="">All risk levels</option>
                    <option value="HIGH">HIGH only</option>
                    <option value="MEDIUM">MEDIUM only</option>
                    <option value="LOW">LOW only</option>
                </select>
            </div>
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
            <tr key={s.id}
    onClick={() => navigate(`/shipments/${s.id}`)}
    style={{ cursor: 'pointer' }}
    onMouseEnter={e => e.currentTarget.style.backgroundColor = '#f5f5f5'}
    onMouseLeave={e => e.currentTarget.style.backgroundColor = ''}>
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

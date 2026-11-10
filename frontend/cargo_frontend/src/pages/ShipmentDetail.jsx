import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosClient from '../api/axiosClient';

const riskStyle = (level) => {
  switch (level){
    case 'HIGH': return { bg: '#dc3545', fg:'white'};
    case 'MEDIUM': return { bg: '#ffc107', fg:'black'};
    case 'LOW': return { bg: '#28a745', fg:'white'};
    default: return { bg: '#6c757d', fg:'white'};
  }
};

const num = ( v, digits = 2) => v === null || v === undefined ? '-' : Number(v).toFixed(digits);
const dateTime = (d)=> d ? new Date(d).toLocaleDateString() : '-';

export default function ShipmentDetail(){
  const { id } = useParams();
  const navigate = useNavigate();
  const [shipment, setShipment] = useState(null);
  const [breakdown, setBreakdown] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    setError('');
    Promise.all([
      axiosClient.get(`/shipments/${id}`),
      axiosClient.get(`/risk/shipments/${id}/breakdown`)
    ])
    .then(([ s , b]) => {
      setShipment(s.data);
      setBreakdown(b.data);
      setLoading(false);
    })
    .catch(err => {
      setError(err.response.status === 404 ? 'Shipment not found' : (err.message || 'Failed to load shipment'));
      setLoading(false);
    });

    if (loading) return <p style={{ textAlign: 'center', marginTop: 50}}>Loading...</p>

    if(error) return (
      <div style={{ maxWidth: 600, margin: '50px auto', textAlign: 'center' }}>
        <p style={{ color: '#dc3545'}}>{error}</p>
        <button onClick={()=> navigate('/dashboard')}>Back to dashboard</button>
      </div>
    );

    const risk = riskStyle(shipment.riskLevel);

    return(
      <div style={{ maxWidth: 1100, margin: '20px auto', padding:20}}>
        <button onClick={()=> navigate('/dashboard')} style={{ marginBottom: 20}}>
           Back to dashboard
        </button>

        {shipment.disruptionOccurred && (
          <div style={{ background: '#f8d7da', color: '#721c24',padding: '12px 16px', borderRadius: 6, marginBottom: 20,
            borderLeft: '4px solid #dc3545'
          }}> <strong>⚠️ Disruption occurred on this shipment.</strong></div>
        )}
        <h2 style={{ marginBottom: 20 }}>Shipment {shipment.shipmentId}</h2>

        <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: 20}}>
          <div style={{ border: '1px solid #ddd', borderRadius: 8, padding: 20 }}>
            <h3 tyle={{ marginTop: 0 }}>Shipment Details</h3>
            <dl style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px 20px', margin: 0}}>
              <dt style={{ color: '#666' }}>Shipment Date</dt>
              <dd style={{ margin: 0}}>{dateOnly(shipment.shipmentDate)}</dd>

              <dt style={{ color: '#666' }}>Origin Port</dt>
              <dd style={{ margin: 0}}>{shipment.originPort}</dd>

              <dt style={{ color: '#666' }}>Destination</dt>
              <dd style={{ margin: 0}}>{shipment.destination}</dd>

              <dt style={{ color: '#666' }}>Transport Mode</dt>
              <dd style={{ margin: 0}}>{shipment.transportMode}</dd>

              <dt style={{ color: '#666' }}>Product Category</dt>
              <dd style={{ margin: 0}}>{shipment.productCategory}</dd>

              <dt style={{ color: '#666' }}>Distance (km)</dt>
              <dd style={{ margin: 0}}>{num(shipment.distance)}</dd>

              <dt style={{ color: '#666' }}>Weight (MT)</dt>
              <dd style={{ margin: 0}}>{num(shipment.weight)}</dd>

              <dt style={{ color: '#666' }}>Fuel Price Index</dt>
              <dd style={{ margin: 0}}>{num(shipment.fuelPrice)}</dd>

              <dt style={{ color: '#666' }}>Carrier Reliability</dt>
              <dd style={{ margin: 0}}>{num(shipment.carrierReliability)}</dd>

              <dt style={{ color: '#666' }}>Lead Time (days)</dt>
              <dd style={{ margin: 0}}>{num(shipment.leadTime,0)}</dd>

              <dt style={{ color: '#666' }}>Weather Condition</dt>
              <dd style={{ margin: 0, fontWeight: [ 'Hurricane', 'Storm', 'Typhoon', 'Cyclone'].includes(shipment.weatherCondition) ? 600 : 400,color: [ 'Hurricane', 'Storm', 'Typhoon', 'Cyclone'].includes(shipment.weatherCondition) ? '#dc3545' : 'inherit'}}>{shipment.weatherCondition || '-'}</dd>

              <dt style={{ color: '#666' }}>Geopolitical Risk</dt>
              <dd style={{ margin: 0}}>{shipment.geopoliticalRisk !=null ? `${Number(shipment.geopoliticalRisk).toFixed(1)} /10` : '-'}</dd>

              <dt style={{ color: '#666' }}>Disruption Occurred</dt>
              <dd style={{ margin: 0}}>{shipment.disruptionOccurred ? 'Yes': 'No'}</dd>
            </dl>
          </div>

          <div style={{ border: '1px solid #ddd', borderRadius: 8, padding: 20}}>
            <h3 style={{ marginTop: 0}}>Current Risk</h3>
            <span style={{ display: 'inline-block', padding: '6px 14px', borderRadius: 20, background: risk.bg, color: risk.fg, fontWeight: 600, marginBottom: 15}}>{shipment.riskLevel}</span>

            <div style={{ fontSize: 32, fontWeight: 700, marginBottom: 6}}>
              {num(shipment.riskScore, 0)}
            </div>
            <div style={{ color: '#666', fontSize: 13 }}>
              Assessed: {dateTime(shipment.assessedAt)}
            </div>
          </div>
        </div>

        {breakdown && breakdown.triggeredRules && (
          <div style={{ border: '1px solid #ddd',borderRadius: 8, padding: 20, marginTop: 20}}>
            <h3 style={{ marginTop: 0 }}>Why this score</h3>
            {breakdown.triggeredRules.length ===0 ? (
              <p style={{ color: '#666', margin: 0 }}>No Risk rules triggered for this shipment.</p>
            ): (
              <>
                <ul style={{ margin: 0, paddingLeft: 20 }}>
                  {breakdown.triggeredRules.map((r,i) => (
                    <li key={i} style={{ marginBottom: 6 }}>
                      <strong>{r.name}</strong>
                      <span style={{ color: '#666'}}>(+{r.points}points)</span>
                      <span style={{
                        marginLeft: 8, fontSize: 12,
                        padding: '1px 6px', borderRadius: 4,
                        background: '#eee', color: '#555'
                      }}>{r.type}</span>
                    </li>
                  ))}
                </ul>
                <div style={{ marginTop: 12, paddingTop: 12, borderTop: '1px dashed #ddd'}}>
                  <strong> Total score: {breakdown.totalScore}</strong>
                  <span style={{ color: '#666', marginLeft: 8 }}>(HIGH &lt; 60,MEDIUM &lt; 30, LOW &lt; 30 )</span>
                </div>
              </>
            )}
          </div>
        )}
      </div>
    )
  })
}

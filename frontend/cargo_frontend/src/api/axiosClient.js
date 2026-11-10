import axios from 'axios';
const axiosClient = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosClient.interceptors.request.use(
  (config)=> {
    const token = localStorage.getItem('jwtToken');
    if(token){
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// axiosClient.interceptors.response.use(
//     (response) => response,
//     (error) => {
//         if (error.response?.status === 401) {
//             localStorage.removeItem('jwtToken');
//             if (window.location.pathname !== '/login') {
//                 window.location.href = '/login';
//             }
//         }
//         return Promise.reject(error);
//     }
// );

export default axiosClient;

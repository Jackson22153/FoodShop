import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import HomeComponent from './components/ui/homePath/home/Home'
import UserComponent from './components/ui/userPath/user/User'
import 'bootstrap/dist/css/bootstrap.min.css';
import './css/responsive.css';
import { useEffect, useState } from 'react';
import { isAuthenticated } from './api/AuthorizationApi';
import { UserInfo } from './model/Type';
import { UserInfoProvider } from './components/contexts/UserInfoContext';

function App() {
  const [userInfo, setUserInfo] = useState<UserInfo>({
    userID: "",
    username: "",
    isAuthenticated: false
  })
  useEffect(()=>{
    initital();
    console.log("testing")
  }, [])

  const initital = ()=>{
    // check and get userInfo
    checkIsAuthenticated();
  }

  const checkIsAuthenticated = async ()=>{
    const res = await isAuthenticated()
    if(res.status===200){
      const data = res.data;
      setUserInfo({
        userID: data.userID,
        username: data.username,
        isAuthenticated: data.authenticated
      });
    }
  }


  return (
    <UserInfoProvider value={userInfo}>
      <Router>
        <Routes>
          <Route path='*' element={<HomeComponent/>}/>
          <Route path='user/*' element={<UserComponent/>}/>
        </Routes>
      </Router>
    </UserInfoProvider>
  )
}

export default App

import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import HomeComponent from './components/ui/homePath/home/Home'
import UserComponent from './components/ui/userPath/user/User'
import 'bootstrap/dist/css/bootstrap.min.css';
import './css/responsive.css';
import { useEffect, useState } from 'react';
import { isAuthenticated } from './api/AuthorizationApi';
import { Notification, UserInfo } from './model/Type';
import { UserInfoProvider } from './components/contexts/UserInfoContext';
import { notificationReceiveConnect } from './api/ReceiveNotificationWsApi';
import { NotificationMessagesProvider } from './components/contexts/NotificationMessagesContext';

function App() {
  const [userInfo, setUserInfo] = useState<UserInfo>({
    user:{
      userID: '',
      username: '',
      email: '',
    },
    roles: []
  })
  const [notification, setNotification] = useState<Notification>(null)
  useEffect(()=>{
    initital();
    console.log("testing")
  }, [])

  const initital = ()=>{
    checkIsAuthenticated();
  }

  // check whether a user is authenticated or not
  const checkIsAuthenticated = async ()=>{
    try {
      const res = await isAuthenticated()
      if(200<=res.status && res.status<300){
        const data = res.data;
        setUserInfo(data); 
        notificationReceiveConnect(getNotification);
      }
    } catch (error) {
      
    }
  }
  // receive notification from backend
  const getNotification = (message: any)=>{
    const notification = message as Notification;
    setNotification(notification)
  }

  return (
    <UserInfoProvider value={userInfo}>
      <NotificationMessagesProvider value={notification}>
        <Router>
          <Routes>
            <Route path='*' element={<HomeComponent/>}/>
            <Route path='user/*' element={<UserComponent/>}/>
          </Routes>
        </Router>
      </NotificationMessagesProvider>
    </UserInfoProvider>
  )
}

export default App

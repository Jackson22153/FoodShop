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
import { NotificationMessagesProvider } from './components/contexts/NotificationMessagesContext';
import { ROLE } from './constant/WebConstant';
import { employeeReceiveNotificationConnect } from './api/EmployeeReceiveNotificationWsApi';
import { customerReceiveNotificationConnect } from './api/CustomerReceiveNotificationWsApi';
import ErrorPageComponent from './components/ui/error/ErrorPage';

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
        // convert role name to lowercase 
        const roles = roleNames(data);
        // receive notification
        if(roles.includes(ROLE.CUSTOMER.toLowerCase())){
          customerReceiveNotificationConnect(receiveNotification);
        }else if(roles.includes(ROLE.EMPLOYEE.toLowerCase())){
          employeeReceiveNotificationConnect(receiveNotification);
        }
      }
    } catch (error) {
      // console.log(error)
    }
  }

    // USER ROLES
    const roleNames = (userinfo: UserInfo)=>{
      const arr = userinfo.roles.map(role => role.toLowerCase());
      return arr;
    }
  // receive notification from backend
  const receiveNotification = (message: any)=>{
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
            <Route path='error/*' element={<ErrorPageComponent/>}/>
          </Routes>
        </Router>
      </NotificationMessagesProvider>
    </UserInfoProvider>
  )
}

export default App

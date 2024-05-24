import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import HomeComponent from './components/ui/homePath/home/Home'
import UserComponent from './components/ui/userPath/user/User'
import 'bootstrap/dist/css/bootstrap.min.css';
import './css/responsive.css';
import { useEffect, useRef, useState } from 'react';
import { isAuthenticated } from './api/AuthorizationApi';
import { Pageable, Role, UserInfo } from './model/Type';
import { Notification } from './model/Type';
import { UserInfoProvider } from './components/contexts/UserInfoContext';
import { getEmployeeNotifications } from './api/EmployeeApi';
import { NotificationMessagesProvider } from './components/contexts/NotificationMessagesContext'
import { CompatClient, Stomp } from '@stomp/stompjs';
import { AccountWSUrl, EmployeeNotificationOrderWsUrl, QUEUE_MESSAGES } from './constant/FoodShoppingApiURL';
import SockJS from 'sockjs-client';
import { ROLE } from './constant/config';
import { StompClientsProvider } from './components/contexts/StompClientsContext';
import { getCustomerNotifications } from './api/UserApi';
import { getAccessToken } from './service/cookie';

function App() {
  const [userInfo, setUserInfo] = useState<UserInfo>({
    user:{
      userID: '',
      username: '',
      email: '',
    },
    roles: []
  })
  const [pageable] = useState<Pageable>({
    first: true,
    last: true,
    number: 0,
    totalPages: 0
  })
  const stompClientShop = useRef<CompatClient | null>(null);
  const stompClientAccount = useRef<CompatClient | null>(null);
  const [notificationMessages, setNotificationMessages] = useState<Notification[]>([]);
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
    if(res.status){
      const data = res.data;
      setUserInfo(data); 
      getNotifications(data.roles)
    }
  }

  const getNotifications = (roles: Role[])=>{
    const roleNames = roles.map(role => role.roleName.toLowerCase());
    // get notification for employee
    if(roleNames.includes(ROLE.EMPLOYEE.toLowerCase())){
      fetchEmployeeNotifications(pageable.number);
      // connectEmployee();
    }else if(roleNames.includes(ROLE.CUSTOMER.toLowerCase())){
      fetchCustomerNotifications(pageable.number);
      // connectCustomer();
    }
  }
  const fetchEmployeeNotifications = async (pageNumber: number)=>{
    const res = await getEmployeeNotifications(pageNumber);
    if(res.status){
      const data = res.data;
      setNotificationMessages(data.content);
    }
  }
  const fetchCustomerNotifications = async (pageNumber: number)=>{
    const res = await getCustomerNotifications(pageNumber);
    if(res.status){
      const data = res.data;
      setNotificationMessages(data.content);
    }
  }

  // stomp
  // employee
  const connectEmployee = ()=>{
    stompClientAccount.current = Stomp.over(()=> new SockJS(AccountWSUrl));
    stompClientAccount.current.connect({
      "Authorization": `Bearer ${getAccessToken()}`
    }, onEmployeeConnect, stompFailureCallback);
  }
  function onEmployeeConnect() {
    if(stompClientAccount.current){
        // listen to customer order
        stompClientAccount.current.subscribe(EmployeeNotificationOrderWsUrl, onEmployeeReceiveNotification, {
          "Authorization": `Bearer ${getAccessToken()}`,
          'auto-delete': 'true'
        });
        // listen to notification send to employee
        stompClientAccount.current.subscribe(QUEUE_MESSAGES, onEmployeeReceiveNotification, {
          "Authorization": `Bearer ${getAccessToken()}`,
          'auto-delete': 'true'
        })
        stompClientAccount.current.reconnect_delay=1000 
    }
  }
  //   receive new order from customer
  async function onEmployeeReceiveNotification(payload: any) {
    const message = JSON.parse(payload.body);
    fetchEmployeeNotifications(pageable.number);
    const notification = message as Notification;
    console.log(notification)
    if(notification){
      setNotificationMessages([...notificationMessages, notification])
    }
  }
  // customer
  const connectCustomer = ()=>{
    stompClientAccount.current = Stomp.over(()=> new SockJS(AccountWSUrl));
    stompClientAccount.current.connect({
      "Authorization": `Bearer ${getAccessToken()}`
    }, onCustomerConnect, stompFailureCallback);
  }
  function onCustomerConnect() {
    if(stompClientAccount.current){
        // console.log('Connected');
        stompClientAccount.current.subscribe(QUEUE_MESSAGES, onCustomerReceiveNotification, {
          "Authorization": `Bearer ${getAccessToken()}`,
          'auto-delete': 'true'
        });
        stompClientAccount.current.reconnect_delay=1000 
    }
  }
  //   receive new order from customer
  async function onCustomerReceiveNotification(payload: any) {
    const message = JSON.parse(payload.body);
    fetchCustomerNotifications(pageable.number);
    const notification = message as Notification;
    if(notification){
      setNotificationMessages([...notificationMessages, notification])
    }
  }





  
  function stompFailureCallback(error: any){
    console.log(error)
  }


  return (
    <UserInfoProvider value={userInfo}>
      <StompClientsProvider value={{stompClientShop, stompClientAccount}}>
        <NotificationMessagesProvider value={{notifications: notificationMessages, setNotifications: setNotificationMessages}}>
          <Router>
            <Routes>
              <Route path='*' element={<HomeComponent/>}/>
              <Route path='user/*' element={<UserComponent/>}/>
            </Routes>
          </Router>
        </NotificationMessagesProvider>
      </StompClientsProvider>
    </UserInfoProvider>
  )
}

export default App

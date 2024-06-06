import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import HomeComponent from './components/ui/homePath/home/Home'
import UserComponent from './components/ui/userPath/user/User'
import 'bootstrap/dist/css/bootstrap.min.css';
import './css/responsive.css';
import { useEffect, useRef, useState } from 'react';
import { isAuthenticated } from './api/AuthorizationApi';
import { Pageable, UserInfo } from './model/Type';
import { Notification } from './model/Type';
import { UserInfoProvider } from './components/contexts/UserInfoContext';
import { NotificationMessagesProvider } from './components/contexts/NotificationMessagesContext'
import { CompatClient, Stomp } from '@stomp/stompjs';
import { AccountWSUrl, EmployeeNotificationOrderWsUrl, QUEUE_MESSAGES } from './constant/FoodShoppingApiURL';
import SockJS from 'sockjs-client';
import { StompClientsProvider } from './components/contexts/StompClientsContext';
import { getUserNotifications } from './api/NotificationApi';

function App() {
  const stompClientShop = useRef<CompatClient | null>(null);
  const stompClientAccount = useRef<CompatClient | null>(null);
  const [notifications, setNotifications] = useState<Notification[]>([])
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
  useEffect(()=>{
    initital();
    console.log("testing")
  }, [])

  const initital = ()=>{
    checkIsAuthenticated();
  }

  // check whether a user is authenticated or not
  const checkIsAuthenticated = async ()=>{
    const res = await isAuthenticated()
    if(res.status){
      const data = res.data;
      setUserInfo(data); 
      fetchUserNotifications(pageable.number)
    }
  }

  const fetchUserNotifications = async (pageNumber: number)=>{
    const res = await getUserNotifications(pageNumber);
    if(res.status){
      const data = res.data;
      setNotifications(data.content);
    }
  }


  // stomp
  // employee
  const connectEmployee = ()=>{
    stompClientAccount.current = Stomp.over(()=> new SockJS(AccountWSUrl));
    stompClientAccount.current.connect({}, onEmployeeConnect, stompFailureCallback);
  }
  function onEmployeeConnect() {
    if(stompClientAccount.current){
        // listen to customer order
        stompClientAccount.current.subscribe(EmployeeNotificationOrderWsUrl, onEmployeeReceiveNotification, {
          'auto-delete': 'true'
        });
        // listen to notification send to employee
        stompClientAccount.current.subscribe(QUEUE_MESSAGES, onEmployeeReceiveNotification, {
          'auto-delete': 'true'
        })
        stompClientAccount.current.reconnect_delay=1000 
    }
  }
  //   receive new order from customer
  async function onEmployeeReceiveNotification(payload: any) {
    const message = JSON.parse(payload.body);

    const newNotification = message as Notification;
    console.log(newNotification)
    if(newNotification){
      setNotifications([...notifications, newNotification])
    }
  }
  // customer
  const connectCustomer = ()=>{
    stompClientAccount.current = Stomp.over(()=> new SockJS(AccountWSUrl));
    stompClientAccount.current.connect({}, onCustomerConnect, stompFailureCallback);
  }
  function onCustomerConnect() {
    if(stompClientAccount.current){
        // console.log('Connected');
        stompClientAccount.current.subscribe(QUEUE_MESSAGES, onCustomerReceiveNotification, {
          'auto-delete': 'true'
        });
        stompClientAccount.current.reconnect_delay=1000 
    }
  }
  //   receive new order from customer
  async function onCustomerReceiveNotification(payload: any) {
    const message = JSON.parse(payload.body);

    const newNotification = message as Notification;
    if(newNotification){
      setNotifications([...notifications, newNotification])
    }
  }





  
  function stompFailureCallback(error: any){
    console.log(error)
  }


  return (
    <UserInfoProvider value={userInfo}>
      <StompClientsProvider value={{stompClientShop, stompClientAccount}}>
        <NotificationMessagesProvider value={{notifications: notifications, setNotifications: setNotifications}}>
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

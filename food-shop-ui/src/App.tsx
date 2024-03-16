import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import HomeComponent from './components/ui/homePath/home/Home'
import UserComponent from './components/ui/userPath/user/User'
import 'bootstrap/dist/css/bootstrap.min.css';
import './css/responsive.css';

function App() {

  return (
    <Router>
      <Routes>
        <Route path='*' element={<HomeComponent/>}/>
        <Route path='user/*' element={<UserComponent/>}/>
      </Routes>
    </Router>
  )
}

export default App

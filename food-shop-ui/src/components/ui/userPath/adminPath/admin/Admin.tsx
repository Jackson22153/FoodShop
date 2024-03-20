import { Route, Router, Routes } from 'react-router-dom';
import SidebarComponent from '../../../../shared/functions/admin/sidebar/SideBar';
import TopBarComponent from '../../../../shared/functions/admin/topbar/TopBar';
import FooterComponent from '../../../../shared/website/footer/Footer';
import FoodsDashBoardComponent from '../food/FoodsDashboard';
import AdminDashBoardComponent from '../adminDashBoard/AdminDashBoard';
import FoodsComponent from '../../../homePath/foods/Foods';
import FoodComponent from '../food/Food';

export default function AdminComponent(){
    return(
        <>
            <div id="wrapper" className='d-flex'>
                {/* <!-- Sidebar --> */}
                <div className="sidebar col-md-2 px-4">
                    <SidebarComponent/>
                </div>
                {/* <!-- End of Sidebar --> */}

                {/* <!-- Content Wrapper --> */}
                <div id="content-wrapper" className="d-flex flex-column col-md-10">

                    {/* <!-- Main Content --> */}
                    <div id="content">
                        {/* <!-- Topbar --> */}
                        <TopBarComponent/>
                        {/* <!-- End of Topbar --> */}

                        {/* <!-- Begin Page Content --> */}
                        <Routes>
                            <Route path='/foods' element={<FoodsDashBoardComponent/>}/> 
                            <Route path='/foods/:foodname' element={<FoodComponent/>}/> 
                            <Route path='*' element={<AdminDashBoardComponent/>}/> 
                        </Routes>
                        
                        {/* <!-- /.container-fluid --> */}
                    </div>
                    {/* <!-- End of Main Content --> */}

                </div>
                {/* <!-- End of Content Wrapper --> */}
            </div>
            {/* <!-- Footer --> */}
            <FooterComponent/>
            {/* <!-- End of Footer --> */}
                {/* <!-- Scroll to Top Button--> */}
            <a className="scroll-to-top rounded" href="#page-top">
                <i className="fas fa-angle-up"></i>
            </a>
            {/* <!-- Logout Modal--> */}
            <div className="modal fade" id="logoutModal" tabIndex={-1}  role="dialog" aria-labelledby="exampleModalLabel"
                aria-hidden="true">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                            <button className="close" type="button" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                        </div>
                        <div className="modal-body">Select "Logout" below if you are ready to end your current session.</div>
                        <div className="modal-footer">
                            <button className="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                            <a className="btn btn-primary" href="login.html">Logout</a>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}
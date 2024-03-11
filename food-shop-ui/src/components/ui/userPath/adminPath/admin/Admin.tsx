import { Route, Routes } from 'react-router-dom';
import AdminDashBoardComponent from '../adminDashBoard/AdminDashBoard';
import SidebarComponent from '../../../../stateless/function/admin/sidebar/SideBar';
import TopBarComponent from '../../../../stateless/function/admin/topbar/TopBar';
import FooterComponent from '../../../../stateless/function/admin/footer/Footer';

export default function AdminComponent(){
    return(
        <>
            <div id="wrapper">
                {/* <!-- Sidebar --> */}
                <SidebarComponent/>
                {/* <!-- End of Sidebar --> */}

                {/* <!-- Content Wrapper --> */}
                <div id="content-wrapper" className="d-flex flex-column">

                    {/* <!-- Main Content --> */}
                    <div id="content">
                        {/* <!-- Topbar --> */}
                        <TopBarComponent/>
                        {/* <!-- End of Topbar --> */}

                        {/* <!-- Begin Page Content --> */}
                        <Routes>
                            <Route path='*' element={<AdminDashBoardComponent/>}/>
                        </Routes>
                        {/* <!-- /.container-fluid --> */}
                    </div>
                    {/* <!-- End of Main Content --> */}

                    {/* <!-- Footer --> */}
                    <FooterComponent/>
                    {/* <!-- End of Footer --> */}
                </div>
                {/* <!-- End of Content Wrapper --> */}
            </div>
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
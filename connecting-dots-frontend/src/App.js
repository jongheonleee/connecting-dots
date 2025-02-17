import './App.css';
import { BrowserRouter as Router, Link, Route, Routes } from 'react-router-dom';
import ProtectedRoute from './ProtectedRoute';
import KakaoAuthRedirect from './pages/KakaoAuthRedirect';
import Login from './pages/Login';
import Signup from './pages/Signup';
import { useEffect, useState } from 'react';
import Dashboard from './pages/Dashboard';
import Details from "./pages/Details";
import Main from "./pages/Main";

const App = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogout = async (e) => {
    e.preventDefault();

    try {
      localStorage.removeItem('token');
      setIsLoggedIn(false);
    } catch (error) {
      console.error(error);
      alert('로그아웃에 실패했습니다.');
    }
  };

  return (
      <Router>
        <div className="app-container">
          <nav className="navbar">
            <div className="navbar-brand">
              <Link to="/" className="navbar-link">
                Connecting-Dots
              </Link>
            </div>
            <div className="navbar-menu">
              <ul>
                {isLoggedIn ? (
                    <>
                      <li>
                        <Link to="/dashboard" className="navbar-link">
                          대시보드
                        </Link>
                      </li>
                      <li>
                        <button className="logout-button" onClick={handleLogout}>
                          로그아웃
                        </button>
                      </li>
                    </>
                ) : (
                    <>
                      <li>
                        <Link to="/login" className="navbar-link">
                          로그인
                        </Link>
                      </li>
                      <li>
                        <Link to="/signup" className="navbar-link">
                          회원가입
                        </Link>
                      </li>
                    </>
                )}
              </ul>
            </div>
          </nav>

          <div className="page-content">
            <Routes>
              <Route path="/" element={<Main />} />
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<Signup />} />
              <Route path="/login/oauth2/code/kakao" element={<KakaoAuthRedirect />} />
              <Route path="/details" element={<Details />} />
              <Route
                  path="/dashboard"
                  element={
                    <ProtectedRoute>
                      <Dashboard />
                    </ProtectedRoute>
                  }
              />
            </Routes>
          </div>
        </div>
      </Router>
  );
};

export default App;

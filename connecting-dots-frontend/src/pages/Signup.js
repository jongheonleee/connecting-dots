import {useState} from "react";
import axios from "axios";
import './signup.css';
import {useNavigate} from "react-router-dom";

const Signup = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password1, setPassword1] = useState('');
  const [password2, setPassword2] = useState('');
  const [phone, setPhone] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) =>  {
    e.preventDefault();

    try {
      // 로그인 요청
      const response = await axios.post('http://localhost:8080/api/user/register', {
        username,
        email,
        password : password1,
        phone
      });

      // 로그인 성공 시 대시보드로 이동
      if (response.status === 200) {
        localStorage.setItem('token', response.data.token);
        navigate('/dashboard');
      }

    } catch (error) {
      console.error(error);
      alert('회원가입에 실패했습니다.');
    }
  }

  return (
      <div className="signup-container">
        <h1 className="logo">connecting-dots</h1>
        <h2 className="title">회원 가입</h2>


        <form onSubmit={handleSubmit} className="signup-form">
          <div className="input-group">
            <input
                type="email"
                name="email"
                placeholder="이메일"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
            />
          </div>
          <div className="input-group">
            <input
                type="text"
                name="username"
                placeholder="이름"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
            />
          </div>
          <div className="input-group">
            <input
                type="password1"
                name="password1"
                placeholder="비밀번호"
                value={password1}
                onChange={(e) => setPassword1(e.target.value)}
                required
            />
          </div>
          <div className="input-group">
            <input
                type="password"
                name="confirmPassword"
                placeholder="비밀번호 확인"
                value={password2}
                onChange={(e) => setPassword2(e.target.value)}
                required
            />
          </div>
          <div className="input-group">
            <input
                type="tel"
                name="phone"
                placeholder="핸드폰 번호"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                required
            />
          </div>
          <button type="submit" className="signup-button">동의하고 가입하기</button>
        </form>
      </div>
  );
}

export default Signup;
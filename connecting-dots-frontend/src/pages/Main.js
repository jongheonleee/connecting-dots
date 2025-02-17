import React from 'react';
import'./Main.css';

const Main = () => {
    return (
          <div className='content'>
          <div className="header">
            <img src="logo.png" alt="DC Inside Logo" />
              <div className="search-bar">
                <input type="text" placeholder="검색어 입력" />
                  <button>🔍</button>
              </div>
          </div>
          <div className="nav">
            <span>갤러리</span>
            <span>마이너갤</span>
            <span>미니갤</span>
            <span>뉴스</span>
            <span>디시위키</span>
          </div>
          <div className="container">
            <div className="main-content">
              <h2>게시판 목록</h2>
              <ul>
                <li>[일반] 게시글 제목 1</li>
                <li>[정보] 게시글 제목 2</li>
                <li>[유머] 게시글 제목 3</li>
              </ul>
            </div>
            <div className="sidebar">
              <h3>실시간 인기글</h3>
              <ul>
                <li>인기 게시글 1</li>
                <li>인기 게시글 2</li>
                <li>인기 게시글 3</li>
              </ul>
            </div>
          </div>
          <div className="footer">
            Copyright © 2025 dcinside. All rights reserved.
          </div>
        </div>
    );
}


export default Main;
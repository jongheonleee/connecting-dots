

const Details = () => {
  return (
      <div>
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
        <div className="post-title">게시글 제목</div>
        <div className="post-meta">작성자: 홍길동 | 작성일: 2025-02-15 | 조회수: 1234</div>
        <div className="post-content">
          <p>이곳은 게시글 본문이 들어가는 영역입니다.</p>
          <img src="post-image.png" alt="게시글 이미지" width="100%" />
        </div>

        <div className="comments">
          <h3>댓글</h3>
          <div className="comment">
            <div className="comment-user">사용자1</div>
            <div className="comment-text">좋은 게시글이네요!</div>
          </div>
          <div className="comment">
            <div className="comment-user">사용자2</div>
            <div className="comment-text">유용한 정보 감사합니다.</div>
          </div>
          <div className="comment-input">
            <input type="text" placeholder="댓글을 입력하세요" />
              <button>등록</button>
          </div>
        </div>
      </div>
        <div className="footer">
          Copyright © 2025 dcinside. All rights reserved.
        </div>
      </div>
  );
}


export default Details;
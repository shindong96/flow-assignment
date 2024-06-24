import logo from './logo.svg';
import './App.css';
import React, { useState } from "react"; 

function App() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  function handleCloseModal(){
    setIsModalOpen(false);
  }

  return (
    <>
      <div className = "container">
        <div className = "header">
          <div>IP 접근 설정</div>
          <button onClick={() => setIsModalOpen(true)}>+ IP 추가</button>
        </div>
        <div className="bodyContainer">
          <div className="searchContainer">
            <div className="contentSearchingContainer">
              <input placeholder="내용 검색.."/>
              <button>검색</button>
            </div>
            <div className="permissionTimeSearchingContainer">
              <input placeholder="사용 시작 시간"/>
              <input placeholder="사용 끝 시간"/>
              <button>검색</button>
            </div>
          </div>
          <div className="tableContainer">
            <table>
              <tr>
                <th>IP 주소</th>
                <th>내용</th>
                <th>사용 시작 시간</th>
                <th>사용 끝 시간</th>
                <th></th>
              </tr>
              <tr>
                <td>일반칸1</td>
                <td>일반칸2</td>
                <td>일반칸3</td>
                <td>일반칸3</td>
                <td>
                  <button>delete</button>
                </td>
              </tr>
            </table>
          </div>
        </div>
      </div>
      {isModalOpen && <Modal handleClose={handleCloseModal}/>}
  </>    
  );
}

export default App;

const Modal = ({ handleClose }) => {
  const [currentIp, setCurrentIp] = useState("");
  const [content, setContent] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  const handleSave = () => {
    console.log(currentIp);
  };

  return (
    <div className="modalLayout">
      <div className="modalContent">
        <div className="modalContentContainer">
          <div className="modalTitle">IP 추가</div>

          <div className="modalInputContainer">
            <div className="modalInputItemContainer">
              <label>IP 주소</label>
              <input
                value={currentIp}
                onChange={(e) => setCurrentIp(e.target.value)}
                placeholder='000.000.000.000'
              />
              <button>현재 IP 불러오기</button>
            </div>
            <div className="modalInputItemContainer">
              <label>설명</label>
              <input
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder='IP 주소의 내용을 작성해주세요'
              />
            </div>
            <div className="modalInputItemContainer">
              <label>허용 시작 시간</label>
              <input
                value={startTime}
                onChange={(e) => setStartTime(e.target.value)}
              />
            </div>
            <div className="modalInputItemContainer">
              <label>허용 끝 시간</label>
              <input
                value={endTime}
                onChange={(e) => setEndTime(e.target.value)}
              />
            </div>
          </div>

          <div className="buttonContainer">
            <button onClick={handleSave}>저장</button>
            <button onClick={handleClose}>취소</button>
          </div>
        </div>
      </div>
    </div>
  );
};

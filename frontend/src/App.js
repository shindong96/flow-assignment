import logo from './logo.svg';
import './App.css';
import React, { useState } from "react"; 
import {useMutation} from "@tanstack/react-query"

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

  const currentIpMutation = useMutation({
    mutationFn: async () => {
      const response = await fetch("https://api.ipify.org?format=json");
      return await response.json();
    },
    onSuccess: (data) => setCurrentIp(data.ip),
    onError: (error) => console.error("Error fetching IP address:", error),
  });

  const saveIpMutation = useMutation({
    mutationFn: (data) => {
      const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;

      return fetch("http://43.202.226.27:8080/access-rules", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Time-Zone": timezone,
        },
        body: JSON.stringify(data),
      });
    },
    onSuccess: () => handleClose(),
    onError: () => alert("문제가 발생했습니다. 다시 시도해주세요."),
  });

  const formatDateTime = (dateTime) => {
    const dateTimeArr = dateTime.split("T");
    const newDateTime =
      dateTimeArr[0].replaceAll("-", "/") + " " + dateTimeArr[1];

    return newDateTime;
  };

  const handleSave = () => {
    const newStartTime = formatDateTime(startTime);
    const newEndTime = formatDateTime(endTime);

    const data = {
      ipAddress: currentIp,
      content: content,
      startTime: newStartTime,
      endTime: newEndTime,
    };

    saveIpMutation.mutate(data);
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
              />
              <button onClick={() => currentIpMutation.mutate()}>
                현재 IP 불러오기
              </button>
            </div>
            <div className="modalInputItemContainer">
              <label>설명</label>
              <input
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />
            </div>
            <div className="modalInputItemContainer">
              <label>허용 시작 시간</label>
              <input
                type="datetime-local"
                value={startTime}
                onChange={(e) => setStartTime(e.target.value)}
              />
            </div>
            <div className="modalInputItemContainer">
              <label>허용 끝 시간</label>
              <input
                type="datetime-local"
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

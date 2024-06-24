import React, { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import formatDateTime from "../util/formatDateTIme.js";

const Modal = ({ handleClose, refetch }) => {
  const [currentIp, setCurrentIp] = useState("");
  const [content, setContent] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  const currentIpMutation = useMutation({
    mutationFn: async () => {
      const response = await fetch("https://api64.ipify.org?format=json");
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
    onSuccess: () => {
      refetch();
      handleClose();
    },
    onError: () => alert("문제가 발생했습니다. 다시 시도해주세요."),
  });

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

export default Modal;

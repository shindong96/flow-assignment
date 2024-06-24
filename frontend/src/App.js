import './App.css';
import React, { useState } from "react"; 
import {useMutation} from "@tanstack/react-query"
import { useQuery } from '@tanstack/react-query';
import Pagination from "react-js-pagination";

const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
const formatDateTime = (dateTime) => {
  if (dateTime === "") return "";
  const dateTimeArr = dateTime.split("T");
  const newDateTime =
    dateTimeArr[0].replaceAll("-", "/") + " " + dateTimeArr[1];

  return newDateTime;
};

function App() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [contentValue, setContentValue] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [page, setPage] = useState(1);

  const [searchType, setSearchType] = useState("all");

  function handleCloseModal(){
    setIsModalOpen(false);
  }

  const { isFetching, data, refetch } = useQuery({
    queryKey: ["getList", page],
    queryFn: async () => {
      const res = await fetch(
        `http://43.202.226.27:8080/access-rules?page=${page}&size=100`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Time-Zone": timezone,
          },
        }
      );
      return res.json();
    },
  });

  const deleteMutation = useMutation({
    mutationFn: async (id) => {
      return await fetch(`http://43.202.226.27:8080/access-rules/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          "Time-Zone": timezone,
        },
      });
    },
    onSuccess: () => {if (searchType === "content") {
        getContentMutation.mutate(page);
        return;
      }
      if (searchType === "permission") {
        getPermissionMutation.mutate(page);
        return;
      }
      refetch();
    },
    onError: () => alert("문제가 발생했습니다. 다시 시도해주세요."),
  });

  const [searchData, setSearchData] = useState(null);

  const getContentMutation = useMutation({
    mutationFn: async (selectedPage) => {
      const res = await fetch(
        `http://43.202.226.27:8080/access-rules/content?page=${selectedPage}&size=100&inclusion=${contentValue}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Time-Zone": timezone,
          },
        }
      );

      return res.json();
    },
    onSuccess: (data) => {
      setSearchType("content");
      setStartTime("");
      setEndTime("");
      setSearchData(data);
    },
    onError: () => alert("문제가 발생했습니다. 다시 시도해주세요."),
  });

  const getPermissionMutation = useMutation({
    mutationFn: async (selectedPage) => {
      const start = formatDateTime(startTime);
      const end = formatDateTime(endTime);

      const res = await fetch(
        `http://43.202.226.27:8080/access-rules/permission?page=${selectedPage}&size=100&startTime=${start}&endTime=${end}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Time-Zone": timezone,
          },
        }
      );

      return res.json();
    },
    onSuccess: (data) => {
      setSearchType("permission");
      setContentValue("");
      setSearchData(data);
    },
    onError: () => alert("문제가 발생했습니다. 다시 시도해주세요."),
  });

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
              <input placeholder="내용 검색.." 
                value={contentValue}
                onChange={(e) => setContentValue(e.target.value)}/>
              <button onClick={() => {
                  setPage(1);
                  getContentMutation.mutate(1);
                }}>검색</button>
            </div>
            <div className="permissionTimeSearchingContainer">
            <input
                placeholder="시작 시간"
                type="datetime-local"
                value={startTime}
                onChange={(e) => setStartTime(e.target.value)}
              />
              <input
                placeholder="끝 시간"
                type="datetime-local"
                value={endTime}
                onChange={(e) => setEndTime(e.target.value)}
              />
              <button onClick={() => {
                  setPage(1);
                  getPermissionMutation.mutate(1);
                }}>검색</button>
            </div>
          </div>
          <div className="tableContainer">
            <table>
              <thead>
                <tr>
                  <th>IP 주소</th>
                  <th>내용</th>
                  <th>사용 시작 시간</th>
                  <th>사용 끝 시간</th>
                  <th></th>
                </tr>
              </thead>
            
              <tbody>
                {searchData ? (
                  searchData.accessRules.map((e) => {
                    return (
                      <tr key={e.id}>
                        <td>{e.ipAddress}</td>
                        <td>{e.content}</td>
                        <td>{e.startTime}</td>
                        <td>{e.endTime}</td>
                        <td>
                          <button onClick={() => deleteMutation.mutate(e.id)}>
                            delete
                          </button>
                        </td>
                      </tr>
                    );
                  })
                ) : (
                  <>
                    {!isFetching &&
                      data &&
                      data.accessRules.map((e) => {
                        return (
                          <tr key={e.id}>
                            <td>{e.ipAddress}</td>
                            <td>{e.content}</td>
                            <td>{e.startTime}</td>
                            <td>{e.endTime}</td>
                            <td>
                              <button
                                onClick={() => deleteMutation.mutate(e.id)}
                              >
                                delete
                              </button>
                            </td>
                          </tr>
                        );
                      })}
                  </>
                )}
              </tbody>
            </table>
          </div>
          {(searchData ||
            data) && (
              <Pagination
                activePage={page}
                itemsCountPerPage={100}
                totalItemsCount={
                  searchData ? searchData.totalCount : data.totalCount
                }
                pageRangeDisplayed={1}
                onChange={(page) => {
                  if (searchType === "permission") {
                    getPermissionMutation.mutate(page);
                  }
                  if (searchType === "content") {
                    getContentMutation.mutate(page);
                  }
                  setPage(page);
                }}
              ></Pagination>
            )}
        </div>
      </div>
      {isModalOpen && <Modal handleClose={handleCloseModal} refetch={refetch}/>}
  </>    
  );
}

export default App;

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

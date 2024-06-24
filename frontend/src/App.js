import './App.css';
import React, { useState } from "react"; 
import {useMutation} from "@tanstack/react-query"
import { useQuery } from '@tanstack/react-query';
import Pagination from "react-js-pagination";
import Modal from './component/Modal';
import formatDateTime from './util/formatDateTIme';
import { deleteRules, getContentRules, getPermissionRules, getRules } from './api/handlers';

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
    queryFn: () => getRules(page),
  });

  const deleteMutation = useMutation({
    mutationFn: (id) => deleteRules(id),
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
    mutationFn: (selectedPage) => getContentRules(selectedPage, contentValue),
    onSuccess: (data) => {
      setSearchType("content");
      setStartTime("");
      setEndTime("");
      setSearchData(data);
    },
    onError: () => alert("문제가 발생했습니다. 다시 시도해주세요."),
  });

  const getPermissionMutation = useMutation({
    mutationFn: (selectedPage) =>
      getPermissionRules(startTime, endTime, selectedPage),
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
                pageRangeDisplayed={10}
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

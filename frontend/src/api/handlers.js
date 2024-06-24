import formatDateTime from "../util/formatDateTIme";

const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;

export const deleteRules = async (id) => {
  return await fetch(`http://43.202.226.27:8080/access-rules/${id}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      "Time-Zone": timezone,
    },
  });
};

export const getRules = async (page) => {
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
};

export const getContentRules = async (selectedPage, contentValue) => {
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

  const result = await res.json();

  return result;
};

export const getPermissionRules = async (startTime, endTime, selectedPage) => {
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
  
    const result = await res.json();
  
    return result;
  };
const formatDateTime = (dateTime) => {
    if (dateTime === "") return "";
  
    const dateTimeArr = dateTime.split("T");
    const newDateTime =
      dateTimeArr[0].replaceAll("-", "/") + " " + dateTimeArr[1];
  
    return newDateTime;
  };
  
  export default formatDateTime;
  
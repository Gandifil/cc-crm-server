//-share------------------------------------------------------------------------------
// consts
const serverURL = "http://" + location.host + "/";
const indexURL = "/";
const parMap = {
    "all": "periodType=ALL",
    "year": "periodType=CURRENT_YEAR",
    "month": "periodType=CURRENT_MONTH"
}
// proxy functions
function makePost(subPath, body, succHandler, errHandler, token = null) {
    let headers = {
        'Content-Type': 'application/json'
    };

    if (token)
        headers.Authorization = "Bearer " + token;

    axios.post(serverURL + subPath, body, { "headers": headers })
            .then(response => succHandler(response))
            .catch(e => errHandler(e));
}
function makeGet(subPath, succHandler, errHandler, token = null) {
    let headers = {
        'Content-Type': 'application/json'
    };

    if (token)
        headers.Authorization = "Bearer " + token;

    axios.get(serverURL + subPath, { "headers": headers })
            .then(response => succHandler(response))
            .catch(e => errHandler(e));
}
// aux
function cout(message) {
    console.log(message);
}
//------------------------------------------------------------------------------------

// elements
var exitBtn;

var sendTable;
var histSwitcher;
var histTable;
var sendSwitcher;

var electro;
var cold;
var hot;

var sendBtn;

var ddList;

// events
document.addEventListener("DOMContentLoaded", initialize);

function initialize() {
    // elements
    exitBtn = document.querySelector('.exit-btn');

    sendTable = document.querySelector('.table.send');
    histSwitcher = document.querySelector('.hist-switcher');
    histTable = document.querySelector('.table.hist');
    sendSwitcher = document.querySelector('.send-switcher');

    electro = document.querySelector('.electro');
    cold = document.querySelector('.cold');
    hot = document.querySelector('.hot');

    sendBtn = document.querySelector('.send-btn');

    ddList = document.querySelector('select');

    // events
    exitBtn.addEventListener('click', exit);

    sendBtn.addEventListener('click', sendData);

    histSwitcher.addEventListener('click', switchToHist);
    sendSwitcher.addEventListener('click', switchToSend);

    ddList.addEventListener('change', fetchData);

    // next
    checkUserToken();
}

// check
function checkUserToken() {
    if (localStorage.hasOwnProperty("token")
            && localStorage.hasOwnProperty("profile")
            && JSON.parse(localStorage.getItem("profile")).role === "user") {
        cout("User token was confirm!");
        fetchData();
    }
    else {
        cout("User token was not confirm!");
        window.location.href = indexURL;
    }
}

// exit
function exit() {
    localStorage.removeItem("token");
    localStorage.removeItem("profile");
    cout("User token and profile were removed!")
    window.location.href = indexURL;
}

// switching
function switchToHist() {
    sendTable.classList.add('hidden');
    histTable.classList.remove('hidden');
    cout("switched to hist!");
}
function switchToSend() {
    histTable.classList.add('hidden');
    sendTable.classList.remove('hidden');
    cout("switched to send!");
}

// data actions
function printErr(err) {
    cout("err!");
    cout(err);
}
function removeElements(selector) {
    let elements = document.querySelectorAll(selector);
    for (let element of elements)
        element.remove();
}
function makeRow(values) {
    let row = document.createElement('tr');
    row.classList.add('removable');
    row.classList.add('reading');
    for (let value of values) {
        let cell = document.createElement('td');
        cell.innerHTML = value;
        row.appendChild(cell);
    }
    return row;
}
function fillTable(response) {
    let data = response.data;
    cout(data);

    removeElements(".table.hist .removable");

    for (let entry of data) {
        let elAmount = entry.electricity;
        let coldAmount = entry.coldWater;
        let hotAmount = entry.hotWater;
        let date = entry.date.substring(0, 19).replace("T", "<br>");
        histTable.appendChild(makeRow([elAmount, coldAmount, hotAmount, date]));
    }
}
function fetchData() {
    electro.value = cold.value = hot.value = "";
    cout("fetching data...");
    let param = parMap[ddList.value];
    let token = localStorage.getItem("token");
    makeGet("meters/?" + param, fillTable, printErr, token);
}
function sendData() {
    let lastData = {
        "electricity": electro.value,
        "coldWater": cold.value,
	    "hotWater": hot.value
    }

    let token = localStorage.getItem("token");

    makePost("meters/send", lastData, fetchData, printErr, token);
}

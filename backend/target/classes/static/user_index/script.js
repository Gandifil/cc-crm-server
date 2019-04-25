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

var lName;
var fName;
var mName;

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

    lName = document.querySelector('.last-name');
    fName = document.querySelector('.first-name');
    mName = document.querySelector('.middle-name');

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
function fillProfile(profile) {
    lName.innerHTML = profile.lastName;
    fName.innerHTML = profile.firstName;
    mName.innerHTML = profile.middleName;
}
function checkUserToken() {
    if (localStorage.hasOwnProperty("token")
            && localStorage.hasOwnProperty("profile")
            && JSON.parse(localStorage.getItem("profile")).role === "user") {
        cout("User token was confirm!");
        let profile = JSON.parse(localStorage.getItem("profile"));
        cout(profile);
        fillProfile(profile);
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
function clearFields() {
    electro.value = cold.value = hot.value = "";
    electro.style.background = "white";
    cold.style.background = "white";
    hot.style.background = "white";
}
function fetchData() {
    clearFields();
    cout("fetching data...");
    let param = parMap[ddList.value];
    let token = localStorage.getItem("token");
    makeGet("meters/?" + param, fillTable, printErr, token);
}
function markOk(element) {
    element.style.background = "lightgreen";
}
function markNot(element) {
    element.style.background = "lightpink";
}
function checkField(field, lowerBound, upperBound, succHandler, errHandler) {
    cout(field.value);
    if (field.value.length >= lowerBound && field.value.length <= upperBound) {
        succHandler(field);
        return true;
    }
    errHandler(field);
    return false;
}
function checkFieldNotContainsAny(field, subs, succHandler, errHandler) {
    cout(field.value);
    for (let sub of subs)
        if (field.value.includes(sub)) {
            errHandler(field);
            return false;
        }
    succHandler(field);
    return true;
}
function checkFields() {
    cout("Checking fields...");
    let results = [ checkField(electro, 1, 10, markOk, markNot) && checkFieldNotContainsAny(electro, [ '+', '-', 'e' ], markOk, markNot),
                    checkField(cold, 1, 10, markOk, markNot) && checkFieldNotContainsAny(cold, [ '+', '-', 'e' ], markOk, markNot),
                    checkField(hot, 1, 10, markOk, markNot) && checkFieldNotContainsAny(hot, [ '+', '-', 'e' ], markOk, markNot) ];
    for (let result of results)
        if (!result)
            return false;
    return true;
}
function sendData() {
    if (!checkFields()) {
        cout("Some fields is not correct!");
        return;
    }
    cout("All fields are correct!");

    cout("Sending data...");

    let lastData = {
        "electricity": electro.value,
        "coldWater": cold.value,
	    "hotWater": hot.value
    }

    let token = localStorage.getItem("token");

    makePost("meters/send", lastData, fetchData, printErr, token);
}

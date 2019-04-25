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

var apartTable;
var debtSwitcher;
var debtTable;
var apartSwitcher;

var apartDDList;
var debtDDList;
var apNumField;

// global
var apartData;

// events
document.addEventListener("DOMContentLoaded", initialize);
function initialize() {
    // elements
    exitBtn = document.querySelector('.exit-btn');

    lName = document.querySelector('.last-name');
    fName = document.querySelector('.first-name');
    mName = document.querySelector('.middle-name');

    apartTable = document.querySelector('.table.apart');
    debtSwitcher = document.querySelector('.debt-switcher');
    debtTable = document.querySelector('.table.debt');
    apartSwitcher = document.querySelector('.apart-switcher');

    apartDDList = document.querySelector('.table.apart .select-td');
    debtDDList = document.querySelector('.table.debt .select-td');
    apNumField = document.querySelector('.apart-number');

    // events
    exitBtn.addEventListener('click', exit);

    debtSwitcher.addEventListener('click', switchToDebt);
    apartSwitcher.addEventListener('click', switchToApart);

    apartDDList.addEventListener('change', fetchData);
    debtDDList.addEventListener('change', fetchData);
    apNumField.addEventListener('change', selectApart);

    // next
    checkAdminToken();
}

// check
function fillProfile(profile) {
    lName.innerHTML = profile.lastName;
    fName.innerHTML = profile.firstName;
    mName.innerHTML = profile.middleName;
}
function checkAdminToken() {
    if (localStorage.hasOwnProperty("token")
            && localStorage.hasOwnProperty("profile")
            && JSON.parse(localStorage.getItem("profile")).role === "admin") {
        cout("Admin token was confirm!");
        let profile = JSON.parse(localStorage.getItem("profile"));
        cout(profile);
        fillProfile(profile);
        fetchData();
    }
    else {
        cout("Admin token was not confirm!");
        window.location.href = indexURL;
    }
}

// exit
function exit() {
    localStorage.removeItem("token");
    localStorage.removeItem("profile");
    cout("Admin token and profile were removed!")
    window.location.href = indexURL;
}

// switching
function switchToDebt() {
    apartTable.classList.add('hidden');
    debtTable.classList.remove('hidden');
    cout("switched to debt!");
}
function switchToApart() {
    debtTable.classList.add('hidden');
    apartTable.classList.remove('hidden');
    cout("switched to apart!");
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
function makeSpanRow(value, classes, count) {
    let row = document.createElement('tr');

    row.classList.add('removable');
    for (let c of classes)
        row.classList.add(c);

    let cell = document.createElement('td');
    cell.innerHTML = value;
    cell.setAttribute("colspan", count);
    row.appendChild(cell);

    return row;
}
function makeRow(values, classes) {
    let row = document.createElement('tr');

    row.classList.add('removable');
    for (let c of classes)
        row.classList.add(c);

    for (let value of values) {
        let cell = document.createElement('td');
        cell.innerHTML = value;
        row.appendChild(cell);
    }
    return row;
}
function fillApartTable(aparts) {
    cout("Filling apart table...");
    cout(aparts);

    removeElements(".table.apart .removable");

    for (let n in aparts) {
        apartTable.appendChild(makeSpanRow(n, [ 'ap-num' ], 4));
        if (aparts[n].length == 0)
            apartTable.appendChild(makeSpanRow("<нет показаний>", [ 'reading', 'empty' ], 4));
        else
            for (let entry of aparts[n]) {
                let elAmount = entry.electricity;
                let coldAmount = entry.coldWater;
                let hotAmount = entry.hotWater;
                let date = entry.date.substring(0, 19).replace("T", "<br>");
                apartTable.appendChild(makeRow([ elAmount, coldAmount, hotAmount, date ], [ 'reading' ]));
            }
    }
}
function selectApart() {
    cout("Selecting apart...");
    let n = apNumField.value;
    if (n === "")
        fillApartTable(apartData);
    else if (apartData.hasOwnProperty(n)) {
        let apart = {};
        apart[n] = apartData[n];
        cout(apart);
        fillApartTable(apart);
    }
    else {
        cout("No such apart!");
        fillApartTable({});
    }
}
function saveAparts(response) {
    cout("Saving apart data...");
    apartData = response.data;
    selectApart();
}
function fillDebtTable(response) {
    cout("Filling debt table...");
    let aparts = response.data;
    cout(aparts);

    removeElements(".table.debt .removable");

    for (let n in aparts) {
        debtTable.appendChild(makeSpanRow(n, [ 'ap-num' ], 5));
        for (let user of aparts[n]) {
            let lastName = user.lastName;
            let firstName = user.firstName;
            let middleName = user.middleName;
            let email = user.email;
            let phoneNumber = user.phoneNumber;
            debtTable.appendChild(makeRow([lastName, firstName, middleName, email, phoneNumber], [ 'users' ]));
        }
    }
}
function fetchData() {
    cout("fetching data...");
    
    // apart table
    let param = parMap[apartDDList.value];
    let token = localStorage.getItem("token");
    makeGet("manage/aparts/?" + param, saveAparts, printErr, token);

    // debt table
    param = parMap[debtDDList.value];
    makeGet("manage/bad_aparts/?" + param, fillDebtTable, printErr, token);
}

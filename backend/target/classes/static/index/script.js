//-share------------------------------------------------------------------------------
// consts
const serverURL = "http://" + location.host + "/";
const userAccountURL = "user_index.html";
const adminAccountURL = "admin_index.html";
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
var signUpForm;
var signInForm;

var signUpSwitch;
var signInSwitch;

var signUpBtn;
var signInBtn;

var suUsername;
var suPassword;
var suEmail;
var suLastName;
var suFirstName;
var suMiddleName;
var suApNum;
var suPhoneNum;

// events
document.addEventListener("DOMContentLoaded", initialize);

function initialize() {
    // elements
    signUpForm = document.querySelector('.sign-up-form');
    signInForm = document.querySelector('.sign-in-form');

    signUpSwitch = document.querySelector('.sign-up-switch');
    signInSwitch = document.querySelector('.sign-in-switch');

    signUpBtn = document.querySelector('.sign-up-btn');
    signInBtn = document.querySelector('.sign-in-btn');

    suUsername = document.querySelector('.sign-up-form .username');
    suPassword = document.querySelector('.sign-up-form .password');
    suEmail = document.querySelector('.sign-up-form .email');
    suLastName = document.querySelector('.sign-up-form .lastName');
    suFirstName = document.querySelector('.sign-up-form .firstName');
    suMiddleName = document.querySelector('.sign-up-form .middleName');
    suApNum = document.querySelector('.sign-up-form .apartment');
    suPhoneNum = document.querySelector('.sign-up-form .phoneNumber');

    // events
    signInSwitch.addEventListener('click', switchToSignIn);
    signUpSwitch.addEventListener('click', switchToSignUp);

    signUpBtn.addEventListener('click', signUp);
    signInBtn.addEventListener('click', signIn);

    // next
    tryToSignIn();
}

// auto signing in
function tryToSignIn() {
    if (localStorage.hasOwnProperty("token")) {
        cout("Found token!");
        let token = localStorage.getItem("token");
        makeGet("profile", openAccount, printError, token)
    }
    else
        cout("Token was not found!");
}

// sign up / in switching
function switchToSignIn() {
    signUpForm.classList.add('hidden');
    signInForm.classList.remove('hidden');
    cout("switched to sign in!");
}
function switchToSignUp() {
    signInForm.classList.add('hidden');
    signUpForm.classList.remove('hidden');
    cout("switched to sign up!");
}

// signing up
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
function checkFieldContainsAll(field, subs, succHandler, errHandler) {
    cout(field.value);
    for (let sub of subs)
        if (!field.value.includes(sub)) {
            errHandler(field);
            return false;
        }
    succHandler(field);
    return true;
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
cout("rele");
function checkFields() {
    cout("Checking fields...");
    let results = [ checkField(suUsername, 4, 24, markOk, markNot),
                    checkField(suPassword, 6, 32, markOk, markNot),
                    checkField(suEmail, 5, 50, markOk, markNot) && checkFieldContainsAll(suEmail, [ '@', '.' ], markOk, markNot),
                    checkField(suLastName, 3, 50, markOk, markNot),
                    checkField(suFirstName, 3, 50, markOk, markNot),
                    checkField(suMiddleName, 3, 50, markOk, markNot),
                    checkField(suApNum, 1, 5, markOk, markNot) && checkFieldNotContainsAny(suApNum, [ '+', '-' ], markOk, markNot),
                    checkField(suPhoneNum, 6, 11, markOk, markNot) && checkFieldNotContainsAny(suPhoneNum, [ '+', '-' ], markOk, markNot) ];
    for (let result of results)
        if (!result)
            return false;
    return true;
}
function printError(message) {
    cout("error:");
    cout(message);
}
function signUp() {
    if (!checkFields()) {
        cout("Some fields is not correct!");
        return;
    }
    cout("All fields are correct!");

    cout("signing up...");

    let user = {
        "username": suUsername.value,
        "password": suPassword.value,
        "email": suEmail.value,
        "lastName": suLastName.value,
        "firstName": suFirstName.value,
        "middleName": suMiddleName.value,
        "apartment": suApNum.value,
        "phoneNumber": suPhoneNum.value
    };

    cout(user);

    makePost("auth/register", user, signIn, printError);
}

// signing in
function openAccount(response) {
    let profile = response.data;
    localStorage.setItem("profile", JSON.stringify(profile));
    cout("Profile was saved!");

    let role = profile.role;

    if (role === "admin")
        window.location.href = adminAccountURL;
    else if (role === "user")
        window.location.href = userAccountURL;
    else
        cout("Unknown role: " + role);
}
function fetchProfile(response) {
    let token = response.data.token;
    localStorage.setItem("token", token);
    cout("Token was saved!");
    makeGet("profile", openAccount, printError, token)
}
function signIn() {
    cout("signing in...");

    let formSelector;
    if (signUpForm.classList.contains("hidden"))
        formSelector = '.sign-in-form';
    else
        formSelector = '.sign-up-form';

    let user = {
        "username": document.querySelector(formSelector + ' .username').value,
        "password": document.querySelector(formSelector + ' .password').value
    };
    
    cout(user)

    makePost("auth/login", user, fetchProfile, printError)
}

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
function printError(message) {
    cout("error:");
    cout(message);
}
function signUp() {
    cout("signing up...");

    let user = {
        "username" : document.querySelector('.sign-up-form .username').value,
        "password": document.querySelector('.sign-up-form .password').value,
        "email": document.querySelector('.sign-up-form .email').value,
        "lastName": document.querySelector('.sign-up-form .lastName').value,
        "firstName": document.querySelector('.sign-up-form .firstName').value,
        "middleName": document.querySelector('.sign-up-form .middleName').value,
        "apartment": document.querySelector('.sign-up-form .apartment').value,
        "phoneNumber": document.querySelector('.sign-up-form .phoneNumber').value
    };

    cout(user);

    makePost("auth/register", user, signIn, printError)
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

// server endpoint
const server = location.host;

function requestProxy(subPath, body, process, error, token = null){
    let headers ={
            'Content-Type': 'application/json'
    };
    if (token)
        headers.Authorization = "Bearer " + token;
    axios.post(server + subPath, body, {headers: headers})
    .then(response => process(response))
    .catch(e => {
        error(e);
        console.error(e.message);
    });
}

function requestGetProxy(subPath, process, error, token = null){
    let headers ={
        'Content-Type': 'application/json'
    };
    if (token)
        headers.Authorization = "Bearer " + token;
    axios.get(server + subPath, {headers: headers})
        .then(response => process(response))
        .catch(e => {
            error(e);
            console.error(e.message);
        });
}
export async function apiCall(url, method, headers, body) {
    let options = {};
    options.method = method;
    if (headers) {
        options.headers = headers;
    }
    if (body) {
        options.body = JSON.stringify(body);
    }
    return await fetch(url, options);
};

export async function apiCall(url, type, headers, body) {
    headers['Content-Type'] = "application/json";
    return await fetch(url, {
      method: type,
      headers: headers,
      body: JSON.stringify(body)
    });
};

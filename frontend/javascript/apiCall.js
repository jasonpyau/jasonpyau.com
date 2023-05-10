export async function apiCall(url, type, headers, body) {
    return await $.ajax({
        url: url,
        type: type,
        data: body,
        headers: headers,
        success: function(response) {
          return response;
        },
        error: function(error) {
          return error;
        }
    });
};
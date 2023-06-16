import { apiCall } from "./apiCall.js";

$(document).ready(async function() {
    const url = 'about_me/get';
    const result = await apiCall(url, "GET", null, null);
    if (result.status !== 200)
        return;
    const json = await result.json();
    document.getElementById("aboutMeSpinner").display = "none";
    document.getElementById("AboutMeContainer").innerHTML = json.text;
});
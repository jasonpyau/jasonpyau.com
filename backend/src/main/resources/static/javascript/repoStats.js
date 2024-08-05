import { apiCall } from "./apiCall.js";

addEventListener('DOMContentLoaded', async(e) => {
    const url = `https://api.github.com/repos/jasonpyau/jasonpyau.com`;
    const result = await apiCall(url, "GET", null, null);
    const json = await result.json();
    const starsCount = json.stargazers_count;
    document.getElementById('StarsCount').textContent = starsCount;
});
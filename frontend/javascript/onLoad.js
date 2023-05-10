import { apiCall } from "./apiCall.js";
import { SERVER_URL } from "./variables.js";

$(document).ready(function() {
    $("#Header").load("../html/header.html");
    loadFooter();
});

function loadFooter() {
    $("#SourceCode").load("../html/sourcecode.html");
    let loaded = 0;
    $("#ViewCount").load("../html/viewcount.html", null, function() {
        loadStats();
    });
    $("#LastUpdated").load("../html/lastupdated.html", null, function() {
        loadStats();
    });

    async function loadStats() {
        loaded++;
        if (loaded == 2) {
            const url = `${SERVER_URL}/stats/update/views`;
            const headers = {};
            const body = {};
            const result = await apiCall(url, "POST", headers, body);
            console.log(result);
            const viewCount = document.getElementById("ViewCountStat");
            const lastUpdated = document.getElementById("LastUpdatedStat");
            viewCount.textContent = result.stats.views;
            lastUpdated.textContent = result.stats.date;
        }
    }
}
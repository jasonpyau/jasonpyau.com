import { apiCall } from "./apiCall.js";
import { SERVER_URL } from "./constants.js";

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
            const result = await apiCall(url, "POST", null, null);
            if (result.status !== 200)
                return;
            const json = await result.json();
            console.log(json);
            const viewCount = document.getElementById("ViewCountStat");
            const lastUpdated = document.getElementById("LastUpdatedStat");
            viewCount.textContent = json.stats.views;
            lastUpdated.textContent = json.stats.date;
        }
    }
}
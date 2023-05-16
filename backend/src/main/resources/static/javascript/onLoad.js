import { apiCall } from "./apiCall.js";

$(document).ready(function() {
    $("#Header").load("header");
    loadFooter();
});

function loadFooter() {
    $("#SourceCode").load("sourcecode");
    let loaded = 0;
    $("#ViewCount").load("viewcount", null, function() {
        loadStats();
    });
    $("#LastUpdated").load("lastupdated", null, function() {
        loadStats();
    });

    async function loadStats() {
        loaded++;
        if (loaded == 2) {
            const url = '/stats/update/views';
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
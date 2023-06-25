import { BlogStats } from "./BlogStats.js";
import { apiCall } from "./apiCall.js";

let blogPreviews = [];
let pageNum = 0;
let pageSize = localStorage.blogPageSize || 5;
let showOnlyLiked = (localStorage.blogShowOnlyLiked === 'true');

$(document).ready(async function() {
    document.getElementById("PageSizeRange").value = pageSize;
    updatePageSizeText();
    document.getElementById("ShowLikedSwitch").checked = showOnlyLiked;

    document.getElementById("PageSizeRange").addEventListener("input", updatePageSizeText);

    document.getElementById("LoadMoreButton").addEventListener("click", async() => {
        loadBlogsPage(false);
    });

    document.getElementById("ApplyButton").addEventListener("click", async () => {
        pageSize = document.getElementById("PageSizeRange").value;
        showOnlyLiked = document.getElementById("ShowLikedSwitch").checked;
        localStorage.blogPageSize = pageSize;
        localStorage.blogShowOnlyLiked = showOnlyLiked;
        deleteBlogPreviews();
        await loadBlogsPage(true);
    });
    await loadBlogsPage(false);
    function updatePageSizeText() {
        document.getElementById("PageSizeText").innerHTML = document.getElementById("PageSizeRange").value;
    }
    
});

async function loadBlogsPage(deleteFirst) {
    const blogsPreviewSpinner = document.getElementById("BlogsPreviewSpinner");
    blogsPreviewSpinner.style.display = "block";
    const url = "/blogs/get/page"+((showOnlyLiked) ? "/liked" : "")+`?pageNum=${pageNum}&pageSize=${pageSize}`;
    const result = await apiCall(url, "GET", null, null);
    if (result.status !== 200) {
        alert("Error in loading blogs, refresh. If this problem persists, contact me.");
        return;
    }
    const json = await result.json();
    const blogs = json.blogs;
    const container = document.getElementById("BlogsContainer");
    if (deleteFirst) {
        deleteBlogPreviews();
    }
    for (let i = 0; i < blogs.length; i++) {
        const blog = blogs[i];
        const blogPreview = new BlogPreview(blog.id, blog.title, blog.date, blog.viewCount, blog.likeCount, blog.isLikedByUser, container);
        blogPreviews.push(blogPreview);
    }
    pageNum++;
    document.getElementById("LoadMoreButton").style.display = (json.hasNext) ? "block" : "none";
    blogsPreviewSpinner.style.display = "none";
}

function deleteBlogPreviews() {
    for (let i = 0; i < blogPreviews.length; i++) {
        blogPreviews[i].delete();
    }
    pageNum = 0;
    blogPreviews = [];
}

class BlogPreview {
    #container;
    #element;
    #blogStats;

    constructor(id, title, date, viewCount, likeCount, isInitallyLiked, container) {
        this.#container = container;
        this.#element = document.createElement("div");
        this.#element.innerHTML =
        `
            <div class="my-2 SecondaryColor border border-white Rounded container-xxl d-flex justify-content-between p-3 BlogPreview">
                <a class="w-75 NoDecoration" href="/blogs/${id}">
                    <u class="fs-4 fw-bold" id="Title">
                        Title
                    </u>
                    <div class="fs-5" id="Date">
                        Date
                    </div>
                </a>
                <span id="BlogStatsContainer">

                </span>
            </div>
        `;
        this.#element.querySelector("#Title").innerHTML = title;
        this.#element.querySelector("#Date").innerHTML = date;
        const BlogStatsContainer = this.#element.querySelector("#BlogStatsContainer");
        this.#blogStats = new BlogStats(id, viewCount, likeCount, isInitallyLiked, BlogStatsContainer);
        this.#container.append(this.#element);
    }

    delete() {
        this.#element.remove();
    }
}
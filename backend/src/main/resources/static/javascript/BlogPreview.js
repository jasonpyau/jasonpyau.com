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
        document.getElementById("SearchInput").value = "";
        for (const blogPreview of blogPreviews) {
            blogPreview.updateVisibility(true);
        }
        loadBlogsPage(false);
    });

    document.getElementById("SearchInput").addEventListener("input", () => {
        const val = document.getElementById("SearchInput").value.toLowerCase();
        for (const blogPreview of blogPreviews) {
            blogPreview.updateVisibility(blogPreview.toString().includes(val));
        }
    });

    document.getElementById("ApplyButton").addEventListener("click", async () => {
        pageSize = document.getElementById("PageSizeRange").value;
        showOnlyLiked = document.getElementById("ShowLikedSwitch").checked;
        localStorage.blogPageSize = pageSize;
        localStorage.blogShowOnlyLiked = showOnlyLiked;
        document.getElementById("SearchInput").value = "";
        pageNum = 0;
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
    const json = await result.json();
    if (result.status !== 200) {
        alert(`Error in loading blogs, refresh. If this problem persists, contact me.\n\nReason: ${json.reason}`);
        return;
    }
    const blogs = json.blogs;
    const container = document.getElementById("BlogsContainer");
    if (deleteFirst) {
        deleteBlogPreviews();
    }
    for (const blog of blogs) {
        const blogPreview = new BlogPreview(blog, container);
        blogPreviews.push(blogPreview);
    }
    pageNum++;
    document.getElementById("LoadMoreButton").style.display = (json.hasNext) ? "block" : "none";
    blogsPreviewSpinner.style.display = "none";
}

function deleteBlogPreviews() {
    for (const blogPreview of blogPreviews) {
        blogPreview.delete();
    }
    blogPreviews = [];
}

class BlogPreview {
    #container;
    #element;
    #blogStats
    #blogString;

    constructor(blog, container) {
        this.#container = container;
        this.#element = document.createElement("div");
        this.#blogString = `${blog.title}, ${blog.body}, ${blog.date}`.toLowerCase();
        this.#element.innerHTML =
        `
            <div class="my-2 SecondaryColor border border-white Rounded container-xxl d-flex py-3 BlogPreview">
                <a class="NoDecoration flex-grow-1 mx-2" href="/blogs/${blog.id}" title="${blog.title}">
                    <u class="fs-4 fw-bold" id="Title">
                        Title
                    </u>
                    <div class="fs-5" id="Date">
                        Date
                    </div>
                </a>
                <span id="BlogStatsContainer" class="mx-2">

                </span>
            </div>
        `;
        this.#element.querySelector("#Title").innerHTML = blog.title;
        this.#element.querySelector("#Date").innerHTML = blog.date;
        const BlogStatsContainer = this.#element.querySelector("#BlogStatsContainer");
        this.#blogStats = new BlogStats(blog.id, blog.viewCount, blog.likeCount, blog.isLikedByUser, BlogStatsContainer);
        this.#container.append(this.#element);
    }

    toString() {
        return this.#blogString;
    }

    updateVisibility(isVisible) {
        this.#element.style.display = (isVisible) ? "inline" : "none";
    }

    delete() {
        this.#element.remove();
    }
}
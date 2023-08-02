import { BlogStats } from "./BlogStats.js";
import { apiCall } from "./apiCall.js";
import { DefaultBlogSearchForm } from "./DefaultBlogSearchForm.js";

let blogPreviews = [];
let pageNum = 0;
let pageSize = localStorage.blogPageSize || 5;

let {orderBy, ascending} = DefaultBlogSearchForm;
let showOnlyLiked = DefaultBlogSearchForm.liked;
let searchInput = DefaultBlogSearchForm.search;

$(document).ready(async function() {
    document.getElementById("PageSizeRange").value = pageSize;
    updatePageSizeText();
    document.getElementById("ShowLikedSwitch").checked = showOnlyLiked;

    document.getElementById("PageSizeRange").addEventListener("input", updatePageSizeText);

    document.getElementById("SearchInput").value = searchInput;

    for (const orderByRadio of document.getElementsByName("OrderByRadio")) {
        if (orderByRadio.getAttribute("api_value") === orderBy) {
            orderByRadio.checked = true;
        }
    }

    document.getElementById("AscendingSwitch").checked = ascending;


    document.getElementById("LoadMoreButton").addEventListener("click", async() => {
        loadBlogsPage(false);
    });

    document.getElementById("ApplyButton").addEventListener("click", async() => {
        await apply();
    });

    document.getElementById("ResetButton").addEventListener("click", async() => {
        document.getElementById("PageSizeRange").value = 5;
        updatePageSizeText();
        document.getElementById("ShowLikedSwitch").checked = false;
        document.getElementById("SearchInput").value = "";
        for (const orderByRadio of document.getElementsByName("OrderByRadio")) {
          if (orderByRadio.getAttribute("api_value") === "unix_time") {
                orderByRadio.checked = true;
            } else {
                orderByRadio.checked = false;
            }
        }
        document.getElementById("AscendingSwitch").checked = false;
        await apply();
    });

    await loadBlogsPage(false);
    function updatePageSizeText() {
        document.getElementById("PageSizeText").textContent = document.getElementById("PageSizeRange").value;
    }

    async function apply() {
        pageSize = document.getElementById("PageSizeRange").value;
        showOnlyLiked = document.getElementById("ShowLikedSwitch").checked;
        searchInput = document.getElementById("SearchInput").value;
        for (const orderByRadio of document.getElementsByName("OrderByRadio")) {
            if (orderByRadio.checked) {
                orderBy = orderByRadio.getAttribute("api_value");
                break;
            }
        }
        ascending = document.getElementById("AscendingSwitch").checked;
        localStorage.blogPageSize = pageSize;
        localStorage.blogShowOnlyLiked = showOnlyLiked;
        localStorage.blogSearchInput = searchInput;
        localStorage.blogOrderBy = orderBy;
        localStorage.blogAscending = ascending;
        pageNum = 0;
        await loadBlogsPage(true);
    }
    
});

async function loadBlogsPage(deleteFirst) {
    if (deleteFirst) {
        deleteBlogPreviews();
    }
    const blogsPreviewSpinner = document.getElementById("BlogsPreviewSpinner");
    blogsPreviewSpinner.style.display = "block";
    const params = {
        pageNum: pageNum,
        pageSize: pageSize,
        search: searchInput,
        orderBy: orderBy,
        ascending: ascending,
        liked: showOnlyLiked
    };
    const url = "/blogs/get/page?"+new URLSearchParams(params);
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

    constructor(blog, container) {
        this.#container = container;
        this.#element = document.createElement("div");
        this.#element.innerHTML =
        `
            <div class="my-2 SecondaryColor border border-white Rounded container-xxl d-flex py-3 BlogPreview">
                <a class="NoDecoration flex-grow-1 mx-2" href="/blogs/${blog.id}" title="${blog.title}">
                    <u class="fs-4 fw-bold" id="Title">
                        Title
                    </u>
                    <br>
                    <i class="fs-5" id="Description">
                        Description
                    </i>
                    <div class="fs-5 mt-2" id="Date">
                        Date
                    </div>
                </a>
                <span id="BlogStatsContainer" class="mx-2">

                </span>
            </div>
        `;
        this.#element.querySelector("#Title").textContent = blog.title;
        this.#element.querySelector("#Description").textContent = blog.description;
        this.#element.querySelector("#Date").textContent = blog.date;
        const BlogStatsContainer = this.#element.querySelector("#BlogStatsContainer");
        this.#blogStats = new BlogStats(blog.id, blog.viewCount, blog.likeCount, blog.isLikedByUser, BlogStatsContainer);
        this.#container.append(this.#element);
    }

    updateVisibility(isVisible) {
        this.#element.style.display = (isVisible) ? "inline" : "none";
    }

    delete() {
        this.#element.remove();
    }
}
import { apiCall } from "./apiCall.js";

export class BlogStats {

    #id;
    #viewCount;
    #likeCount;
    #isLiked;
    #unlikedIcon;
    #likedIcon;
    #likeCountElement;
    #likeButton;

    constructor(id, viewCount, likeCount, isInitallyLiked, container) {
        this.#id = id;
        this.#viewCount = viewCount;
        this.#likeCount = likeCount;
        this.#isLiked = isInitallyLiked;
        container.innerHTML =
            `<button type="button" class="btn btn-secondary" id="LikeButton">
                <span id="UnlikedIcon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-heart" viewBox="0 0 16 16">
                    <path d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z"></path>
                    </svg>
                </span>
                <span id="LikedIcon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-heart-fill" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"></path>
                    </svg>
                </span>
                <span id="LikeCountElement" class="fs-5 fw-semibold">0</span>
            </button>
            <div>
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-eye" viewBox="0 0 16 16">
                    <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.134 13.134 0 0 1 1.172 8z"></path>
                    <path d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5zM4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0z"></path>
                </svg>
                <span id="ViewCountElement" class="fs-5 fw-semibold">0</span>
            </div>`;
            this.#unlikedIcon = container.querySelector("#UnlikedIcon");
            this.#likedIcon = container.querySelector("#LikedIcon");
            this.#likeCountElement = container.querySelector("#LikeCountElement");
            this.#likeButton = container.querySelector("#LikeButton");
            container.querySelector("#ViewCountElement").textContent = numberFormat(this.#viewCount);
            this.updateLikeDisplay();
            this.#likeButton.addEventListener("click", async () => {
                const method = (this.#isLiked) ? "unlike" : "like";
                const url = `/blogs/${method}/${this.#id}`;
                const result = await apiCall(url, "POST", null, null);
                const json = await result.json();
                if (result.status !== 200) {
                    alert(`Error in liking, refresh. If this problem persists, contact me.\n\nReason: ${json.reason}`);
                    return;
                }
                if (this.#isLiked) {
                    this.#likeCount--;
                    this.#isLiked = false;
                } else {
                    this.#likeCount++;
                    this.#isLiked = true;
                }
                this.updateLikeDisplay();
            });
    }

    updateLikeDisplay() {
        if (this.#isLiked) {
            this.#likedIcon.style.display = "inline";
            this.#unlikedIcon.style.display = "none";
        } else {
            this.#likedIcon.style.display = "none";
            this.#unlikedIcon.style.display = "inline";
        }
        this.#likeCountElement.textContent = numberFormat(this.#likeCount);
    }
}

function numberFormat(num) {
    return Intl.NumberFormat('en-US', {
        notation: "compact",
        maximumFractionDigits: 2
    }).format(num);
}
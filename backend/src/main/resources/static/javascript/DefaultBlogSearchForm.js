export const DefaultBlogSearchForm = {
    liked: (localStorage.blogShowOnlyLiked === 'true') || false,
    search: (localStorage.blogSearchInput) || "",
    orderBy: (localStorage.blogOrderBy) || "unix_time",
    ascending: (localStorage.blogAscending === 'true') || false
};
const $review = document.querySelector(".review");

function fetchData () {
    fetch(
    `${API_KEY}`
    )
        .then((response) => response.json())
        .then((result) => result.items.map(item => reviewTemplate(item)))
        .catch((error) => console.log("error", error));
}

fetchData();

function reviewTemplate(data) {

    const reviews = `
    <div class="review-margin">
    <img class="review-img" src=${data}>
    <div class="user-info">
        <span>${data}</span>
        <span>| ${data}</span>
    </div>
    <div class="review-content">
        <div>구매 옵션 | ${data}</div>
        <div class="content">
            <p>${data}</p>
        </div>
    </div>
    <div class="hr-bottom">
    <hr>
</div>
    `;

    $review.insertAdjacentHTML('beforeend', reviews);
}
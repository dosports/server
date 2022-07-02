/*광고페이지*/
let imagelist = ["black", "blue", "red"];
let imageNum = 1;
setInterval(changeAd, 8000);
function changeAd() {
	document.querySelector("#ad").style.background = imagelist[imageNum];
	let npage = document.querySelectorAll(".npage");
	npage.forEach((element) => {
		element.style.background = "none";
	});
	npage[imageNum].style.background = "white";
	imageNum = (imageNum + 1) % imagelist.length;
}
/*네비게이션 바 메뉴*/
document.querySelector(".nav-menu-item").addEventListener("mouseenter", () => {
	navMenu_mouseOver();
});
document.querySelectorAll(".nav-menu-item").forEach((element, index) => {
	element.addEventListener("mouseover", () => {
		if (index != 0) {
			navMenu_mouseLeave();
		} else {
			element.style.background = "#7b7485";
		}
	});
});
document.querySelector("nav").addEventListener("mouseleave", () => {
	navMenu_mouseLeave();
});
function navMenu_mouseOver() {
	document.querySelector("#nav_bar_categorys").style.display = "block";
}
function navMenu_mouseLeave() {
	document.querySelector("#nav_bar_categorys").style.display = "none";
	document.querySelector(".nav-menu-item").style.background = "#b0a8ba";
}

/*추천 페이지 버튼*/
document.querySelector("#recommend-next_btn").addEventListener("click", recommend_next);
function recommend_next() {
	const recommend_list = document.querySelector("#recommend-list");
	rm1 = recommend_list.querySelector(".recommend-item");
	recommend_list.removeChild(rm1);
	recommend_list.appendChild(rm1);
	rm1 = recommend_list.querySelector(".recommend-item");
	recommend_list.removeChild(rm1);
	recommend_list.appendChild(rm1);
	rm1 = recommend_list.querySelector(".recommend-item");
	recommend_list.removeChild(rm1);
	recommend_list.appendChild(rm1);
}
document.querySelector("#recommend-prev_btn").addEventListener("click", recommend_prev);
function recommend_prev() {
	const recommend_list = document.querySelector("#recommend-list");
	rm1 = recommend_list.querySelector(".recommend-item:last-child");
	recommend_list.removeChild(rm1);
	recommend_list.prepend(rm1);
	rm1 = recommend_list.querySelector(".recommend-item:last-child");
	recommend_list.removeChild(rm1);
	recommend_list.prepend(rm1);
	rm1 = recommend_list.querySelector(".recommend-item:last-child");
	recommend_list.removeChild(rm1);
	recommend_list.prepend(rm1);
}

/*베스트페이지 카테고리*/
const menuList = document.querySelectorAll(".best-menu-text");
menuList.forEach((element, index) => {
	element.addEventListener("click", () => {
		for (let i = 0; i < menuList.length; i++) {
			menuList[i].style.color = "gray";
			menuList[i].style.border = "none";
		}
		element.style.color = "black";
		element.style.borderBottom = "1px solid black";
	});
});

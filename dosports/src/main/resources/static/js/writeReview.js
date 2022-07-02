const review_form = document.querySelector("#review-form") ;
const form_input = review_form.querySelectorAll("input");
const hamburger_icon = document.querySelector(".hamburger-bar") ;
const menu_bar = document.querySelector("#menu-bar")
const cancel_btn = document.querySelector("#menu-bar .cancel") ;


function clickBurgerBar() {
    menu_bar.classList.remove('hide');
}

function subForm (e) {
    e.preventDafualt() ;
}

function clickCancelBtn () {
    menu_bar.classList.add("hide");
}

review_form.addEventListener("submit", subForm);

hamburger_icon.addEventListener("click" , clickBurgerBar) ;

cancel_btn.addEventListener("click" , clickCancelBtn) ;
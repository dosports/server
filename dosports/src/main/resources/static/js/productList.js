// filter pop up
const $sort_filter_icon = document.querySelector('.sort_filter_icon');
const $filter_bg_container = document.querySelector('.filter_bg_container');
const $filter_container = document.querySelector('.filter_container')
const $filter_close = document.querySelector('.filter_close');

$sort_filter_icon.addEventListener('click', (event) =>{
    $filter_bg_container.className = "filter_bg_container show"
    $filter_container.className = "filter_container show"

})
$filter_close.addEventListener('click', (event) => {
    $filter_bg_container.className = "filter_bg_container hidden"
    $filter_container.className = "filter_container hidden"
})

// page span click
const $clothes_image_page_container = document.querySelector('.clothes_image_page_container');

$clothes_image_page_container.addEventListener('click', (event) =>{
    addClassToChoose($clothes_image_page_container,'page_check',event, 'SPAN');
    changeClothImage();
})

// filter page click _ check sign
function removeCheckSign(dom, className){
    const removeCheck = dom.querySelector(`.${className}`);
    if(removeCheck!=null){
        removeCheck.classList.remove(className);
    }
}
function addClassToChoose(dom, className, event, tagName){
    const target = event.target;
    if(target.tagName != tagName) return;

    removeCheckSign(dom, className);
    target.classList.add(className);
}

const $filter_gender_box = document.querySelector(".filter_gender_box");
$filter_gender_box.addEventListener('click', (event) => {
    addClassToChoose($filter_gender_box, 'check', event, 'SPAN');
})

const $level_detail_container = document.querySelector('.level_detail_container');
$level_detail_container.addEventListener('click', (event) => {
    const target = event.target;
    if(!target.classList.contains('level')) return;

    removeCheckSign($level_detail_container, 'check');
    target.classList.add('check');
})

const $price_container = document.querySelector('.price_container');
$price_container.addEventListener('click', (event)=>{
    addClassToChoose($price_container, 'check', event, 'SPAN');
})

// detail category
$detail_category = document.querySelector('.detail_category');
$detail_category.addEventListener('click', (event) =>{
    addClassToChoose($detail_category, 'choose', event, 'SPAN');
    $clothes_image_page_container.querySelector('.page_check').classList.remove('page_check');
    $clothes_image_page_container.children[0].classList.add('page_check');
    changeClothImage();
})

// expert opinion open & close
const $expert_opinion_arrow = document.querySelector('.expert_opinion_arrow');
const $expert_opinion_main = document.querySelector('.expert_opinion_main');

$expert_opinion_arrow.addEventListener('click',(event) => {
    $expert_opinion_main.classList.toggle('expert_opinion_show');
    $expert_opinion_arrow.classList.toggle('expert_opinion_arrow_up');
})

// change cloth image
// const cloth_detail_category = ["상의", "팬츠", "스커트", "운동화", "스포츠용품"];
const $cloth_images = document.querySelectorAll('.cloth_image');
const cloth_images = {
    '상의' :
    ["https://image.nbkorea.com/NBRB_Product/20220627/NB20220627124135358001.jpg",
    'https://image.nbkorea.com/NBRB_Product/20220627/NB20220627124201146001.jpg',
    'https://image.nbkorea.com/NBRB_Product/20220627/NB20220627124148594001.jpg'],
    '팬츠' :
    ["https://image.nbkorea.com/NBRB_Product/20220624/NB20220624142359153001.jpg",
    'https://image.nbkorea.com/NBRB_Product/20220624/NB20220624142426181001.jpg',
    'https://image.nbkorea.com/NBRB_Product/20220624/NB20220624142412883001.jpg'],
    '스커트' :
    ["https://image.nbkorea.com/NBRB_Product/20220523/NB20220523150748131001.jpg",
    'https://image.nbkorea.com/NBRB_Product/20220329/NB20220329163014985001.jpg',
    'https://image.nbkorea.com/NBRB_Product/20220120/NB20220120105038056001.jpg'],
    '운동화' :
    ["https://image.nbkorea.com/NBRB_Product/20220608/NB20220608160147835001.jpg",
    'https://image.nbkorea.com/NBRB_Product/20220629/NB20220629092520275001.jpg',
    'https://image.nbkorea.com/NBRB_Product/20220629/NB20220629174428397001.jpg'],
    '스포츠용품' :
    ["https://cdn3-aka.makeshop.co.kr/shopimages/jo112/0210000000383.jpg?1647239036",
    'https://cdn3-aka.makeshop.co.kr/shopimages/jo112/0210000000123.jpg?1639716730',
    'https://cdn3-aka.makeshop.co.kr/shopimages/jo112/0210000000173.jpg?1639714567']
};
function changeClothImage(){
    const cloth_cate = $detail_category.querySelector('.choose').innerText;
    const page_num = $clothes_image_page_container.querySelector('.page_check').innerText;
    const imageSrc = cloth_images[cloth_cate];
    for(let i=0;i<$cloth_images.length;i++){
        $cloth_images[i].children[0].src = imageSrc[page_num-1];
    }
}

// on load
window.addEventListener('load', (event)=>{
    $clothes_image_page_container.children[0].classList.add('page_check');
    $detail_category.children[0].classList.toggle('choose');
    changeClothImage();
    // const $filter_bg_container = document.querySelector('.filter_bg_container');
    // $filter_bg_container.style.height = document.innerHeight;
    // console.log(document.documentElement.clientHeight);
})
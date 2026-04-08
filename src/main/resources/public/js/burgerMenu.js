const menu = document.querySelector(".burger-menu-picture");
const offScreenMenu = document.querySelector(".burger-menu");

menu.addEventListener("click", () =>{
    menu.classList.toggle("active")
    offScreenMenu.classList.toggle("burger-menu-active");
})

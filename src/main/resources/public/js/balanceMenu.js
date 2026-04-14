document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".money-button");
    const balanceBox = document.querySelector(".balance-box");



    buttons.forEach((button) => {
        button.addEventListener("click", () => {
            const id = button.getAttribute("data-id");
            const email = button.getAttribute("data-email");
            const balance = button.getAttribute("data-balance");
            const balanceText = document.querySelector("#balance-text")

            balanceText.innerText = `Kunde med email: ${email}'s balance er: ${balance}DKK`;



            const hiddenUserId = document.querySelector('#hidden-user-id')
            hiddenUserId.value = id;

            button.classList.toggle("active");
            balanceBox.classList.toggle("balance-box-active")


        })
    })
});


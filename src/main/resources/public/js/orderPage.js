function updateCupcakeName() {
    const top = document.querySelector('input[name="top"]:checked')?.value;
    const bottom = document.querySelector('input[name="bottom"]:checked')?.value;

    if (top && bottom) {
        const key = top + "|" + bottom;
        const name = cupcakeNames[key];

        const titleElement = document.getElementById("cupcake-name-title");
        if (name) {
            titleElement.textContent = name;
        } else {
            titleElement.textContent = "Hjemmelavet Cupcake";
        }
    }
}

document.querySelectorAll('input[name="top"], input[name="bottom"]')
    .forEach(radio => radio.addEventListener("change", updateCupcakeName));

updateCupcakeName();
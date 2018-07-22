window.onload = () => {
    let input_content = document.getElementById("content");
    let input_button = document.getElementById("send");

    let root = document.getElementById("display");
    let name = 1;

    const nameArray = ["Eric", "Derek", "Mars", "Max"];
    const contentArray = ["Hello ~",  "Trump is good", "what??", "hehe"];
    input_button.addEventListener("click", () => {
        for (let i = 0; i < 3; i++) {
            setTimeout(() => {
                name = nameArray[i];
                content = contentArray[i];
                root.appendChild(buildLeftBubble(name, content));
                root.appendChild(document.createElement("br"));}, 1000*(i+Math.random()));

        }

        //  let placeholder = document.createElement("div");
       //  placeholder.setAttribute("class", "mdui-col-xs-8");
       //  placeholder.innerText = 1;
       //  root.appendChild(placeholder);
       root.appendChild(buildRightBubble(name, input_content.value));
       root.appendChild(document.createElement("br"));
       input_content.value = "";
    });
};

let buildLeftBubble = (name, content) => {
    let div = document.createElement("p");
    div.setAttribute("class", "mdui-chip");
    let icon = document.createElement("span");
    icon.setAttribute("class", "mdui-chip-icon");
    let message = document.createElement("span");
    message.setAttribute("class", "mdui-chip-title");
    icon.innerText = name;
    message.innerText = content;
    div.appendChild(icon);
    div.appendChild(message);
    return div;
};

let buildRightBubble = (name, content) => {
    let div = document.createElement("p");
    div.setAttribute("class", "mdui-chip right");
    let icon = document.createElement("span");
    icon.setAttribute("class", "mdui-chip-icon");
    let message = document.createElement("span");
    message.setAttribute("class", "mdui-chip-title");
    icon.innerText = name;
    message.innerText = content;
    div.appendChild(message);
    div.appendChild(icon);
    return div;
};

